package com.cota.biomes.files;

import org.bukkit.configuration.file.FileConfiguration;

public class playerData extends abstractFile{


    public playerData(String uuid){
        super(uuid + ".yml");

    }


    public FileConfiguration getPlayerData() {

        return config;
    }
}
