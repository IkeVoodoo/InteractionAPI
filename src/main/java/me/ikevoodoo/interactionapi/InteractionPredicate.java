package me.ikevoodoo.interactionapi;

import org.bukkit.World;
import org.bukkit.entity.Player;

public interface InteractionPredicate<T, E extends Enum<?>> {

    boolean test(Player source, World world, T extra, E type);

}
