package viewModels

import org.json.JSONObject
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PaymentValidationTest {

    @Test
    fun paymentTrueValidation() {
        val payment = Payment()
        val jsonObject =
            JSONObject(
                mapOf(
                    "payment" to 2955,
                    "type" to "REGULAR",
                    "currency" to "USD",
                    "date" to "2018-09-08"
                )
            )
        assertTrue { payment.loadAndValidate(jsonObject) }
        assertEquals(payment.validation.messages.size, 0)
    }

    @Test
    fun paymentMiddleFalseValidation() {
        val payment = Payment()
        val jsonObject =
            JSONObject(
                mapOf(
                    "payment" to 2955,
                    "type" to "error",
                    "currency" to "error",
                    "date" to "2018-09-08"
                )
            )
        assertFalse { payment.loadAndValidate(jsonObject) }
        assertEquals(payment.validation.messages.size, 2)
    }

    @Test
    fun paymentLastFalseValidation() {
        val payment = Payment()
        val jsonObject =
            JSONObject(
                mapOf(
                    "payment" to "error",
                    "type" to "REGULAR",
                    "currency" to "USD",
                    "date" to "error"
                )
            )
        assertFalse { payment.loadAndValidate(jsonObject) }
        assertEquals(payment.validation.messages.size, 2)
    }
}