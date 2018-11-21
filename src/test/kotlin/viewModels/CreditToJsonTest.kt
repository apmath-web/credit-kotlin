package viewModels

import org.json.JSONObject
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreditToJsonTest {

    private fun jsonProvider() = Stream.of(
        Arguments.of(
            JSONObject()
                .put("person", JSONObject()
                    .put("firstName", "Konstantin")
                    .put("lastName", "Malinin"))
                .put("amount", 1000000)
                .put("agreementAt", "2018-01-12")
                .put("currency", "RUR")
                .put("duration", 12)
                .put("percent", 8)
        )
    )
    @ParameterizedTest
    @MethodSource("jsonProvider")
    fun fetchJsonTest(json: JSONObject) {
        val credit = Credit()

        // when
        assertTrue(credit.loadAndValidate(json))

        // then
        // TODO how to assert simple JSONObjects ? @malinink
        assertEquals(json.toString(), credit.fetchJson().toString())
    }
}
