package io.github.lmvdz.client;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

@FunctionalInterface
public interface MinecraftClientReloadResourcesCallback {

    Event<MinecraftClientReloadResourcesCallback> EVENT =
            EventFactory.createArrayBacked(MinecraftClientReloadResourcesCallback.class,
                    (listeners) -> (client, cancelAndReturn) -> {
                        for (MinecraftClientReloadResourcesCallback listener : listeners) {
                            listener.onReloadResources(client, cancelAndReturn);
                        }
                    });

    void onReloadResources(MinecraftClient client,
            BiConsumer<Boolean, CompletableFuture<Void>> cancelAndReturn);
}
