package io.github.lmvdz.client.mixin;

import java.util.concurrent.CompletableFuture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import io.github.lmvdz.client.MinecraftClientReloadResourcesCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {


    @Inject(at = @At("HEAD"), method = "reloadResources", cancellable = true)
    public void onReloadResources(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        MinecraftClientReloadResourcesCallback.EVENT.invoker().onReloadResources(
                (MinecraftClient) (Object) this,
                (Boolean cancel, CompletableFuture<Void> returnValue) -> {
                    if (cancel) {
                        cir.cancel();
                        cir.setReturnValue(returnValue);
                    }
                });
    }
}
