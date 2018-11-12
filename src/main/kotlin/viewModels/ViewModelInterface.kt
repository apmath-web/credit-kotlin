package viewModels

import valueObjects.ValidationInterface


interface ViewModelInterface {
    fun fill(json: Any): Boolean
    fun fetch(): Any
    fun validate(): ValidationInterface
    fun getValidation(): ValidationInterface?
}
