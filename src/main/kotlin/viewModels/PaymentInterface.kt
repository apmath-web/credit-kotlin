package viewModels

import java.util.*
import valueObjects.PaymentInterface as PaymentValueObjectInterface


interface PaymentInterface : ViewModelInterface {
    fun hydrate(person: PaymentValueObjectInterface)
    fun getPayment(): data.Money
    fun getType(): data.Type
    fun getCurrency(): data.Currency
    fun getDate(): Date
    fun getState(): data.State
}
