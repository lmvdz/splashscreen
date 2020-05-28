

package io.github.lmvdz;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@FunctionalInterface
public interface MinecraftClientSetOverlayCallback {

    Event<MinecraftClientSetOverlayCallback> EVENT = EventFactory.createArrayBacked(MinecraftClientSetOverlayCallback.class, (listeners) -> () -> {
        for (MinecraftClientSetOverlayCallback listener : listeners) {
            listener.afterOverlaySet();
        }
    });
    
    void afterOverlaySet();
}