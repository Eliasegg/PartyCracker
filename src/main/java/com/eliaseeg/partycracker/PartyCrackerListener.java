package com.eliaseeg.partycracker;

import com.eliaseeg.partycracker.objects.PartyCracker;
import com.eliaseeg.partycracker.objects.PartyCrackersManager;
import com.eliaseeg.partycracker.scheduler.PartyCrackerScheduler;
import com.eliaseeg.partycracker.util.DataUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PartyCrackerListener implements Listener {

    // Handles the PartyCracker initializing.
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Item itemDroppped = event.getItemDrop();
        if (!(DataUtils.hasData(itemDroppped.getItemStack(), "cracker_type"))) return;

        // Only be able to throw one at a time.
        if (itemDroppped.getItemStack().getAmount() > 1) {
            itemDroppped.getItemStack().setAmount(itemDroppped.getItemStack().getAmount() - 1);

            // Assigning a new variable as a hacky workaround to ensure we aren't modifying the one in our hand.
            ItemStack newItem = itemDroppped.getItemStack().clone();
            newItem.setAmount(1);

            itemDroppped = event.getItemDrop().getLocation().getWorld().dropItem(event.getItemDrop().getLocation(), newItem); // Set the itemDropped after throwing to ensure we have the updated item.
            event.setCancelled(true);
        }

        PartyCracker partyCracker = PartyCrackersManager.getInstance().getPartyCracker(DataUtils.getData(itemDroppped.getItemStack(), "cracker_type"));
        Bukkit.getServer().getScheduler().runTaskLater(PartyCrackerPlugin.getInstance(), new PartyCrackerScheduler(partyCracker, event.getPlayer(), itemDroppped), (20*partyCracker.getExplosionTime()));
    }

    // Cancel entities from picking up the party cracker.
    @EventHandler
    public void onPickUp(EntityPickupItemEvent event) {
        if (!(DataUtils.hasData(event.getItem().getItemStack(), "cracker_type"))) return;
        event.setCancelled(true);
    }

    // Cancel the ability to use PartyCrackers
    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR || event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!(DataUtils.hasData(event.getItem(), "cracker_type"))) return;
        event.setCancelled(true);
    }

    // Cancel the ability to place PartyCrackers
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!(DataUtils.hasData(event.getItemInHand(), "cracker_type"))) return;
        event.setCancelled(true);
    }
}
