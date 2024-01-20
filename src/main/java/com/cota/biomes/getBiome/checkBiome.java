package com.cota.biomes.getBiome;

import com.cota.biomes.files.playerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;
import org.bukkit.material.Vine;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

import static com.cota.biomes.Biomes.plugin;
import static com.cota.biomes.getBiome.managers.*;


public class checkBiome implements Listener {

    private HashMap<Player, Boolean> checked = new HashMap<>();
    @EventHandler
    public void check(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        String biome = p.getWorld().getBiome(p.getLocation()).name();


        ArrayList<String> types = new ArrayList<>();

        types.add("plains");
        types.add("forest");
        types.add("savanna");
        types.add("cherry");
        types.add("jungle");
        types.add("slopes");
        types.add("ocean");
        types.add("taiga");
        types.add("spikes");
        types.add("dark");
        types.add("desert");
        types.add("swamp");
        types.add("badlands");
        types.add("mushroom");

        for (String s: types) {
            if (biome.toLowerCase().contains(s)) {
                playerData pd = new playerData(p.getUniqueId().toString());
                String selection = pd.getPlayerData().getString("selection");
                if (selection != null) {
                    if (selection.equals(s)) {
                        pd.getPlayerData().set("selection", null);
                        pd.getPlayerData().set("biome", selection);
                        pd.save();
                    }
                }
            }
        }











        playerData pd = new playerData(p.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("badlands")) {





                if (checked.get(p) == null) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999999, -1+((int) Math.round(getMultiplier(p)))));
                    checked.put(p, true);
                }

            } else {
                p.removePotionEffect(PotionEffectType.FAST_DIGGING);
                checked.remove(p);
            }






        if (Biome.contains("mushroom")) {


                if (checked.get(p) == null) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999999, -1+((int) Math.round(getMultiplier(p)))));
                    checked.put(p, true);
                }


        }







    }

    @EventHandler
    public void punch(EntityDamageByEntityEvent e) {

        if (!(e.getDamager() instanceof Player p)) return;

        String biome = p.getWorld().getBiome(p.getLocation()).name();



        playerData pd = new playerData(p.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("ocean")) {


                if (isCooldownActive(p, 5 * 60)) {
                    e.setDamage(e.getDamage() + 10*getMultiplier(p));

                }



        }



        if (Biome == null) return;

        if (Biome.contains("dark")) {



                if (isCooldownActive(p, 10 * 60)) {
                    e.setDamage(e.getDamage() + 30*getMultiplier(p));

                }



        }




        if (Biome.contains("desert")) {


                p.damage(e.getDamage()*getMultiplier(p));



        }









    }

    @EventHandler
    public void Break(BlockBreakEvent e) {

        Player p = e.getPlayer();

        String biome = p.getWorld().getBiome(p.getLocation()).name();





        playerData pd = new playerData(p.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("badlands")) {





                if (pd.getPlayerData().get("started") != null) {
                    if (randomizer(p)) {
                        e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));

                        pd.getPlayerData().set("started", null);

                       pd.save();
                        setCooldown(p);

                    }

                }




        }

    }

    public boolean randomizer(Player player) {
        boolean Chance = false;

        Random random = new Random();
        double randomValue = random.nextDouble() * 100; // Generating a random value between 0 and 100


        if (randomValue > 1*getMultiplier(player)) {
            Chance = false;
        } else {
            Chance = true;
        }





        return Chance;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        String biome = player.getWorld().getBiome(player.getLocation()).name();

        playerData pd = new playerData(player.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("jungle")) {


            // Check for right-click while sneaking
            if (event.getAction().name().contains("RIGHT") && player.isSneaking()) {
                Block block = event.getClickedBlock(); // Adjust the range as needed

                if (block != null && canPlaceVine(block)) {
                    placeVine(player, block, event.getBlockFace());
                }
            }
        }
    }

    private boolean canPlaceVine(Block block) {
        // Add any additional checks here (e.g., block type)
        return block.getType().isSolid(); // Change to the desired block type
    }

    private void placeVine(Player player, Block block, BlockFace bf) {
        // Get the block in front of the player

        BlockFace newBlockFace = BlockFace.SELF;

        Location bloc = block.getLocation();
        if (bf.toString().toLowerCase().equals("north")) {
            bloc.subtract(0, 0, 1);
            newBlockFace = BlockFace.SOUTH;
        }
        if (bf.toString().toLowerCase().equals("south")) {
            bloc.add(0, 0, 1);
            newBlockFace = BlockFace.NORTH;
        }
        if (bf.toString().toLowerCase().equals("east")) {
            bloc.add(1, 0, 0);
            newBlockFace = BlockFace.WEST;
        }
        if (bf.toString().toLowerCase().equals("west")) {
            bloc.subtract(1, 0, 0);
            newBlockFace = BlockFace.EAST;
        }


        player.getWorld().getBlockAt(bloc).setType(Material.VINE);


        Block b = player.getWorld().getBlockAt(bloc);

        if (b.getState().getData() instanceof Vine v) {


            BlockData bd = v.getItemType().createBlockData();

            if (bd instanceof MultipleFacing mp) {
                mp.setFace(newBlockFace, true);
                b.setBlockData(mp);
                b.getState().update();
            }


        }

        new BukkitRunnable() {
            @Override
            public void run() {
                b.getWorld().getBlockAt(bloc).setType(Material.AIR);
            }
        }.runTaskLater(plugin(), 20L *20*((int) Math.round(getMultiplier(player))));


    }
}
