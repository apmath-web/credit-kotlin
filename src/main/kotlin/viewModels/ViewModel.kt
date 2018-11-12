package viewModels

import valueObjects.MessageInterface
import valueObjects.Validation
import valueObjects.ValidationInterface

abstract class ViewModel : ViewModelInterface {

    private var json : Any? = null
    private var innerValidation : ValidationInterface? = null

    override fun fill(json: Any): Boolean {
        // TODO @malinink
        // fill json with data
        return true;
    }

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
