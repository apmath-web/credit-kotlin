package viewModels

import org.json.JSONObject
import valueObjects.Message

class Person : ViewModel(), PersonInterface {
    override var firstName: String? = null
        private set

    override var lastName: String? = null
        private set

    override fun fetchJson(): JSONObject {
        return JSONObject()
            .append("firstName", firstName)
            .append("lastName", lastName)
    }

    override fun hydrate(person: valueObjects.PersonInterface) {
        firstName = person.firstName
        lastName = person.lastName
    }

    override fun loadAndValidate(json: JSONObject): Boolean {
        return loadAndValidateFirstName(json)
                && loadAndValidateLastName(json)
    }

    private fun loadAndValidateFirstName(json: JSONObject): Boolean
    {
        if (!json.has(FIRST_NAME)) {
            addMessage(Message(REQUIRED, FIRST_NAME))
            return false
        }

        val raw = json.get(FIRST_NAME)
        if (raw == null) {
            addMessage(Message(NOT_NULL, FIRST_NAME))
            return false
        }

        if (raw !is String) {
            addMessage(Message(STRING, FIRST_NAME))
            return false
        }

        val string: String = raw
        if (string.isEmpty()) {
            addMessage(Message(STRING_1_CHARACTER, FIRST_NAME))
            return false
        }

        firstName = string
        return true
    }

    private fun loadAndValidateLastName(json: JSONObject): Boolean
    {
        if (!json.has(LAST_NAME)) {
            addMessage(Message(REQUIRED, LAST_NAME))
            return false
        }

        val raw = json.get(LAST_NAME)
        if (raw == null) {
            addMessage(Message(NOT_NULL, LAST_NAME))
            return false
        }

        if (raw !is String) {
            addMessage(Message(STRING, LAST_NAME))
            return false
        }

        val string: String = raw
        if (string.isEmpty()) {
            addMessage(Message(STRING_1_CHARACTER, LAST_NAME))
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
