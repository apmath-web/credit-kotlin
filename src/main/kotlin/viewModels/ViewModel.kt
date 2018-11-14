package viewModels

import org.json.*
import valueObjects.MessageInterface
import valueObjects.Validation
import valueObjects.ValidationInterface

abstract class ViewModel : ViewModelInterface {

    override val validation: ValidationInterface = Validation()

    override fun loadAndValidate(json: String): Boolean {
        // TODO use pretty try/catch here
        return validate(JSONObject(json))
    }

    fun loadAndValidate(json: JSONObject): Boolean {
        return validate(JSONObject(json))
    }

    protected abstract fun validate(json: JSONObject): Boolean

    override fun fetch(): String
    {
        return fetchJson().toString()
    }

    abstract fun fetchJson(): JSONObject

    protected fun addMessage(message : MessageInterface) {
        validation.addMessage(message)
    }
}
