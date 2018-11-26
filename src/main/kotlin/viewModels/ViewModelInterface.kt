package viewModels

import org.json.JSONObject
import domain.valueObjects.ValidationInterface


interface ViewModelInterface {
    fun loadAndValidate(json: String): Boolean
    fun fetch(): String
    fun fetchJson(): JSONObject
    val validation: ValidationInterface
}
