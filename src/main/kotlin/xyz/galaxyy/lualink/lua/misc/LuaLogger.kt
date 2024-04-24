package xyz.galaxyy.lualink.lua.misc

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.plugin.java.JavaPlugin
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import xyz.galaxyy.lualink.LuaLink

class LuaLogger(private val plugin: JavaPlugin) : LuaTable() {
    init {
        this.set("info", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue? {
                plugin.componentLogger.info(MiniMessage.miniMessage().deserialize(arg.tojstring()))
                return LuaValue.NIL
            }
        })

        this.set("warning", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue? {
                plugin.componentLogger.warn(MiniMessage.miniMessage().deserialize(arg.tojstring()))
                return LuaValue.NIL
            }
        })

        this.set("error", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue? {
                plugin.componentLogger.error(MiniMessage.miniMessage().deserialize(arg.tojstring()))
                return LuaValue.NIL
            }
        })

        this.set("severe", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue? {
                plugin.componentLogger.error(MiniMessage.miniMessage().deserialize(arg.tojstring()))
                return LuaValue.NIL
            }
        })
    }
}