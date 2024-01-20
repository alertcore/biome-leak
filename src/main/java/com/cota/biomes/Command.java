package com.cota.biomes;

import com.cota.biomes.files.playerData;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class Command implements CommandExecutor {



    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player p)) return false;


        if (args.length==2) {
            if (args[0].equals("change")) {
                if (p.hasPermission("biomes.change")) {
                    playerData pd = new playerData(p.getUniqueId().toString());

                    pd.getPlayerData().set("biome", args[1]);
                    pd.save();
                    p.sendMessage("Biome changed to " + args[1]);
                }
            }
        }


        return false;
    }
}
