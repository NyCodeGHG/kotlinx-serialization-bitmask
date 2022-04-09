import dev.nycode.kotlinx.serialization.bitmask.SerializableBitmask
import kotlinx.serialization.Serializable

@SerializableBitmask
@Serializable(with = UnixPermissionSerializer::class)
data class UnixPermission(
    val read: Boolean,
    val write: Boolean,
    val execute: Boolean
)
