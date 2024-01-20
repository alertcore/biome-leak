package com.cota.biomes.getBiome.Abilities.CherryGrove;

import com.cota.biomes.files.playerData;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

import static com.cota.biomes.getBiome.managers.*;


public class poisonFlower implements Listener {


    private long lastShiftPressTime =0;

    @EventHandler
    public void Dash(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        String biome = player.getWorld().getBiome(player.getLocation()).name();


        playerData pd = new playerData(player.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("cherry")) {
            // Check if the player is sneaking
            if (event.isSneaking()) {

                long currentTime = System.currentTimeMillis();

                // Check if the time difference between the current and last shift press is within the threshold

                if (currentTime - lastShiftPressTime <= DOUBLE_PRESS_THRESHOLD) {

                    // Check if the time difference between the current and last shift press is within the threshold
                    if (!isCooldownActive(player, 3*60)) {
                        if (player.isOnGround()) {
                            activateFlowerAbility(player);
                            setStarted(player);
                            Regeneration(player);
                        }
                    }else {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText("§cOn cooldown"));
                    }
                }
                lastShiftPressTime = currentTime;


            }
        }
    }

    private void Regeneration(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 3*20, 1));
    }

    private void activateFlowerAbility(Player player) {
        // Get player's location
        Location playerLocation = player.getLocation();

        int m = (int) Math.round(getMultiplier(player));
        int numFlowers = 10*  m ; // Number of flowers to spawn
        double radius = 3.0*getMultiplier(player); // Radius of the circle

        // Spawn flowers in a 5x5 area around the player
        for (int i = 0; i < numFlowers; i++) {
            double angle = 2 * Math.PI * i / numFlowers;
            double x = playerLocation.getX() + radius * Math.cos(angle);
            double z = playerLocation.getZ() + radius * Math.sin(angle);
            Location flowerLocation = new Location(playerLocation.getWorld(), x, playerLocation.getY(), z);

            // Find the ground by decreasing the Y-coordinate until a solid block is found
            while (flowerLocation.getY() > 0) {
                Block blockBelow = flowerLocation.getBlock().getRelative(0, -1, 0);
                if (blockBelow.getType() == Material.AIR) {
                    flowerLocation.subtract(0, 1, 0);
                } else {
                    break;
                }
            }

            // Check if there is a block at the flower location before spawning


            // Emit a poisonous projectile (Arrow with Poison effect) from each flower to a random direction
            if (flowerLocation.getBlock().getType() == Material.AIR) {
                // Spawn the flower
                playerLocation.getWorld().getBlockAt(flowerLocation).setType(Material.RED_TULIP);

                // Find the nearest entity around the flower
                Entity nearestEntity = findNearestEntity(flowerLocation, 10.0); // Adjust the range as needed

                if (nearestEntity != null) {
                    // Emit a poisonous projectile (Arrow with Poison effect) towards the nearest entity
                    Arrow arrow = flowerLocation.getWorld().spawn(flowerLocation, Arrow.class);
                    arrow.setVelocity(nearestEntity.getLocation().toVector().subtract(arrow.getLocation().toVector()).normalize().multiply(2));
                    arrow.addCustomEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.POISON, 100, 1), true);
                }
            }

        }
        setCooldown(player);

    }

    private Entity findNearestEntity(Location location, double range) {
        List<Entity> nearbyEntities = location.getWorld().getEntities();
        Entity nearestEntity = null;
        double nearestDistanceSquared = Double.MAX_VALUE;

        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player || entity instanceof Arrow) {
                continue; // Exclude players and other arrows from consideration
            }

            double distanceSquared = entity.getLocation().distanceSquared(location);
            if (distanceSquared < nearestDistanceSquared && distanceSquared <= range * range) {
                nearestEntity = entity;
                nearestDistanceSquared = distanceSquared;
            }
        }

        return nearestEntity;
    }

        // Emit a poisonous projectile (Arrow with Poison effect)



    private void setRandomDirection(Arrow arrow) {
        // Generate a random pitch and yaw for the arrow's direction
        Random random = new Random();
        float pitch = random.nextFloat() * 360;
        float yaw = random.nextFloat() * 360;

        // Convert pitch and yaw to radians
        double pitchRadians = Math.toRadians(pitch);
        double yawRadians = Math.toRadians(yaw);

        // Calculate the direction vector
        double x = Math.sin(pitchRadians) * Math.cos(yawRadians);
        double y = Math.sin(pitchRadians) * Math.sin(yawRadians);
        double z = Math.cos(pitchRadians);

        arrow.setVelocity(new Vector(x, y, z));
    }


}
