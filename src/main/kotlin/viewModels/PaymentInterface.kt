package viewModels

import domain.data.Type
import domain.data.Money
import domain.data.Currency
import domain.data.State
import java.time.LocalDate
import domain.valueObjects.PaymentInterface as PaymentValueObjectInterface

interface PaymentInterface : ViewModelInterface {
    fun hydrate(person: PaymentValueObjectInterface)
    val payment: Money
    val type: Type
    val currency: Currency
    val date: LocalDate
    val state: State
}
