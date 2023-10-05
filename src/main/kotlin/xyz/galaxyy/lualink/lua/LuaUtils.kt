package xyz.galaxyy.lualink.lua

import org.bukkit.Bukkit
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.VarArgFunction

class LuaUtils(private val script: LuaScript) : LuaTable() {
    init {

        this.set("instanceOf", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                if (args == null || args.narg() != 2) {
                    throw IllegalArgumentException("instanceOf expects 2 arguments: object, class")
                }

                val obj = args.arg(1).checkuserdata()
                val clazzName = args.checkjstring(2)

                // Get the class of the object
                val objClass = obj.javaClass

                // Get the class of the specified class name
                val specifiedClass = try {
                    Class.forName(clazzName)
                } catch (e: ClassNotFoundException) {
                    throw IllegalArgumentException("Class not found: $clazzName")
                }

                // Check if the object's class is assignable from the specified class
                return LuaValue.valueOf(specifiedClass.isAssignableFrom(objClass))
            }
        })

        this.set("cancelTask", object: OneArgFunction() {
            override fun call(arg: LuaValue?): LuaValue {
                if (arg == null || !arg.isint()) {
                    throw IllegalArgumentException("cancelTask expects 1 argument: taskId")
                }

                val taskId = arg.checkint()

                Bukkit.getScheduler().cancelTask(taskId)

                if (script.tasks.contains(taskId)) {
                    script.tasks.remove(taskId)
                }

                return LuaValue.NIL
            }
        })

    }
}