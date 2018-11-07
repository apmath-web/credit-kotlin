package valueObjects

import java.util.*


interface PaymentInterface {
    fun getPayment(): Currency
    fun getType()
    fun getCurrency()
    fun getDate(): Date
    fun getState()
    fun getPercent(): Currency
    fun getBody(): Currency
    fun getRemainCreditBody(): Currency
    fun getFullEarlyRepayment(): Currency
}
