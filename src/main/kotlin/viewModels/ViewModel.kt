package viewModels

import org.json.*
import valueObjects.MessageInterface
import valueObjects.Validation
import valueObjects.ValidationInterface

abstract class ViewModel : ViewModelInterface {

    private var innerValidation : ValidationInterface? = null

    override fun loadAndValidate(json: String): Boolean {
        // TODO use pretty try/catch here
        return validate(JSONObject(json))
    }

    protected abstract fun validate(json: JSONObject): Boolean

    override fun getValidation(): ValidationInterface? {
        return innerValidation
    }

    protected fun addMessage(message : MessageInterface) {
        initValidation().addMessage(message)
    }

    private fun initValidation(): ValidationInterface {
        if (innerValidation === null) {
            innerValidation = Validation()
        }
        return innerValidation as ValidationInterface
    }
}
