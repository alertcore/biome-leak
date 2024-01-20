package com.cota.biomes.getBiome.Abilities.Desert;

import com.cota.biomes.files.playerData;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

import static com.cota.biomes.Biomes.plugin;
import static com.cota.biomes.getBiome.managers.*;

public class sandTornado implements Listener {


    private long lastShiftPressTime =0;
    @EventHandler
    public void Dash(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        String biome = player.getWorld().getBiome(player.getLocation()).name();
        playerData pd = new playerData(player.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("desert")) {

            // Check if the player is sneaking
            if (event.isSneaking()) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastShiftPressTime <= DOUBLE_PRESS_THRESHOLD) {

                    if (!isCooldownActive(player, 40)) {
                        if (player.isOnGround()) {
                            spawnSandTornado(player);
                            setStarted(player);
                        }
                    }else {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText("§cOn cooldown"));
                    }
                }

                lastShiftPressTime = currentTime;


            }
        }
    }

    private int counter=0;



    private void spawnSandTornado(Player player) {
        double radius = 2.0;
        int height = 10;
        int particlesPerLayer = 50;


        Location ploc = player.getLocation();

        player.getLocation().getWorld().spawnParticle(Particle.FALLING_DUST, player.getLocation(), 1, 0, 0, 0, 0, Material.SAND.createBlockData());

        new BukkitRunnable() {

            int i=0;
            @Override
            public void run() {




                if (i>=15*4) {
                    cancel();
                }

                Entity nearestEntity = findNearestEntity(ploc, 10.0, player); // Adjust the range as needed

                if (nearestEntity != null) {
                    Location centerLocation = nearestEntity.getLocation().clone();
                    Vector direction = player.getLocation().subtract(centerLocation).toVector().normalize();
                    centerLocation.add(direction.multiply(0.1));





                    for (int y = 0; y < height; y++) {
                        double layerRadius = radius * (y / (double) height);
                        double angleIncrement = 2 * Math.PI / particlesPerLayer;

                        for (int i = 0; i < particlesPerLayer; i++) {
                            double angle = i * angleIncrement;
                            double xOffset = layerRadius * Math.cos(angle);
                            double zOffset = layerRadius * Math.sin(angle);

                            // Adjust particle location based on the direction
                            Location particleLocation = centerLocation.clone().add(xOffset, y, zOffset);

                            // Spawn sand particle effect
                            particleLocation.getWorld().spawnParticle(Particle.FALLING_DUST, particleLocation, 1, 0, 0, 0, 0, Material.SAND.createBlockData());

                            ploc.set(centerLocation.getBlockX(), centerLocation.getY(), centerLocation.getZ());


                            for (LivingEntity le: centerLocation.getNearbyLivingEntities(0.8)) {
                                Player target = (Player) le;
                                target.damage(0.50*getMultiplier(player));
                            }

                        }
                    }
                }else {
                    for (int y = 0; y < height; y++) {
                        double layerRadius = radius * (y / (double) height);
                        double angleIncrement = 2 * Math.PI / particlesPerLayer;

                        for (int i = 0; i < particlesPerLayer; i++) {
                            double angle = i * angleIncrement;
                            double xOffset = layerRadius * Math.cos(angle);
                            double zOffset = layerRadius * Math.sin(angle);

                            Location centerLocation = ploc;
                            // Adjust particle location based on the direction
                            Location particleLocation = centerLocation.clone().add(xOffset, y, zOffset);

                            // Spawn sand particle effect
                            particleLocation.getWorld().spawnParticle(Particle.FALLING_DUST, particleLocation, 1, 0, 0, 0, 0, Material.SAND.createBlockData());

                            ploc.set(centerLocation.getBlockX(), centerLocation.getY(), centerLocation.getZ());
                        }
                    }
                }
                i++;
            }
        }.runTaskTimer(plugin(), 0, 5);

        setCooldown(player);

    }

    private Entity findNearestEntity(Location location, double range, Player player) {
        List<Entity> nearbyEntities = location.getWorld().getEntities();

        nearbyEntities.remove(player);
        Entity nearestEntity = null;


        double nearestDistanceSquared = Double.MAX_VALUE;

        for (Entity entity : nearbyEntities) {
            if (!(entity instanceof Player )& entity instanceof Arrow) {
                continue; // Exclude players and other arrows from consideration
            }

            double distanceSquared = entity.getLocation().distanceSquared(location);
            if (distanceSquared < nearestDistanceSquared && distanceSquared <= range * range) {

                nearestEntity = entity;
                nearestDistanceSquared = distanceSquared;
            }
        }

        if (!(nearestEntity instanceof Player p)) {



            return null;
        }

        return nearestEntity;
    }

}
