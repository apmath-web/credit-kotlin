package models

import valueObjects.PaymentInterface
import valueObjects.PersonInterface
import java.time.LocalDate


interface CreditInterface {
    var id: Int?
    val person: PersonInterface
    val amount: data.Money
    val agreementAt: LocalDate
    val currency: data.Currency
    val duration: Int
    val percent: Int

    fun getPayments(type: String?, state: String?): List<PaymentInterface>
    fun writeOf(payment: PaymentInterface)
}
