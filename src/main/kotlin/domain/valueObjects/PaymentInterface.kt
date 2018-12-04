package domain.valueObjects

import domain.data.Type
import domain.data.State
import domain.data.Money
import domain.data.Currency
import java.time.LocalDate

interface PaymentInterface {
    val payment: Money
    val type: Type
    val currency: Currency
    val date: LocalDate
    val state: State
    val percent: Money
    val body: Money
    val remainCreditBody: Money
    val fullEarlyRepayment: Money
}
