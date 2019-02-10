package actions.credit.payments

import actions.credit.AbstractCreditHandler
import domain.data.Money
import domain.data.Type
import domain.exceptions.*
import domain.repositories.CreditsRepositoryInterface
import domain.valueObjects.PaymentRequest
import exceptions.BadRequestException
import exceptions.BadRequestValidationException
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.util.CharsetUtil
import org.json.JSONObject
import viewModels.PaymentRequest as PaymentRequestViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Request(repository: CreditsRepositoryInterface) : AbstractCreditHandler(repository) {

    private val paymentRequestViewModel = PaymentRequestViewModel()

    @Throws
    override fun handle(request: FullHttpRequest): FullHttpResponse {
        if (!validateRequest(request)) {
            throw BadRequestValidationException(validation)
        }

        val paymentRequest = PaymentRequest(
            paymentRequestViewModel.payment as Money,
            paymentRequestViewModel.type as Type,
            paymentRequestViewModel.currency,
            paymentRequestViewModel.date
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
            throw BadRequestException("Payment can't be less than regular paymentViewModel")
        } catch (e: PaymentMoreThanFullEarlyRepaimentException) {
            throw BadRequestException("Payment cannot be more than credit amount")
        } catch (e: PaymentDateMoreThanNextPaymentDateException) {
            throw BadRequestException("Payment cannot be early than last paymentViewModel date")
        }

        val json = JSONObject()
            .put(PAYMENT_EXECUTED_AT, LocalDate.now().format(DateTimeFormatter.ISO_DATE))

        return getResponse(HttpResponseStatus.OK, json)
    }

    private fun validateRequest(request: FullHttpRequest): Boolean {
        val groups = Regex(ROUTE).matchEntire(request.uri())!!.groups
        val body = request.content().toString(CharsetUtil.UTF_8)

        return validateCreditId(groups)
            .and(validatePaymentViewModel(body))
    }

    private fun validatePaymentViewModel(body: String): Boolean {
        if (!paymentRequestViewModel.loadAndValidate(body)) {
            validation.addMessages(paymentRequestViewModel.validation)
            return false
        }
        return true
    }

    companion object {
        const val ROUTE                 = "^/credit/(?<id>[0-9]+)$"
        const val PAYMENT_EXECUTED_AT   = "paymentExecutedAt"
    }
}
