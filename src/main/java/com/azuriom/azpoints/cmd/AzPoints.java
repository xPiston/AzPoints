package com.azuriom.azpoints.cmd;

import com.azuriom.azpoints.Main;
import com.azuriom.azpoints.utils.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AzPoints implements CommandExecutor {
    private static MySQL mysql = new MySQL();
    private static Main plugin = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("azuriom.azpoints")) {
            if (args.length == 0) {
                sender.sendMessage("/AzPoints add pseudo 20");
                sender.sendMessage("/AzPoints take pseudo 20");
                sender.sendMessage("/AzPoints set pseudo 20");
                return true;
            } else if (args[0].equalsIgnoreCase("add")) {
                if (sender.hasPermission("azuriom.azpoints.give")) {
                    Player cible = Bukkit.getPlayer(args[1]);
                    String pseudo = getPseudoPlayer(cible);
                    if (pseudo != null && pseudo.equalsIgnoreCase(args[1])) {
                        double cible_money = 0;
                        cible_money = getMoneyPlayer(cible);
                        double new_money = cible_money + Integer.parseInt(args[2]);
                        setPlayerCoins(cible, new_money);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.msg-add")).replace("%player%", cible.getDisplayName()).replace("%money%",args[2]));
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Lang.no-register")));
                        return false;
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Lang.no-permision")));
                    return false;
                }
            } else if (args[0].equalsIgnoreCase("take")) {
                if (sender.hasPermission("azuriom.azpoints.take")) {
                    Player cible = Bukkit.getPlayer(args[1]);
                    String pseudo = getPseudoPlayer(cible);
                    if (pseudo != null && pseudo.equalsIgnoreCase(args[1])) {
                        double cible_money = 0;
                        cible_money = getMoneyPlayer(cible);
                        double new_money = cible_money - Integer.parseInt(args[2]);
                        setPlayerCoins(cible, new_money);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.msg-take")).replace("%player%", cible.getDisplayName()).replace("%money%",args[2]));
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Lang.no-register")));
                        return false;
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Lang.no-permision")));
                    return false;
                }
            } else if (args[0].equalsIgnoreCase("set")) {
                if (sender.hasPermission("azuriom.azpoints.set")) {
                    Player cible = Bukkit.getPlayer(args[1]);
                    String pseudo = getPseudoPlayer(cible);
                    if (pseudo != null && pseudo.equalsIgnoreCase(args[1])) {
                        setPlayerCoins(cible, Integer.parseInt(args[2]));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.msg-set")).replace("%player%", cible.getDisplayName()).replace("%money%",args[2]));
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Lang.no-register")));
                        return false;
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Lang.no-permision")));
                    return false;
                }
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Lang.no-permision")));
            return false;
        }
        return false;
    }

    public static double getMoneyPlayer(Player player) {
        double money;
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = mysql.getConnection();
            ps = c.prepareStatement("SELECT money FROM user WHERE name = ?");
            ps.setString(1, player.getDisplayName());
            rs = ps.executeQuery();
            if (!rs.next()) {
                double d = 0.0;
                return d;
            }
            money = rs.getDouble("money");
        } catch (SQLException var9) {
            var9.printStackTrace();
            double d = 0.0;
            return d;
        }
        return money;
    }

    public static void setPlayerCoins(Player player, double money) {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = mysql.getConnection();
            ps = c.prepareStatement("UPDATE user SET money = ? WHERE name = ?");

            ps.setDouble(1, money);
            ps.setString(2, player.getDisplayName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPseudoPlayer(Player player) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = mysql.getConnection();
            ps = c.prepareStatement("SELECT name FROM user WHERE name = ?");

            ps.setString(1, player.getDisplayName());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
