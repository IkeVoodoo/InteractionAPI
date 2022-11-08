package me.ikevoodoo.interactionapi.interactables;

import me.ikevoodoo.interactionapi.InteractableBase;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;

public class InteractableBlock extends InteractableBase<Location, Block, InteractableBlock.InteractionType> {

    public enum InteractionType {
        LEFT_CLICK,
        RIGHT_CLICK,
        STEPPED_ON
    }

    public InteractableBlock(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void register(Location key) {
        Location safeLocation = key.clone().getBlock().getLocation();

        this.listenFor(PlayerInteractEvent.class, event -> {
            if (event.getHand() == EquipmentSlot.OFF_HAND) return;

            Action action = event.getAction();
            if (action != Action.LEFT_CLICK_BLOCK && action != Action.RIGHT_CLICK_BLOCK) return;

            Block clickedBlock = event.getClickedBlock();
            assert clickedBlock != null; // IntelliJ warnings
            if (!clickedBlock.getLocation().equals(safeLocation)) return;

            boolean cancel = this.triggerInteraction(
                event.getPlayer(),
                clickedBlock.getWorld(),
                clickedBlock,

                action == Action.LEFT_CLICK_BLOCK
                    ? InteractionType.LEFT_CLICK
                    : InteractionType.RIGHT_CLICK
            );

            if (cancel) {
                event.setCancelled(true);
            }
        });

        this.listenFor(PlayerMoveEvent.class, event -> {
            Location to = event.getTo();
            if(to == null) return;
            Location from = event.getFrom();
            if (to.getBlock().equals(from.getBlock())) return;

            
            Block steppedOn = to.getBlock().getRelative(BlockFace.DOWN);
            if (!steppedOn.getLocation().equals(safeLocation)) return;

            boolean cancel = this.triggerInteraction(
                event.getPlayer(),
                steppedOn.getWorld(),
                steppedOn,
                InteractionType.STEPPED_ON
            );

            if (cancel) {
                event.setCancelled(true);
            }
        });
    }

}
