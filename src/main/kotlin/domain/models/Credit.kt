package domain.models

import domain.data.Money
import domain.data.State
import domain.data.Type
import domain.exceptions.ChangeIdentifiedCreditIdException
import domain.exceptions.CreditAmountTooSmallException
import domain.exceptions.PaymentLessThanMinimalException
import domain.valueObjects.Payment
import domain.valueObjects.PaymentInterface
import domain.valueObjects.PersonInterface
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.round


class Credit(
    override val person: PersonInterface,
    override val amount: domain.data.Money,
    override val agreementAt: LocalDate,
    override val currency: domain.data.Currency,
    override val duration: Int,
    override val percent: Int
) : CreditInterface {
    override var id: Int? = null
        set(value) {
            if (field == null) {
                field = value
            } else {
                throw ChangeIdentifiedCreditIdException()
            }
        }

    override var isFinished: Boolean = false
        private set

    private val rounding: Int
    private var regularPayment: Money
    private val payments: MutableList<PaymentInterface> = arrayListOf()
    private var remainAmount: Money = amount

    init {
        val annuityPayment = getAnnuityPayment()
        rounding = getRounding(annuityPayment)
        regularPayment = getRegularPayment(annuityPayment)
    }

    override fun getPayments(type: Type?, state: State?): MutableList<PaymentInterface> {
        val results: MutableList<PaymentInterface> = arrayListOf()

        if (state == State.PAID || state == null) {
            when (type) {
                Type.REGULAR, Type.EARLY -> {
                    results.addAll(payments.filter { it.type == type })
                }
                null -> {
                    results.addAll(payments)
                }
                Type.NEXT -> {
                    // no results for that case
                }
            }
        }

        if ((state == State.UPCOMING || state == null) && (remainAmount.value > 0)) {
            when (type) {
                Type.NEXT -> {
                    results.add(fetchNextPayment(getLastPayment(), Type.NEXT))
                }
                Type.REGULAR, null -> {
                    var payment = fetchNextPayment(getLastPayment(), Type.NEXT)
                    results.add(payment)

                    while (payment.payment.value != payment.fullEarlyRepayment.value) {
                        payment = fetchNextPayment(payment, Type.REGULAR)
                        results.add(payment)
                    }
                }
                Type.EARLY -> {
                    // no results for that case
                }
            }
        }

        return results
    }

    /**
     * Returns last Payment
     * If there is no any payments yet, create one with zeroes in a date of agreementAt
     */
    private fun getLastPayment(): PaymentInterface {
        return try {
            payments.last()
        } catch (e: NoSuchElementException) {
            Payment(
                Money(0),
                Type.REGULAR,
                currency,
                agreementAt,
                State.PAID,
                Money(0),
                Money(0),
                amount,
                amount
            )
        }
    }

    private fun fetchNextPayment(previousPayment: PaymentInterface, type: Type): PaymentInterface {
        val body: Money
        val date = fetchNextPaymentDate(previousPayment)
        val remainCreditBody = Money(previousPayment.remainCreditBody.value - previousPayment.body.value)
        var currentPayment = regularPayment

        var percent = fetchPercent(previousPayment.date, date, remainCreditBody)

        if (currentPayment.value - percent.value < remainCreditBody.value) {
            body = Money(currentPayment.value - percent.value)
        } else {
            // different order and formulas for payment, body and percent calculation
            // when it is last payment
            currentPayment = Money(floor((percent.value + remainCreditBody.value)/10.0).toLong()*10)
            body = Money(remainCreditBody.value)
            percent = Money(currentPayment.value - body.value)
        }

        return Payment(
            currentPayment,
            Type.REGULAR,
            currency,
            date,
            State.UPCOMING,
            percent,
            body,
            remainCreditBody,
            Money(floor((remainCreditBody.value + percent.value)/10.0).toLong()*10)
        )
    }

    /**
     * Calculates the next regular payment date
     * Uses knowledge of previous payment and agreementAt date
     * Next payment date should have same dayOfMonth as agreementAt date does
     * If payment date month have such day, last month day otherwise
     */
    private fun fetchNextPaymentDate(previousPayment: PaymentInterface): LocalDate {
        val paymentDayOfMonth = agreementAt.dayOfMonth

        if (previousPayment.type == Type.EARLY) {
            val paymentDateCandidate = previousPayment.date
            val daysInMonth = YearMonth.of(paymentDateCandidate.year, paymentDateCandidate.monthValue).lengthOfMonth()

            if (paymentDayOfMonth > paymentDateCandidate.dayOfMonth && daysInMonth > paymentDateCandidate.dayOfMonth) {
                return paymentDateCandidate.withDayOfMonth(minOf(paymentDayOfMonth, daysInMonth))
            }
        }

        val paymentDateCandidate = previousPayment.date.plusMonths(1)
        val daysInMonth = YearMonth.of(paymentDateCandidate.year, paymentDateCandidate.monthValue).lengthOfMonth()

        return when {
            daysInMonth > paymentDayOfMonth -> paymentDateCandidate.withDayOfMonth(paymentDayOfMonth)
            else                            -> paymentDateCandidate.withDayOfMonth(daysInMonth)
        }
    }

    /**
     * Calculate percents according to document
     * @link http://mobile-testing.ru/loancalc/rachet_dosrochnogo_pogashenia/
     */
    private fun fetchPercent(from: LocalDate, to: LocalDate, creditBody: Money, inclusiveTo: Boolean = false): Money {
        if (from.year != to.year && to.dayOfMonth != 1) {
            val firstPercent = fetchPercent(from, LocalDate.of(from.year, 12, 31), creditBody, true)
            val secondPercent = fetchPercent(LocalDate.of(to.year, 1, 1), to, creditBody, false)
            return Money(firstPercent.value + secondPercent.value)
        }
        val percentDays = from.until(to, ChronoUnit.DAYS) + if (inclusiveTo) { 1 } else { 0 }
        val yearDays = Year.of(from.year).length()

        return Money(round(
            creditBody.value.toDouble()*percent.toDouble()*percentDays.toDouble()/100.0/yearDays.toDouble()
        ).toLong())
    }

    override fun writeOf(payment: PaymentInterface) {
        //TODO check valid payment
        payments.add(payment)
    }

    private fun getAnnuityPayment(): Double {

        val monthPercent = percent.toDouble()/12.0/100.0
        val power = (1.0 + monthPercent).pow(duration)

        return amount.value.toDouble()*monthPercent*(power/(power - 1.0))
    }

    private fun getRounding(annuityPayment: Double): Int {

        if (annuityPayment < 100.0) {
            throw PaymentLessThanMinimalException()
        }

        arrayOf(100.0, 10.0, 1.0).forEach {
            if ((it - (annuityPayment % it))*duration.toDouble() < annuityPayment) {
                return it.toInt()
            }
        }

        throw CreditAmountTooSmallException()
    }

    private fun getRegularPayment(annuityPayment: Double): Money {

        return Money((Math.ceil(annuityPayment/rounding.toDouble())*rounding).toLong())
    }
}
