package domain.valueObjects

import domain.data.Type
import domain.data.State
import domain.data.Money
import domain.data.Currency
import java.time.LocalDate


interface PaymentInterface {
    fun getPayment(): Money
    fun getType(): Type
    fun getCurrency(): Currency
    fun getDate(): LocalDate
    fun getState(): State
    fun getPercent(): Money
    fun getBody(): Money
    fun getRemainCreditBody(): Money
    fun getFullEarlyRepayment(): Money
}
