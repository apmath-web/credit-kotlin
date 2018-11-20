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
            .append(FIRST_NAME, firstName)
            .append(LAST_NAME, lastName)
    }

    override fun hydrate(person: valueObjects.PersonInterface) {
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
        val raw = loadNotNullRequiredField(json, FIRST_NAME) ?: return false

        if (raw !is String) {
            addMessage(Message(MESSAGE_NOT_STRING, FIRST_NAME))
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
        val raw = loadNotNullRequiredField(json, LAST_NAME) ?: return false

        if (raw !is String) {
            addMessage(Message(MESSAGE_NOT_STRING, LAST_NAME))
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
