package viewModels

import org.json.JSONObject
import valueObjects.ValidationInterface


interface ViewModelInterface {
    fun loadAndValidate(json: String): Boolean
    fun loadAndValidate(json: JSONObject): Boolean
    fun fetch(): String
    val validation: ValidationInterface
}
