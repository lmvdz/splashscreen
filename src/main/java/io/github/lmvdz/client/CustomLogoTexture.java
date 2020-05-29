package io.github.lmvdz.client;

import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.metadata.TextureResourceMetadata;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import net.fabricmc.api.EnvType;

@Environment(EnvType.CLIENT)
public class CustomLogoTexture extends ResourceTexture {

    public static final HashMap<Identifier, CustomLogoTexture> logoTextures = new HashMap<>();

    public CustomLogoTexture(Identifier logo) {
        super(logo);
    }

    public static CustomLogoTexture getLogoTexture(Identifier logo) {
        return logoTextures.computeIfAbsent(logo, tex -> new CustomLogoTexture(tex));
    }

    protected ResourceTexture.TextureData loadTextureData(ResourceManager resourceManager) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        DefaultResourcePack defaultResourcePack =
                minecraftClient.getResourcePackDownloader().getPack();

        try {
            InputStream inputStream =
                    defaultResourcePack.open(ResourceType.CLIENT_RESOURCES, this.location);
            Throwable var5 = null;

            ResourceTexture.TextureData var6;
            try {
                var6 = new ResourceTexture.TextureData((TextureResourceMetadata) null,
                        NativeImage.read(inputStream));
            } catch (Throwable var16) {
                var5 = var16;
                throw var16;
            } finally {
                if (inputStream != null) {
                    if (var5 != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable var15) {
                            var5.addSuppressed(var15);
                        }
                    } else {
                        inputStream.close();
                    }
                }

            }

            return var6;
        } catch (IOException var18) {
            return new ResourceTexture.TextureData(var18);
        }
    }
}
