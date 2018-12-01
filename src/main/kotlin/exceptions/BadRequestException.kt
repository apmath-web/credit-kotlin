package exceptions

import io.netty.handler.codec.http.HttpResponseStatus

class BadRequestException(message: String) : ApiException(HttpResponseStatus.BAD_REQUEST, message)
