package com.azuriom.azpoints.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class AzPoints extends PlaceholderExpansion {
    public String getAuthor() {
        return "Piston";
    }

    public String getIdentifier() {
        return "azuriom";
    }

    public String getVersion() {
        return "0.0.1";
    }

    public String onRequest(OfflinePlayer player, String identifier) {
        if (identifier.equals("get_money")) {
            double money = com.azuriom.azpoints.cmd.AzPoints.getMoneyPlayer(player.getPlayer());
            return String.valueOf(money);
        }
        return null;
    }
}
