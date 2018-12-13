package viewModels

import org.json.JSONObject
import domain.valueObjects.ValidationInterface


interface ViewModelInterface {
    val validation: ValidationInterface

    fun loadAndValidate(json: String): Boolean
    fun fetch(): String
    fun fetchJson(): JSONObject
}
