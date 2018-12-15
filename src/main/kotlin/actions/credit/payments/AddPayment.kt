package actions.credit.payments

import actions.credit.AbstractCreditHandler
import domain.exceptions.CreditNotFoundException
import domain.repositories.CreditsRepositoryInterface
import domain.valueObjects.payment.PayPayment
import domain.valueObjects.payment.PayPaymentInterface
import exceptions.BadRequestValidationException
import exceptions.NotFoundException
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.util.CharsetUtil
import org.json.JSONObject
import viewModels.Payment
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddPayment(private val repository: CreditsRepositoryInterface) : AbstractCreditHandler() {
    @Throws
    override fun handle(request: FullHttpRequest): FullHttpResponse {
        val groups = Regex(ROUTE).matchEntire(request.uri())!!.groups
        if (!validateCreditId(groups)) {
            throw BadRequestValidationException(validation)
        }

        val payment = getPayment(request)

        try {
            repository.get(creditId!!).writeOf(payment)
        } catch (e: CreditNotFoundException) {
            throw NotFoundException("Credit not found")
        } catch (e: Throwable) {
            throw NotFoundException("exception " + e.printStackTrace())
        }

        return getResponse()
    }

    private fun getResponse(): FullHttpResponse {
        val responseJSON = JSONObject()
            .put(PAYMENT_EXECUTED_AT, LocalDate.now().format(DateTimeFormatter.ISO_DATE))
        return getResponse(HttpResponseStatus.OK, responseJSON)
    }


    private fun getPayment(request: FullHttpRequest): PayPaymentInterface {
        val payment = Payment()
        val body = request.content().toString(CharsetUtil.UTF_8)
        if (!payment.loadAndValidate(body)) {
            throw BadRequestValidationException(payment.validation)
        }

        return PayPayment(
            payment.payment!!,
            payment.type!!,
            payment.currency!!,
            payment.date!!
        )
    }

    companion object {
        const val ROUTE = "^/credit/(?<id>[0-9]+)$"
        const val PAYMENT_EXECUTED_AT = "paymentExecutedAt"
    }
}