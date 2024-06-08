package xyz.galaxyy.lualink.commands.arguments

import org.bukkit.command.CommandSender
import org.incendo.cloud.annotations.parser.Parser
import org.incendo.cloud.annotations.suggestion.Suggestions
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.context.CommandInput
import xyz.galaxyy.lualink.commands.exceptions.ReplyingParseException
import xyz.galaxyy.lualink.lua.LuaScript
import xyz.galaxyy.lualink.lua.LuaScriptManager

class LoadedScriptParser(private val scriptManager: LuaScriptManager) {

    @Parser(suggestions = "loaded_scripts") // This parser does not specify a name, making it default parser for the returned type.
    fun parse(context: CommandContext<CommandSender>, input: CommandInput) : LuaScript {
        val value = input.readString()
        val script = this.scriptManager.getLoadedScripts().find { it.file.name == value }
        // Returning the value or throwing exception if null.
        return script ?: throw ReplyingParseException { context.sender().sendRichMessage("<red>Script <yellow>$value <red>has not been found or is not loaded.") }
    }

    @Suggestions("loaded_scripts")
    fun suggest(context: CommandContext<CommandSender>, input: CommandInput) : Iterable<String> {
        return this.scriptManager.getLoadedScripts().map { it.file.name }
    }

}