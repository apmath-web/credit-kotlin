package exceptions

import io.netty.handler.codec.http.HttpResponseStatus

class NotFoundException(message: String) : ApiException(HttpResponseStatus.NOT_FOUND, message)
