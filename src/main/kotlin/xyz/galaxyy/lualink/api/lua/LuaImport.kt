package xyz.galaxyy.lualink.api.lua

import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.jse.CoerceJavaToLua

class LuaImport : OneArgFunction() {
    override fun call(arg: LuaValue?): LuaValue {
        if (arg == null) {
            throw IllegalArgumentException("import expects 1 argument: string")
        }

        val import = arg.checkjstring()

        val importClass = Class.forName(import)

        return CoerceJavaToLua.coerce(importClass)
    }
}