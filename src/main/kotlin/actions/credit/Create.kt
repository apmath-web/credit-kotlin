package actions.credit

import actions.AbstractHandler
import io.netty.handler.codec.http.*
import domain.repositories.CreditsRepositoryInterface
import org.json.JSONObject


class Create(private val repository: CreditsRepositoryInterface) : AbstractHandler() {

    override fun handle(request: HttpRequest): FullHttpResponse {

        val json = JSONObject().put("id", 1)

        return  getResponse(HttpResponseStatus.OK, json)
    }
}
