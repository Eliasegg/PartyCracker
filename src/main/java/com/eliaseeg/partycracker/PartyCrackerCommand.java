package com.eliaseeg.partycracker;

import com.eliaseeg.partycracker.objects.PartyCracker;
import com.eliaseeg.partycracker.objects.PartyCrackersManager;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

// partycracker give|reload username type amount
public class PartyCrackerCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1 || args.length > 5) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.GRAY + "/partycracker give|reload [username] [amount] [type]");
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {

            if (!(sender.hasPermission("partyckracker.give"))) {
                sender.sendMessage(ChatColor.RED + "You have no permission to execute this command.");
                return true;
            }

            if (args.length != 4) {
                sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.GRAY + "/partycracker give [username] [amount] [type]");
                return true;
            }

            Player player = Bukkit.getPlayer(args[1]);
            String crackerType = args[2].toLowerCase();
            int amount = Integer.parseInt(args[3]);

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player must be online.");
                return true;
            }

            PartyCracker partyCracker = PartyCrackersManager.getInstance().getPartyCracker(crackerType);
            if (partyCracker == null) {
                sender.sendMessage(ChatColor.RED + "Invalid cracker type. If you've changed the config perhaps try /partycracker reload?");
                return true;
            }

            ItemStack partyCrackerItem = partyCracker.getCrackerItem(amount);
            final Map<Integer, ItemStack> map = player.getInventory().addItem(partyCrackerItem);

            // Account for full inventory
            if (!map.isEmpty()) {
                for (final ItemStack item : map.values()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                }
            }

            sender.sendMessage(ChatColor.YELLOW + "Successfully given: " + ChatColor.GREEN + amount + " " + crackerType + ChatColor.YELLOW + " party crackers to: " + ChatColor.GREEN + player.getName() + "!");
            return true;
        }

        // Handle reload.
        if (args[0].equalsIgnoreCase("reload")) {

            if (!(sender.hasPermission("partyckracker.reload"))) {
                sender.sendMessage(ChatColor.RED + "You have no permission to execute this command.");
                return true;
            }

            if (args.length != 1) {
                sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.GRAY + "/partycracker reload");
                return true;
            }

            PartyCrackersManager.getInstance().updateCrackersList();
            sender.sendMessage(ChatColor.YELLOW + "Successfully reloaded all configuration values.");
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return Arrays.asList("give", "reload");
        if (args.length == 2 && args[0].equalsIgnoreCase("give")) return Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
        if (args.length == 3 && args[0].equalsIgnoreCase("give")) return PartyCrackersManager.getInstance().getCrackers().stream().map(PartyCracker::getCrackerType).toList();
        if (args.length == 4 && args[0].equalsIgnoreCase("give")) return Arrays.asList("1", "5", "10");
        return null;
    }
}
