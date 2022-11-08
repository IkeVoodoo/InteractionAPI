package me.ikevoodoo.interactionapi;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public abstract class InteractableBase<K, V, E extends Enum<?>> {

    private final List<InteractionHandler<V, E>> handlers = new ArrayList<>();
    private final List<InteractionPredicate<V, E>> predicates = new ArrayList<>();
    private final Plugin plugin;
    private final Listener listener;

    protected InteractableBase(Plugin plugin) {
        this.plugin = plugin;
        this.listener = new Listener() {};
    }

    public InteractableBase<K, V, E> onInteract(InteractionHandler<V, E> handler) {
        this.handlers.add(handler);
        return this;
    }

    public InteractableBase<K, V, E> addPredicate(InteractionPredicate<V, E> predicate) {
        this.predicates.add(predicate);
        return this;
    }

    public abstract void register(K key);

    public final void unregister() {
        HandlerList.unregisterAll(this.listener);
    }

    protected final Plugin getPlugin() {
        return this.plugin;
    }

    @SuppressWarnings("unchecked")
    protected final <C extends Event> void listenFor(Class<C> event, Consumer<C> consumer) {
        Bukkit.getServer().getPluginManager().registerEvent(
            event,
            this.listener,
            EventPriority.MONITOR,
            (calledListener, ev) -> consumer.accept((C) ev),
            this.getPlugin()
        );
    }

    protected boolean triggerInteraction(Player source, World world, V value, E type) {
        for (InteractionPredicate<V, E> predicate : this.predicates) {
            if (!predicate.test(source, world, value, type)) {
                return false;
            }
        }

        for (InteractionHandler<V, E> handler : this.handlers) {
            if (!handler.handle(source, world, value, type)) {
                return false;
            }
        }

        return true;
    }

}
