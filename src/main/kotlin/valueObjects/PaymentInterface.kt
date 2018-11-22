package valueObjects

import java.util.*


interface PaymentInterface {
    fun getPayment(): data.Money
    fun getType(): data.Type
    fun getCurrency(): data.Currency
    fun getDate(): Date
    fun getState(): data.State
    fun getPercent(): data.Money
    fun getBody(): data.Money
    fun getRemainCreditBody(): data.Money
    fun getFullEarlyRepayment(): data.Money
}
