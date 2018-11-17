package viewModels

import org.json.*
import valueObjects.Message
import valueObjects.MessageInterface
import valueObjects.Validation
import valueObjects.ValidationInterface


abstract class ViewModel : ViewModelInterface {

    override val validation: ValidationInterface = Validation()

    override fun loadAndValidate(json: String): Boolean {
        val jsonObject: JSONObject
        try {
            jsonObject = JSONObject(json)
        } catch (e: JSONException) {
            addMessage(Message(INVALID_JSON))
            return false
        }
        return loadAndValidate(jsonObject)
    }

    abstract fun loadAndValidate(json: JSONObject): Boolean

    override fun fetch(): String
    {
        return fetchJson().toString()
    }

    abstract fun fetchJson(): JSONObject

    protected fun addMessage(message : MessageInterface) {
        validation.addMessage(message)
    }

    companion object {
        const val INVALID_JSON  = "Invalid JSON format"
        const val REQUIRED      = "Is required"
        const val NOT_NULL      = "Must be not null"
        const val NOT_OBJECT    = "Must be an object"
        const val STRING        = "Must be a string"
    }
}
