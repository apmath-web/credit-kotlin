package viewModels

import domain.data.*
import domain.valueObjects.Message
import exceptions.runtime.RestrictedMethodCallException
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import domain.valueObjects.PaymentRequestInterface as PaymentRequestValueObjectInterface

class PaymentRequest : ViewModel(), PaymentRequestInterface {
    override var type: Type? = null
    override var date: LocalDate? = null
    override var currency: Currency? = null
    override var payment: Money? = null

    override fun loadAndValidate(json: JSONObject): Boolean {
        return loadAndValidatePayment(json)
            .and(loadAndValidateType(json))
            .and(loadAndValidateCurrency(json))
            .and(loadAndValidateDate(json))
    }

    private fun loadAndValidateDate(json: JSONObject): Boolean {
        val raw = loadField(json, DATE, false, false) ?: return true

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

    override fun fetchJson(): JSONObject {
        throw RestrictedMethodCallException()
    }

    companion object {
        const val TYPE                  = "type"
        const val DATE                  = "date"
        const val CURRENCY              = "currency"
        const val PAYMENT               = "payment"
    }
}
