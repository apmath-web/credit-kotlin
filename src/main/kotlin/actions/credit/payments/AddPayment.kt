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

    private val payment: Payment = Payment()

    @Throws
    override fun handle(request: FullHttpRequest): FullHttpResponse {
        if (!validateRequest(request))
            throw BadRequestValidationException(validation + payment.validation)

        val payment = PayPayment(
            payment.payment!!,
            payment.type!!,
            payment.currency!!,
            payment.date!!
        )

        try {
            repository.get(creditId!!).writeOf(payment)
        } catch (e: CreditNotFoundException) {
            throw NotFoundException("Credit not found")
        }

        return getResponse(
            HttpResponseStatus.OK, JSONObject()
                .put(PAYMENT_EXECUTED_AT, LocalDate.now().format(DateTimeFormatter.ISO_DATE))
        )
    }

    private fun validateRequest(request: FullHttpRequest): Boolean {
        val groups = Regex(ROUTE).matchEntire(request.uri())!!.groups
        val body = request.content().toString(CharsetUtil.UTF_8)

        return validateCreditId(groups)
            .and(payment.loadAndValidate(body))
    }

    companion object {
        const val ROUTE                 = "^/credit/(?<id>[0-9]+)$"
        const val PAYMENT_EXECUTED_AT   = "paymentExecutedAt"
    }
}