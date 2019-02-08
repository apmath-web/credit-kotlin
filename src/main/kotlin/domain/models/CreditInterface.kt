package domain.models

import domain.data.Money
import domain.data.Currency
import domain.data.State
import domain.data.Type
import domain.valueObjects.PersonInterface
import domain.valueObjects.PaymentInterface
import domain.valueObjects.PaymentRequestInterface
import java.time.LocalDate


interface CreditInterface {
    var id: Int?
    val person: PersonInterface
    val amount: Money
    val agreementAt: LocalDate
    val currency: Currency
    val duration: Int
    val percent: Int
    val isFinished: Boolean

    fun getPayments(type: Type?, state: State?): List<PaymentInterface>
    fun writeOf(paymentRequest: PaymentRequestInterface)
}
