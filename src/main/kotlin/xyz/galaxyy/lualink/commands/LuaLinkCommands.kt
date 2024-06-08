package xyz.galaxyy.lualink.commands

import org.bukkit.command.CommandSender
import org.incendo.cloud.annotation.specifier.Greedy
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.luaj.vm2.lib.jse.JsePlatform
import xyz.galaxyy.lualink.LuaLink
import xyz.galaxyy.lualink.lua.LuaScript
import xyz.galaxyy.lualink.lua.LuaScriptManager
import java.io.File

@Suppress("unused")
class LuaLinkCommands(private val plugin: LuaLink, private val scriptManager: LuaScriptManager) {

    @Command("lualink reload <script>")
    @Permission("lualink.scripts.reload")
    @CommandDescription("Reload a Lua script")
    fun reloadScript(sender: CommandSender, script: LuaScript) {
        val fileName = script.file.name
        this.scriptManager.unLoadScript(script)
        this.scriptManager.loadScript(File(this.plugin.dataFolder, "scripts/$fileName"))
        sender.sendRichMessage("<green>Reloaded script <yellow>$fileName<green>.")
    }

    @CommandDescription("Unload a Lua script")
    @Command("lualink unload <script>")
    @Permission("lualink.scripts.unload")
    fun unloadScript(sender: CommandSender, script: LuaScript) {
        this.scriptManager.unLoadScript(script)
        sender.sendRichMessage("<green>Unloaded script <yellow>${script.file.name}<green>.")
    }

    @CommandDescription("Load a Lua script")
    @Command("lualink load <script>")
    @Permission("lualink.scripts.load")
    fun loadScript(sender: CommandSender, script: File) {
        this.scriptManager.loadScript(script)
        sender.sendRichMessage("<green>Loaded script <yellow>${script.name}<green>.")
    }

    @CommandDescription("Disable a Lua script")
    @Command("lualink disable <script>")
    @Permission("lualink.scripts.disable")
    fun disableScript(sender: CommandSender, script: LuaScript) {
        scriptManager.disableScript(script)
        sender.sendRichMessage("<green>Disabled and unloaded script <yellow>${script.file.name}<green>.")
    }

    @Command("lualink enable <script>")
    @Permission("lualink.scripts.enable")
    @CommandDescription("Enable a Lua script")
    fun enableScript(sender: CommandSender, script: File) {
        scriptManager.enableScript(script)
        sender.sendRichMessage("<green>Enabled and loaded script <yellow>${script.name}<green>.")
    }

    @Command("lualink run <code>")
    @Permission("lualink.scripts.run")
    @CommandDescription("Run Lua code")
    fun runCode(sender: CommandSender, @Greedy code: String) {
        JsePlatform.standardGlobals().load(code).eval()
    }
}