package com.azuriom.azpoints;

import com.azuriom.azpoints.cmd.AzPoints;
import com.azuriom.azpoints.utils.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    public MySQL mysql = new MySQL();
    private static Main INSTANCE;

    @Override
    public void onEnable() {
        super.onEnable();
        INSTANCE = this;
        getCommand("azpoints").setExecutor(new AzPoints());
        mysql.connect(this.getConfig().getString("Database.HOST"), this.getConfig().getString("Database.PORT"), this.getConfig().getString("Database.DB") + "?autoReconnect=true&useSSL=false", this.getConfig().getString("Database.USER"), this.getConfig().getString("Database.PW"));
        if(mysql.isConnected()){
            Bukkit.getConsoleSender().sendMessage("§7Base de données: §aTrouver !");
        } else {
            Bukkit.getConsoleSender().sendMessage("§7Base de données: §cIntrouvable !");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new com.azuriom.azpoints.placeholder.AzPoints().register();
        }
        createConfig();
    }

    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Config.yml not found, creating!");
                saveDefaultConfig();
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mysql.disconnect();
    }

    public static Main getInstance() {
        return INSTANCE;
    }

}
