package viewModels

import domain.data.Currency
import domain.data.Money
import domain.data.State
import domain.data.Type
import java.time.LocalDate
import domain.valueObjects.PaymentInterface as PaymentValueObjectInterface

interface PaymentInterface : ViewModelInterface {
    val type: Type?
    val state: State?
    val date: LocalDate?
    val currency: Currency?
    val payment: Money?
    val percent: Money?
    val body: Money?
    val remainCreditBody: Money?
    val fullEarlyRepayment: Money?

    fun hydrate(payment: PaymentValueObjectInterface)
}
