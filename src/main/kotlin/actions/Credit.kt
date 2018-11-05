package actions

import io.netty.buffer.Unpooled
import io.netty.handler.codec.http.*
import io.netty.util.CharsetUtil

class Credit() {
    fun create(request: HttpRequest): FullHttpResponse {

        val json = Unpooled.copiedBuffer("{id:1}", CharsetUtil.UTF_8)

        val response = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, json)

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes())

        return response;
    }
}
