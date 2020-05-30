

package io.github.lmvdz.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.options.GameOptions;

@FunctionalInterface
public interface GameOptionsInitCallback {

    Event<GameOptionsInitCallback> EVENT = EventFactory
            .createArrayBacked(GameOptionsInitCallback.class, (listeners) -> (gameOptions) -> {
                for (GameOptionsInitCallback listener : listeners) {
                    listener.onInit(gameOptions);
                }
            });

    void onInit(GameOptions gameOptions);
}
