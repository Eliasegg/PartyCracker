package com.eliaseeg.partycracker.objects;

import com.eliaseeg.partycracker.PartyCrackerPlugin;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class PartyCrackersManager {

    private static volatile PartyCrackersManager instance;
    @Getter private final List<PartyCracker> crackers = new ArrayList<>();

    private PartyCrackersManager() {}

    public static PartyCrackersManager getInstance() {
        PartyCrackersManager result = instance;

        // DCL to avoid race conditions between multiple threads.
        if (result != null) return result;
        synchronized(PartyCrackersManager.class) {
            if (instance == null) {
                instance = new PartyCrackersManager();
            }
            return instance;
        }
    }

    public void updateCrackersList() {
        FileConfiguration config = PartyCrackerPlugin.getInstance().getConfig();
        for (String crackerType : PartyCrackerPlugin.getInstance().getConfig().getConfigurationSection("crackers").getKeys(false)) {
            PartyCracker partyCracker = PartyCracker.builder()
                    .crackerType(crackerType)
                    .material(Material.valueOf(config.getString("crackers." + crackerType + ".item.material")))
                    .name(config.getString("crackers." + crackerType + ".item.name"))
                    .lore(config.getStringList("crackers." + crackerType + ".item.lore"))
                    .enchantGlow(config.getBoolean("crackers." + crackerType + ".item.glow"))
                    .explosionTime(config.getInt("crackers." + crackerType + ".explosion_time"))
                    .disappearAfter(config.getInt("crackers." + crackerType + ".disappear_after"))
                    .particles(config.getStringList("crackers." + crackerType + ".particle_list"))
                    .sounds(config.getStringList("crackers." + crackerType + ".sound_list")).build();

            // Add probability to each reward.
            for (String key : config.getConfigurationSection("crackers." + crackerType +".rewards").getKeys(false)) {
                partyCracker.getRewardKeys().add(config.getInt("crackers." + crackerType +".rewards." + key + ".chance"), key);
            }

            this.crackers.add(partyCracker);
        }
    }

    public PartyCracker getPartyCracker(String name) {
        return this.crackers.stream().filter(partyCracker -> partyCracker.getCrackerType().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
