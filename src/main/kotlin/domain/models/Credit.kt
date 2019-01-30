package domain.models

import domain.data.Money
import domain.data.State
import domain.data.Type
import domain.exceptions.ChangeIdentifiedCreditIdException
import domain.exceptions.CreditAmountTooSmallException
import domain.exceptions.PaymentLessThanMinimalException
import domain.valueObjects.PaymentInterface
import domain.valueObjects.PersonInterface
import java.time.LocalDate
import kotlin.math.pow


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
                    // filter current payments list
                }
                null -> {
                    // add all payments into result
                }
                Type.NEXT -> {
                    // no results for that case
                }
            }
        }

        if (state == State.UPCOMING || state == null) {
            when (type) {
                Type.NEXT -> {
                    // only next payment
                }
                Type.REGULAR, null -> {
                    // all next payments
                }
                Type.EARLY -> {
                    // no results for that case
                }
            }
        }

        return results
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
