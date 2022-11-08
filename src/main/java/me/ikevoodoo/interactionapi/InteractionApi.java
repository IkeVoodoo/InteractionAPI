package me.ikevoodoo.interactionapi;

import me.ikevoodoo.interactionapi.interactables.InteractableBlock;
import me.ikevoodoo.interactionapi.interactables.InteractableItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class InteractionApi extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        new InteractableBlock(this)
            .onInteract((source, world, block, type) -> {
                String message = "§6%s§r interacted with block §3%s§r (At %s) in world §c%s§r (Action type: §a%s§r)";
                Bukkit.broadcastMessage(String.format(
                    message,
                    source.getName(),
                    block.getType(),
                    block.getLocation().getBlockX() + " " + block.getLocation().getBlockY() + " " + block.getLocation().getBlockZ(),
                    world.getName(),
                    type
                ));

                return false;
            })
            .register(new Location(Bukkit.getWorld("world"), 0, 100, 0));

        new InteractableItem(this)
            .onInteract((source, world, item, action) -> {

                item.setAmount(item.getAmount() - 1);
                source.sendMessage("§aYou gained a heart!");

                return true;
            })
            .register(new NamespacedKey(this, "heart_item"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
