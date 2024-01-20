package com.cota.biomes.getBiome.Abilities.Taiga;

import com.cota.biomes.files.playerData;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

import static com.cota.biomes.getBiome.managers.*;

public class reArrange implements Listener {


    public static HashMap<Player, Boolean> checking = new HashMap<>();
    public static HashMap<Player, Boolean> arranging = new HashMap<>();

    private long lastShiftPressTime =0;
    @EventHandler
    public void Dash(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!event.getAction().isLeftClick()) return;



        String biome = player.getWorld().getBiome(player.getLocation()).name();
        playerData pd = new playerData(player.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("taiga")) {


            // Check if the player is sneaking
            if (player.isSneaking()) {
                if (!isCooldownActiveSecond(player, 3 * 60)) {

                    event.setCancelled(true);


                    if (player.getTargetEntity(3) != null) {

                        if (player.getTargetEntity(3) instanceof Player target) {
                            rearrange(player, target);
                            setStartedSecond(player);
                            checking.put(player, true);
                        }
                    }


                }else {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText("§cOn cooldown"));
                }
            }
        }


    }

    private void rearrange(Player player, Player target) {

        Inventory inv = target.getInventory();






        for (int i=0; i<9; i++) {
            if ( target.getInventory().getItem(i) != null)
                inv.setItem(i, target.getInventory().getItem(i));
        }

        player.openInventory(inv);
        arranging.put(player, true);

        setCooldownSecond(player);




    }


}
