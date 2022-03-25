package com.eliaseeg.partycracker.util;

import lombok.Builder;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Builder
public class ItemBuilder {

    private final Material material;
    private final String name;
    private final int amount;
    private final boolean glow;
    private final List<String> lore;

    public ItemStack getItem() {
        ItemStack item = new ItemStack(this.material, this.amount > 0 ? this.amount : 1);
        if (this.glow) item.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1); // Add a dummy enchantment as a hacky workaround for glow effects. Set it before item meta is grabbed.

        ItemMeta meta = item.getItemMeta();
        if (this.name != null) meta.setDisplayName(HexUtils.applyColor(this.name));
        if (this.lore != null) meta.setLore(HexUtils.applyColor(this.lore));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Hide enchants workaround for item glow.

        item.setItemMeta(meta);
        return item;
    }

}
