import dev.nycode.kotlinx.serialization.bitmask.SerializableBitmask
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

@SerializableBitmask
@Serializable(with = ExampleSerializer::class)
data class Example(
    val write: Boolean,
    val read: Boolean,
    val execute: Boolean
)

internal class SerializerTest {

    companion object {
        @JvmStatic
        private fun testData() =
            sequenceOf(
                arguments(false, false, false, 0b000),
                arguments(false, false, true, 0b100),
                arguments(false, true, false, 0b010),
                arguments(false, true, true, 0b110),
                arguments(true, false, false, 0b001),
                arguments(true, false, true, 0b101),
                arguments(true, true, false, 0b011),
                arguments(true, true, true, 0b111),
            ).iterator()
    }

    @ParameterizedTest
    @MethodSource("testData")
    fun `Test serializer`(write: Boolean, read: Boolean, execute: Boolean, expected: Int) {
        val example = Example(write, read, execute)
        val json = Json.encodeToJsonElement(example)
        assertEquals(expected, json.jsonPrimitive.int)
        val decodedExample = Json.decodeFromJsonElement<Example>(json)
        assertEquals(example, decodedExample)
    }
}
