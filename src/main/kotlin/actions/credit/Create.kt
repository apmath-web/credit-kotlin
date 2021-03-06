package actions.credit

import actions.AbstractHandler
import domain.data.Currency
import domain.data.Money
import domain.exceptions.CreditAmountTooSmallException
import domain.exceptions.PaymentLessThanMinimalException
import domain.models.Credit
import io.netty.handler.codec.http.*
import domain.repositories.CreditsRepositoryInterface
import domain.valueObjects.Person
import exceptions.BadRequestException
import exceptions.BadRequestValidationException
import io.netty.util.CharsetUtil
import org.json.JSONObject
import java.time.LocalDate
import viewModels.Credit as CreditViewModel
import viewModels.Person as PersonViewModel


class Create(private val repository: CreditsRepositoryInterface) : AbstractHandler() {

    override fun handle(request: FullHttpRequest): FullHttpResponse {

        val creditViewModel = CreditViewModel()

        val body = request.content().toString(CharsetUtil.UTF_8)
        if (!creditViewModel.loadAndValidate(body)) {
            throw BadRequestValidationException(creditViewModel.validation)
        }

        val credit: Credit

        try {
            val personViewModel = creditViewModel.person as PersonViewModel
            credit = Credit(
                Person(personViewModel.firstName as String, personViewModel.lastName as String),
                creditViewModel.amount as Money,
                creditViewModel.agreementAt as LocalDate,
                creditViewModel.currency as Currency,
                creditViewModel.duration as Int,
                creditViewModel.percent as Int
            )
        } catch (e: PaymentLessThanMinimalException) {
            throw BadRequestException("Credit payment should be more than minimum of 100")
        } catch (e: CreditAmountTooSmallException) {
            throw BadRequestException("Too small Credit amount for concrete duration")
        }

        repository.store(credit)

        val json = JSONObject().put("id", credit.id)

        return getResponse(HttpResponseStatus.OK, json)
    }
}
