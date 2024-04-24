package xyz.galaxyy.lualink.api

import com.github.only52607.luakt.lib.LuaKotlinExLib
import com.github.only52607.luakt.lib.LuaKotlinLib
import org.bukkit.plugin.java.JavaPlugin
import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.jse.JsePlatform
import xyz.galaxyy.lualink.api.addons.LuaAddon
import xyz.galaxyy.lualink.lua.*
import xyz.galaxyy.lualink.lua.LuaAddons
import xyz.galaxyy.lualink.lua.misc.PrintOverride
import xyz.galaxyy.lualink.lua.wrappers.LuaEnumWrapper

class LuaLinkAPI {
    //TODO: API should probably be in it's own package.
    companion object {
        private val registeredAddons = mutableMapOf<String, LuaAddon>()
        private val addonCache = mutableMapOf<String, LuaTable>()

        /**
         * Registers an addon to the LuaLink API.
         * @param name The name of the addon.
         * @param addon The addon to register.
         */
        fun registerAddon(name: String, addon: LuaAddon) {
            registeredAddons[name] = addon
            // Cache the addon's LuaTable when registering it.
            val addonTable = LuaTable()
            for (func in addon.getFunctions()) {
                addonTable.set(func.key, func.value)
            }
            for (table in addon.getTables()) {
                addonTable.set(table.key, table.value)
            }
            addonCache[name] = addonTable
        }


        /**
         * Unregisters an addon from the LuaLink API.
         * @param name The name of the addon.
         */
        fun unregisterAddon(name: String) {
            registeredAddons.remove(name)
            addonCache.remove(name)
        }

        internal fun getAddon(name: String): LuaAddon? {
            return registeredAddons[name]
        }

        internal fun getAddonFromCache(name: String): LuaTable? {
            return addonCache[name]
        }

        fun createNewScriptEnvironment(plugin: JavaPlugin): LuaScript {
            val globals = JsePlatform.standardGlobals()
            val script = LuaScript(plugin, globals)
            globals.load(LuaKotlinLib())
            globals.load(LuaKotlinExLib())
            globals.set("script", script)
            globals.set("print", PrintOverride(plugin))
            globals.set("utils", LuaUtils()) // Passing script to LuaUtils for state
            globals.set("scheduler", LuaScheduler(plugin, script)) // Passing script to LuaScheduler for state
            globals.set("enums", LuaEnumWrapper()) //TODO: Remove as it's obsolete now that you can import Java classes
            globals.set("import", LuaImport())
            globals.set("addons", LuaAddons())
            return script
        }
    }
}

