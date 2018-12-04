package viewModels

import domain.data.Currency
import domain.data.Money
import domain.data.State
import domain.data.Type
import domain.valueObjects.Message
import domain.valueObjects.Validation
import domain.valueObjects.ValidationInterface
import domain.valueObjects.PaymentInterface as PaymentInterfaceValueObject
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class Payment(
    override var payment: Money,
    override var type: Type,
    override var currency: Currency,
    override var date: LocalDate,
    override var state: State
) : ViewModel(), PaymentInterface {
    /*override var state: State = State.PAID
    private var percent: String
    private var body: String
    private var remaindCreditbody: String*/

    override val validation: ValidationInterface = Validation()

    override fun hydrate(payment: PaymentInterfaceValueObject) {
        TODO("Should be?")
        /*this.type = payment.type
        state = payment.state
        date = payment.date
        this.payment=payment.payment
        percent=payment.percent
        body=payment.body
        remaindCreditbody=payment.remainCreditBody
        fullEarlyRepayment =  payment.fullEarlyRepayment*/
    }

    override fun loadAndValidate(json: JSONObject): Boolean {
        return loadAndValidatePayment(json)
            .and(loadAndValidateType(json))
            .and(loadAndValidateCurrency(json))
            .and(loadAndValidateDate(json))
    }

    private fun loadAndValidateDate(json: JSONObject): Boolean {
        val raw = loadNotNullRequiredField(json, CURRENCY) ?: return false

        if (raw !is String) {
            addMessage(Message(DATE, MESSAGE_NOT_STRING))
            return false
        }

        if (!Regex(DATE_FORMATE).matches(raw)) {
            addMessage(Message(DATE, MESSAGE_NOT_DATE))
            return false
        }

        try {
            date = LocalDate.parse(raw, DateTimeFormatter.ISO_DATE)
        } catch (e: DateTimeParseException) {
            addMessage(Message(DATE, MESSAGE_DATE_INVALID))
            return false
        }

        return true
    }

    private fun loadAndValidateCurrency(json: JSONObject): Boolean {
        val raw = loadNotNullRequiredField(json, CURRENCY) ?: return false

        if (raw !is String) {
            addMessage(Message(CURRENCY, MESSAGE_NOT_STRING))
            return false
        }

        try {
            currency = Currency.valueOf(raw)
        } catch (e: IllegalArgumentException) {
            addMessage(Message(CURRENCY, MESSAGE_CURRENCY_UNKNOWN))
            return false
        }

        return true
    }

    private fun loadAndValidateType(json: JSONObject): Boolean {
        val raw = loadNotNullRequiredField(json, TYPE) ?: return false

        if (raw !is String) {
            addMessage(Message(TYPE, MESSAGE_NOT_STRING))
            return false
        }

        try {
            currency = Currency.valueOf(raw)
        } catch (e: IllegalArgumentException) {
            addMessage(Message(TYPE, MESSAGE_TYPE_UNKNOWN))
            return false
        }

        return true
    }

    private fun loadAndValidatePayment(json: JSONObject): Boolean {
        val raw = loadNotNullRequiredField(json, PAYMENT) ?: return false

        if ((raw !is Long) && (raw !is Int)) {
            addMessage(Message(Credit.AMOUNT, MESSAGE_NOT_INT))
            return false
        }

        val long: Long = if (raw is Int) raw.toLong() else raw as Long

        if (long < 1 || long > 3000000000000000) {
            addMessage(Message(Credit.AMOUNT, "Must be between 1 and 3000000000000000"))
            return false
        }

        payment = Money(long)
        return true
    }

    override fun fetch(): String {
        return fetchJson().toString()
    }

    override fun fetchJson(): JSONObject {
        //TODO("Should be here?")
        return JSONObject()
            .put("type", type)
            .put("state", state)
            .put("date", date)
            .put("payment", payment)
            //.put("percent", percent)
            //.put("body", body)
            //.put("remainCreditBody", remaindCreditbody)
    }

    companion object {
        const val PAYMENT = "currency"
        const val TYPE = "person"
        const val CURRENCY = "amount"
        const val DATE = "agreementAt"
    }

}