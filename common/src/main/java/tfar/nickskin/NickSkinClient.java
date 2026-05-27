package tfar.nickskin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.ResourceLocation;

public class NickSkinClient {

    public static ResourceLocation getPlayerSkin(GameProfile gameProfile) {
        Minecraft minecraft = Minecraft.getInstance();
        SkinManager skinManager = minecraft.getSkinManager();
        PlayerSkin map = skinManager.getInsecureSkin(gameProfile);
        return map.texture();
    }

    public static PlayerSkin.Model getModelInfo(GameProfile gameProfile) {
        Minecraft minecraft = Minecraft.getInstance();
        SkinManager skinManager = minecraft.getSkinManager();
        PlayerSkin map = skinManager.getInsecureSkin(gameProfile);
        return map.model();
    }
}
