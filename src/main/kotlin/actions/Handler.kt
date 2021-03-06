package actions

import actions.credit.Create
import actions.credit.Delete
import actions.credit.payments.List
import actions.credit.Read
import actions.credit.payments.Request
import io.netty.handler.codec.http.*
import domain.repositories.CreditsRepository
import domain.repositories.CreditsRepositoryInterface
import exceptions.ApiException
import exceptions.BadRequestValidationException
import exceptions.NotFoundException
import org.json.JSONObject


class Handler : AbstractHandler() {

    override fun handle(request: FullHttpRequest): FullHttpResponse {

        return try {
            routeRequest(request)
        } catch (e: Throwable) {
            when (e) {
                // order make sense
                is ApiException -> getResponse(e)
                is RuntimeException, is Error -> getUnexpectedExceptionResponse(e)
                is Exception -> getExpectedExceptionResponse(e)
                else -> getExpectedExceptionResponse(e)
            }
        }
    }

    private fun routeRequest(request: FullHttpRequest): FullHttpResponse {

        when {

            request.method() == HttpMethod.POST && request.uri() == "/credit" ->
                return Create(repository).handle(request)

            request.method() == HttpMethod.GET && Regex(Read.ROUTE).matches(request.uri()) ->
                return Read(repository).handle(request)

            request.method() == HttpMethod.DELETE && Regex(Delete.ROUTE).matches(request.uri()) ->
                return Delete(repository).handle(request)

            request.method() == HttpMethod.GET && Regex(List.ROUTE).matches(request.uri()) ->
                return List(repository).handle(request)

            request.method() == HttpMethod.PUT && Regex(Request.ROUTE).matches(request.uri()) ->
                return Request(repository).handle(request)

        }

        throw NotFoundException("Route not found")
    }

    private fun getResponse(e: ApiException): FullHttpResponse {

        val json = JSONObject().put(MESSAGE, e.message)

        if (e is BadRequestValidationException) {
            val description = JSONObject()
            e.messages.forEach {
                description.put(it.field, it.text)
            }
            json.put(DESCRIPTION, description)
        }

        return getResponse(e.status, json)
    }

    private fun getUnexpectedExceptionResponse(e: Throwable): FullHttpResponse {

        val json = JSONObject().put(MESSAGE, "Unexpected exception '${e.javaClass}' happend")

        return getResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR, json)
    }

    private fun getExpectedExceptionResponse(e: Throwable): FullHttpResponse {

        val json = JSONObject().put(MESSAGE, "Expected exception '${e.javaClass}' not catched")

        return getResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR, json)
    }

    companion object {

        private val repository: CreditsRepositoryInterface = CreditsRepository()

        const val MESSAGE       = "message"
        const val DESCRIPTION   = "description"
    }
}
