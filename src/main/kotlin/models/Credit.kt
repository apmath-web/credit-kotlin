package models

import valueObjects.PaymentInterface
import valueObjects.PersonInterface
import java.time.LocalDate


class Credit(
    person: PersonInterface,
    amount: data.Money,
    agreementAt: LocalDate,
    currency: data.Currency,
    duration: Int,
    percent: Int
) : CreditInterface {
    override var id: Int? = null
        set(value) {
            if (field == null) {
                field = value
            }
        }
    override val person: PersonInterface = person
    override val amount: data.Money = amount
    override val agreementAt: LocalDate = agreementAt
    override val currency: data.Currency = currency
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
