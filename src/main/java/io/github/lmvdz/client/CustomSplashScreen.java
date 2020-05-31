package io.github.lmvdz.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.mojang.blaze3d.systems.RenderSystem;

import io.github.lmvdz.client.mixin.SoundManagerAccess;
import io.github.lmvdz.client.mixin.SoundSystemAccess;
import io.github.lmvdz.client.mixin.SplashScreenAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundEngine;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.resource.ResourceReloadMonitor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

public class CustomSplashScreen extends Overlay {

    public Identifier defaultLogo;
    public Identifier currentLogo;
    public List<Identifier> logos;
    public Sound splashSound;
    public int logoIndex = 0;
    public boolean loop;
    public boolean canRenderNext;
    public boolean firstRender;
    public MinecraftClient client;
    public ResourceReloadMonitor reloadMonitor;
    public Consumer<Optional<Throwable>> exceptionHandler;
    public boolean reloading;
    public float progress;
    public long applyCompleteTime = -1L;
    public long prepareCompleteTime = -1L;

    public CustomSplashScreen(MinecraftClient client, ResourceReloadMonitor monitor,
            Consumer<Optional<Throwable>> exceptionHandler, boolean reloading,
            Identifier defaultLogo, List<Identifier> logos, Sound splashSound) {
        this.client = client;
        this.reloadMonitor = monitor;
        this.exceptionHandler = exceptionHandler;
        this.reloading = reloading;
        this.defaultLogo = defaultLogo;
        this.currentLogo = defaultLogo;
        this.logos = logos == null ? new ArrayList<>() : logos;
        this.canRenderNext = false;
        this.loop = false;
        this.firstRender = true;
        this.splashSound = splashSound;
        registerTexture(defaultLogo, getDefaultLogoTexture());
        for (int x = 0; x < this.logos.size(); x++) {
            registerTexture(this.logos.get(x), CustomLogoTexture.getLogoTexture(this.logos.get(x)));
        }
    }

	public List<Identifier> getLogoTextureIdentifiers() {
		return this.logos;
	}

    public CustomLogoTexture getCurrentLogoTexture() {
        return CustomLogoTexture.getLogoTexture(this.getCurrentLogo());
    }

    public CustomLogoTexture getDefaultLogoTexture() {
        return CustomLogoTexture.getLogoTexture(this.getDefaultLogo());
    }

    public void registerTexture(Identifier id, AbstractTexture texture) {
        this.client.getTextureManager().registerTexture(id, texture);
    }

    public void render(int mouseX, int mouseY, float delta, Identifier logo) {
        // play sound on first render
        if (firstRender) {
            this.firstRender = false;
            this.canRenderNext = true;
            if (splashSound != null) {
                ((SoundSystemAccess) ((SoundManagerAccess) CustomSplashScreenManager.SoundManagerInstance)
                    .getSoundSystem()).getSoundLoader()
                            .loadStreamed(splashSound.getLocation())
                            .thenAccept(streamedSound -> {
                                ((SoundSystemAccess) ((SoundManagerAccess) CustomSplashScreenManager.SoundManagerInstance)
                                        .getSoundSystem()).getChannel()
                                                .createSource(SoundEngine.RunMode.STREAMING)
                                                .run((source) -> {
                                                    source.setStream(streamedSound);
                                                    source.play();
                                                });
                            });
            }
            
        }

        int i = client.getWindow().getScaledWidth();
        int j = client.getWindow().getScaledHeight();
        long l = Util.getMeasuringTimeMs();
        if (this.reloading
                && (this.reloadMonitor.isPrepareStageComplete() || client.currentScreen != null)
                && this.prepareCompleteTime == -1L) {
            this.prepareCompleteTime = l;
        }

        float f = this.applyCompleteTime > -1L ? (float) (l - this.applyCompleteTime) / 1000.0F
                : -1.0F;
        // System.out.println(f);
        float g = this.prepareCompleteTime > -1L ? (float) (l - this.prepareCompleteTime) / 500.0F
                : -1.0F;
        float o;
        int m;
        if (f >= 1.0F) {
            if (client.currentScreen != null) {
                client.currentScreen.render(0, 0, delta);
            }

            m = MathHelper.ceil((1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
            fill(0, 0, i, j, 16777215 | m << 24);
            o = 1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F);
        } else if (this.reloading) {
            if (client.currentScreen != null && g < 1.0F) {
                client.currentScreen.render(mouseX, mouseY, delta);
            }

            m = MathHelper.ceil(MathHelper.clamp((double) g, 0.15D, 1.0D) * 255.0D);
            fill(0, 0, i, j, 16777215 | m << 24);
            o = MathHelper.clamp(g, 0.0F, 1.0F);
        } else {
            fill(0, 0, i, j, -1);
            o = 1.0F;
        }
        client.getTextureManager().bindTexture(logo);
        RenderSystem.enableBlend();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, o);
        blit(0, 0, 0F, 0F, client.getWindow().getWidth(), client.getWindow().getHeight(),
                (1920 / 1080) * client.getWindow().getScaledWidth(),
                (1920 / 1080) * client.getWindow().getScaledHeight());
        float r = this.reloadMonitor.getProgress();
        this.progress = MathHelper.clamp(this.progress * 0.95F + r * 0.050000012F, 0.0F, 1.0F);
        if (f < 1.0F) {
            renderProgressBar(this.progress, i / 2 - 150, j / 4 * 3, i / 2 + 150, j / 4 * 3 + 10,
                    1.0F - MathHelper.clamp(f, 0.0F, 1.0F));
        }

        if (f >= 2.0F) {
            client.setOverlay((Overlay) null);
        }

        if (this.applyCompleteTime == -1L && this.reloadMonitor.isApplyStageComplete()
                && (!this.reloading || g >= 2.0F)) {
            try {
                this.reloadMonitor.throwExceptions();
                this.exceptionHandler.accept(Optional.empty());
            } catch (Throwable var15) {
                this.exceptionHandler.accept(Optional.of(var15));
            }

            this.applyCompleteTime = Util.getMeasuringTimeMs();
            if (client.currentScreen != null) {
                client.currentScreen.init(client, client.getWindow().getScaledWidth(),
                        client.getWindow().getScaledHeight());
            }
        }
    }

    public static void renderProgressBar(float currentProgress, int minX, int minY, int maxX,
            int maxY, float progress) {
        int i = MathHelper.ceil((float) (maxX - minX - 1) * currentProgress);
        fill(minX - 1, minY - 1, maxX + 1, maxY + 1,
                -16777216 | Math.round((1.0F - progress) * 255.0F) << 16
                        | Math.round((1.0F - progress) * 255.0F) << 8
                        | Math.round((1.0F - progress) * 255.0F));
        fill(minX, minY, maxX, maxY, -1);
        fill(minX + 1, minY + 1, minX + i, maxY - 1,
                -16777216 | (int) MathHelper.lerp(1.0F - progress, 226.0F, 255.0F) << 16
                        | (int) MathHelper.lerp(1.0F - progress, 40.0F, 255.0F) << 8
                        | (int) MathHelper.lerp(1.0F - progress, 55.0F, 255.0F));
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        render(mouseX, mouseY, delta, this.getCurrentLogo());
        if (canRenderNext) {
            if (logos.size() >= 128) {
                this.logoIndex += (int) Math.round((double) logos.size() / (double) 128);
            } else if (logos.size() > 0) {
                this.logoIndex++;
            }
        }
    }


    public Identifier getCurrentLogo() {
        if (logos.size() == 0)
            return this.currentLogo;
        if (this.logoIndex < 0) {
            this.logoIndex = 0;
        } else if (this.logoIndex >= logos.size()) {
            if (this.loop)
                this.logoIndex = 0;
            else
                this.logoIndex = logos.size() - 1;
        }
        return logos.get(this.logoIndex);
    }


    public Identifier getDefaultLogo() {
        return defaultLogo;
    }

	public void reset(SplashScreen supplier) {
        this.client = ((SplashScreenAccess)supplier).getClient();
        this.reloadMonitor = ((SplashScreenAccess)supplier).getReloadMonitor();
        this.exceptionHandler = ((SplashScreenAccess)supplier).getExceptionHandler();
        this.reloading = ((SplashScreenAccess)supplier).isReloading();
        this.currentLogo = this.defaultLogo;
        this.canRenderNext = false;
        this.firstRender = true;
        this.logoIndex = 0;
        this.applyCompleteTime = -1L;
        this.prepareCompleteTime = -1L;
        this.progress = 0;
        this.client.overlay = this;
	}
}
