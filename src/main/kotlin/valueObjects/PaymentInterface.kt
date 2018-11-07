package valueObjects

import java.util.*


interface PaymentInterface {
    fun getPayment(): Currency
    fun getType(): data.Type
    fun getCurrency(): data.Currency
    fun getDate(): Date
    fun getState(): data.State
    fun getPercent(): Currency
    fun getBody(): Currency
    fun getRemainCreditBody(): Currency
    fun getFullEarlyRepayment(): Currency
}
