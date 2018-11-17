package viewModels

import org.json.JSONObject
import org.junit.Assert.assertFalse
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonInvalidJsonTest {

    private fun invalidJsonProvider() = Stream.of(
        Arguments.of("{\"lastName\":\"Malinin\"}"),
        Arguments.of("{\"firstName\":null,\"lastName\":\"Malinin\"}"),
        Arguments.of("{\"firstName\":\"\",\"lastName\":\"Malinin\"}")
    )

    @ParameterizedTest
    @MethodSource("invalidJsonProvider")
    fun validateTest(data: String) {
        val person = Person()
        val json = JSONObject(data)

        assertFalse(person.loadAndValidate(json));
        //TODO check concrete message given
    }
}
