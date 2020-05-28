package io.github.lmvdz;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashScreen;

@FunctionalInterface
public interface SplashScreenInitCallback {

    Event<SplashScreenInitCallback> EVENT = EventFactory.createArrayBacked(SplashScreenInitCallback.class, (listeners) -> (splash, client) -> {
        for (SplashScreenInitCallback listener : listeners) {
            listener.splash(splash, client);
        }
    });
    
    void splash(SplashScreen splashScreen, MinecraftClient client);
}