package viewModels

import domain.data.Type
import domain.data.Money
import domain.data.Currency
import domain.data.State
import java.time.LocalDate
import domain.valueObjects.PaymentInterface as PaymentValueObjectInterface


interface PaymentInterface : ViewModelInterface {
    fun hydrate(person: PaymentValueObjectInterface)
    fun getPayment(): Money
    fun getType(): Type
    fun getCurrency(): Currency
    fun getDate(): LocalDate
    fun getState(): State
}
