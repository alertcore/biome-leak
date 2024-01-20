package com.cota.biomes.getBiome.Abilities.Ocean;

import com.cota.biomes.files.playerData;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static com.cota.biomes.Biomes.plugin;
import static com.cota.biomes.getBiome.managers.*;

public class shark implements Listener {



    private long lastShiftPressTime =0;
    @EventHandler
    public void Dash(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        String biome = player.getWorld().getBiome(player.getLocation()).name();
        playerData pd = new playerData(player.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("ocean")) {

            // Check if the player is sneaking
            if (event.isSneaking()) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastShiftPressTime <= DOUBLE_PRESS_THRESHOLD) {
                    // Check if the time difference between the current and last shift press is within the threshold
                    if (!isCooldownActive(player, 5 * 60)) {

                        shark(player);
                        setStarted(player);

                    }else {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText("§cOn cooldown"));
                    }
                }

                lastShiftPressTime = currentTime;


            }
        }
    }

    private void shark(Player player) {


        for (Entity e : player.getNearbyEntities(20, 20, 20)) {
            if (e instanceof Player target) {
                target.spawnParticle(Particle.MOB_APPEARANCE, target.getLocation(), 1);
                target.playSound(target, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 10, 1);
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 5*60, 1));
            }
        }

        player.setRemainingAir(9000);
        player.addPotionEffect(PotionEffectType.DOLPHINS_GRACE.createEffect(5*60, 1));
        player.setInvisible(true);

        Dolphin d = player.getWorld().spawn(player.getLocation(), Dolphin.class);
        d.setAI(false);
        d.setCollidable(false);
        player.setCollidable(false);
        d.setInvulnerable(true);

        playerData pd = new playerData(player.getUniqueId().toString());
        pd.getPlayerData().set("shark", true);
        pd.save();


        new BukkitRunnable() {


            @Override
            public void run() {

                Location loc = player.getLocation().clone();
                loc.setY(loc.getY()-1);
                if (isCooldownActive(player,5*60 )) {
                    d.teleport(loc);
                }else {
                    player.setInvisible(false);
                    pd.getPlayerData().set("shark", false);
                    pd.save();
                    d.remove();
                }
            }
        }.runTaskTimer(plugin(), 0, 1);
        setCooldown(player);




    }


}
