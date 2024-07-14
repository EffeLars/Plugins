/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package me.rok.main.listener;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerPaperData {
    private static FileConfiguration dataConfig;
    private static File dataFile;

    public static void setup() {
        dataFile = new File("plugins/Messaging/PlayerPaperData.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration((File)dataFile);
    }

    public static int getPlayerMessageAmount(UUID playerUUID) {
        return dataConfig.getInt(playerUUID.toString(), 0);
    }

    public static void setPlayerMessageAmount(UUID playerUUID, int amount) {
        dataConfig.set(playerUUID.toString(), (Object)amount);
        try {
            dataConfig.save(dataFile);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}

