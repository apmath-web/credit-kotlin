package viewModels

import valueObjects.ValidationInterface


interface ViewModelInterface {
    fun load(json: Any): Boolean
    fun unload(): Any
    fun validate(): Boolean
    fun getValidation(): ValidationInterface
}
