package actions.credit

import io.netty.handler.codec.http.*
import domain.repositories.CreditsRepositoryInterface
import exceptions.BadRequestValidationException
import viewModels.Credit as CreditViewModel
import viewModels.Person as PersonViewModel


class Read(repository: CreditsRepositoryInterface) : AbstractCreditHandler(repository) {

    override fun handle(request: FullHttpRequest): FullHttpResponse {
        if (!validateParameters(request)) {
            throw BadRequestValidationException(validation)
        }

        val creditViewModel = CreditViewModel()

        creditViewModel.hydrate(getCredit())

        return getResponse(HttpResponseStatus.OK, creditViewModel.fetchJson())
    }

    private fun validateParameters(request: FullHttpRequest): Boolean {
        val groups = Regex(ROUTE).matchEntire(request.uri())!!.groups

        return validateCreditId(groups)
    }

    companion object {
        const val ROUTE = "^/credit/(?<id>[0-9]+)$"
    }
}
