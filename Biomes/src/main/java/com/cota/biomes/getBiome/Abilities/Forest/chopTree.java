package com.cota.biomes.getBiome.Abilities.Forest;

import com.cota.biomes.files.playerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.*;

public class chopTree implements Listener {


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();


        String biome = player.getWorld().getBiome(player.getLocation()).name();

        playerData pd = new playerData(player.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("forest")) {
            if (block.getType().toString().toLowerCase().contains("log") & hasConnectedLeaves(block)) {
                event.setCancelled(true); // Cancel the original block break event

                // Call the method to chop down the tree
                chopDownTree(block, player);
            }
        }
    }

    private boolean hasConnectedLeaves(Block startBlock) {
        Set<Block> visited = new HashSet<>();
        Queue<Block> queue = new LinkedList<>();

        queue.add(startBlock);
        visited.add(startBlock);

        while (!queue.isEmpty()) {
            Block currentBlock = queue.poll();

            for (BlockFace face : BlockFace.values()) {
                Block neighbor = currentBlock.getRelative(face);

                if (neighbor.getType().toString().toLowerCase().contains("leaves")) {
                    return true;
                }

                if (!visited.contains(neighbor) && neighbor.getType() == startBlock.getType()) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }

        return false;
    }



    private void chopDownTree(Block startBlock, Player player) {
        // Perform a depth-first search to find all connected log blocks
        // and add them to a list
        // Then break all the logs in the list

        // Example of a simple DFS, you may want to use a more efficient algorithm
        // based on your specific requirements and world structure
        dfsChop(startBlock, player);
    }

    private void dfsChop(Block currentBlock, Player player) {
        if (currentBlock.getType() == Material.AIR) {
            return; // Skip air blocks
        }

        if (currentBlock.getType().toString().toLowerCase().contains("log")) {
            // Break the log block
            currentBlock.breakNaturally();

            // Recursively check neighboring blocks
            for (BlockFace face : BlockFace.values()) {
                Block neighbor = currentBlock.getRelative(face);
                dfsChop(neighbor, player);
            }
        }
    }

}
