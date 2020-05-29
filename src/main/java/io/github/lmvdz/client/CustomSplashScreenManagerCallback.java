package io.github.lmvdz.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@FunctionalInterface
public interface CustomSplashScreenManagerCallback {

    Event<CustomSplashScreenManagerCallback> EVENT = EventFactory.createArrayBacked(
            CustomSplashScreenManagerCallback.class, (listeners) -> (splashScreen) -> {
                for (CustomSplashScreenManagerCallback listener : listeners) {
                    listener.customSplashScreenCreated(splashScreen);
                }
            });

    void customSplashScreenCreated(CustomSplashScreen splashScreen);
}
