package com.eliaseeg.partycracker.objects;

import com.eliaseeg.partycracker.util.DataUtils;
import com.eliaseeg.partycracker.util.ItemBuilder;
import com.eliaseeg.partycracker.util.WeightedCollection;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;


@Builder
public class PartyCracker {

    @Getter private final String crackerType; // Getter to search all party crackers later down the line.

    // ItemStack related values
    private final Material material;
    private final String name;
    private final List<String> lore;
    private final boolean enchantGlow;

    // Explosion related values
    @Getter private final WeightedCollection<String> rewardKeys = new WeightedCollection<String>();
    @Getter private final List<String> particles;
    @Getter private final List<String> sounds;
    @Getter private final int explosionTime;
    @Getter private final int disappearAfter;

    public ItemStack getCrackerItem(int amount) {
        ItemStack crackerItem = ItemBuilder.builder().material(this.material).amount(amount)
                .name(this.name)
                .lore(this.lore)
                .glow(this.enchantGlow).build().getItem();
        DataUtils.setPersistentData(crackerItem, "cracker_type", this.crackerType);
        return crackerItem;
    }

    // Gets the configuration key associated with a reward based on a weight percentage.
    public String nextReward() {
        return this.rewardKeys.next();
    }

}
