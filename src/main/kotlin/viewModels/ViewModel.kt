package viewModels

import org.json.*
import domain.valueObjects.Message
import domain.valueObjects.MessageInterface
import domain.valueObjects.Validation
import domain.valueObjects.ValidationInterface
import exceptions.BadRequestException


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

    /**
     * Function loads field value
     * Main concept is that required and not null field could not have default value, cause we should return validation message on that
     * All cases affects NULL case or case when field not present in JSON at all
     *
     * So, there is three use cases:
     *  - retrieve null and add validation message, do it by passing first two arguments
     *  - retrieve default value, when field not present or equals null, do it by passing default value (you could skip required and notNull flags)
     *  - retrieve null, and do not cause validation message, do it by passing false for both: required and notNull
     */
    protected fun loadField(json: JSONObject, field: String, required: Boolean = true, notNull: Boolean = true, default: Any? = null): Any? {
        val isRequired  = if (default == null) { required } else { false }
        val isNotNull   = if (default == null) { notNull } else { false }

        if (!json.has(field)) {
            if (isRequired) {
                addMessage(Message(field, MESSAGE_REQUIRED))
            }
            return default
        }

        val raw = json.get(field)
        if (raw == JSONObject.NULL) {
            if (isNotNull) {
                addMessage(Message(field, MESSAGE_NOT_NULL))
            }
            return default
        }
        return raw
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
