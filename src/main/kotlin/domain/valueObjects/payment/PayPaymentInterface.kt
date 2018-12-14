package domain.valueObjects.payment

import domain.data.Currency
import domain.data.Money
import domain.data.Type
import java.time.LocalDate

interface PayPaymentInterface {
    val payment: Money
    val type: Type
    val currency: Currency
    val date: LocalDate
}