package viewModels

import domain.data.*
import exceptions.runtime.RestrictedMethodCallException
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import domain.valueObjects.PaymentInterface as PaymentValueObjectInterface

class Payment : ViewModel(), PaymentInterface {
    override var type: Type? = null
    override var state: State? = null
    override var date: LocalDate? = null
    override var currency: Currency? = null
    override var payment: Money? = null
    override var percent: Money? = null
    override var body: Money? = null
    override var remainCreditBody: Money? = null
    override var fullEarlyRepayment: Money? = null

    override fun hydrate(payment: PaymentValueObjectInterface) {
        type = payment.type
        state = payment.state
        date = payment.date
        currency = payment.currency
        this.payment = payment.payment
        percent = payment.percent
        body = payment.body
        remainCreditBody = payment.remainCreditBody
        fullEarlyRepayment = payment.fullEarlyRepayment
    }

    override fun loadAndValidate(json: JSONObject): Boolean {
        throw RestrictedMethodCallException()
    }

    override fun fetchJson(): JSONObject {
        return JSONObject().put(TYPE, type.toString().toLowerCase())
            .put(STATE, state.toString().toLowerCase())
            .put(DATE, date?.format(DateTimeFormatter.ISO_DATE))
            .put(CURRENCY, currency)
            .put(PAYMENT, payment?.value)
            .put(PERCENT, percent?.value)
            .put(BODY, body?.value)
            .put(REMAIN_CREDIT_BODY, remainCreditBody?.value)
            .put(FULL_EARLY_REPAYMENT, fullEarlyRepayment?.value)
    }

    companion object {
        const val TYPE                  = "type"
        const val STATE                 = "state"
        const val DATE                  = "date"
        const val CURRENCY              = "currency"
        const val PAYMENT               = "payment"
        const val PERCENT               = "percent"
        const val BODY                  = "body"
        const val REMAIN_CREDIT_BODY    = "remainCreditBody"
        const val FULL_EARLY_REPAYMENT  = "fullEarlyRepayment"
    }
}
