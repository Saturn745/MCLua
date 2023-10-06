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
- **[External Libraries/API](external_libraries.md)**
- **Addons**
- **[Reference](reference.md)**

<br />

# Addons
Addons extend Lua scripting capabilities by adding custom variables and functions.

<br />

### Usage Example
Addons are distributed as `.jar` files and should be placed inside your server's `plugins/` directory.
```lua
-- Getting addon instance.
local MiniPlaceholders = addons.get("MiniPlaceholders")

-- Retrieving global placeholders,
local globalPlaceholders = MiniPlaceholders.getGlobalPlaceHolders()
```
In this example, we are getting instance of `MiniPlaceholders` addon provided by [LuaLink-MiniPlaceholders](), and using it to retrieve global placeholders. Of course, different addons will serve different purposes, some may expose plugins' API, while others may add utility functions.

<br />


### Existing Addons
List of both official and community-made addons. Please refer to their respective documentation to learn how to interact with them.
- [LuaLink-MiniPlaceholders](https://github.com/LuaLink/LuaLink-MiniPlaceholders) - allows to use MiniPlaceholders' placeholders within LuaLink scripts.
- [LuaLink-Vault](https://github.com/LuaLink/LuaLink-Vault) - allows to interact with Vault plugin.

<br />

### Creating Addon
Not documented yet. You can use [this addon template](https://github.com/LuaLink/LuaLink-ExampleAddon) to get started.