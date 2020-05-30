package io.github.lmvdz.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.client.sound.Channel;
import net.minecraft.client.sound.Listener;
import net.minecraft.client.sound.SoundEngine;
import net.minecraft.client.sound.SoundLoader;
import net.minecraft.client.sound.SoundSystem;

@Mixin(SoundSystem.class)
public interface SoundSystemAccess {
    @Invoker
    void invokeStart();

    @Accessor
    SoundLoader getSoundLoader();

    @Accessor
    Channel getChannel();

    @Accessor
    SoundEngine getSoundEngine();

    @Accessor
    Listener getListener();

    @Accessor
    void setStarted(boolean started);
}
