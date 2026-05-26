package tfar.nickskin.attachments;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import tfar.nickskin.NickSkin;
import tfar.nickskin.platform.Services;

public class AttachmentHelper {

    public static void setNickName(Player player,@Nullable String nickname) {
        Services.PLATFORM.setAttachedValue(player,CommonDataAttachments.NICKNAME,nickname);
        if (!player.level().isClientSide()) {
            Services.PLATFORM.refreshDisplayName(player);
            NickSkin.nickSkinData.update(player,nickname);
        }
    }

    public static void setNickNameOffline(Player player,@Nullable String nickname) {
        Services.PLATFORM.setAttachedValue(player,CommonDataAttachments.NICKNAME,nickname);
        if (!player.level().isClientSide()) {
            Services.PLATFORM.refreshDisplayName(player);
            NickSkin.nickSkinData.update(player,nickname);
        }
    }

    public static String getNickName(Player player) {
        return Services.PLATFORM.getAttachedValue(player,CommonDataAttachments.NICKNAME);
    }

    public static void clearNickName(Player player) {
        setNickName(player,null);
    }
}
