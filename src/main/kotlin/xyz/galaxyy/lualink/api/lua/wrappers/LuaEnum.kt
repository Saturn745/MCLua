package xyz.galaxyy.lualink.api.lua.wrappers

import com.github.only52607.luakt.CoerceKotlinToLua
import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.OneArgFunction
import kotlin.reflect.KClass

class LuaEnum<T : Enum<T>>(enumClass: KClass<T>) : LuaTable() {
    init {
        this.set("valueOf", object : OneArgFunction() {
            override fun call(arg: org.luaj.vm2.LuaValue?): org.luaj.vm2.LuaValue {
                if (arg == null) {
                    throw IllegalArgumentException("valueOf expects 1 argument: string")
                }

                val enumName = arg.checkjstring()

                return CoerceKotlinToLua.coerce(enumClass.java.enumConstants.find { it.name == enumName })
            }
        })

        this.set("values", object : OneArgFunction() {
            override fun call(arg: org.luaj.vm2.LuaValue?): org.luaj.vm2.LuaValue {
                if (arg == null) {
                    throw IllegalArgumentException("values expects 1 argument: string")
                }

                return CoerceKotlinToLua.coerce(enumClass.java.enumConstants)
            }
        })

        this.set("get", object : OneArgFunction() {
            override fun call(arg: org.luaj.vm2.LuaValue?): org.luaj.vm2.LuaValue {
                if (arg == null) {
                    throw IllegalArgumentException("get expects 1 argument: string")
                }

                val enumName = arg.checkjstring()

                return CoerceKotlinToLua.coerce(enumClass.java.enumConstants.find { it.name == enumName })
            }
        })

        for (enum in enumClass.java.enumConstants) {
            this.set(enum.name, CoerceKotlinToLua.coerce(enum))
        }
    }
}