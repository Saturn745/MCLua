package xyz.galaxyy.lualink.api.lua

import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.OneArgFunction
import xyz.galaxyy.lualink.api.LuaLinkAPI

internal class LuaAddons : LuaTable() {
    init {
        this.set("get", object : OneArgFunction() {
            override fun call(arg: org.luaj.vm2.LuaValue?): org.luaj.vm2.LuaValue {
                if (arg == null) {
                    throw IllegalArgumentException("get expects 1 argument: addonName")
                }
                val addonName = arg.checkjstring()

                return LuaLinkAPI.getAddonFromCache(addonName)
                    ?: throw IllegalArgumentException("Addon $addonName does not exist")
            }
        })

    }
}