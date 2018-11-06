package models

import valueobjects.PaymentInterface
import valueobjects.PersonInterface
import java.math.BigInteger
import java.util.*


interface CreditInterface {
    fun getId(): Int
    fun getPerson(): PersonInterface
    fun getAmount(): BigInteger
    fun getAgreementAt(): Date
    fun getCurrency()
    fun getDuration(): Int
    fun getPercent(): Int
    fun getRounding(): Int

    fun getRemainAmount(): Currency
    fun getPayments(type: String, state: String): List<PaymentInterface>

    fun writeOf(payment: PaymentInterface)
}
