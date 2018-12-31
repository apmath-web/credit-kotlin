package actions.credit

import domain.exceptions.RemoveUnfinishedCreditException
import domain.repositories.CreditsRepositoryInterface
import exceptions.BadRequestException
import exceptions.BadRequestValidationException
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpResponseStatus


class Delete(repository: CreditsRepositoryInterface) : AbstractCreditHandler(repository) {

    override fun handle(request: FullHttpRequest): FullHttpResponse {
        if (!validateParameters(request)) {
            throw BadRequestValidationException(validation)
        }

        val credit = getCredit()

        try {
            repository.remove(credit)
        } catch (e: RemoveUnfinishedCreditException) {
            throw BadRequestException("Credit is not finished, only finished credits are allowed to remove")
        }

        return getResponse(HttpResponseStatus.NO_CONTENT)
    }

    private fun validateParameters(request: FullHttpRequest): Boolean {
        val groups = Regex(ROUTE).matchEntire(request.uri())!!.groups

        return validateCreditId(groups)
    }

    companion object {
        const val ROUTE = "^/credit/(?<id>[0-9]+)$"
    }
}
