package com.eliaseeg.partycracker.util;

import com.eliaseeg.partycracker.PartyCrackerPlugin;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DataUtils {

    /**
     * Sets persistent data that lasts through restarts to an item.
     * @param item - Item this data is being applied to.
     * @param key - Key that will be used to retrieve said data.
     * @param value - The actual data value that we will be applying to the item.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void setPersistentData(ItemStack item, String key, String value) {
        if (item != null && item.getItemMeta() != null) {
            ItemMeta meta = item.getItemMeta();
            NamespacedKey namedKey = new NamespacedKey(PartyCrackerPlugin.getInstance(), key);
            meta.getPersistentDataContainer().set(namedKey, PersistentDataType.STRING, value);
            item.setItemMeta(meta);
        }
    }

    /**
     * Checks if an item has any String data values for a specific key.
     * @param item - ItemStack we are checking.
     * @param key - The key we are checking.
     * @returns true if there was any value associated with the given key.
     */
    public static boolean hasData(ItemStack item, String key) {
        NamespacedKey namedKey = new NamespacedKey(PartyCrackerPlugin.getInstance(), key);
        if (item != null && item.getItemMeta() != null) {
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            if (container.has(namedKey , PersistentDataType.STRING)) return true;
        }
        return false;
    }

    /**
     * Retrieves the data for a given key. Use DataUtils#hasData first to ensure there's an actual value tied to this key.
     * @param item - The ItemStack we are checking.
     * @param key - The key we are checking.
     * @returns the value of the key tied to the item.
     */
    public static String getData(ItemStack item, String key) {
        String value = null;
        NamespacedKey namedKey = new NamespacedKey(PartyCrackerPlugin.getInstance(), key);
        ItemMeta meta = item.getItemMeta(); // No checks here because we are already doing it in DataUtils#hasPersistentData
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(namedKey , PersistentDataType.STRING)) value = container.get(namedKey, PersistentDataType.STRING);
        return value;
    }

}