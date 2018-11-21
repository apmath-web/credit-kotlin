package viewModels

import data.Currency
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
    override var person: PersonInterface? = null
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
        return JSONObject()
            .put(PERSON, person?.fetchJson())
            .put(AMOUNT, amount?.value)
            .put(AGREEMENT_AT, (agreementAt as LocalDate).format(DateTimeFormatter.ISO_DATE))
            .put(CURRENCY, when (currency as data.Currency) {
                data.Currency.USD   -> "USD"
                data.Currency.EUR   -> "EUR"
                data.Currency.RUR   -> "RUR"
            })
            .put(DURATION, duration)
            .put(PERCENT, percent)
    }

    override fun hydrate(credit: CreditModelInterface) {
        person      = Person().apply {
            hydrate(credit.person)
        }
        amount      = credit.amount
        agreementAt = credit.agreementAt
        currency    = credit.currency
        duration    = credit.duration
        percent     = credit.percent
    }

    override fun loadAndValidate(json: JSONObject): Boolean {
        return arrayOf(
            loadAndValidatePerson(json),
            loadAndValidateAmount(json),
            loadAndValidateAgreementAt(json),
            loadAndValidateCurrency(json),
            loadAndValidateDuration(json),
            loadAndValidatePercent(json)
        ).reduce { a, b -> a && b }
    }

    private fun loadAndValidatePerson(json: JSONObject): Boolean
    {
        val raw = loadNotNullRequiredField(json, PERSON) ?: return false

        if (raw !is JSONObject) {
            addMessage(Message(MESSAGE_NOT_OBJECT, PERSON))
            return false
        }

        val rawPerson = Person()
        if (!rawPerson.loadAndValidate(raw)) {
            rawPerson.validation.messages.forEach{
                addMessage(Message(it.text, "${PERSON}.${it.field}"))
            }
            return false
        }

        person = rawPerson
        return true
    }

    private fun loadAndValidateAmount(json: JSONObject): Boolean
    {
        val raw = loadNotNullRequiredField(json, AMOUNT) ?: return false

        if ((raw !is Long) && (raw !is Int)) {
            addMessage(Message(MESSAGE_NOT_LONG, AMOUNT))
            return false
        }

        val long: Long = if (raw is Int) raw.toLong() else raw as Long

        if (long < 1 || long > 3000000000000000L) {
            addMessage(Message("Must be between 1 and 3000000000000000", AMOUNT))
            return false
        }

        amount = Money(long)
        return true
    }

    private fun loadAndValidateAgreementAt(json: JSONObject): Boolean
    {
        if (!json.has(AGREEMENT_AT)) {
            return true
        }

        val raw = json.get(AGREEMENT_AT) ?: return true

        if (raw !is String) {
            addMessage(Message(MESSAGE_NOT_STRING, AGREEMENT_AT))
            return false
        }

        if (!Regex("^[0-9]{4}-[0-9]{2}-[0-9]{2}$").matches(raw)) {
            addMessage(Message(MESSAGE_NOT_DATE, AGREEMENT_AT))
            return false
        }

        agreementAt = try { LocalDate.parse(raw, DateTimeFormatter.ISO_DATE) } catch (e: DateTimeParseException) {
            addMessage(Message(MESSAGE_DATE_INVALID, AGREEMENT_AT))
            return false
        }

        return true
    }

    private fun loadAndValidateCurrency(json: JSONObject): Boolean
    {
        val raw = loadNotNullRequiredField(json, CURRENCY) ?: return false

        if (raw !is String) {
            addMessage(Message(MESSAGE_NOT_STRING, CURRENCY))
            return false
        }

        currency = when (raw) {
            "EUR"   -> Currency.EUR
            "USD"   -> Currency.USD
            "RUR"   -> Currency.RUR
            else    -> null
        }

        if (currency == null) {
            addMessage(Message(MESSAGE_CURRENCY_UNKNOWN, CURRENCY))
            return false
        }

        return true
    }

    private fun loadAndValidateDuration(json: JSONObject): Boolean
    {
        val raw = loadNotNullRequiredField(json, DURATION) ?: return false

        if (raw !is Int) {
            addMessage(Message(MESSAGE_NOT_INT, DURATION))
            return false
        }

        val int: Int = raw
        if (int < 6 || int > 1200) {
            addMessage(Message("Must be between 6 and 1200", DURATION))
            return false
        }

        duration = int
        return true
    }

    private fun loadAndValidatePercent(json: JSONObject): Boolean
    {
        val raw = loadNotNullRequiredField(json, PERCENT) ?: return false

        if (raw !is Int) {
            addMessage(Message(MESSAGE_NOT_LONG, PERCENT))
            return false
        }

        val int: Int = raw
        if (int < 1 || int > 300) {
            addMessage(Message("Must be between 1 and 300", PERCENT))
            return false
        }

        percent = int
        return true
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
