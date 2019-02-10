package viewModels

import org.json.JSONObject
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentRequestFromJsonTest {

    private fun dataProvider() = Stream.of(
        //invalid data
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
            arrayOf("type", "currency")
        ),
        Arguments.of(
            JSONObject(
                mapOf(
                    "payment" to 0,
                    "type" to "regular",
                    "currency" to "USD",
                    "date" to "error"
                )
            ),
            false,
            arrayOf("payment", "date")
        ),
        Arguments.of(
            JSONObject(
                mapOf(
                    "payment" to 2955,
                    "type" to JSONObject.NULL,
                    "currency" to "USD",
                    "date" to "01-01-2018"
                )
            ),
            false,
            arrayOf("date")
        ),
        Arguments.of(
            JSONObject(
                mapOf(
                    "payment" to JSONObject.NULL,
                    "type" to "next",
                    "currency" to "error",
                    "date" to "2018/09/08"
                )
            ),
            false,
            arrayOf("payment", "type", "currency", "date")
        ),
        //valid fields
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
            null
        ),
        Arguments.of(
            JSONObject(
                mapOf(
                    "payment" to 2955,
                    "type" to JSONObject.NULL,
                    "currency" to JSONObject.NULL,
                    "date" to JSONObject.NULL
                )
            ),
            true,
            null
        ),
        Arguments.of(
            JSONObject(
                mapOf(
                    "payment" to 2955
                )
            ),
            true,
            null
        ),
        Arguments.of(
            JSONObject(
                mapOf(
                    "payment" to 375000000,
                    "type" to "early"
                )
            ),
            true,
            null
        ),
        Arguments.of(
            JSONObject(
                mapOf(
                    "payment" to 2955,
                    "currency" to "EUR"
                )
            ),
            true,
            null
        )
    )

    @ParameterizedTest
    @MethodSource("dataProvider")
    fun loadAndValidateTest(json: JSONObject, isValid: Boolean, fields: Array<String>?) {
        val paymentRequest = PaymentRequest()

        if (isValid) {
            assertTrue(paymentRequest.loadAndValidate(json))
        } else {
            assertFalse(paymentRequest.loadAndValidate(json))
            fields?.forEach { field ->
                var messages = paymentRequest.validation.messages.filter {
                    it.field == field
                }
                assertNotEquals(0, messages.size, "No error message for field ${field} found")
            }
        }
    }
}
