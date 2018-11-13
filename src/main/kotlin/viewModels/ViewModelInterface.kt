package viewModels

import valueObjects.ValidationInterface


interface ViewModelInterface {
    fun loadAndValidate(json: String): Boolean
    fun fetch(): String
    fun getValidation(): ValidationInterface?
}
