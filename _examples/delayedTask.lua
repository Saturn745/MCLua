script.registerSimpleCommand(function(sender, args)
    sender:sendRichMessage("<green>Hello, "..sender:getName())
    scheduler.runDelayed(function()
            sender:sendRichMessage("<yellow>This was sent 40 ticks later (2 seconds)! BukkitScheduler works!")
    end, 40)
end, {
    name = "hello",
    description = "Hello world from MCLua"
})