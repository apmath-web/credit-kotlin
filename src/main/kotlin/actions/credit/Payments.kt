package actions.credit

import domain.data.Type
import domain.data.State
import domain.exceptions.CreditNotFoundException
import domain.models.CreditInterface
import io.netty.handler.codec.http.*
import domain.repositories.CreditsRepositoryInterface
import domain.valueObjects.Message
import exceptions.BadRequestValidationException
import exceptions.NotFoundException
import org.json.JSONArray
import org.json.JSONObject
import viewModels.Credit as CreditViewModel
import viewModels.Person as PersonViewModel
import viewModels.Payment as PaymentViewModel
import java.util.HashMap


class Payments(private val repository: CreditsRepositoryInterface) : AbstractCreditHandler() {

    var paymentsType: Type? = null
    var paymentsState: State? = null

    override fun handle(request: FullHttpRequest): FullHttpResponse {
        if (!validateParameters(request)) {
            throw BadRequestValidationException(validation)
        }

        val credit: CreditInterface

        try {
            credit = repository.get(creditId as Int)
        } catch (e: CreditNotFoundException) {
            throw NotFoundException("Credit not found")
        }

        val payments = credit.getPayments(paymentsType, paymentsState)
        val paymentsJson = JSONArray()
        val json = JSONObject().put("payments", paymentsJson)

        payments.forEach {
            val paymentViewModel = PaymentViewModel()
            paymentViewModel.hydrate(it)
            paymentsJson.put(paymentViewModel.fetchJson())
        }

        return getResponse(HttpResponseStatus.OK, json)
    }


    private fun validateParameters(request: FullHttpRequest): Boolean {

        val groups = Regex(ROUTE).matchEntire(request.uri())!!.groups
        val queryParams = HashMap<String, String>()

        // parse query string and put pairs into hashMap
        try {
            val query = groups["query"]?.value?.substring(1) ?: throw IllegalArgumentException()

            query.split("&").forEach {
                val keyValue = it.split("=")
                if(keyValue.size >= 2){
                    queryParams[keyValue[0]] = keyValue[1];
                }
            }
        } catch (e: IllegalArgumentException) {
            // case when query not present at all
        }

        return validateCreditId(groups)
            .and(validateType(queryParams))
            .and(validateState(queryParams))
    }

    private fun validateType(query: HashMap<String, String>): Boolean {

        val value = query[TYPE] ?: return true

        if (value.toLowerCase() != value) {
            validation.addMessage(Message(TYPE, MESSAGE_TYPE_UNKNOWN))
            return false
        }

        try {
            paymentsType = Type.valueOf(value.toUpperCase())
        } catch (e: IllegalArgumentException) {
            validation.addMessage(Message(TYPE, MESSAGE_TYPE_UNKNOWN))
            return false
        }

        return true
    }

    private fun validateState(query: HashMap<String, String>): Boolean {

        val value = query[STATE] ?: return true

        if (value.toLowerCase() != value) {
            validation.addMessage(Message(STATE, MESSAGE_STATE_UNKNOWN))
            return false
        }

        try {
            paymentsState = State.valueOf(value.toUpperCase())
        } catch (e: IllegalArgumentException) {
            validation.addMessage(Message(STATE, MESSAGE_STATE_UNKNOWN))
            return false
        }

        return true
    }


    companion object {
        const val ROUTE     = "^/credit/(?<id>[0-9]+)/payments(?<query>\\?.*)?$"
        const val TYPE      = "type"
        const val STATE     = "state"

        const val MESSAGE_TYPE_UNKNOWN  = "Must be a valid type ['regular', 'early', 'next'] allowed"
        const val MESSAGE_STATE_UNKNOWN = "Must be a valid state ['paid', 'upcoming'] allowed"
    }
}
