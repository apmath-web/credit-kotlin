package exceptions

import io.netty.handler.codec.http.HttpResponseStatus

abstract class ApiException(status: HttpResponseStatus, message: String) : Exception(message) {
    val status: HttpResponseStatus = status
}
