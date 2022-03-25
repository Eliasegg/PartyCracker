package com.eliaseeg.partycracker;

import com.eliaseeg.partycracker.objects.PartyCrackersManager;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PartyCrackerPlugin extends JavaPlugin {

    @Getter private static PartyCrackerPlugin instance;
    @Getter private boolean holoPluginEnabled;

    @Override
    public void onEnable() {
        instance = this;

        this.getServer().getPluginManager().registerEvents(new PartyCrackerListener(), this);
        this.getCommand("partycracker").setExecutor(new PartyCrackerCommand());

        holoPluginEnabled = Bukkit.getPluginManager().isPluginEnabled("DecentHolograms");

        saveDefaultConfig(); // Save default config first before updating the party crackers list.
        PartyCrackersManager.getInstance().updateCrackersList();
    }

}