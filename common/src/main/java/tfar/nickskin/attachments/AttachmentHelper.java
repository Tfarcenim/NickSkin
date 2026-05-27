package tfar.nickskin.attachments;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.jetbrains.annotations.Nullable;
import tfar.nickskin.NickSkin;
import tfar.nickskin.platform.Services;

public class AttachmentHelper {

    public static void setNickName(Player player,@Nullable String nickname) {
        Services.PLATFORM.setAttachedValue(player,CommonDataAttachments.NICKNAME,nickname);
        if (!player.level().isClientSide()) {
            Services.PLATFORM.refreshDisplayName(player);
            Services.PLATFORM.refreshTabNameList(player);
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
        if (skin != null && !skin.isResolved()) {
            skin.resolve().thenAcceptAsync(profile -> setSkin(player, profile), SkullBlockEntity.CHECKED_MAIN_THREAD_EXECUTOR);
        }
        Services.PLATFORM.setAttachedValue(player,CommonDataAttachments.SKIN,skin);
    }

    public static ResolvableProfile getSkin(Player player) {
        return Services.PLATFORM.getAttachedValue(player, CommonDataAttachments.SKIN);
    }

    public static void setShouldPreventDeath(ServerPlayer player, boolean preventDeath) {
        Services.PLATFORM.setAttachedValue(player,CommonDataAttachments.PREVENT_DEATH,preventDeath ? Unit.INSTANCE : null);
    }

    public static boolean getShouldPreventDeath(ServerPlayer player) {
        return Services.PLATFORM.getAttachedValue(player,CommonDataAttachments.PREVENT_DEATH) != null;
    }

    public static void clearSkin(Player player) {
        setSkin(player,null);
    }
}
