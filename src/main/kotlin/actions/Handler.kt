package actions

import io.netty.handler.codec.http.*

class Handler() {
    fun handle(request: HttpRequest): FullHttpResponse {
        if (request.uri() == "/credit" && request.method() == HttpMethod.POST) {
            return Credit().handle(request)
        }
        return notFound()
    }

    private fun notFound(): FullHttpResponse {
        val response = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND)

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes())

        return response;
    }
}
