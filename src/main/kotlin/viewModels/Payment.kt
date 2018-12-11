package viewModels

import domain.data.Currency
import domain.data.Money
import domain.data.State
import domain.data.Type
import domain.valueObjects.Message
import domain.valueObjects.Validation
import domain.valueObjects.ValidationInterface
import domain.valueObjects.PaymentInterface as PaymentInterfaceValueObject
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class Payment : ViewModel(), PaymentInterface {
    override var type: Type? = null
    override var currency: Currency? = null
    override var date: LocalDate? = null
    override var state: State? = null
    override var payment: Money? = null
    override val validation: ValidationInterface = Validation()

    override fun hydrate(payment: PaymentInterfaceValueObject) {
        this.payment = payment.payment
        type = payment.type
        currency = payment.currency
        date = payment.date
        state = payment.state
    }

    override fun loadAndValidate(json: JSONObject): Boolean {
        return loadAndValidatePayment(json)
            .and(loadAndValidateType(json))
            .and(loadAndValidateCurrency(json))
            .and(loadAndValidateDate(json))
    }

    private fun loadAndValidateDate(json: JSONObject): Boolean {
        val raw = loadNotNullRequiredField(json, DATE) ?: return false

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
        val raw = loadNotNullRequiredField(json, CURRENCY) ?: return false

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
        val raw = loadNotNullRequiredField(json, TYPE) ?: return false

        if (raw !is String) {
            addMessage(Message(TYPE, MESSAGE_NOT_STRING))
            return false
        }

        try {
            type = Type.valueOf(raw)
        } catch (e: IllegalArgumentException) {
            addMessage(Message(TYPE, MESSAGE_TYPE_UNKNOWN))
            return false
        }

        return true
    }

    private fun loadAndValidatePayment(json: JSONObject): Boolean {
        val raw = loadNotNullRequiredField(json, PAYMENT) ?: return false

        if ((raw !is Long) && (raw !is Int)) {
            addMessage(Message(PAYMENT, MESSAGE_NOT_INT))
            return false
        }

        val long: Long = if (raw is Int) raw.toLong() else raw as Long

        if (long < 1 || long > 3000000000000000) {
            addMessage(Message(PAYMENT, "Must be between 1 and 3000000000000000"))
            return false
        }

        payment = Money(long)
        return true
    }

    override fun fetch(): String {
        return fetchJson().toString()
    }

    override fun fetchJson(): JSONObject {
        return JSONObject()
            .put(PAYMENT, payment)
            .put(TYPE, type)
            .put(CURRENCY, currency)
            .put(DATE, date)
    }

    companion object {
        const val PAYMENT = "payment"
        const val TYPE = "type"
        const val CURRENCY = "currency"
        const val DATE = "date"
    }

}