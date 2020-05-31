package io.github.lmvdz.client.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.sound.SoundManager;

@FunctionalInterface
public interface SoundManagerCreatedCallback {

    Event<SoundManagerCreatedCallback> EVENT = EventFactory
            .createArrayBacked(SoundManagerCreatedCallback.class, (listeners) -> (soundManager) -> {
                for (SoundManagerCreatedCallback listener : listeners) {
                    listener.soundManagerCreated(soundManager);
                }
            });

    void soundManagerCreated(SoundManager soundManager);
}
