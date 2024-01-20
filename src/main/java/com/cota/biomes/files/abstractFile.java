package com.cota.biomes.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;


import static com.cota.biomes.Biomes.plugin;

public class abstractFile {



    private File file;
    protected FileConfiguration config;





    public abstractFile(String fileName) {
        this.file = new File(plugin().getDataFolder(),  "playerdata/" + fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);

    }
    public  void save() {
        try {
            config.save(file);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        config = null;
        file.delete();
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public boolean exists() {

        return file.exists();
    }

    public String getName() {
        return file.getName();
    }

}
