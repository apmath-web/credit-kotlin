package domain.models

import domain.data.Money
import domain.data.State
import domain.data.Type
import domain.exceptions.*
import domain.exceptions.runtime.ChangeIdentifiedCreditIdException
import domain.valueObjects.PersonInterface
import domain.valueObjects.payment.PaidPayment
import domain.valueObjects.payment.PaidPaymentInterface
import domain.valueObjects.payment.PayPaymentInterface
import java.time.LocalDate
import kotlin.math.pow
import kotlin.math.roundToLong

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

    /**
     * @see <a href="https://docs.google.com/spreadsheets/d/1dQnuL1G1iUDinTugekdn79CxFYJv1sgswymuRsSowXU/edit#gid=0&range=G7">Округление итоговое</a>
     */
    private var rounding: Int
    /**
     * @see <a href="https://docs.google.com/spreadsheets/d/1dQnuL1G1iUDinTugekdn79CxFYJv1sgswymuRsSowXU/edit#gid=0&range=G10">Округление итоговое</a>
     */
    private var regularPayment: Money
    /**
     * @see <a href="https://docs.google.com/spreadsheets/d/1dQnuL1G1iUDinTugekdn79CxFYJv1sgswymuRsSowXU/edit#gid=0&range=G10">Округление итоговое</a>
     */
    private val payments: MutableList<PaidPaymentInterface> = arrayListOf()
    var remainAmount: Money = amount

    private var lastDate: LocalDate

    init {
        val annuityPayment = getAnnuityPayment()
        rounding = getRounding(annuityPayment)
        regularPayment = getRegularPayment(annuityPayment)
        lastDate = agreementAt
    }

    override fun getPayments(type: Type?, state: State?): MutableList<PaidPaymentInterface> {
        val results = arrayListOf<PaidPaymentInterface>()
        payments.forEach {
            if (it.state == state && it.type == type || type == null)
                results.add(it)
            else if (it.type == type && it.state == state || state == null)
                results.add(it)
        }

        return results
    }

    override fun writeOf(payment: PayPaymentInterface) {
        val type = getType(payment)

        when {
            isClosed()                          -> throw CreditAlreadyPaidException()
            payment.currency != currency        -> throw WrongCurrencyException()
            payment.type != type                -> throw WrongTypeException()
            lastDate >= payment.date            -> throw PaymentTooEarlyException()
        }

        val percent = (remainAmount.value * getPercentPayment(payment.date)).roundToLong()
        val body = payment.payment.value - percent

        when {
            Money(body) > remainAmount                   -> throw PaymentMoreThanCreditAmontException()
            (payment.payment < Money(100)
                    && Money(body) != remainAmount)     -> throw PaymentLessThanMinimalException()
            body == remainAmount.value                  -> percent.minus(remainAmount.value % (rounding / 10))
            payment.payment < regularPayment    -> throw PaymentLessThanRegularException()
        }

        remainAmount -= Money(body)

        if (type == Type.EARLY) {
            if (remainAmount != Money(0))
                recalculate()
        } else
            lastDate = lastDate.plusMonths(1)

        payments.add(
            PaidPayment(
                payment,
                State.PAID,
                Money(percent),
                Money(body),
                remainAmount,
                Money(getEarlyRepayment(payment.date))
            )
        )
    }

    //TODO write month check
    //TODO write like 30-28 check
    private fun getType(payment: PayPaymentInterface): Type {
        return if (payment.date.dayOfMonth == agreementAt.dayOfMonth)
            Type.REGULAR
        else
            Type.EARLY
    }

    private fun recalculate() {
        val annuityPayment = getAnnuityPayment()
        rounding = getRounding(annuityPayment)
        regularPayment = getRegularPayment(annuityPayment)
        println(regularPayment)
    }

    /** @see <a href="http://mobile-testing.ru/wp-content/uploads/2013/03/percent_year.jpg">Percent</a> */
    private fun getPercentPayment(now: LocalDate): Double {
        val daysFirst = if (lastDate.isLeapYear) 366 else 365
        val daysSecond = if (now.isLeapYear) 366 else 365
        return percent / 100.0 / daysFirst * (lastDate.lengthOfMonth() - lastDate.dayOfMonth + 1) +
                percent / 100.0 / daysSecond * (now.dayOfMonth - 1)
    }

    private fun getEarlyRepayment(now: LocalDate): Long {
        return ((1 + getPercentPayment(now)) * remainAmount.value).roundToLong()
    }

    private fun getAnnuityPayment(): Double {

        val monthPercent = percent.toDouble() / 12.0 / 100.0
        val power = (1.0 + monthPercent).pow(duration)

        return remainAmount.value.toDouble() * monthPercent * (power / (power - 1.0))
    }

    private fun getRounding(annuityPayment: Double): Int {

        if (annuityPayment < 100.0) {
            throw PaymentLessThanMinimalException()
        }

        arrayOf(100.0, 10.0, 1.0).forEach {
            if ((it - (annuityPayment % it)) * duration.toDouble() < annuityPayment) {
                return it.toInt()
            }
        }

        throw CreditAmountTooSmallException()
    }

    private fun getRegularPayment(annuityPayment: Double): Money {

        return Money((Math.ceil(annuityPayment / rounding.toDouble()) * rounding).toLong())
    }

    fun isClosed(): Boolean {
        return remainAmount == Money(0)
    }
}
