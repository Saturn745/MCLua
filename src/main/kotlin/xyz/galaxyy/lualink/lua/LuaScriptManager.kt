package xyz.galaxyy.lualink.lua

import org.luaj.vm2.LuaError
import xyz.galaxyy.lualink.LuaLink
import xyz.galaxyy.lualink.api.LuaLinkAPI
import xyz.galaxyy.lualink.api.LuaScript
import java.io.File

internal class LuaScriptManager(private val plugin: LuaLink) {
    private val loadedScripts: MutableList<LuaScript> = mutableListOf()

    fun getLoadedScripts(): List<LuaScript> {
        return loadedScripts.toList()
    }

    fun loadScript(file: File) {
        val script = LuaLinkAPI.createNewScriptEnvironment(plugin)
        this.plugin.logger.info("Loading script ${file.name}")
        try {
            script.globals.loadfile(file.path).call()
        } catch (e: LuaError) {
            this.plugin.logger.severe("LuaLink encountered an error while loading ${file.name}: ${e.message}")
            return
        }
        loadedScripts.add(script)
        if (script.onLoadCB?.isfunction() == true) {
            try {
                script.onLoadCB?.call()
            } catch (e: LuaError) {
                this.plugin.logger.severe("LuaLink encountered an error while called onLoad for ${file.name}: ${e.message}")
                return
            }
        }
        script.globals.set("__file_path", file.path)
        script.globals.set("__file_name", file.name)
        script.initialize() // Call the generic init method
        this.plugin.logger.info("Loaded script ${file.name}")
    }

    fun unLoadScript(script: LuaScript) {
        script.cleanup() // Call the generic cleanup method
        if (script.onUnloadCB?.isfunction() == true) {
            try {
                script.onUnloadCB?.call()
            } catch (e: LuaError) {
                this.plugin.logger.severe("LuaLink encountered an error while called onUnload for ${script.globals.get("__file_name").tojstring()}: ${e.message}")
                return
            }
        }
        this.loadedScripts.remove(script)
    }

    fun disableScript(script: LuaScript) {
        val filePath = script.globals.get("__file_path").tojstring()
        val file = File(filePath)
        file.renameTo(File("$filePath.d"))
        this.unLoadScript(script)
    }

    fun enableScript(script: File) {
        script.renameTo(File(script.path.removeSuffix(".d")))
        this.loadScript(File(script.path.removeSuffix(".d")))
    }

    fun loadScripts() {
        this.plugin.logger.info("Loading scripts...")
        if (!File(this.plugin.dataFolder.path+"/scripts").exists()) {
            File(this.plugin.dataFolder.path+"/scripts").mkdirs()
        }

        File(this.plugin.dataFolder.path+"/scripts").walk().forEach { file ->
            if (file.extension == "lua") {
                if (file.name.startsWith(".")) {
                    return@forEach
                }
                this.loadScript(file)
            } else {
                if (file.name != "scripts") {
                    if (file.name.endsWith(".d"))
                        return@forEach
                    this.plugin.logger.warning("${file.name} is in the scripts folder but is not a lua file!")
                }
            }
        }
    }
}