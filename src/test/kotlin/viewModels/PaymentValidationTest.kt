package viewModels

import org.json.JSONObject
import org.junit.Assert
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentValidationTest {

    private fun jsonProvider() = Stream.of(
        Arguments.of(
            JSONObject(
                mapOf(
                    "payment" to 2955,
                    "type" to "regular",
                    "currency" to "USD",
                    "date" to "2018-09-08"
                )
            ),
            true,
            0
        ),
        Arguments.of(
            JSONObject(
                mapOf(
                    "payment" to 2955,
                    "type" to "error",
                    "currency" to "error",
                    "date" to "2018-09-08"
                )
            ),
            false,
            2
        ),
        Arguments.of(
            JSONObject(
                mapOf(
                    "payment" to "error",
                    "type" to "regular",
                    "currency" to "USD",
                    "date" to "error"
                )
            ),
            false,
            2
        ),
        Arguments.of(
            JSONObject(
                mapOf(
                    "payment" to 2955,
                    "type" to null,
                    "currency" to "USD",
                    "date" to "error"
                )
            ),
            false,
            1
        ),
        Arguments.of(
            JSONObject(
                mapOf(
                    "payment" to 2955,
                    "type" to null,
                    "currency" to null,
                    "date" to null
                )
            ),
            true,
            0
        ),
        Arguments.of(
            JSONObject(
                mapOf(
                    "payment" to null,
                    "type" to "error",
                    "currency" to "error",
                    "date" to "2018-09-08"
                )
            ),
            false,
            3
        )
    )

    // todo rewrite test according to CreditFromJsonTest
    @ParameterizedTest
    @MethodSource("jsonProvider")
    fun loadAndValidateTest(json: JSONObject, isValid: Boolean, fields: Int) {
        val payment = Payment()

        if (isValid) {
            Assert.assertTrue(payment.loadAndValidate(json))
        } else {
            Assert.assertFalse(payment.loadAndValidate(json))
        }
        Assert.assertEquals(payment.validation.messages.size, fields)
    }
}
