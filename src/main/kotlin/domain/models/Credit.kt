package domain.models

import domain.data.Money
import domain.data.Currency
import domain.exceptions.ChangeIdentifiedCreditIdException
import domain.valueObjects.PaymentInterface
import domain.valueObjects.PersonInterface
import java.time.LocalDate

class Credit(
    override val person: PersonInterface,
    override val amount: Money,
    override val agreementAt: LocalDate,
    override val currency: Currency,
    override val duration: Int,
    override val percent: Int
) : CreditInterface {
    private val payments: ArrayList<PaymentInterface> = arrayListOf()
    override var id: Int? = null
        set(value) {
            if (field == null) {
                field = value
            } else {
                throw ChangeIdentifiedCreditIdException()
            }
        }

    override fun getPayments(type: String?, state: String?): ArrayList<PaymentInterface> {
        return payments
    }

    override fun writeOf(payment: PaymentInterface) {
        payments.add(payment)
    }
}
