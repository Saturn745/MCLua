# LuaLink Documentation
The project is in an experimental state, so is the documentation - expect things to change quite often. 

For new Lua users, [this community-contributed documentation](https://devdocs.io/lua~5.2-language/) may help you to get started.

<br />

### Navigation
1. **[Basics](#1-basics)**
2. **[Script Life-cycle](#2-script-life-cycle)**
3. **[Importing](#3-importing)**
4. **[Constructors and Instances](#4-constructors-and-instances)**
5. **[Commands](#5-commands)**
6. **[Events](#6-events)**
7. **[Scheduler](#7-scheduler)**
- **[External Libraries/API](/docs/external_libraries.md)**
- **[Addons](/docs/addons.md)**
- **[Reference](/docs/reference.md)**

<br />

### 1. Basics
In your Lua scripts, you have access to the following global variables that are set in every script.
- `script` - represents the current Lua script.
- `print` - overrides the default `print` function to log messages to the plugin's logger.
- `utils` - provides utility functions.
- `enums` - provides access to various enums.
- `import` - allows importing Java or Kotlin classes by name.
- `addons` - provides access to LuaLink addons.

More information about these variables and their functions can be found on the [Reference](reference.md) page.

<br />

### 2. Script Life-cycle
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

### 3. Importing
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

### 4. Constructors and Instances
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

### 5. Commands
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

### 6. Events
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

### 7. Scheduler
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

### Examples
Additional examples can be found in the [examples folder](/_examples) or [scripts collection repository](https://github.com/LuaLink/Scripts).