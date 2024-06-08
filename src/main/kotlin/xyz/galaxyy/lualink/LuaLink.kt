package xyz.galaxyy.lualink

import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.bukkit.CloudBukkitCapabilities
import org.incendo.cloud.exception.ArgumentParseException
import org.incendo.cloud.exception.handling.ExceptionHandler.unwrappingHandler
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.LegacyPaperCommandManager
import xyz.galaxyy.lualink.commands.arguments.AvailableScriptParser
import xyz.galaxyy.lualink.commands.arguments.LoadedScriptParser
import xyz.galaxyy.lualink.commands.LuaLinkCommands
import xyz.galaxyy.lualink.commands.exceptions.ReplyingParseException
import xyz.galaxyy.lualink.listeners.ServerLoadListener
import xyz.galaxyy.lualink.lua.LuaScriptManager

class LuaLink : JavaPlugin() {
    private lateinit var manager: LegacyPaperCommandManager<CommandSender>
    private lateinit var annotationParser: AnnotationParser<CommandSender>
    private val scriptManager: LuaScriptManager = LuaScriptManager(this)

    override fun onEnable() {
        this.server.pluginManager.registerEvents(ServerLoadListener(this.scriptManager), this)
        this.setupCommands()
    }

    override fun onDisable() {
        val scripts = this.scriptManager.getLoadedScripts()
        scripts.forEach(this.scriptManager::unLoadScript)
    }

    private fun setupCommands() {
        // Creating basic LegacyPaperCommandManager instance. We're not using PaperCommandManager here because it only supports 1.20.6 and higher.
        this.manager = LegacyPaperCommandManager.createNative(this, ExecutionCoordinator.simpleCoordinator())
        // Registering Brigadier integration if capable, or asynchronous completions otherwise. It's not recommended to have both capabilities registered.
        if (this.manager.hasCapability(CloudBukkitCapabilities.BRIGADIER))
            this.manager.registerBrigadier()
        else this.manager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)
        // Creating AnnotationParser instance.
        this.annotationParser = AnnotationParser(this.manager, CommandSender::class.java)
        // Registering handler(s)...
        this.manager.exceptionController().registerHandler(ArgumentParseException::class.java, unwrappingHandler(ReplyingParseException::class.java))
        this.manager.exceptionController().registerHandler(ReplyingParseException::class.java) { it.exception().runnable.run() }
        // Registering parser(s)...
        this.annotationParser.parse(AvailableScriptParser(this, this.scriptManager))
        this.annotationParser.parse(LoadedScriptParser(this.scriptManager))
        // Registering command(s)...
        this.annotationParser.parse(LuaLinkCommands(this, this.scriptManager))
    }

}
