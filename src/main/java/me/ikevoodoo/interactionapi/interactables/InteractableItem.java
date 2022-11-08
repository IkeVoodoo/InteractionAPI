package me.ikevoodoo.interactionapi.interactables;

import me.ikevoodoo.interactionapi.InteractableBase;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

public class InteractableItem extends InteractableBase<NamespacedKey, ItemStack, Action> {
    public InteractableItem(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void register(NamespacedKey key) {
        this.listenFor(PlayerInteractEvent.class, event -> {
            ItemStack stack = event.getItem();
            if (stack == null) return;

            ItemMeta meta = stack.getItemMeta();
            if (meta == null) return;

            PersistentDataContainer container = meta.getPersistentDataContainer();
            boolean contains = container.getKeys().stream().anyMatch(key::equals);

            if (contains) {
                Player player = event.getPlayer();

                boolean cancel = this.triggerInteraction(
                    player,
                    player.getWorld(),
                    stack,
                    event.getAction()
                );

                if(cancel) {
                    event.setCancelled(true);
                }
            }
        });
    }
}
