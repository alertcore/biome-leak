package com.cota.biomes.getBiome.Abilities.DeepDark;

import com.cota.biomes.files.playerData;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static com.cota.biomes.Biomes.plugin;
import static com.cota.biomes.getBiome.managers.*;

public class warden implements Listener {



    private long lastShiftPressTime =0;
    @EventHandler
    public void Dash(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        String biome = player.getWorld().getBiome(player.getLocation()).name();
        playerData pd = new playerData(player.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("dark")) {
            if (!biome.toLowerCase().contains("dark")) return;

            // Check if the player is sneaking
            if (event.isSneaking()) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastShiftPressTime <= DOUBLE_PRESS_THRESHOLD) {
                    // Check if the time difference between the current and last shift press is within the threshold
                    if (!isCooldownActive(player, 10 * 60)) {

                        Warden(player);
                        setStarted(player);

                    }
                }

                lastShiftPressTime = currentTime;


            }
        }
    }

    @EventHandler
    public void beam(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        String biome = player.getWorld().getBiome(player.getLocation()).name();

        playerData pd = new playerData(player.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("dark")) {

            // Check if the player is sneaking
            if (player.isSneaking()) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastShiftPressTime <= DOUBLE_PRESS_THRESHOLD) {
                    // Check if the time difference between the current and last shift press is within the threshold
                    if (!isCooldownActiveSecond(player, 60)) {

                        Beam(player);
                        setStartedSecond(player);

                    }else {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText("§cOn cooldown"));
                    }
                }

                lastShiftPressTime = currentTime;


            }
        }
    }

    private void Beam(Player player) {
        Location playerLocation = player.getLocation();
        Vector direction = playerLocation.getDirection().normalize();

        // Adjust the starting point of the beam
        Location beamStart = playerLocation.add(direction);

        // Set the distance the beam should travel
        double maxDistance = 50.0;

        // Iterate along the beam path and spawn particles
        for (double distance = 0; distance < maxDistance; distance += 0.5) {
            Location particleLocation = beamStart.clone().add(direction.clone().multiply(distance));

            player.getWorld().spawnParticle(Particle.SONIC_BOOM, particleLocation, 1);
            for (Entity entity : player.getWorld().getNearbyEntities(particleLocation, 1, 1, 1)) {
                if (entity instanceof LivingEntity && !entity.equals(player)) {
                    ((LivingEntity) entity).damage(45.0); // You can adjust the damage value
                }
            }
            // Spawn particle effect

        }
        setCooldownSecond(player);



    }

    private void Warden(Player player) {




        player.addPotionEffect(PotionEffectType.SLOW.createEffect(15*20, 2));
        player.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(15*20, 1));
        player.setInvisible(true);
        player.setMaxHealth(45);
        player.setHealth(45);

        Warden d = player.getWorld().spawn(player.getLocation(), Warden.class);
        d.setAI(false);
        d.setCollidable(false);
        player.setCollidable(false);
        d.setInvulnerable(true);

        playerData pd = new playerData(player.getUniqueId().toString());
        pd.getPlayerData().set("warden", true);
        pd.save();

        new BukkitRunnable() {


            @Override
            public void run() {

                Location loc = player.getLocation().clone();
                if (player.getFacing().equals(BlockFace.NORTH)) {
                    loc.add(0, 0, 1);
                }
                if (player.getFacing().equals(BlockFace.EAST)) {
                    loc.subtract(1, 0, 0);
                }

                if (player.getFacing().equals(BlockFace.WEST)) {
                    loc.add(1, 0, 0);
                }
                if (player.getFacing().equals(BlockFace.SOUTH)) {
                    loc.subtract(0, 0, 1);
                }

                if (player.getFacing().equals(BlockFace.NORTH_EAST)) {
                    loc.add(0, 0, 1);
                    loc.subtract(1, 0, 0);
                }
                if (player.getFacing().equals(BlockFace.NORTH_WEST)) {
                    loc.add(1, 0, 1);
                }

                if (player.getFacing().equals(BlockFace.SOUTH_WEST)) {
                    loc.add(1, 0, 0);
                    loc.subtract(0, 0, 1);
                }
                if (player.getFacing().equals(BlockFace.SOUTH_EAST)) {
                    loc.subtract(1, 0, 1);
                }

                if (isCooldownActive(player,30 )) {
                    d.teleport(loc);
                }else {
                    player.setInvisible(false);
                    pd.getPlayerData().set("warden", false);
                    pd.save();
                    d.remove();
                    player.setMaxHealth(20);
                    player.setHealth(20);
                }
            }

        }.runTaskTimer(plugin(), 0, 1);
        setCooldown(player);





    }


}
