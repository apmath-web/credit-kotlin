package models

import domain.data.Currency
import domain.data.Money
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
            172357
        ),
        /**  */
        Arguments.of(
            1000000,
            LocalDate.of(2018, 10, 1),
            10,
            12,
            105600,
            105384
        ),
        /**  */
        Arguments.of(
            1000000,
            LocalDate.of(2018, 10, 1),
            10,
            12,
            105600,
            105384
        )
    )

    // todo rewrite test according to CreditFromJsonTest
    @ParameterizedTest
    @MethodSource("jsonProvider")
    fun loadAndValidateTest(amount: Int, date: LocalDate, dur: Int, per: Int, pay: Int, lastPay: Int) {
        val credit = cred(amount, date, dur, per)

        for (i in 1 until dur) {
            val payment = payment(pay, date.plusMonths(i.toLong()))
            credit.writeOf(payment)
        }
        val payment = payment(lastPay, date.plusMonths(dur.toLong()))
        credit.writeOf(payment)
        assertTrue { credit.isClosed() }
    }



    fun cred(amount: Int, date: LocalDate, dur: Int, per: Int): Credit {
        val js = JSONObject().put("person", JSONObject().put("firstName", "Alexandrova").put("lastName", "Chernyshova"))
            .put("amount", amount)
            .put("agreementAt", date.format(DateTimeFormatter.ISO_DATE))
            .put("currency", "USD")
            .put("duration", dur)
            .put("percent", per)
        val creditViewModel = CreditViewModel()
        creditViewModel.loadAndValidate(js)

        val personViewModel = creditViewModel.person as viewModels.Person

        println("Credit is: $js")
        return Credit(
            Person(personViewModel.firstName as String, personViewModel.lastName as String),
            creditViewModel.amount as Money,
            creditViewModel.agreementAt as LocalDate,
            creditViewModel.currency as Currency,
            creditViewModel.duration as Int,
            creditViewModel.percent as Int
        )
    }

    fun payment(amount: Int, date: LocalDate): PaymentRequest {
        val js = JSONObject()
            .put("payment", amount)
            .put("date", date.format(DateTimeFormatter.ISO_DATE))
            .put("currency", "USD")
            .put("type", "regular")

        val paymentVM = Payment()
        paymentVM.loadAndValidate(js)

        println(js)

        return PaymentRequest(
            paymentVM.payment!!,
            paymentVM.type!!,
            paymentVM.currency!!,
            paymentVM.date!!
        )
    }
}
