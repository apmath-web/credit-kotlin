package actions

import actions.credit.Create
import io.netty.handler.codec.http.*
import domain.repositories.CreditsRepository
import domain.repositories.CreditsRepositoryInterface
import exceptions.ApiException
import exceptions.NotFoundException

class Handler {
    fun handle(request: HttpRequest): FullHttpResponse {
        try {
            return routeRequest(request)
        } catch (e : ApiException) {
            return getResponse(e)
        } catch (e: Throwable) {
            return getResponse(e);
        }
    }

    private fun routeRequest(request: HttpRequest): FullHttpResponse {
        when {
            request.uri() == "/credit" && request.method() == HttpMethod.POST
                -> return Create(repository).handle(request)

        }
        throw NotFoundException("Route not found")
    }

    private fun getResponse(e: ApiException): FullHttpResponse {
        return DefaultFullHttpResponse(HttpVersion.HTTP_1_1, e.status)
    }

    private fun getResponse(e: Throwable): FullHttpResponse {
        return DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR)
    }

    companion object {
        private val repository: CreditsRepositoryInterface = CreditsRepository()
    }
}
