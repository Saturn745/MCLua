package xyz.galaxyy.mclua.lua.wrappers

import com.github.only52607.luakt.CoerceKotlinToLua
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.luaj.vm2.*
import org.luaj.vm2.lib.VarArgFunction
import org.luaj.vm2.lib.ZeroArgFunction
import xyz.galaxyy.mclua.MCLua
import xyz.galaxyy.mclua.lua.commands.LuaCommandHandler
import xyz.galaxyy.mclua.lua.misc.LuaLogger


class LuaPluginWrapper : LuaTable() {
    var onLoadCB: LuaValue? = null
        private set

    var onEnableCB: LuaValue? = null
        private set

    var onDisableCB: LuaValue? = null
        private set

    val commands: MutableList<LuaCommandHandler> = mutableListOf()
    val listeners: MutableList<Listener> = mutableListOf()

    init {
        this.set("onLoad", object : VarArgFunction() {
            override fun call(callback: LuaValue): LuaValue {
                if (callback.isfunction()) {
                    onLoadCB = callback
                } else {
                    throw LuaError("onLoad callback must be a function")
                }
                return LuaValue.NIL
            }
        })

        this.set("onEnable", object : VarArgFunction() {
            override fun call(callback: LuaValue): LuaValue {
                if (callback.isfunction()) {
                    onEnableCB = callback
                } else {
                    throw LuaError("onEnable callback must be a function")
                }
                return LuaValue.NIL
            }
        })

        this.set("onDisable", object : VarArgFunction() {
            override fun call(callback: LuaValue): LuaValue {
                if (callback.isfunction()) {
                    onDisableCB = callback
                } else {
                    throw LuaError("onDisable callback must be a function")
                }
                return LuaValue.NIL
            }
        })

        this.set("registerSimpleCommand", object : VarArgFunction() {
            override fun invoke(args: Varargs): Varargs {
                if (args.narg() != 2 || !args.isfunction(1) || !args.istable(2)) {
                    throw IllegalArgumentException("registerSimpleCommand expects 2 arguments: function, string")
                }

                val callback: LuaFunction = args.checkfunction(1)
                val metadata: LuaTable = args.checktable(2)

                this@LuaPluginWrapper.registerCommand(callback, metadata)
                return LuaValue.NIL
            }
        })

        this.set("hook", object: VarArgFunction() {
            override fun invoke(args: Varargs): LuaValue {
                val eventName: String = args.checkjstring(1)
                val callback: LuaFunction = args.checkfunction(2)

                if (eventName.isEmpty() || !callback.isfunction()) {
                    throw IllegalArgumentException("hook expects 2 arguments: string, function")
                }

                this@LuaPluginWrapper.registerListener(eventName, callback)

                return LuaValue.NIL
            }
        })

        this.set("logger", CoerceKotlinToLua.coerce(LuaLogger()))
        this.set("getServer", object: ZeroArgFunction() {
            override fun call(): LuaValue {
                return CoerceKotlinToLua.coerce(Bukkit.getServer())
            }
        })
    }

    private fun registerCommand(callback: LuaFunction, metadata: LuaTable) {
        val command: LuaCommandHandler = LuaCommandHandler(callback, metadata)

        this.commands.add(command)

        MCLua.getInstance().server.commandMap.register("mclua", command)
    }
    private fun registerListener(event: String, callback: LuaFunction) {
        try {
            val eventClass = Class.forName(event)
            if (!Event::class.java.isAssignableFrom(eventClass)) {
                throw IllegalArgumentException("Event class must be a subclass of org.bukkit.event.Event")
            }
            val listener = object : Listener {}

            MCLua.getInstance().server.pluginManager.registerEvent(eventClass as Class<out org.bukkit.event.Event>, listener, EventPriority.NORMAL, { _, event ->
                callback.invoke(CoerceKotlinToLua.coerce(event))
            }, MCLua.getInstance())

            this.listeners.add(listener)

        } catch (e: ClassNotFoundException) {
            throw IllegalArgumentException("Event class not found: $event")
        }
    }
}