package io.github.lmvdz.client.mixin;

import java.util.concurrent.CompletableFuture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.resource.ClientResourcePackProfile;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.util.Unit;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccess {
    @Accessor
    void setOverlay(Overlay overlay);

    @Accessor
    ResourcePackManager<ClientResourcePackProfile> getResourcePackManager();

    @Accessor
    ReloadableResourceManager getResourceManager();

    @Accessor
    static CompletableFuture<Unit> getCOMPLETED_UNIT_FUTURE() {
        throw new AssertionError();
    }

    @Invoker
    void invokeHandleResourceReloadExecption(Throwable throwable);
}
