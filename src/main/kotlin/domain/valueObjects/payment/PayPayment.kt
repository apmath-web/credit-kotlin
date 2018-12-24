package domain.valueObjects.payment

import domain.data.Currency
import domain.data.Money
import domain.data.Type
import java.time.LocalDate

open class PayPayment(
    override val payment: Money,
    override val type: Type,
    override val currency: Currency,
    override val date: LocalDate
) : PayPaymentInterface
