package exceptions

import io.netty.handler.codec.http.HttpResponseStatus

open class BadRequestException(message: String) : ApiException(HttpResponseStatus.BAD_REQUEST, message)
