package domain.valueObjects.payment

import domain.data.Money
import domain.data.State

interface PaidPaymentInterface : PayPaymentInterface {
    val state: State
    val percent: Money
    val body: Money
    val remainCreditBody: Money
    val fullEarlyRepayment: Money
}