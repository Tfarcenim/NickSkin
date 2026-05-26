package tfar.nickskin.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
        @Redirect(
                method = {"getNameForDisplay(Lnet/minecraft/client/multiplayer/PlayerInfo;)Lnet/minecraft/network/chat/Component;"},
                at = @At(
                        value = "INVOKE",
                        target = "Lcom/mojang/authlib/GameProfile;getName()Ljava/lang/String;"
                )
        )
        private static String modifyReturnValue(GameProfile instance) {
            return instance.getName();
        }
    }
