package viewModels

import data.Money
import org.json.*
import valueObjects.Message
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import valueObjects.PersonInterface as PersonValueObjectInterface
import valueObjects.Person as PersonValueObject
import models.CreditInterface as CreditModelInterface


class Credit : ViewModel(), CreditInterface {
    override var person: PersonValueObjectInterface? = null
        private set
    override var amount: data.Money? = null
        private set
    override var agreementAt: LocalDate? = null
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

    override fun loadAndValidate(json: JSONObject): Boolean {
        return loadAndValidatePerson(json)
                && loadAndValidateAmount(json)
                && loadAndValidateAgreementAt(json)
                && loadAndValidateCurrency(json)
                && loadAndValidateDuration(json)
                && loadAndValidatePercent(json)
    }

    private fun loadAndValidatePerson(json: JSONObject): Boolean
    {
        if (!json.has(PERSON)) {
            addMessage(Message(REQUIRED, PERSON))
            return false
        }

        val raw = json.get(PERSON)
        if (raw == null) {
            addMessage(Message(NOT_NULL, PERSON))
            return false
        }

        if (raw !is JSONObject) {
            addMessage(Message(NOT_OBJECT, PERSON))
            return false
        }

        val rawPerson = Person()
        if (!rawPerson.loadAndValidate(raw)) {
            rawPerson.validation.messages.forEach{
                addMessage(Message(it.text, "${PERSON}/${it.field}"))
            }
            return false
        }

        person = PersonValueObject(rawPerson.firstName as String, rawPerson.lastName as String)
        return true
    }

    private fun loadAndValidateAmount(json: JSONObject): Boolean
    {
        if (!json.has(AMOUNT)) {
            addMessage(Message(REQUIRED, AMOUNT))
            return false
        }

        val raw = json.get(AMOUNT)
        if (raw == null) {
            addMessage(Message(NOT_NULL, AMOUNT))
            return false
        }

        if (raw !is Long) {
            addMessage(Message(LONG, AMOUNT))
            return false
        }

        val long: Long = raw
        if (long < 1 || long > 3000000000000000L) {
            addMessage(Message("Must be between 1 and 3000000000000000", AMOUNT))
            return false
        }

        amount = long as Money
        return true
    }

    private fun loadAndValidateAgreementAt(json: JSONObject): Boolean
    {
        if (!json.has(AGREEMENT_AT)) {
            return true
        }

        val raw = json.get(AGREEMENT_AT) ?: return true

        if (raw !is String) {
            addMessage(Message(STRING, AGREEMENT_AT))
            return false
        }

        val string: String = raw
        if (!string.matches(Regex("/[0-9]{4}-[0-9]{2}-[0-9]{2}/u"))) {
            addMessage(Message(DATE, AGREEMENT_AT))
            return false
        }

        agreementAt = try { LocalDate.parse(string, DateTimeFormatter.ISO_DATE) } catch (e: DateTimeParseException) {
            addMessage(Message(DATE_INVALID, AGREEMENT_AT))
            return false
        }

        return true
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

    companion object {
        const val PERSON        = "person"
        const val AMOUNT        = "amount"
        const val AGREEMENT_AT  = "agreementAt"
        const val CURRENCY      = "currency"
        const val DURATION      = "duration"
        const val PERCENT       = "percent"
    }
}
