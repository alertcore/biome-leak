package com.cota.biomes.getBiome.Abilities.Jungle;

import com.cota.biomes.files.playerData;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static com.cota.biomes.Biomes.plugin;
import static com.cota.biomes.getBiome.managers.*;

public class forceField implements Listener {


    private long lastShiftPressTime =0;
    @EventHandler
    public void Dash(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        String biome = player.getWorld().getBiome(player.getLocation()).name();

        playerData pd = new playerData(player.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("jungle")) {



            // Check if the player is sneaking
            if (event.isSneaking()) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastShiftPressTime <= DOUBLE_PRESS_THRESHOLD) {

                    if (!isCooldownActive(player, 60)) {
                        if (player.isOnGround()) {
                            test(player);
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
    private void test(Player player) {


        // Create a BukkitRunnable to spawn particles in a star-shaped spiral

        // Adjust the radius as needed
        double angularSpeed = 25.0; // Adjust the speed of the rotation as needed




        new BukkitRunnable() {

            Location center = player.getLocation();
            Location block = new Location(center.getWorld(), center.getBlockX(), center.getBlockY(), center.getBlockZ());
            double radius = 2.0;
            int blue = 153;
            int red = 255;
            int green = 0;
            int newblue = 0;
            float size = 2;
            double angle = 0;

            double aY = center.getY();;





            @Override
            public void run() {
                if (angle >= 1080.0) {
                    this.cancel();


                    block.setY(block.getBlockY()-1);

                    while (block.getBlock().getType() == Material.AIR) {
                        block.subtract(0, 1, 0);

                    }





                    block.add(0, 1, 0);

                    return;
                }

                double radians = Math.toRadians(angle);
                double x = center.getX() + radius * Math.cos(radians);
                double y = aY;
                double z = center.getZ() + radius * Math.sin(radians);










                Location particleLocation = new Location(center.getWorld(), x, aY, z);






             //   while (isAir.getBlock().getType() == Material.AIR) {
                    //player.sendMessage(isAir.getBlock().getType().toString());
                   // particleLocation.subtract(0, 1, 0);
              //
                //  }

                //player.sendMessage(isAir.getBlock().getType().toString());







                //if (particleLocation.getBlock().getType().isSolid()) {


                   /* Location isAirAbove = particleLocation;
                    isAirAbove.setY(isAirAbove.getBlockY()+2);

                    Location isBelow = particleLocation;
                    isBelow.setY(isBelow.getY()-0.1);*/


                    /*if (!isBelow.getBlock().isSolid()) {
                        particleLocation.subtract(0, 1, 0);
                    }*/






                    particleLocation.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 60, new Particle.DustOptions(Color.fromRGB(255, 255, blue), size));





                    //radius+= 0.02;
                    if (blue >= 0 && blue - angle/7 >= 0) {

                        blue = (int) (blue - angle/7);
                        size += 0.01;


                    }



                   aY+=0.1;

               // player.sendMessage("a");

                    //particleLocation.add(0, 1, 0);
                    //}
                //}
                angle += angularSpeed;


            }
        }.runTaskTimer(plugin(), 0, 1); // Run the task every tick (1/20th of a second)

        setCooldown(player);
    }

}
