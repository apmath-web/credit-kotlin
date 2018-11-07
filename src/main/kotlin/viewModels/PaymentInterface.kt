package viewModels

import valueObjects.PaymentInterface as PaymentValueObjectInterface


interface PaymentInterface : ViewModelInterface {
    fun hydrate(person: PaymentValueObjectInterface)
    fun getPayment(): String
    fun getType(): String
    fun getCurrency(): String
    fun getDate(): String
    fun getState(): String
}
