package viewModels

import org.json.JSONObject
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertNotEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreditFromJsonTest {

    private fun invalidJsonProvider() = Stream.of(
        // TODO add more true/false cases @malinink

        // valid data
        Arguments.of(
            JSONObject()
                .put("person", JSONObject()
                    .put("firstName", "Konstantin")
                    .put("lastName", "Malinin"))
                .put("amount", 1000000)
                .put("agreementAt", "2018-01-12")
                .put("currency", "RUR")
                .put("duration", 12)
                .put("percent", 8),
            true,
            null
        )
    )
    @ParameterizedTest
    @MethodSource("invalidJsonProvider")
    fun loadAndValidateTest(json: JSONObject, isValid: Boolean, fields: Array<String>?) {
        val credit = Credit()

        if (isValid) {
            assertTrue(credit.loadAndValidate(json))
        } else {
            assertFalse(credit.loadAndValidate(json))
            fields?.forEach {
                field -> var messages = credit.validation.messages.filter {
                    it.field == field
                }
                assertNotEquals(0, messages.size, "No error message for field ${field} found")
            }
        }
    }
}
