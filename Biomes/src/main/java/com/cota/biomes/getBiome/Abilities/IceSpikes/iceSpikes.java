package com.cota.biomes.getBiome.Abilities.IceSpikes;

import com.cota.biomes.files.playerData;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
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

public class iceSpikes implements Listener {



    private long lastShiftPressTime =0;
    @EventHandler
    public void Dash(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        String biome = player.getWorld().getBiome(player.getLocation()).name();
        playerData pd = new playerData(player.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("ice"))

            // Check if the player is sneaking
            if (event.isSneaking()) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastShiftPressTime <= DOUBLE_PRESS_THRESHOLD) {
                    // Check if the time difference between the current and last shift press is within the threshold
                    if (!isCooldownActive(player, 2 * 60)) {

                        TurtelMaster(player);
                        setStarted(player);

                    }
                }

                lastShiftPressTime = currentTime;


            }

    }

    @EventHandler
    public void beam(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        String biome = player.getWorld().getBiome(player.getLocation()).name();

        playerData pd = new playerData(player.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;


        if (Biome.contains("ice")) {

            // Check if the player is sneaking
            if (player.isSneaking()) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastShiftPressTime <= DOUBLE_PRESS_THRESHOLD) {
                    // Check if the time difference between the current and last shift press is within the threshold
                    if (!isCooldownActiveSecond(player, 35)) {

                        IceBeam(player);
                        setStartedSecond(player);

                    }else {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText("§cOn cooldown"));
                    }
                }

                lastShiftPressTime = currentTime;


            }
        }
    }

    private void IceBeam(Player player) {

        Location playerLocation = player.getLocation().set(player.getLocation().getBlockX(), player.getLocation().getY()-1, player.getLocation().getZ());
        Vector direction = playerLocation.getDirection();

        // Define the number of fangs and spacing between them
        int numberOfFangs = 15;
        double spacing = 0.5;

        // Calculate the starting point of the line
        Location startLocation = playerLocation.clone().add(direction.clone().multiply(1.5));

        // Summon a line of Evoker fangs
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {




                if(i > numberOfFangs) {
                    cancel();
                    return;
                }
                Location fangLocation = startLocation.clone().add(direction.clone().multiply(i * spacing).setY(0));

                EvokerFangs evokerFang = fangLocation.getWorld().spawn(fangLocation, EvokerFangs.class);
                evokerFang.setOwner(player);

                Location partLoc = fangLocation.clone().add(0, 1, 0);

                new BukkitRunnable() {
                    double o = 0;

                    @Override
                    public void run() {
                        if (o> 2) {
                            cancel();
                            return;
                        }
                        partLoc.add(0, o*0.3, 0);
                        fangLocation.getWorld().spawnParticle(Particle.REDSTONE, partLoc, 3, new Particle.DustOptions(Color.fromRGB(0, 200, 255), 2));
                        o+=0.3;
                    }
                }.runTaskTimer(plugin(), 0, 3);

                i++;
            }




        }.runTaskTimer(plugin(), 0, 3);

        setCooldownSecond(player);



    }

    private void TurtelMaster(Player player) {




        player.addPotionEffect(PotionEffectType.SLOW.createEffect(4*20, 3));
        player.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(4*20, 2));


        setCooldown(player);




    }


}
