package domain.models

import domain.data.Money
import domain.data.Currency
import domain.valueObjects.PaymentInterface
import domain.valueObjects.PersonInterface
import java.time.LocalDate


interface CreditInterface {
    var id: Int?
    val person: PersonInterface
    val amount: Money
    val agreementAt: LocalDate
    val currency: Currency
    val duration: Int
    val percent: Int

    fun getPayments(type: String?, state: String?): List<PaymentInterface>
    fun writeOf(payment: PaymentInterface)
}
