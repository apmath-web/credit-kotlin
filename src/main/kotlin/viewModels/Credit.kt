package viewModels

import org.json.*
import valueObjects.Message
import java.util.*
import valueObjects.PersonInterface as PersonValueObjectInterface
import valueObjects.Person as PersonValueObject
import models.CreditInterface as CreditModelInterface

class Credit : ViewModel(), CreditInterface {
    override var person: PersonValueObjectInterface? = null
        private set
    override var amount: data.Money? = null
        private set
    override var agreementAt: Date? = null
        private set
    override var currency: data.Currency? = null
        private set
    override var duration: Int? = null
        private set
    override var percent: Int? = null
        private set

    override fun fetchJson(): JSONObject {
        TODO("not implemented")
    }

    override fun hydrate(credit: CreditModelInterface) {
        TODO("not implemented")
    }

    override fun validate(json: JSONObject): Boolean {
        return loadAndValidatePerson(json)
                && loadAndValidateAmount(json)
                && loadAndValidateAgreementAt(json)
                && loadAndValidateCurrency(json)
                && loadAndValidateDuration(json)
                && loadAndValidatePercent(json)
    }

    private fun loadAndValidatePerson(json: JSONObject): Boolean
    {
        if (!json.has("person")) {
            addMessage(Message("Is required", "person"))
            return false
        }
        val raw = Person()
        if (!raw.loadAndValidate(json.getJSONObject("person"))) {
            raw.validation.messages.forEach{
                addMessage(Message(it.text, "person/${it.field}"))
            }
            return false
        }
        person = PersonValueObject(raw.firstName as String, raw.lastName as String)
        return true
    }

    private fun loadAndValidateAmount(json: JSONObject): Boolean
    {
        TODO("not implemented")
    }

    private fun loadAndValidateAgreementAt(json: JSONObject): Boolean
    {
        TODO("not implemented")
    }

    private fun loadAndValidateCurrency(json: JSONObject): Boolean
    {
        TODO("not implemented")
    }

    private fun loadAndValidateDuration(json: JSONObject): Boolean
    {
        TODO("not implemented")
    }

    private fun loadAndValidatePercent(json: JSONObject): Boolean
    {
        TODO("not implemented")
    }
}