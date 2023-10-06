# LuaLink Documentation
The project is in an experimental state, so is the documentation - expect things to change quite often.

For new Lua users, [this community-contributed documentation](https://devdocs.io/lua~5.2-language/) may help you to get started.

<br />

### Navigation
1. **[Basics](main.md#1-basics)**
2. **[Script Life-cycle](main.md#2-script-life-cycle)**
3. **[Importing](main.md#3-importing)**
4. **[Constructors and Instances](main.md#4-constructors-and-instances)**
5. **[Commands](main.md#5-commands)**
6. **[Events](main.md#6-events)**
7. **[Scheduler](main.md#7-scheduler)**
- **[External Libraries/API](external_libraries.md#external-libraries)**
- **[Addons](addons.md#addons)**
- **Reference**

<br />

## Reference
Full reference of variables and functions provided by LuaLink.

<br />

### [LuaScript](https://github.com/LuaLink/LuaLink/blob/main/src/main/kotlin/xyz/galaxyy/lualink/lua/LuaScript.kt)
Click the link to view `LuaScript` class in the source code.

<br />

`script: LuaScript`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; LuaScript class, accessed with `script`, represents a Lua script that can be executed within your LuaLink plugin.

<br />

`script.logger: LuaLogger`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Returns the [LuaLogger](https://github.com/LuaLink/LuaLink/blob/main/src/main/kotlin/xyz/galaxyy/lualink/lua/misc/LuaLogger.kt) instance of LuaLink plugin.

<br />

`script.getServer(): Server`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Returns the [Server](https://jd.papermc.io/paper/1.20/org/bukkit/Server.html) instance.

<br />

`script.onLoad(callback: () -> void): void`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Called after the script has been successfully loaded.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `callback` - function to be executed.

<br />

`script.onUnload(callback: () -> void): void`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Called before the script is attempted to be unloaded.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `callback` - function to be executed.

<br />

`script.hook(event: string, callback: (event: Event) -> void): void`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Registers an event hook for specified event class.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `event` - the name of the event class to hook into.  
&nbsp;&nbsp;&nbsp;&nbsp; `callback` - function to execute when the event occurs.

<br />

`script.registerSimpleCommand(callback: (sender: CommandSender, args: table) -> void, metadata: table): void`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Registers command with specified `callback` and `metadata` to the server.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `callback` - function to be executed when command is run.  
&nbsp;&nbsp;&nbsp;&nbsp; `metadata` - metadata for the command.

&nbsp;&nbsp; **Metadata**  
&nbsp;&nbsp;&nbsp;&nbsp; `name` - primary name of the command.  
&nbsp;&nbsp;&nbsp;&nbsp; `permission` - optional access permission of the command.  
&nbsp;&nbsp;&nbsp;&nbsp; `usage` - optional usage of the command.  
&nbsp;&nbsp;&nbsp;&nbsp; `description` - optional description of the command.  
&nbsp;&nbsp;&nbsp;&nbsp; `tabComplete` - optional function to be executed for tab-completion.

<br />
<br />

### [LuaUtils](https://github.com/LuaLink/LuaLink/blob/main/src/main/kotlin/xyz/galaxyy/lualink/lua/LuaUtils.kt)
Click the link to view `LuaUtils` class in the source code.

<br />

`utils: LuaUtils`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; The LuaUtils class, accessed with `utils`, provides utility functions for Lua scripts.

<br />

`utils.instanceOf(object: ?, class: string): boolean`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Checks if an object is an instance of a specified class.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `object` - the object to check.  
&nbsp;&nbsp;&nbsp;&nbsp; `class` - the name of the class to check against.

<br />

`utils.cancelTask(taskId: number): void`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Cancels a currently running Bukkit task.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `taskId` - the Bukkit task id.

<br />
<br />

### [LuaLogger](https://github.com/LuaLink/LuaLink/blob/main/src/main/kotlin/xyz/galaxyy/lualink/lua/misc/LuaLogger.kt)
Click the link to view `LuaUtils` class in the source code.

<br />

`logger: LuaUtils`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; The LuaLogger class, accessed with `logger`, allows you to log messages from your Lua scripts.

<br />

`logger.info(message: string): void`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Logs an informational message to the console.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `message` - the message to log as an information.

<br />

`logger.warning(message: string): void`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Logs a warning message to the console.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `message` - the message to log as a warning.

<br />

`logger.severe(message: string): void`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Logs a severe (error) message to the console.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `message` - the message to log as an error.

<br />
<br />

### [LuaScheduler](https://github.com/LuaLink/LuaLink/blob/main/src/main/kotlin/xyz/galaxyy/lualink/lua/LuaScheduler.kt)
Click the link to view `LuaScheduler` class in the source code.

<br />

`scheduler: LuaScheduler`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; The LuaScheduler class, accessed with `scheduler`, provides functions to register single-use, delayed or repeating tasks.

<br />

`scheduler.run(callback: () -> void): void`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Schedules task to be run on the next tick.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `callback` - function to be executed.

<br />

`scheduler.runAsync(callback: () -> void): void`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Schedules asynchronous task to be run on the next tick.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `callback` - function to be executed.

<br />

`scheduler.runDelayed(callback: (task: BukkitTask) -> void, delay: number): void`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Schedules task to be run after `delay` ticks has passed.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `callback` - function to be executed.  
&nbsp;&nbsp;&nbsp;&nbsp; `delay` - delay measured in ticks.

<br />

`scheduler.runDelayedAsync(callback: (task: BukkitTask) -> void, delay: number): void`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Schedules asynchronous task to be run after `delay` ticks has passed.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `callback` - function to be executed.  
&nbsp;&nbsp;&nbsp;&nbsp; `delay` - delay measured in ticks.

<br />

`scheduler.runRepeating(callback: (task: BukkitTask) -> void, delay: number, period: number): void`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Schedules task to be run after `delay` ticks has passed, and repeated every `delay` ticks.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `callback` - function to be executed.  
&nbsp;&nbsp;&nbsp;&nbsp; `delay` - delay measured in ticks.  
&nbsp;&nbsp;&nbsp;&nbsp; `period` - period of task re-occurrence measured in ticks.

<br />

`scheduler.runRepeatingAsync(callback: (task: BukkitTask) -> void, delay: number, period: number): void`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Schedules asynchronous task to be run after `delay` ticks has passed, and repeated every `delay` ticks.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `callback` - function to be executed.  
&nbsp;&nbsp;&nbsp;&nbsp; `delay` - delay measured in ticks.  
&nbsp;&nbsp;&nbsp;&nbsp; `period` - period of task re-occurrence measured in ticks.

<br />
<br />

### [LuaEnumWrapper](https://github.com/LuaLink/LuaLink/blob/main/src/main/kotlin/xyz/galaxyy/lualink/lua/wrappers/LuaEnumWrapper.kt)
Click the link to view `LuaEnumWrapper` class in the source code.

<br />

`enums: LuaEnumWrapper`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; The LuaUtils class, accessed with `utils`, provides utility functions for Lua scripts.

<br />

`enums.EntityType: LuaEnum<EntityType>`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Provides access to Bukkit's [EntityType](https://jd.papermc.io/paper/1.20/org/bukkit/entity/EntityType.html) enum wrapped as [LuaEnum<EntityType>](https://github.com/LuaLink/LuaLink/blob/main/src/main/kotlin/xyz/galaxyy/lualink/lua/wrappers/LuaEnum.kt).

<br />

`enums.Material: LuaEnum<Material>`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Provides access to Bukkit's [Material](https://jd.papermc.io/paper/1.20/org/bukkit/Material.html) enum wrapped as [LuaEnum<Material>](https://github.com/LuaLink/LuaLink/blob/main/src/main/kotlin/xyz/galaxyy/lualink/lua/wrappers/LuaEnum.kt).

<br />

`enums.Sound: LuaEnum<Sound>`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Provides access to Bukkit's [Sound](https://jd.papermc.io/paper/1.20/org/bukkit/Sound.html) enum wrapped as [LuaEnum<Sound>](https://github.com/LuaLink/LuaLink/blob/main/src/main/kotlin/xyz/galaxyy/lualink/lua/wrappers/LuaEnum.kt).

<br />
<br />

### [LuaEnum](https://github.com/LuaLink/LuaLink/blob/main/src/main/kotlin/xyz/galaxyy/lualink/lua/wrappers/LuaEnum.kt)
Click the link to view `LuaEnum<E>` class in the source code.

<br />

`(LuaEnum<E>).get(name: string): E`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Returns enum instance of `E` from provided case-sensitive name, or `nil` if not found.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `name` - case-sensitive name of the enum constant.

<br />

`(LuaEnum<E>).valueOf(name: string): E`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Returns enum instance of `E` from provided case-sensitive name, or `nil` if not found.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `name` - case-sensitive name of the enum constant.

<br />

`(LuaEnum<E>).values(): table`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Returns all enum constants defined in this enum class.

<br />
<br />

### [LuaAddons](https://github.com/LuaLink/LuaLink/blob/main/src/main/kotlin/xyz/galaxyy/lualink/lua/LuaAddons.kt)
Click the link to view `LuaAddons` class in the source code.

<br />

`addons: LuaAddons`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; The LuaAddons class, accessed with `addons`, provides a way to get instance of loaded addon.

<br />

`addons.get(name: string): ?`

&nbsp;&nbsp; **Description**  
&nbsp;&nbsp;&nbsp;&nbsp; Returns instance of addon with specified name. Throws `IllegalArgumentException` if not found.

&nbsp;&nbsp; **Parameters**  
&nbsp;&nbsp;&nbsp;&nbsp; `name` - case-sensitive name of the addon.

<br />
