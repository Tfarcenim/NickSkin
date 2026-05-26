package tfar.nickskin.attachments;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ResolvableProfile;
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

    public static String getNickName(Player player) {
        return Services.PLATFORM.getAttachedValue(player,CommonDataAttachments.NICKNAME);
    }

    public static void clearNickName(Player player) {
        setNickName(player,null);
    }


    public static void setSkin(Player player, @Nullable ResolvableProfile skin) {
        Services.PLATFORM.setAttachedValue(player,CommonDataAttachments.SKIN,skin);

    }

    public static ResolvableProfile getSkin(Player player) {
        return Services.PLATFORM.getAttachedValue(player,CommonDataAttachments.SKIN);
    }

    public static void clearSkin(Player player) {
        setSkin(player,null);
    }
}
