package actions.credit.payments

import actions.credit.AbstractCreditHandler
import domain.exceptions.*
import domain.repositories.CreditsRepositoryInterface
import domain.valueObjects.PaymentRequest
import exceptions.BadRequestException
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

class Request(repository: CreditsRepositoryInterface) : AbstractCreditHandler(repository) {

    private val payment: Payment = Payment()

    @Throws
    override fun handle(request: FullHttpRequest): FullHttpResponse {
        if (!validateRequest(request))
            throw BadRequestValidationException(validation + payment.validation)

        val paymentRequest = PaymentRequest(
            payment.payment!!,
            payment.type!!,
            payment.currency,
            payment.date
        )

        val credit = getCredit()

        try {
            credit.writeOf(paymentRequest)
        } catch (e: CreditAlreadyPaidException) {
            throw BadRequestException("Credit already paid")
        } catch (e: PaymentCurrencyInvalidException) {
            throw BadRequestException("Payment with wrong currency")
        } catch (e: PaymentTypeInvalidException) {
            throw BadRequestException("Payment with wrong type")
        } catch (e: PaymentLessThanMinimalException) {
            throw BadRequestException("Payment should be more than minimum of 100")
        } catch (e: PaymentLessThanRegularException) {
            throw BadRequestException("Payment can't be less than regular payment")
        } catch (e: PaymentMoreThanFullEarlyRepaimentException) {
            throw BadRequestException("Payment cannot be more than credit amount")
        } catch (e: PaymentDateMoreThanNextPaymentDateException) {
            throw BadRequestException("Payment cannot be early than last payment date")
        }

        val json = JSONObject()
            .put(PAYMENT_EXECUTED_AT, LocalDate.now().format(DateTimeFormatter.ISO_DATE))

        return getResponse(HttpResponseStatus.OK, json)
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
