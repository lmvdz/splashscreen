
package io.github.lmvdz.client;

import java.util.function.Consumer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screen.SplashScreen;

@FunctionalInterface
public interface SplashScreenRenderCallback {

    Event<SplashScreenRenderCallback> EVENT =
            EventFactory.createArrayBacked(SplashScreenRenderCallback.class,
                    (listeners) -> (splashScreen, mouseX, mouseY, delta, cancel) -> {
                        for (SplashScreenRenderCallback listener : listeners) {
                            listener.onRender(splashScreen, mouseX, mouseY, delta, cancel);
                        }
                    });

    void onRender(SplashScreen splashScreen, int mouseX, int mouseY, float delta,
            Consumer<Boolean> cancel);
}
