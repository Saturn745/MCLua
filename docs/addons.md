# LuaLink Documentation
The project is in an experimental state, so is the documentation - expect things to change quite often.

For new Lua users, [this community-contributed documentation](https://devdocs.io/lua~5.2-language/) may help you to get started.

<br />

### Navigation
- **[Home](home.md#navigation)**
- **[External Libraries/API](external_libraries.md#navigation)**
- **Addons**
  - [Home](#addons)
  - [Usage Example](#usage-example)
  - [Existing Addons](#existing-addons)
  - [Creating Addon](#creating-addon)
- **[Reference](reference.md#navigation)**

<br />

# Addons
Addons extend Lua scripting capabilities by adding custom variables and functions.

<br />

### Usage Example
Addons are distributed as `.jar` files and should be placed inside your server's `plugins` directory.
```lua
-- Getting addon instance.
local MiniPlaceholders = addons.get("MiniPlaceholders")

-- Retrieving global placeholders,
local globalPlaceholders = MiniPlaceholders.getGlobalPlaceHolders()
```
In this example, we are getting instance of `MiniPlaceholders` addon provided by [LuaLink-MiniPlaceholders](https://github.com/LuaLink/LuaLink-MiniPlaceholders), and using it to retrieve global placeholders. Of course, different addons serve different purposes, some may expose plugins' API, while others may add utility functions.

<br />


### Existing Addons
List of both official and community-made addons. Please refer to their respective documentation to learn how to interact with them.
- [LuaLink-MiniPlaceholders](https://github.com/LuaLink/LuaLink-MiniPlaceholders) - allows to use [MiniPlaceholders'](https://github.com/MiniPlaceholders/MiniPlaceholders) placeholders within LuaLink scripts.
- [LuaLink-Vault](https://github.com/LuaLink/LuaLink-Vault) - allows to interact with [Vault](https://github.com/milkbowl/Vault) plugin.

<br />

### Creating Addon
Not documented yet. You can use [this addon template](https://github.com/LuaLink/LuaLink-ExampleAddon) to get started.