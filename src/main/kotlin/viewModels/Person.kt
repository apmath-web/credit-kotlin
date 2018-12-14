package viewModels

import org.json.JSONObject
import domain.valueObjects.Message


class Person : ViewModel(), PersonInterface {
    override var firstName: String? = null
        private set

    override var lastName: String? = null
        private set

    override fun fetchJson(): JSONObject {
        return JSONObject()
            .put(FIRST_NAME, firstName)
            .put(LAST_NAME, lastName)
    }

    override fun hydrate(person: domain.valueObjects.PersonInterface) {
        firstName = person.firstName
        lastName = person.lastName
    }

    override fun loadAndValidate(json: JSONObject): Boolean {
        return arrayOf(
            loadAndValidateFirstName(json),
            loadAndValidateLastName(json)
        ).reduce { a, b -> a && b }
    }

    private fun loadAndValidateFirstName(json: JSONObject): Boolean
    {
        val raw = loadField(json, FIRST_NAME) ?: return false

        if (raw !is String) {
            addMessage(Message(FIRST_NAME, MESSAGE_NOT_STRING))
            return false
        }

        val string: String = raw
        if (string.isEmpty()) {
            addMessage(Message(FIRST_NAME, STRING_1_CHARACTER))
            return false
        }

        firstName = string
        return true
    }

    private fun loadAndValidateLastName(json: JSONObject): Boolean
    {
        val raw = loadField(json, LAST_NAME) ?: return false

        if (raw !is String) {
            addMessage(Message(LAST_NAME, MESSAGE_NOT_STRING))
            return false
        }

        val string: String = raw
        if (string.isEmpty()) {
            addMessage(Message(LAST_NAME, STRING_1_CHARACTER))
            return false
        }

        lastName = string
        return true
    }

    companion object {
        const val STRING_1_CHARACTER = "Must be at least 1 character long"

        const val FIRST_NAME    = "firstName"
        const val LAST_NAME     = "lastName"
    }
}
