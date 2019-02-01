package domain.valueObjects

import domain.data.Type
import domain.data.State
import domain.data.Money
import domain.data.Currency
import java.time.LocalDate

class Payment(
    override val payment: Money,
    override val type: Type,
    override val currency: Currency,
    override val date: LocalDate,
    override val state: State,
    override val percent: Money,
    override val body: Money,
    override val remainCreditBody: Money,
    override val fullEarlyRepayment: Money
) : PaymentInterface
