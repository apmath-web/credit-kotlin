package viewModels

import org.json.JSONObject
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonJsonTest {

    private fun invalidJsonProvider() = Stream.of(
        // wrong firstName
        Arguments.of(
            JSONObject()
                .put("lastName", "Malinin"),
            false,
            arrayOf("firstName")
        ),
        Arguments.of(
            JSONObject()
                .put ("firstName", JSONObject.NULL)
                .put("lastName", "Malinin"),
            false,
            arrayOf("firstName")
        ),
        Arguments.of(
            JSONObject()
                .put("firstName", "")
                .put("lastName", "Malinin"),
            false,
            arrayOf("firstName")
        ),
        // wrong lastNAme
        Arguments.of(
            JSONObject()
                .put("firstName", "Konstantin"),
            false,
            arrayOf("lastName")
        ),
        Arguments.of(
            JSONObject()
                .put("firstName", "Konstantin")
                .put("lastName", JSONObject.NULL),
            false,
            arrayOf("lastName")
        ),
        Arguments.of(
            JSONObject()
                .put("firstName", "Konstantin")
                .put("lastName", ""),
            false,
            arrayOf("lastName")
        ),
        // wrong both: lastName and firstName
        Arguments.of(
            JSONObject()
            ,
            false,
            arrayOf("lastName", "firstName")
        ),
        // valid data
        Arguments.of(
            JSONObject()
                .put("firstName", "Konstantin")
                .put("lastName", "Malinin"),
            true,
            null
        ),
        Arguments.of(
            JSONObject()
                .put("lastName", "Malinin")
                .put("firstName", "Konstantin"),
            true,
            null
        ),
        Arguments.of(
            JSONObject()
                .put("lastName", "Malinin")
                .put("firstName", "Konstantin")
                .put("surname", "Alexandrovich"),
            true,
            null
        )
    )
    @ParameterizedTest
    @MethodSource("invalidJsonProvider")
    fun validateTest(json: JSONObject, isValid: Boolean, fields: Array<String>?) {
        val person = Person()

        if (isValid) {
            assertTrue(person.loadAndValidate(json))
        } else {
            assertFalse(person.loadAndValidate(json))
            fields?.forEach {
                field -> var messages = person.validation.messages.filter {
                    it.field == field
                }
                assertNotEquals(0, messages.size, "No error message for field ${field} found")
            }
        }
    }
}
