package viewModels

import domain.exceptions.WrongFieldException
import org.json.*
import domain.valueObjects.Message
import domain.valueObjects.MessageInterface
import domain.valueObjects.Validation
import domain.valueObjects.ValidationInterface
import exceptions.BadRequestException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

abstract class ViewModel : ViewModelInterface {

    override val validation: ValidationInterface = Validation()

    override fun loadAndValidate(json: String): Boolean {
        val jsonObject: JSONObject
        try {
            jsonObject = JSONObject(json)
        } catch (e: JSONException) {
            throw BadRequestException(MESSAGE_INVALID_JSON)
        }
        return loadAndValidate(jsonObject)
    }

    abstract fun loadAndValidate(json: JSONObject): Boolean

    override fun fetch(): String {
        return fetchJson().toString()
    }

    protected fun addMessage(message: MessageInterface) {
        validation.addMessage(message)
    }

    protected fun loadNotNullRequiredField(json: JSONObject, field: String): Any? {
        if (!json.has(field)) {
            addMessage(Message(field, MESSAGE_REQUIRED))
            return null
        }

        val raw = json.get(field)
        if (raw == null) {
            addMessage(Message(field, MESSAGE_NOT_NULL))
            return null
        }
        return raw
    }

    protected fun loadNullableNotRequiredField(json: JSONObject, field: String): Any {
        return if (!json.has(field) || json.get(field) == JSONObject.NULL) {
            when (field) {
                "type"      -> "REGULAR"
                "currency"  -> "USD"
                "date"      -> LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                else        -> WrongFieldException(field)
            }
        } else
            json.get(field)
    }

    companion object {
        const val DATE_FORMATE                  = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$"
        const val MESSAGE_INVALID_JSON          = "Invalid JSON format"
        const val MESSAGE_REQUIRED              = "Is required"
        const val MESSAGE_NOT_NULL              = "Must be not null"
        const val MESSAGE_NOT_OBJECT            = "Must be an object"
        const val MESSAGE_NOT_STRING            = "Must be a string"
        const val MESSAGE_NOT_LONG              = "Must be a 64bit number"
        const val MESSAGE_NOT_INT               = "Must be a number"
        const val MESSAGE_NOT_DATE              = "Must be a YYYY-MM-DD date"
        const val MESSAGE_DATE_INVALID          = "Must be a valid date"
        const val MESSAGE_CURRENCY_UNKNOWN      = "Must be a valid currency ['RUR', 'USD', 'EUR'] allowed"
        const val MESSAGE_TYPE_UNKNOWN          = "Must be a valid type ['REGULAR', 'EARLY', 'NEXT'] allowed"
    }
}
