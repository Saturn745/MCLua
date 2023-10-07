# LuaLink Documentation
The project is in an experimental state, so is the documentation - expect things to change quite often.

For new Lua users, [this community-contributed documentation](https://devdocs.io/lua~5.2-language/) may help you to get started.

<br />

### Navigation
- **[Home](home.md#navigation)**
- **External Libraries/API**
  - [Home](#external-libraries)
  - [Configuration Example](#configuration-example)
- **[Addons](addons.md#navigation)**
- **[Reference](reference.md#navigation)**

<br />

## External Libraries
Starting from build [15](https://github.com/LuaLink/LuaLink/commit/65ce0ab517260daa099c6ef4522619c4e081def2), you can now add external Java/Kotlin libraries to your Lua scripts by configuring the `/plugins/LuaLink/libraries.json` file.

<br />

### Configuration Example
```json5
{
    // Repositories to be used for dependency resolution.
    "repositories": {
        "MavenCentral": "https://repo.maven.apache.org/maven2/"
    },
    // Dependencies to be downloaded and exposed to the scripting runtime.
    // Entries must be specified using Maven coordinate format: groupId:artifactId:version
    "dependencies": [
        "com.github.stefvanschie.inventoryframework:IF:0.10.11"
    ]
}
```

In this example, we are adding stefvanschie's [IF](https://github.com/stefvanschie/IF) library of version `0.10.11` from [Maven Central](https://repo.maven.apache.org/maven2/) repository.

Now, after restarting the server, we should be able to import and access any class that belongs to this library.