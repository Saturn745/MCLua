# LuaLink Plugin
<a href=https://modrinth.com/plugin/lualink><img alt="modrinth" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg"></a>
<a href=https://hangar.papermc.io/Saturn/LuaLink><img alt="hangar" height="40" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/hangar_vector.svg"></a>

LuaLink is an experimental plugin that provides a basic Lua scripting runtime for Paper-based Minecraft servers. It is designed for small and simple tasks and serves as an alternative to Skript.

<br />

## Requirements

To use the LuaLink plugin, you need the following:

- A [Paper](https://papermc.io/) based Minecraft server.
- A basic understanding of Lua scripting.

<br />

## Documentation
The project is in an experimental state, so is the documentation, and you can expect things to change quite often. For new **Lua** users, [this community-contributed documentation](https://devdocs.io/lua~5.2-language/) may help you to get started.

Lua scripting runtime is based on [luakt](https://github.com/only52607/luakt) and [luaj](https://github.com/luaj/luaj). For more details on implementation specifics or differences, please refer to their respective documentation.

<br />

#### Basic Usage
Scripts are automatically loaded after server has been fully started. They can also be loaded, unloaded or reloaded manually using commands.
```lua
-- Called when script has been loaded.
script.onLoad(function()
    script.logger.info("Script has been loaded.")
end)

-- Called before unloading script.
script.onUnload(function()
    script.logger.info("Script is about to be unloaded.")
end)
```

<br />

#### Importing
Each referenced Java and Kotlin class must be imported using the `import` keyword.
```lua
local Bukkit = import "org.bukkit.Bukkit"
local MiniMessage = import "net.kyori.adventure.text.minimessage.MiniMessage"

script.onLoad(function(event)
    -- Creating Component using MiniMessage serializer. https://docs.advntr.dev/minimessage/index.html
    local component = MiniMessage:miniMessage():deserialize("<rainbow>Did you know you can make rainbow text?!")
    -- Sending component to everyone, including console.
    Bukkit:getServer():sendMessage(component)
end)
```

<br />

#### Constructors and Instances
New instances of Java and Kotlin classes can be created as follows.
```lua
local Bukkit = import "org.bukkit.Bukkit"
local NamespacedKey = import "org.bukkit.NamespacedKey"

script.onLoad(function()
    -- Creating new instance of NamespacedKey class.
    local key = NamespacedKey.new("minecraft", "overworld")
    -- Getting instance of the primary world.
    local world = Bukkit:getWorld(key)
    -- Checking if World is instance of Keyed. (SPOILER: IT IS)
    if (utils.instanceOf(world, "net.kyori.adventure.key.Keyed") == true) then
        -- Sending loaded chunks count to the console.
        script.logger.info("World " .. world:key():asString() .. " has " .. world:getChunkCount() .. " chunks loaded.")
    end
end)
```

<br />

#### Commands
Non-complex commands can be created with little effort using built-in API.
```lua
local Bukkit = import "org.bukkit.Bukkit"

-- Function to handle command tab-completion.
function onTabComplete(sender, alias, args)
    -- No suggestions will be shown for this command.
    return {}
end

script.registerSimpleCommand(function(sender, args)
    -- Joining arguments to string using space as delimiter.
    local message = table.concat(args, " ")
    -- Sending message back to the sender.
    sender:sendRichMessage(message)
end, {
    -- REQUIRED
    name = "echo",
    -- OPTIONAL
    aliases = {"e", "print"},
    permission = "scripts.command.echo",
    description = "Prints specified message to the sender.",
    usage = "/echo [player]",
    tabComplete = onTabComplete
})
```

<br />

#### Events
Bukkit events can be hooked into relatively easily.
```lua
-- Called when player joins the server.
script.hook("org.bukkit.event.player.PlayerJoinEvent", function(event)
    -- Getting player associated with the event. 
    local player = event:getPlayer()
    -- Playing firework sound to the player.
    player:playSound(player:getLocation(), "entity.firework_rocket.launch", 1.0, 1.0)
    -- Sending welcome message to the player.
    player:sendRichMessage("<green>Welcome back to the server, " .. player:getName() .. "!")
end)
```

<br />

#### Scheduler
Scheduler can be used to register single-use, delayed or repeating tasks.
```lua
-- Schedules task to be run on the next tick.
script.run(function()
    -- Whatever belongs to the task goes here.
end)

-- Schedules task to be run after 20 ticks has passed. Task function parameter can be ommited if not used. 
script.runDelayed(function(task)
    -- Whatever belongs to the task goes here.
end, 20)

-- Schedules task to be run after 20 ticks has passed, and repeated every 160 ticks. Task function parameter can be ommited if not used. 
script.runRepeating(function(task)
    -- Whatever belongs to the task goes here.
end, 20, 160)
```

Tasks can also be run asynchronously, but please note that neither the Bukkit API nor the LuaLink API is guaranteed to be thread-safe.
```lua
-- Schedules asynchronous task to be run on the next tick.
script.runAsync(callback: () -> void): void
-- Schedules asynchronous task to be run after {delay} ticks has passed. Task function parameter can be ommited if not used. 
script.runDelayedAsync(callback: (task: BukkitTask) -> void, delay: number): BukkitTask
-- Schedules task to be run after {delay} ticks has passed, and repeated every {period} ticks. Task function parameter can be ommited if not used. 
script.runRepeatingAsync(callback: (task: BukkitTask) -> void, delay: number, period: number): BukkitTask
```

<br />

#### Examples
Additional examples can be found in the [examples folder](https://github.com/LuaLink/LuaLink/tree/main/_examples) or [scripts collection repository](https://github.com/LuaLink/Scripts).