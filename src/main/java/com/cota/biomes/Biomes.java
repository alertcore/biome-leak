package com.cota.biomes;

import com.cota.biomes.biomeRandomizer.onJoin;
import com.cota.biomes.getBiome.Abilities.Badlands.goldenApple;
import com.cota.biomes.getBiome.Abilities.DeepDark.warden;
import com.cota.biomes.getBiome.Abilities.Desert.sandTornado;
import com.cota.biomes.getBiome.Abilities.Mushroom.strength;
import com.cota.biomes.getBiome.Abilities.Ocean.shark;
import com.cota.biomes.getBiome.Abilities.Savanna.repairArmor;
import com.cota.biomes.getBiome.Abilities.CherryGrove.poisonFlower;
import com.cota.biomes.getBiome.Abilities.Forest.chopTree;
import com.cota.biomes.getBiome.Abilities.IceSpikes.iceSpikes;
import com.cota.biomes.getBiome.Abilities.Jungle.forceField;
import com.cota.biomes.getBiome.Abilities.Plains.dash;
import com.cota.biomes.getBiome.Abilities.SnowySlopes.powderedSnow;
import com.cota.biomes.getBiome.Abilities.Swamp.slowSlime;
import com.cota.biomes.getBiome.Abilities.Taiga.reArrange;
import com.cota.biomes.getBiome.Abilities.Taiga.steal;
import com.cota.biomes.getBiome.Listeners;
import com.cota.biomes.getBiome.checkBiome;
import org.bukkit.plugin.java.JavaPlugin;

public final class Biomes extends JavaPlugin {


    private static Biomes p;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new checkBiome(), this);
        getServer().getPluginManager().registerEvents(new dash(), this);
        getServer().getPluginManager().registerEvents(new repairArmor(), this);
        getServer().getPluginManager().registerEvents(new chopTree(), this);
        getServer().getPluginManager().registerEvents(new poisonFlower(), this);
        getServer().getPluginManager().registerEvents(new forceField(), this);
        getServer().getPluginManager().registerEvents(new shark(), this);
        getServer().getPluginManager().registerEvents(new steal(), this);
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        getServer().getPluginManager().registerEvents(new reArrange(), this);
        getServer().getPluginManager().registerEvents(new warden(), this);
        getServer().getPluginManager().registerEvents(new sandTornado(), this);
        getServer().getPluginManager().registerEvents(new slowSlime(), this);
        getServer().getPluginManager().registerEvents(new goldenApple(), this);
        getServer().getPluginManager().registerEvents(new strength(), this);
        getServer().getPluginManager().registerEvents(new powderedSnow(), this);
        getServer().getPluginManager().registerEvents(new onJoin(), this);
        getServer().getPluginManager().registerEvents(new iceSpikes(), this);
        getCommand("biomes").setExecutor(new Command());
        getCommand("biomes").setTabCompleter(new tabCompleter());


        this.p = this;

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Biomes plugin() {
        return p;
    }
}
