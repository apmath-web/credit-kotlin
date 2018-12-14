package actions

import io.netty.buffer.Unpooled
import io.netty.handler.codec.http.*
import io.netty.util.CharsetUtil
import org.json.JSONArray
import org.json.JSONObject


abstract class AbstractHandler {

    abstract fun handle(request: FullHttpRequest): FullHttpResponse

    protected fun getResponse(status: HttpResponseStatus): FullHttpResponse {

        return DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status)

    }

    protected fun getResponse(status: HttpResponseStatus, json: JSONObject): FullHttpResponse {

        return getResponse(status, json.toString())
    }

    protected fun getResponse(status: HttpResponseStatus, json: JSONArray): FullHttpResponse {

        return getResponse(status, json.toString())
    }

    protected fun getResponse(status: HttpResponseStatus, json: String): FullHttpResponse {

        val content = Unpooled.copiedBuffer(json, CharsetUtil.UTF_8)

        val response = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content)

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes())

        return response
    }
}
