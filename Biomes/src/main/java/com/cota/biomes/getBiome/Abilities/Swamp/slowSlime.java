package com.cota.biomes.getBiome.Abilities.Swamp;

import com.cota.biomes.files.playerData;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

import static com.cota.biomes.Biomes.plugin;
import static com.cota.biomes.getBiome.managers.*;


public class slowSlime implements Listener {


    private long lastShiftPressTime =0;

    @EventHandler
    public void Dash(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        String biome = player.getWorld().getBiome(player.getLocation()).name();

        if (!biome.equalsIgnoreCase("swamp")) return;

        // Check if the player is sneaking
        if (event.isSneaking()) {

            long currentTime = System.currentTimeMillis();

            // Check if the time difference between the current and last shift press is within the threshold

            if (currentTime - lastShiftPressTime <= DOUBLE_PRESS_THRESHOLD) {

                // Check if the time difference between the current and last shift press is within the threshold
                if (!isCooldownActive(player, 60)) {
                    if (player.isOnGround()) {

                        summonSlime(player);
                        setStarted(player);


                    }
                }
            }
            lastShiftPressTime = currentTime;


        }
    }
    @EventHandler
    public void hit(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        String biome = player.getWorld().getBiome(player.getLocation()).name();
        playerData pd = new playerData(player.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("swamp")) {

            // Check if the player is sneaking
            if (player.isSneaking()) {


                // Check if the time difference between the current and last shift press is within the threshold


                // Check if the time difference between the current and last shift press is within the threshold
                if (!isCooldownActiveSecond(player, 30)) {
                    if (player.isOnGround()) {

                        activateSlowAbility(player);
                        setStartedSecond(player);


                    }
                }else {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText("§cOn cooldown"));
                }


            }
        }
    }


    private void summonSlime(Player p) {
        Slime slime = p.getWorld().spawn(p.getLocation(), Slime.class);
        slime.setSize(1);

        setStarted(p);
    }

    private void activateSlowAbility(Player player) {
        // Get player's location
        Entity nearestEntity = findNearestEntity(player.getLocation(), 10.0); // Adjust the range as needed

        player.spawnParticle(Particle.SLIME, player.getLocation(), 100, 1, 1, 1);

        for (Entity le: player.getLocation().getNearbyLivingEntities(4)) {
            if (le instanceof Player target) {

                target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20, 1));

                Location loc = target.getLocation();


                new BukkitRunnable() {
                    int i=0;
                    @Override
                    public void run() {

                        if (i>=15*20*((int) Math.round(getMultiplier(player)))) {
                            cancel();
                        }

                        if (!target.equals(player)) {

                            target.teleport(loc);



                        }

                        i++;

                    }

                }.runTaskTimer(plugin(), 0, 1);
            }
        }
        setCooldownSecond(player);

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






}
