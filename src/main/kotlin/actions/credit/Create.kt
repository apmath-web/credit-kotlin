package actions.credit

import actions.AbstractHandler
import io.netty.handler.codec.http.*
import domain.repositories.CreditsRepositoryInterface
import io.netty.util.CharsetUtil
import org.json.JSONObject


class Create(private val repository: CreditsRepositoryInterface) : AbstractHandler() {

    override fun handle(request: FullHttpRequest): FullHttpResponse {

        val json = JSONObject().put("id", 1)

        return getResponse(HttpResponseStatus.OK, json)
    }
}
