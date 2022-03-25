package com.eliaseeg.partycracker.scheduler;

import com.eliaseeg.partycracker.PartyCrackerPlugin;
import com.eliaseeg.partycracker.objects.PartyCracker;
import com.eliaseeg.partycracker.util.HexUtils;
import com.eliaseeg.partycracker.util.particles.ParticleDisplay;

import com.eliaseeg.partycracker.util.particles.XParticle;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.apache.commons.lang.text.StrSubstitutor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PartyCrackerScheduler implements Runnable {

    private final Pattern amountPattern = Pattern.compile("(\\$\\{random_\\d{1,3}:\\d{1,3}\\})");   // Example: {random_20:10}

    private final PartyCracker partyCracker;
    private final Player player;
    private final Item item;
    private final Random random = new Random();

    public PartyCrackerScheduler(PartyCracker partyCracker, Player player, Item item) {
        this.partyCracker = partyCracker;
        this.player = player;
        this.item = item;
    }

    @Override
    public void run() {
        FileConfiguration config = PartyCrackerPlugin.getInstance().getConfig();
        String rewardKey = this.partyCracker.nextReward();
        String rewardPath = "crackers." + partyCracker.getCrackerType() + ".rewards." + rewardKey;

        Map<String, String> data = new HashMap<>();
        data.put("player", player.getName());

        // Replace {random_20:10} and {player}
        for (String command : config.getStringList(rewardPath + ".commands")) {
            String commandFormatted = StrSubstitutor.replace(command, data); // Replace {player}

            Matcher matcher = amountPattern.matcher(commandFormatted);

            if (matcher.find()) {
                String[] numbers = matcher.group(1).replace("${random_", "").replace("}", "").split(":");
                commandFormatted = commandFormatted.replaceAll("(\\$\\{random_\\d{1,3}:\\d{1,3}\\})", String.valueOf(this.random.nextInt(Integer.parseInt(numbers[0]) - Integer.parseInt(numbers[1])) + Integer.parseInt(numbers[1])));
            }

            Bukkit.dispatchCommand(PartyCrackerPlugin.getInstance().getServer().getConsoleSender(), commandFormatted);
        }

        if (!(config.getStringList(rewardPath + ".chat_messages").isEmpty()) && (config.getStringList(rewardPath + ".chat_messages").get(0).length() > 1)) {
            config.getStringList(rewardPath + ".chat_messages").forEach(message -> Bukkit.broadcastMessage(HexUtils.applyColor(StrSubstitutor.replace(message, data))));
        }

        // Add placeholder effect and remove it as well.
        if (config.getBoolean(rewardPath + ".after_placeholder.enabled")) {
            if (PartyCrackerPlugin.getInstance().isHoloPluginEnabled()) {

                Hologram hologram = DHAPI.createHologram(UUID.randomUUID().toString(), this.item.getLocation().add(0, config.getStringList("crackers." + partyCracker.getCrackerType() + ".rewards." + rewardKey + ".after_placeholder.lines").size() * 0.7, 0), false,  HexUtils.applyColor(config.getStringList(rewardPath + ".after_placeholder.lines")));
                DHAPI.addHologramLine(hologram, "#ICON: " + config.getString(rewardPath + ".after_placeholder.material"));

                Bukkit.getServer().getScheduler().runTaskLater(PartyCrackerPlugin.getInstance(), hologram::delete, (20 * partyCracker.getDisappearAfter()));
            }
        }

        // Spawn a random explode animation
        this.spawnAnimation(this.item.getLocation(), config);

        // Remove item from the ground and play sound.
        this.item.getLocation().getWorld().getNearbyEntities(this.item.getLocation(), 7, 7, 7).stream().filter(entityStream -> entityStream instanceof Item).map(Item.class::cast).filter(item -> item.getLocation().equals(this.item.getLocation())).forEach(Item::remove);
        this.item.getLocation().getWorld().playSound(this.item.getLocation(), this.partyCracker.getSounds().get(new Random().nextInt(this.partyCracker.getSounds().size())), 1f, 1f);
    }

    // Handles animation for the PartyCracker.
    private void spawnAnimation(Location location, FileConfiguration config) {
        Particle particle = Particle.valueOf(config.getStringList("crackers." + this.partyCracker.getCrackerType() + ".particle_list").get(this.random.nextInt(config.getStringList("crackers." + this.partyCracker.getCrackerType() + ".particle_list").size())));
        String animation = config.getStringList("crackers." + this.partyCracker.getCrackerType() + ".animation_list").get(this.random.nextInt(config.getStringList("crackers." + this.partyCracker.getCrackerType() + ".animation_list").size()));

        ParticleDisplay display = ParticleDisplay.display(location, particle);

        switch (animation) {
            case "BLACK_SUN" -> XParticle.blackSun(3, .5, 5, 1, display);
            case "DIAMOND" -> XParticle.diamond(0.1, 0.3, 2, display);
            case "FLOWER" -> XParticle.flower(5, 2, display, () -> XParticle.magicCircles(PartyCrackerPlugin.getInstance(), 0.1, 0.3, 0.5, 0.4, display));
            case "RINGS" -> XParticle.infinity(3, 5, display);
            case "MEGUMIN_EXPLOSION" -> XParticle.meguminExplosion(6, display);
            default -> XParticle.infinity(3, 5, display);
        }
    }

}
