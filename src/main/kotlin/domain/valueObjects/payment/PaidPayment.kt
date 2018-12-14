package domain.valueObjects.payment

import domain.data.Currency
import domain.data.Money
import domain.data.State
import domain.data.Type
import java.time.LocalDate

class PaidPayment : PayPayment, PaidPaymentInterface {
    override val state: State
    override val percent: Money
    override val body: Money
    override val remainCreditBody: Money
    override val fullEarlyRepayment: Money

    constructor(
        payment: Money,
        type: Type,
        currency: Currency,
        date: LocalDate,
        state: State,
        percent: Money,
        body: Money,
        remainCreditBody: Money,
        fullEarlyRepayment: Money
    ) : super(payment, type, currency, date) {
        this.state = state
        this.percent = percent
        this.body = body
        this.remainCreditBody = remainCreditBody
        this.fullEarlyRepayment = fullEarlyRepayment
    }

    constructor(
        p: PayPaymentInterface,
        state: State,
        percent: Money,
        body: Money,
        remainCreditBody: Money,
        fullEarlyRepayment: Money
    ) : super(p.payment, p.type, p.currency, p.date) {
        this.state = state
        this.percent = percent
        this.body = body
        this.remainCreditBody = remainCreditBody
        this.fullEarlyRepayment = fullEarlyRepayment
    }
}