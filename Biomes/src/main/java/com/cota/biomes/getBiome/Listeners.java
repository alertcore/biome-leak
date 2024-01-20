package com.cota.biomes.getBiome;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

import static com.cota.biomes.getBiome.Abilities.Taiga.reArrange.arranging;
import static com.cota.biomes.getBiome.Abilities.Taiga.steal.checking;

public class Listeners implements Listener {


    private static HashMap<Player, ItemStack> store1 = new HashMap<>();
    private static HashMap<Player, Integer> store2 = new HashMap<>();
    @EventHandler
    public void inventory(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (checking.get(p) != null) {

            if (e.getCurrentItem() == null) return;
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem().clone();


            int s = e.getSlot();




            ItemStack pitem = item.clone();
            pitem.setAmount(pitem.getAmount()-1);
            e.getInventory().setItem(s, pitem);

            item.setAmount(1);
            p.getInventory().addItem(item);
            //



            p.closeInventory();
        }

        if (arranging.get(p) != null) {



            if (e.getRawSlot() != 0 & e.getRawSlot() != 1 &e.getRawSlot() != 2 &e.getRawSlot() != 3 &e.getRawSlot() != 4 &
                    e.getRawSlot() != 5 &e.getRawSlot() != 6 &e.getRawSlot() != 7 & e.getRawSlot() !=8 & e.getRawSlot() !=9) {
                e.setCancelled(true);
                return;
            }
            if (e.getCurrentItem() == null) return;
            e.setCancelled(true);

            if (store1.get(p) == null) {
                store1.put(p, e.getCurrentItem());
                store2.put(p, e.getSlot());
            }else {
                e.getInventory().setItem(store2.get(p), e.getCurrentItem());
                e.getInventory().setItem(e.getSlot(), store1.get(p));

                store1.remove(p);
                store2.remove(p);

            }
            //




        }
    }

    @EventHandler
    public void closeinventory(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        if (checking.get(p) != null) {

            checking.remove(p);
        }
        if (arranging.get(p) != null) {

            arranging.remove(p);
        }
    }
}
