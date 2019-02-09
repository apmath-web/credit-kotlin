package viewModels

import domain.data.Currency
import domain.data.Money
import domain.data.Type
import java.time.LocalDate
import domain.valueObjects.PaymentRequestInterface as PaymentRequestValueObjectInterface

interface PaymentRequestInterface : ViewModelInterface {
    val type: Type?
    val date: LocalDate?
    val currency: Currency?
    val payment: Money?
}
