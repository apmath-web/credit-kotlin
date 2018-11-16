package viewModels

import org.json.JSONObject
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonValidJsonTest {

    private fun validJsonProvider() = Stream.of(
        "{\"firstName\":\"Konstantin\",\"lastName\":\"Malinin\"}",
        "{\"firstName\":\"k\",\"lastName\":\"m\"}",
        "{\"lastName\":\"Malinin\",\"firstName\":\"Konstantin\"}",
        "{\"firstName\":\"Konstantin\",\"lastName\":\"Malinin\",\"surname\":\"Alexandrovich\"}"
    )

    @ParameterizedTest
    @MethodSource("validJsonProvider")
    fun validateTest(data: String) {
        val person = Person()
        val json = JSONObject(data)

        assert(person.loadAndValidate(json));
    }

    //    Arguments.of("", 0),
}
