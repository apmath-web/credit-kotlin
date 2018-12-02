package domain.models

import domain.data.Money
import domain.data.Currency
import domain.exceptions.ChangeIdentifiedCreditIdException
import domain.exceptions.CreditAmountTooSmallException
import domain.exceptions.PaymentLessThanMinimalException
import domain.valueObjects.PaymentInterface
import domain.valueObjects.PersonInterface
import java.time.LocalDate
import kotlin.math.pow


class Credit(
    person: PersonInterface,
    amount: domain.data.Money,
    agreementAt: LocalDate,
    currency: domain.data.Currency,
    duration: Int,
    percent: Int
) : CreditInterface {
    override var id: Int? = null
        set(value) {
            if (field == null) {
                field = value
            } else {
                throw ChangeIdentifiedCreditIdException()
            }
        }
    override val person: PersonInterface = person
    override val amount: Money = amount
    override val agreementAt: LocalDate = agreementAt
    override val currency: Currency = currency
    override val duration: Int = duration
    override val percent: Int = percent

    private val rounding: Int
    private val currentRegularPayment: Money

    init {
        val annuityPayment = getAnnuityPayment()
        rounding = getRounding(annuityPayment)
        currentRegularPayment = getRegularPayment(annuityPayment)
    }

    override fun getPayments(type: String?, state: String?): List<PaymentInterface> {
        // TODO
        return arrayListOf()
    }

    override fun writeOf(payment: PaymentInterface) {
        // TODO
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
