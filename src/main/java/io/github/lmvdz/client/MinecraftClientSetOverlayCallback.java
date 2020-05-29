

package io.github.lmvdz.client;

import java.util.function.Consumer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screen.Overlay;

@FunctionalInterface
public interface MinecraftClientSetOverlayCallback {

    Event<MinecraftClientSetOverlayCallback> EVENT = EventFactory.createArrayBacked(
            MinecraftClientSetOverlayCallback.class, (listeners) -> (o, cancel) -> {
                for (MinecraftClientSetOverlayCallback listener : listeners) {
                    listener.afterOverlaySet(o, cancel);
                }
            });

    void afterOverlaySet(Overlay o, Consumer<Boolean> cancel);
}
