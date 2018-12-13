package actions.credit

import actions.AbstractHandler
import io.netty.handler.codec.http.*
import domain.repositories.CreditsRepositoryInterface
import domain.valueObjects.Message
import exceptions.BadRequestValidationException
import org.json.JSONObject
import viewModels.Credit as CreditViewModel
import viewModels.Person as PersonViewModel


class Payments(private val repository: CreditsRepositoryInterface) : AbstractCreditHandler() {

    override fun handle(request: FullHttpRequest): FullHttpResponse {
        if (!validateParameters(request)) {
            throw BadRequestValidationException(validation)
        }

        val json = JSONObject().put("id", creditId)

        return getResponse(HttpResponseStatus.OK, json)
    }


    private fun validateParameters(request: FullHttpRequest): Boolean {

        val groups = Regex(ROUTE).matchEntire(request.uri())!!.groups
        System.out.println(request.uri())
        return validateCreditId(groups)
            .and(validateType())
            .and(validateState())
    }

    private fun validateType(): Boolean {

        return true
    }

    private fun validateState(): Boolean {

        return true
    }


    companion object {
        const val ROUTE = "^/credit/(?<id>[0-9]+)/payments$"
    }
}
