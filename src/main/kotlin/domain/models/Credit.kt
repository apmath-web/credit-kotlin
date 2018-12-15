package domain.models

import domain.data.Money
import domain.data.State
import domain.data.Type
import domain.exceptions.ChangeIdentifiedCreditIdException
import domain.exceptions.CreditAmountTooSmallException
import domain.exceptions.PaymentLessThanMinimalException
import domain.valueObjects.PersonInterface
import domain.valueObjects.payment.PaidPayment
import domain.valueObjects.payment.PaidPaymentInterface
import domain.valueObjects.payment.PayPaymentInterface
import java.time.LocalDate
import java.time.temporal.ChronoUnit
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
        println(annuityPayment)
        println(rounding)
        println(regularPayment)
    }

    override fun getPayments(type: Type?, state: State?): MutableList<PaidPaymentInterface> {
        return payments
    }

    //TODO Update with different currency
    //TODO Update with early payments
    override fun writeOf(payment: PayPaymentInterface) {
        if (payment.currency != currency)
            throw Exception("Currency")//TODO remove exception

        val percent: Long = (remainAmount.value * getPercentPayment(payment.date)).roundToLong()
        val body: Long = payment.payment.value - percent

        if (percent == 0L)
            throw Exception("Same day")//TODO remove exception
        if (payment.payment == remainAmount) {
            percent.minus(remainAmount.value % 10)
        } else
            if (payment.payment.value < 100)
                throw Exception("Min pay >= 100")//TODO create new exception

        if (Money(body) > remainAmount)
            throw Exception("Currency")//TODO create new exception
        remainAmount -= Money(body)

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

        //TODO recalculate credit
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
