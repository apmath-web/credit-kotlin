package models

import valueObjects.PaymentInterface
import valueObjects.PersonInterface
import java.util.*


interface CreditInterface {
    fun getId(): Int?
    fun getPerson(): PersonInterface
    fun getAmount(): data.Money
    fun getAgreementAt(): Date
    fun getCurrency(): data.Currency
    fun getDuration(): Int
    fun getPercent(): Int
    fun getRounding(): Int
    fun getRemainAmount(): data.Money
    fun getPayments(type: String?, state: String?): List<PaymentInterface>
    fun writeOf(payment: PaymentInterface)
}
