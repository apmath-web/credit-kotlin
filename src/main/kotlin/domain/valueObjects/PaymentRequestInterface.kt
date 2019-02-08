package domain.valueObjects

import domain.data.Currency
import domain.data.Money
import domain.data.Type
import java.time.LocalDate

interface PaymentRequestInterface {
    val payment: Money
    val type: Type
    val currency: Currency?
    val date: LocalDate?
}
