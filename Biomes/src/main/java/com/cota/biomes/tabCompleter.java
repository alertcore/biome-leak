package com.cota.biomes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class tabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings.length==1) {

            return List.of("change");
        }
        if (strings.length==2) {
            ArrayList<String> types = new ArrayList<>();

            types.add("plains");
            types.add("forest");
            types.add("savanna");
            types.add("cherry");
            types.add("jungle");
            types.add("slopes");
            types.add("ocean");
            types.add("taiga");
            types.add("spikes");
            types.add("dark");
            types.add("desert");
            types.add("swamp");
            types.add("badlands");
            types.add("mushroom");
            return types;
        }


        return null;
    }
}
