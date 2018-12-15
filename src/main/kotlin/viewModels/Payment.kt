package viewModels

import domain.data.Currency
import domain.data.Money
import domain.data.State
import domain.data.Type
import domain.valueObjects.Message
import domain.valueObjects.payment.PaidPaymentInterface
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import domain.valueObjects.PaymentInterface as PaymentInterfaceValueObject

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

    override fun hydrate(payment: PaidPaymentInterface) {
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
        return loadAndValidatePayment(json)
            .and(loadAndValidateType(json))
            .and(loadAndValidateCurrency(json))
            .and(loadAndValidateDate(json))
    }

    private fun loadAndValidateDate(json: JSONObject): Boolean {
        val raw = loadField(json, DATE, default = LocalDate.now()) ?: return true

        if (raw !is String) {
            addMessage(Message(DATE, MESSAGE_NOT_STRING))
            return false
        }

        if (!Regex(DATE_FORMATE).matches(raw)) {
            addMessage(Message(DATE, MESSAGE_NOT_DATE))
            return false
        }

        try {
            date = LocalDate.parse(raw, DateTimeFormatter.ISO_DATE)
        } catch (e: DateTimeParseException) {
            addMessage(Message(DATE, MESSAGE_DATE_INVALID))
            return false
        }

        return true
    }

    private fun loadAndValidateCurrency(json: JSONObject): Boolean {
        val raw = loadField(json, CURRENCY, false, false) ?: return true

        if (raw !is String) {
            addMessage(Message(CURRENCY, MESSAGE_NOT_STRING))
            return false
        }

        try {
            currency = Currency.valueOf(raw)
        } catch (e: IllegalArgumentException) {
            addMessage(Message(CURRENCY, MESSAGE_CURRENCY_UNKNOWN))
            return false
        }

        return true
    }

    private fun loadAndValidateType(json: JSONObject): Boolean {
        val raw = loadField(json, TYPE, default = "regular")

        if (raw !is String) {
            addMessage(Message(TYPE, MESSAGE_NOT_STRING))
            return false
        }

        if (raw.toLowerCase() != raw) {
            addMessage(Message(TYPE, MESSAGE_TYPE_UNKNOWN))
            return false
        }

        try {
            val rawType = Type.valueOf(raw.toUpperCase())
            // NEXT is not allowed as income value due to specification
            if (rawType == Type.NEXT) {
                throw IllegalArgumentException()
            }
            type = rawType
        } catch (e: IllegalArgumentException) {
            addMessage(Message(TYPE, MESSAGE_TYPE_UNKNOWN))
            return false
        }

        return true
    }

    private fun loadAndValidatePayment(json: JSONObject): Boolean {
        val raw = loadField(json, PAYMENT) ?: return false

        if ((raw !is Long) && (raw !is Int)) {
            addMessage(Message(PAYMENT, MESSAGE_NOT_INT))
            return false
        }

        val long: Long = if (raw is Int) raw.toLong() else raw as Long

        if (long < 1 || long > 3750000000000000) {
            addMessage(Message(PAYMENT, "Must be between 1 and 3750000000000000"))
            return false
        }

        payment = Money(long)
        return true
    }

    override fun fetch(): String {
        return fetchJson().toString()
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
