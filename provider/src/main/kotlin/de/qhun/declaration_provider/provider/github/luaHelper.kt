package de.qhun.declaration_provider.provider.github

import org.luaj.vm2.LuaNil
import org.luaj.vm2.LuaValue

internal fun <T> LuaValue.getNullable(index: String, mapper: (LuaValue) -> T? = { a -> a as? T }): T? {
    val value = this.rawget(index)
    if (value is LuaNil) {
        return null
    }
    return mapper(value)
}

internal inline fun <reified T> LuaValue.getNullable(index: Int, mapper: (LuaValue) -> T? = { a -> a as? T }): T? {
    val value = this.rawget(index)
    if (value is LuaNil) {
        return null
    }
    return mapper(value)
}
