package domain.models

import domain.data.Money
import domain.data.Currency
import domain.exceptions.ChangeIdentifiedCreditIdException
import domain.valueObjects.PaymentInterface
import domain.valueObjects.PersonInterface
import java.time.LocalDate


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

    override fun getPayments(type: String?, state: String?): List<PaymentInterface> {
        // TODO
        return arrayListOf()
    }

    override fun writeOf(payment: PaymentInterface) {
        // TODO
    }
}
