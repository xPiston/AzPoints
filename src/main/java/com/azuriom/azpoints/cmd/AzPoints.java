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
                    if (cible != null && cible != sender) {
                        String pseudo = getPseudoPlayer(cible);
                        if (pseudo != null && pseudo.equalsIgnoreCase(args[1])) {
                            double cible_money = 0;
                            cible_money = getMoneyPlayer(cible);
                            double new_money = cible_money + Integer.parseInt(args[2]);
                            setPlayerCoins(cible, new_money);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.msg-add")).replace("%player%", cible.getDisplayName()).replace("%money%", args[2]));
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.no-register")));
                            return false;
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.no-co")));
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.no-permision")));
                    return false;
                }
            } else if (args[0].equalsIgnoreCase("take")) {
                if (sender.hasPermission("azuriom.azpoints.take")) {
                    Player cible = Bukkit.getPlayer(args[1]);
                    if (cible != null && cible != sender) {
                        String pseudo = getPseudoPlayer(cible);
                        if (pseudo != null && pseudo.equalsIgnoreCase(args[1])) {
                            double cible_money = 0;
                            cible_money = getMoneyPlayer(cible);
                            double new_money = cible_money - Integer.parseInt(args[2]);
                            setPlayerCoins(cible, new_money);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.msg-take")).replace("%player%", cible.getDisplayName()).replace("%money%", args[2]));
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.no-register")));
                            return false;
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.no-co")));
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.no-permision")));
                    return false;
                }
            } else if (args[0].equalsIgnoreCase("set")) {
                if (sender.hasPermission("azuriom.azpoints.set")) {
                    Player cible = Bukkit.getPlayer(args[1]);
                    if (cible != null && cible != sender) {
                        String pseudo = getPseudoPlayer(cible);
                        if (pseudo != null && pseudo.equalsIgnoreCase(args[1])) {
                            setPlayerCoins(cible, Integer.parseInt(args[2]));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.msg-set")).replace("%player%", cible.getDisplayName()).replace("%money%", args[2]));
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.no-register")));
                            return false;
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.no-co")));
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.no-permision")));
                    return false;
                }
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.no-permision")));
            return false;
        }
        return false;
    }

    public static double getMoneyPlayer(Player uuid) {
        double money;
        money=0;
        try {
            PreparedStatement sts = Main.getInstance().mysql.getConnection().prepareStatement("SELECT * FROM users WHERE name = ?");
            sts.setString(1, uuid.getDisplayName());
            ResultSet rs = sts.executeQuery();
            if (rs.next()) {
                return rs.getDouble("money");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return money;
    }
    public static void setPlayerCoins(Player player, double money) {
        try {
            PreparedStatement sts = Main.getInstance().mysql.getConnection().prepareStatement("UPDATE users SET money = ? WHERE name = ?");
            sts.setDouble(1, money);
            sts.setString(2,player.getDisplayName());
            sts.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPseudoPlayer(Player player) {
        try {
            PreparedStatement sts = Main.getInstance().mysql.getConnection().prepareStatement("SELECT * FROM users WHERE name=?");
            sts.setString(1, player.getDisplayName());
            ResultSet rs = sts.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
