package me.ikevoodoo.interactionapi;

import org.bukkit.World;
import org.bukkit.entity.Player;

public interface InteractionHandler<T, E extends Enum<?>> {

    boolean handle(Player source, World world, T extra, E type);

}
