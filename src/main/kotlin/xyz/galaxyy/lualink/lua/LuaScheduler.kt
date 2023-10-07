package xyz.galaxyy.lualink.lua

import com.github.only52607.luakt.CoerceKotlinToLua
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.VarArgFunction
import xyz.galaxyy.lualink.LuaLink

class LuaScheduler(private val plugin: LuaLink, private val script: LuaScript) : LuaTable() {
    init {

        // schedules single task to be executed on the next tick
        this.set("run", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                // validating function call
                if (args == null || args.narg() != 1) {
                    throw IllegalArgumentException("run expects 1 argument: callback")
                }
                // parsing function arguments
                val callback = args.arg(1).checkfunction()
                // scheduling new task
                val task = object : BukkitRunnable() {
                    override fun run() {
                        callback.call()
                    }
                }.runTask(plugin)
                // adding task id to the list
                script.tasks.add(task.taskId)
                // returning nil
                return LuaValue.NIL
            }
        })

        // schedules single (asynchronous) task to be executed on the next tick
        this.set("runAsync", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                // validating function call
                if (args == null || args.narg() != 1) {
                    throw IllegalArgumentException("runAsync expects 1 argument: callback")
                }
                // parsing function arguments
                val callback = args.arg(1).checkfunction()
                // scheduling new task
                val task = object : BukkitRunnable() {
                    override fun run() {
                        callback.call()
                    }
                }.runTaskAsynchronously(plugin)
                // adding task id to the list
                script.tasks.add(task.taskId)
                // returning nil
                return LuaValue.NIL
            }
        })

        // schedules single task to be executed after n ticks has passed
        this.set("runDelayed", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                // validating function call
                if (args == null || args.narg() != 2)
                    throw IllegalArgumentException("runDelayed expects 2 arguments: callback, delay")
                // parsing function arguments
                val callback = args.arg(1).checkfunction()
                val delay = args.arg(2).checklong()
                // scheduling new task
                val task = if (callback.narg() == 1)
                               // scheduling new task with callback returning this task instance
                               object : BukkitRunnable() {
                                   override fun run() { callback.call(CoerceKotlinToLua.coerce(this)) }
                               }.runTaskLater(plugin, delay)
                           else
                               // scheduling new task with no-arg callback
                               object : BukkitRunnable() {
                                   override fun run() { callback.call() }
                               }.runTaskLater(plugin, delay)
                // adding task id to the list
                script.tasks.add(task.taskId)
                // returning task
                return CoerceKotlinToLua.coerce(task)
            }
        })

        // schedules single (asynchronous) task to be executed after n ticks has passed
        this.set("runDelayedAsync", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                // validating function call
                if (args == null || args.narg() != 2)
                    throw IllegalArgumentException("runDelayedAsync expects 2 arguments: callback, delay")
                // parsing function arguments
                val callback = args.arg(1).checkfunction()
                val delay = args.arg(2).checklong()
                // scheduling new task
                val task = if (callback.narg() == 1)
                               // scheduling new task with callback returning this task instance
                               object : BukkitRunnable() {
                                   override fun run() { callback.call(CoerceKotlinToLua.coerce(this)) }
                               }.runTaskLaterAsynchronously(plugin, delay)
                           else
                               // scheduling new task with no-arg callback
                               object : BukkitRunnable() {
                                   override fun run() { callback.call() }
                               }.runTaskLaterAsynchronously(plugin, delay)
                // adding task id to the list
                script.tasks.add(task.taskId)
                // returning task
                return CoerceKotlinToLua.coerce(task)
            }
        })

        // schedules repeating task to be started after n ticks has passed and repeated each m ticks
        this.set("runRepeating", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                // validating function call
                if (args == null || args.narg() != 3)
                    throw IllegalArgumentException("runRepeating expects 3 arguments: callback, delay, period")
                // parsing function arguments
                val callback = args.arg(1).checkfunction()
                val delay = args.arg(2).checklong()
                val period = args.arg(3).checklong()
                // scheduling new task
                val task = if (callback.narg() == 1)
                               // scheduling new task with callback returning this task instance
                               object : BukkitRunnable() {
                                   override fun run() { callback.call(CoerceKotlinToLua.coerce(this)) }
                               }.runTaskTimer(plugin, delay, period)
                           else
                               // scheduling new task with no-arg callback
                               object : BukkitRunnable() {
                                   override fun run() { callback.call() }
                               }.runTaskTimer(plugin, delay, period)
                // adding task id to the list
                script.tasks.add(task.taskId)
                // returning task
                return CoerceKotlinToLua.coerce(task)
            }
        })

        // schedules repeating (asynchronous) task to be started after n ticks has passed and repeated each m ticks
        this.set("runRepeatingAsync", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                // validating function call
                if (args == null || args.narg() != 3)
                    throw IllegalArgumentException("runRepeatingAsync expects 3 arguments: callback, delay, period")
                // parsing function arguments
                val callback = args.arg(1).checkfunction()
                val delay = args.arg(2).checklong()
                val period = args.arg(3).checklong()
                // scheduling new task
                val task = if (callback.narg() == 1)
                               // scheduling new task with callback returning this task instance
                               object : BukkitRunnable() {
                                   override fun run() { callback.call(CoerceKotlinToLua.coerce(this)) }
                               }.runTaskTimerAsynchronously(plugin, delay, period)
                           else
                               // scheduling new task with no-arg callback
                               object : BukkitRunnable() {
                                   override fun run() { callback.call() }
                               }.runTaskTimerAsynchronously(plugin, delay, period)
                // adding task id to the list
                script.tasks.add(task.taskId)
                // returning task
                return CoerceKotlinToLua.coerce(task)
            }
        })

        // cancels bukkit task with specified id
        this.set("cancelTask", object: OneArgFunction() {
            override fun call(arg: LuaValue?): LuaValue {
                // validating function call
                if (arg == null || !arg.isint())
                    throw IllegalArgumentException("cancelTask expects 1 argument: taskId")
                // parsing function arguments
                val taskId = arg.checkint()
                // cancelling the task; should not throw exception when cancelled directly with BukkitTask#cancel
                Bukkit.getScheduler().cancelTask(taskId)
                // removing task from the list
                if (script.tasks.contains(taskId))
                    script.tasks.remove(taskId)
                // returning nil
                return LuaValue.NIL
            }
        })

    }
}