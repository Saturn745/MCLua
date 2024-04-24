package xyz.galaxyy.lualink.api

import com.github.only52607.luakt.CoerceKotlinToLua
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.luaj.vm2.*
import org.luaj.vm2.lib.VarArgFunction
import org.luaj.vm2.lib.ZeroArgFunction
import xyz.galaxyy.lualink.api.lua.commands.LuaCommandHandler
import xyz.galaxyy.lualink.api.lua.misc.LuaLogger

// LuaScript contains the Lua script's globals, callbacks, and command and listener handlers and is used to store script state and metadata
open class LuaScript(private val plugin: JavaPlugin, val globals: Globals) : LuaTable() {
    internal var onLoadCB: LuaValue? = null
        private set

    internal var onUnloadCB: LuaValue? = null
        private set

    val commands: MutableList<LuaCommandHandler> = mutableListOf()
    val listeners: MutableList<Listener> = mutableListOf()
    // Stores task IDs so they can be cancelled on unload
    internal val tasks: MutableList<Int> = mutableListOf()
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

        this.set("onUnload", object : VarArgFunction() {
            override fun call(callback: LuaValue): LuaValue {
                if (callback.isfunction()) {
                    onUnloadCB = callback
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

                this@LuaScript.registerCommand(callback, metadata)
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

                this@LuaScript.registerListener(eventName, callback)

                return LuaValue.NIL
            }
        })

        this.set("logger", LuaLogger(this.plugin))
        this.set("getServer", object: ZeroArgFunction() {
            override fun call(): LuaValue {
                return CoerceKotlinToLua.coerce(Bukkit.getServer())
            }
        })
    }

    fun initialize() {
        Bukkit.getServer().javaClass.getMethod("syncCommands").invoke(Bukkit.getServer())
    }

    fun cleanup() {
        this.listeners.forEach { listener ->
            HandlerList.unregisterAll(listener)
        }
        this.commands.forEach { command ->
            command.unregister(this.plugin.server.commandMap)
            this.plugin.server.commandMap.knownCommands.remove(command.name)
            command.aliases.forEach { alias ->
                this.plugin.server.commandMap.knownCommands.remove(alias)
            }
        }
        Bukkit.getServer().javaClass.getMethod("syncCommands").invoke(Bukkit.getServer())
    }

    private fun registerCommand(callback: LuaFunction, metadata: LuaTable) {
        val command = LuaCommandHandler(this.plugin, callback, metadata)

        this.commands.add(command)

        this.plugin.server.commandMap.register("lualinkscript", command)
    }
    private fun registerListener(event: String, callback: LuaFunction) {
        try {
            val eventClass = Class.forName(event)
            if (!Event::class.java.isAssignableFrom(eventClass)) {
                throw IllegalArgumentException("Event class must be a subclass of org.bukkit.event.Event")
            }
            val listener = object : Listener {}

            this.plugin.server.pluginManager.registerEvent(eventClass as Class<out org.bukkit.event.Event>, listener, EventPriority.NORMAL, { _, eventObj ->
                if (eventClass.isInstance(eventObj))
                    callback.invoke(CoerceKotlinToLua.coerce(eventObj))
            }, this.plugin)

            this.listeners.add(listener)

        } catch (e: ClassNotFoundException) {
            throw IllegalArgumentException("Event class not found: $event")
        }
    }
}