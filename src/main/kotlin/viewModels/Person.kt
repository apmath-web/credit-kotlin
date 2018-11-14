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

    override fun validate(json: JSONObject): Boolean {
        return loadAndValidateFirstName(json)
                && loadAndValidateLastName(json)
    }

    private fun loadAndValidateFirstName(json: JSONObject): Boolean
    {
        if (!json.has("firstName")) {
            addMessage(Message("Is required", "firstName"))
            return false
        }

        val raw = json.get("firstName")
        if (raw !is String) {
            addMessage(Message("Must be a string", "firstName"))
            return false
        }

        val string = raw as String
        if (string?.length ?: 0 < 1) {
            addMessage(Message("Must be at least 1 character long", "firstName"))
            return false
        }

        firstName = string
        return true
    }

    private fun loadAndValidateLastName(json: JSONObject): Boolean
    {
        if (!json.has("lastName")) {
            addMessage(Message("Is required", "lastName"))
            return false
        }

        val raw = json.get("lastName")
        if (raw !is String) {
            addMessage(Message("Must be a string", "lastName"))
            return false
        }

        val string = raw as String
        if (string?.length ?: 0 < 1) {
            addMessage(Message("Must be at least 1 character long", "lastName"))
            return false
        }

        lastName = string
        return true
    }
}
