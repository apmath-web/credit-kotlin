package actions.credit

import actions.AbstractHandler
import domain.exceptions.CreditNotFoundException
import domain.repositories.CreditsRepositoryInterface
import exceptions.BadRequestException
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpResponseStatus
import java.lang.Exception

class Delete(val repository: CreditsRepositoryInterface) : AbstractHandler() {


    override fun handle(request: FullHttpRequest): FullHttpResponse {

        val id = request.uri().subSequence(7, request.uri().length).toString().toInt()

        try {
            if (id < 1) {
                throw Exception()
            }
        } catch (e: Exception) {
            throw BadRequestException("400")
        }

        if (repository.get(id).amount.value != 0L) {
            throw BadRequestException("100")
        }

        try {
            repository.get(id)
        } catch (e:CreditNotFoundException) {
            throw BadRequestException("404")
        }

        repository.remove(repository.get(id))

        return getResponse(HttpResponseStatus.OK)
    }

}
