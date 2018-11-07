package viewModels

import java.util.*
import valueObjects.PaymentInterface as PaymentValueObjectInterface


interface PaymentInterface : ViewModelInterface {
    fun hydrate(person: PaymentValueObjectInterface)
    fun getPayment(): String
    fun getType(): String
    fun getCurrency(): String
    fun getDate(): String
    fun getState(): String
    fun getPercent(): String
    fun getBody(): Currency
    fun getRemainCreditBody(): Currency
    fun getFullEarlyRepayment(): Currency
}
