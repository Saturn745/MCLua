package xyz.galaxyy.lualink.commands.arguments

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.incendo.cloud.annotations.parser.Parser
import org.incendo.cloud.annotations.suggestion.Suggestions
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.context.CommandInput
import xyz.galaxyy.lualink.LuaLink
import xyz.galaxyy.lualink.commands.exceptions.ReplyingParseException
import xyz.galaxyy.lualink.lua.LuaScriptManager
import java.io.File

class AvailableScriptParser(private val plugin: LuaLink, private val scriptManager: LuaScriptManager) {

    @Parser(suggestions = "available_scripts") // This parser does not specify a name, making it default parser for the returned type.
    fun parse(context: CommandContext<CommandSender>, input: CommandInput) : File {
        val value = input.readString()
        val file = File(this.plugin.dataFolder, "scripts" + File.separator + value)
        // Throwing exception if file exists, is a directory, is not a .lua file or is already loaded.
        if (!file.exists() || file.isDirectory || file.extension != "lua" || this.scriptManager.getLoadedScripts().any { it.file.name.equals(file.name) })
            throw ReplyingParseException { context.sender().sendRichMessage("<red>Script <yellow>${MiniMessage.miniMessage().stripTags(value)} <red>has not been found or is already loaded.") }
        // Returning the value.
        return file
    }

    @Suggestions("available_scripts")
    fun suggest(context: CommandContext<CommandSender>, input: CommandInput) : Iterable<String> {
        return File(this.plugin.dataFolder, "scripts").listFiles()
            ?.filter { it.extension == "lua" && !it.name.contains(' ') && !this.scriptManager.getLoadedScripts().any { script -> script.file.name.equals(it.name) } }
            ?.map { it.name } ?: emptyList()
    }

}