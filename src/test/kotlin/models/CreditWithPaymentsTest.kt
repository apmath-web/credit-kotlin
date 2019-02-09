package models

import domain.data.Currency
import domain.data.Money
import domain.data.Type
import domain.valueObjects.Person
import org.json.JSONObject
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import domain.models.Credit
import domain.valueObjects.PaymentRequest
import viewModels.Payment
import viewModels.Credit as CreditViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Stream
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreditWithPaymentsTest {

    private fun jsonProvider() = Stream.of(
        /**  */
        Arguments.of(
            1000000,
            LocalDate.of(2018, 10, 1),
            6,
            12,
            172600,
            172350
        ),
        /**  */
        Arguments.of(
            1000000,
            LocalDate.of(2018, 10, 1),
            10,
            12,
            105600,
            105380
        ),
        /**  */
        Arguments.of(
            1000000,
            LocalDate.of(2018, 10, 1),
            10,
            12,
            105600,
            105380
        )
    )

    // todo rewrite test according to CreditFromJsonTest
    @ParameterizedTest
    @MethodSource("jsonProvider")
    fun loadAndValidateTest(amount: Int, date: LocalDate, duration: Int, percent: Int, payment: Int, lastPayment: Int) {
        val credit = getCredit(amount, date, duration, percent)
        var paymentRequest: PaymentRequest

        for (i in 1 until duration) {
            paymentRequest = getPaymentRequest(payment)
            credit.writeOf(paymentRequest)
        }
        paymentRequest = getPaymentRequest(lastPayment)
        credit.writeOf(paymentRequest)
        assertTrue { credit.isFinished }
    }



    private fun getCredit(amount: Int, date: LocalDate, dur: Int, per: Int): Credit {
        val js = JSONObject().put("person", JSONObject().put("firstName", "Alexandrova").put("lastName", "Chernyshova"))
            .put("amount", amount)
            .put("agreementAt", date.format(DateTimeFormatter.ISO_DATE))
            .put("currency", "USD")
            .put("duration", dur)
            .put("percent", per)
        val creditViewModel = CreditViewModel()
        creditViewModel.loadAndValidate(js)

        val personViewModel = creditViewModel.person as viewModels.Person

        return Credit(
            Person(personViewModel.firstName as String, personViewModel.lastName as String),
            creditViewModel.amount as Money,
            creditViewModel.agreementAt as LocalDate,
            creditViewModel.currency as Currency,
            creditViewModel.duration as Int,
            creditViewModel.percent as Int
        )
    }

    private fun getPaymentRequest(amount: Int): PaymentRequest {
        val js = JSONObject()
            .put("payment", amount)

        val paymentVM = Payment()
        paymentVM.loadAndValidate(js)

        return PaymentRequest(
            paymentVM.payment as Money,
            paymentVM.type as Type,
            paymentVM.currency,
            paymentVM.date
        )
    }
}
