package domain.models

import domain.data.Money
import domain.data.State
import domain.data.Type
import domain.exceptions.ChangeIdentifiedCreditIdException
import domain.exceptions.CreditAmountTooSmallException
import domain.exceptions.PaymentLessThanMinimalException
import domain.exceptions.PaymentMoreThanCreditAmontException
import domain.valueObjects.PersonInterface
import domain.valueObjects.payment.PaidPayment
import domain.valueObjects.payment.PaidPaymentInterface
import domain.valueObjects.payment.PayPaymentInterface
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.stream.Stream
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
    private val rounding: Int
    /**
     * @see <a href="https://docs.google.com/spreadsheets/d/1dQnuL1G1iUDinTugekdn79CxFYJv1sgswymuRsSowXU/edit#gid=0&range=G10">Округление итоговое</a>
     */
    private var regularPayment: Money
    /**
     * @see <a href="https://docs.google.com/spreadsheets/d/1dQnuL1G1iUDinTugekdn79CxFYJv1sgswymuRsSowXU/edit#gid=0&range=G10">Округление итоговое</a>
     */
    private val payments: MutableList<PaidPaymentInterface> = arrayListOf()
    private var remainAmount: Money = amount

    init {
        val annuityPayment = getAnnuityPayment()
        rounding = getRounding(annuityPayment)
        regularPayment = getRegularPayment(annuityPayment)
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

    //TODO Update with different currency
    //TODO Update with early payments
    override fun writeOf(payment: PayPaymentInterface) {
        if (payment.currency != currency)
            throw Exception("Currency")//TODO remove exception

        val percent: Long = (remainAmount.value * getPercentPayment(payment.date)).roundToLong()
        val body: Long = payment.payment.value - percent

        //TODO uncomment if necessary
        //if (percent == 0L)
        //    throw Exception("Same day") //create new exception
        if (payment.payment == remainAmount) {
            percent.minus(remainAmount.value % 10)
        }

        if (payment.payment.value < 100)
            throw PaymentLessThanMinimalException()

        if (Money(body) > remainAmount)
            throw PaymentMoreThanCreditAmontException()

        remainAmount -= Money(body)

        //TODO if (state==EARLY) recalculate

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

    private fun recalculate() {
        val annuityPayment = getAnnuityPayment()
        regularPayment = getRegularPayment(annuityPayment)
    }

    private fun getPercentPayment(now: LocalDate): Double {
        return percent / 365.0 / 100.0 * getDateRange(now)
    }

    private fun getDateRange(pending: LocalDate): Long {
        return if (payments.size == 0)
            getDateRange(agreementAt, pending)
        else
            getDateRange(payments[payments.lastIndex].date, pending)
    }

    private fun getDateRange(prev: LocalDate, now: LocalDate): Long {
        return ChronoUnit.DAYS.between(prev, now)
    }

    //not exactly
    private fun getEarlyRepayment(now: LocalDate): Long {
        return ((1 + getPercentPayment(now)) * remainAmount.value).toLong()
    }

    private fun getAnnuityPayment(): Double {

        val monthPercent = percent.toDouble() / 12.0 / 100.0
        val power = (1.0 + monthPercent).pow(duration)

        return amount.value.toDouble() * monthPercent * (power / (power - 1.0))
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
}
