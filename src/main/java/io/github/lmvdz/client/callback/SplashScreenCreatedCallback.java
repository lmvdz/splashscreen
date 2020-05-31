package io.github.lmvdz.client.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screen.SplashScreen;

@FunctionalInterface
public interface SplashScreenCreatedCallback {

    Event<SplashScreenCreatedCallback> EVENT = EventFactory
            .createArrayBacked(SplashScreenCreatedCallback.class, (listeners) -> (splashScreen) -> {
                for (SplashScreenCreatedCallback listener : listeners) {
                    listener.onNewSplashScreen(splashScreen);
                }
            });

    void onNewSplashScreen(SplashScreen splashScreen);
}
