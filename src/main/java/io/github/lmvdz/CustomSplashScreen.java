package io.github.lmvdz;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.mojang.blaze3d.systems.RenderSystem;

import io.github.lmvdz.mixin.SplashScreenMixin;
import io.github.lmvdz.mixin.SplashScreenMixin.Access;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.resource.ResourceReloadMonitor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

public class CustomSplashScreen extends SplashScreen implements IHasLogo, IHasLogoTexture {

    public Identifier logo;
    public final List<Identifier> logos;
    public int logoIndex = 0;
    public boolean loop;

    public CustomSplashScreen(MinecraftClient client, ResourceReloadMonitor monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading, Identifier logo, List<Identifier> logos) {
        super(client, monitor, exceptionHandler, reloading);
        this.logo = logo;
        this.logos = logos == null ? new ArrayList<>() : logos;
        this.loop = logos.size() > 0;
        registerTexture(client);
        for(int x = 0; x < this.logos.size(); x++) {
            CustomLogoTexture.logoTextures.computeIfAbsent(this.logos.get(x), logoToCreate -> new CustomLogoTexture(logoToCreate));
        }
        
    }



    @Override
    public CustomLogoTexture getLogoTexture() {
        return CustomLogoTexture.getLogoTexture(this.getLogo());
    }

    public void registerTexture(MinecraftClient client) {
        client.getTextureManager().registerTexture(this.getLogo(), this.getLogoTexture());
    }

    public void render(int mouseX, int mouseY, float delta, Identifier logo) {
        Access temp = (SplashScreenMixin.Access) (SplashScreen) this;
        MinecraftClient client = temp.getClient();
        int i = client.getWindow().getScaledWidth();
        int j = client.getWindow().getScaledHeight();
        long l = Util.getMeasuringTimeMs();
        if (temp.isReloading() && (temp.getReloadMonitor().isPrepareStageComplete() || client.currentScreen != null) && temp.getPrepareCompleteTime() == -1L) {
            temp.setPrepareCompleteTime(l);
        }

        float f = temp.getApplyCompleteTime() > -1L ? (float)(l - temp.getApplyCompleteTime()) / 1000.0F : -1.0F;
        float g = temp.getPrepareCompleteTime() > -1L ? (float)(l - temp.getPrepareCompleteTime()) / 500.0F : -1.0F;
        float o;
        int m;
        if (f >= 1.0F) {
            if (client.currentScreen != null) {
                client.currentScreen.render(0, 0, delta);
            }

            m = MathHelper.ceil((1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
            fill(0, 0, i, j, 16777215 | m << 24);
            o = 1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F);
        } else if (temp.isReloading()) {
            if (client.currentScreen != null && g < 1.0F) {
                client.currentScreen.render(mouseX, mouseY, delta);
            }

            m = MathHelper.ceil(MathHelper.clamp((double)g, 0.15D, 1.0D) * 255.0D);
            fill(0, 0, i, j, 16777215 | m << 24);
            o = MathHelper.clamp(g, 0.0F, 1.0F);
        } else {
            fill(0, 0, i, j, -1);
            o = 1.0F;
        }

        m = (client.getWindow().getScaledWidth() - 256) / 2;
        int q = (client.getWindow().getScaledHeight() - 256) / 2;
        client.getTextureManager().bindTexture(logo);
        RenderSystem.enableBlend();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, o);
        this.blit(m, q, 0, 0, 256, 256);
        float r = temp.getReloadMonitor().getProgress();
        temp.setProgress(MathHelper.clamp(temp.getProgress() * 0.95F + r * 0.050000012F, 0.0F, 1.0F));
        if (f < 1.0F) {
            temp.renderProgressBar(i / 2 - 150, j / 4 * 3, i / 2 + 150, j / 4 * 3 + 10, 1.0F - MathHelper.clamp(f, 0.0F, 1.0F));
        }

        if (f >= 2.0F) {
            client.setOverlay((Overlay)null);
        }

        if (temp.getApplyCompleteTime() == -1L && temp.getReloadMonitor().isApplyStageComplete() && (!temp.isReloading() || g >= 2.0F)) {
            try {
                temp.getReloadMonitor().throwExceptions();
                temp.getExceptionHandler().accept(Optional.empty());
            } catch (Throwable var15) {
                temp.getExceptionHandler().accept(Optional.of(var15));
            }

            temp.setApplyCompleteTime(Util.getMeasuringTimeMs());
            if (client.currentScreen != null) {
                client.currentScreen.init(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight());
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        render(mouseX, mouseY, delta, this.getLogo());
        if (logos.size() > 0) this.logoIndex++;
    }

    @Override
    public Identifier getLogo() {
        if (logos.size() == 0) return this.logo;
        if (this.logoIndex < 0) {
            this.logoIndex = 0;
        } else if (this.logoIndex > logos.size()) {
            if (this.loop) 
                this.logoIndex = 0;
            else
                this.logoIndex = logos.size();
        }
        return logos.get(this.logoIndex);
    }
}