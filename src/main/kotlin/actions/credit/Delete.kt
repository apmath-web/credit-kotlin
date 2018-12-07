package actions.credit

import actions.AbstractHandler
import domain.repositories.CreditsRepositoryInterface
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpResponseStatus

class Delete(val repository: CreditsRepositoryInterface) : AbstractHandler() {
    override fun handle(request: FullHttpRequest): FullHttpResponse {
        val id = request.uri().subSequence(7, request.uri().length).toString().toInt()

        if (repository.get(id).amount.value != 0L) {
            throw Exception()
        }
        repository.remove(repository.get(id))

        return getResponse(HttpResponseStatus.OK)
    }

}
