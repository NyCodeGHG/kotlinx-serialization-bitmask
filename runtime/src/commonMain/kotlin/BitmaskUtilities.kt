package dev.nycode.kotlinx.serialization.bitmask.runtime

@InternalBitmaskApi
public fun Number.toBoolean(): Boolean {
    return when (this) {
        0 -> false
        1 -> true
        else -> error("Can't parse number $this to a boolean.")
    }
}

@InternalBitmaskApi
public fun Boolean.toInt(): Int {
    return if (this) 1 else 0
}
