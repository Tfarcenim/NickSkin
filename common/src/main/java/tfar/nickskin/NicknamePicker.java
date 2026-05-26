package tfar.nickskin;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.component.ResolvableProfile;
import org.jetbrains.annotations.Nullable;
import tfar.nickskin.attachments.AttachmentHelper;

import java.util.List;
import java.util.function.Supplier;

public class NicknamePicker {

    public static @Nullable Problem trySetSkin(ServerPlayer player, GameProfile skin) {
        Problem problem = getSkinProblem(player.server, skin);
        if (problem != null) return problem;
        AttachmentHelper.setSkin(player, new ResolvableProfile(skin));
        return null;
    }

    public enum Problem {
        TOO_LONG(() -> TextComponents.NICKNAME_TOO_LONG),
        INVALID(() -> TextComponents.INVALID_NICKNAME),
        ALREADY_TAKEN(() -> TextComponents.NICKNAME_ALREADY_TAKEN),
        MATCHES_PLAYER(() -> TextComponents.NICKNAME_MATCHES_PLAYER_NAME);
        public final Supplier<Component> error;

        Problem(Supplier<Component> error) {
            this.error = error;
        }
    }

    @Nullable
    public static Problem getSkinProblem(MinecraftServer server, GameProfile nickname) {
        return null;
    }

    @Nullable
    public static Problem getNicknameProblem(MinecraftServer server, String nickname) {
        if (nickname.length() > server.getGameRules().getInt(NickSkinGameRules.MAX_NICKNAME_LENGTH)) {
            return Problem.TOO_LONG;
        } else if (false) {
            return Problem.INVALID;//implement filtering?
        } else if (!server.getGameRules().getBoolean(NickSkinGameRules.ALLOW_DUPLICATE_NICKNAMES) && nicknameTaken(nickname)) {
            return Problem.ALREADY_TAKEN;
        } else if (!server.getGameRules().getBoolean(NickSkinGameRules.ALLOW_PLAYER_NAMES_AS_NICKNAMES) && nicknameInPlayerNames(server, nickname)) {
            return Problem.MATCHES_PLAYER;
        } else {
            return null;
        }
    }


    private static boolean nicknameTaken(String nickname) {
        NickSkinData nickSkinData = NickSkin.nickSkinData;
        return nickSkinData.nicknameInUse(nickname);
    }

    private static boolean nicknameInPlayerNames(MinecraftServer server, String nickname) {
        NickSkinData nickSkinData = NickSkin.nickSkinData;
        if (nickSkinData.isPlayerName(nickname)) {
            return true;
        } else {
            for (ServerPlayer onlinePlayer : server.getPlayerList().getPlayers()) {
                if (onlinePlayer.getGameProfile().getName().equals(nickname)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static String getRandomNickname(MinecraftServer server) {
        List<? extends String> names = NickSkinConfig.CONFIG.nicknames.get();
        String name = names.get(server.overworld().random.nextInt(names.size()));
        return name;
    }

    public static String getRandomSkin(MinecraftServer server) {
        List<? extends String> names = NickSkinConfig.CONFIG.skins.get();
        String name = names.get(server.overworld().random.nextInt(names.size()));
        return name;
    }

    @Nullable
    public static Problem trySetNickname(ServerPlayer player, String nickname) {
        Problem availabilityCode = getNicknameProblem(player.server,nickname);
        if (availabilityCode != null) {
            return availabilityCode;
        } else {
            AttachmentHelper.setNickName(player, nickname);
            return null;
        }
    }
}
