package tfar.nickskin;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.storage.PlayerDataStorage;
import org.jetbrains.annotations.Nullable;
import tfar.nickskin.attachments.AttachmentHelper;
import tfar.nickskin.platform.Services;

import java.util.Collection;

public class NickSkinCommands {
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((Commands.literal("nick").executes(NickSkinCommands::resetOwnNickname))
                .then(Commands.argument("nickname", StringArgumentType.greedyString()).executes(NickSkinCommands::setNickname)));

        dispatcher.register((Commands.literal("nickskin").executes(NickSkinCommands::resetOwnSkin))
                .then(Commands.argument("skin", GameProfileArgument.gameProfile())
                        .executes(NickSkinCommands::setSkin)));

        dispatcher.register((Commands.literal("unnick")
                .executes(NickSkinCommands::resetOwnNickname)).then((Commands.literal("players")
                .requires((src) -> src.hasPermission(Commands.LEVEL_ADMINS)))
                .then(Commands.argument("players", GameProfileArgument.gameProfile())
                        .executes(NickSkinCommands::resetByGameProfile))));

    }

    private static int resetByGameProfile(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        Collection<GameProfile> gameProfiles = GameProfileArgument.getGameProfiles(context,"players");

        for (GameProfile gameProfile : gameProfiles) {
            ServerPlayer player = source.getServer().getPlayerList().getPlayer(gameProfile.getId());
            if (player != null) {
                AttachmentHelper.clearNickName(player);
            } else {
                MinecraftServer server = source.getServer();
                ServerLevel level = source.getLevel();
                ServerPlayer fakePlayer = Services.PLATFORM.getFakePlayer(level,gameProfile);

                PlayerDataStorage saveHandler = server.playerDataStorage;
                CompoundTag tag = saveHandler.load(fakePlayer).orElseThrow(EntityArgument.NO_PLAYERS_FOUND::create);
                AttachmentHelper.clearNickName(fakePlayer);
                saveHandler.save(fakePlayer);
            }
        }

        return 1;
    }

    private static int randomNicknameSelf(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayer();
        if (randomNickPlayer(player)) {
            return 1;
        } else {
            source.sendFailure(TextComponents.RANDOM_NICKNAME_FAILURE);
            return 0;
        }
    }

    private static int randomNickPlayers(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        int successCount = 0;

        for (ServerPlayer target : targets) {
            if (randomNickPlayer(target)) {
                ++successCount;
            }
        }

        if (successCount == 0) {
            source.sendFailure(TextComponents.RANDOM_NICKNAME_FAILURE);
            return 0;
        } else {

            source.sendSuccess(() -> TextComponents.RANDOM_NICKNAME_SUCCESS, true);
            return 1;
        }
    }

    private static boolean randomNickPlayer(ServerPlayer target) {
        int maxAttempts = 10;

        for (int i = 0; i < maxAttempts; ++i) {
            String randomNickname = NicknamePicker.getRandomNickname(target.server);
            NicknamePicker.@Nullable Problem problem = NicknamePicker.trySetNickname(target, randomNickname);
            if (problem == null) {
                target.sendSystemMessage(TextComponents.setRandomNickName(randomNickname), false);
                return true;
            }
        }

        return false;
    }

    private static int listNicknames(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        NickSkinData nickSkinData = NickSkin.nickSkinData;
        int nickedUsers = nickSkinData.getUsernamesToNicknames().size();
        MutableComponent feedback = TextComponents.listNicknamedUsers(nickSkinData.getUsernamesToNicknames().toString());

        source.sendSuccess(() -> feedback, false);
        return 1;
    }

    private static int resetOwnNickname(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        AttachmentHelper.clearNickName(player);
        source.sendSuccess(() -> TextComponents.CLEAR_NICKNAME, true);
        return 1;
    }

    private static int resetOwnSkin(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        AttachmentHelper.clearSkin(player);
        source.sendSuccess(() -> TextComponents.CLEAR_SKIN, true);
        return 1;
    }

    private static int setNickname(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        String nickname = StringArgumentType.getString(context, "nickname");
        if (nickname.equals(AttachmentHelper.getNickName(player))) {
            source.sendFailure(TextComponents.NICKNAME_ALREADY_SET);
            return 0;
        } else {
            NicknamePicker.@Nullable Problem problem = NicknamePicker.trySetNickname(player, nickname);
            if (problem == null) {
                MutableComponent success = TextComponents.setNickName(nickname);
                source.sendSuccess(() -> success, true);
                return 1;
            } else {
                source.sendFailure(problem.error.get());
                return 0;
            }
        }
    }

    private static int setSkin(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        Collection<GameProfile> skins = GameProfileArgument.getGameProfiles(context, "skin");
        if (skins.size()!= 1) {
            return 0;
        }
        GameProfile skin = skins.iterator().next();
        ResolvableProfile currentSkin = AttachmentHelper.getSkin(player);
        if (skin.equals(currentSkin)) {
            source.sendFailure(TextComponents.SKIN_ALREADY_SET);
            return 0;
        } else {
            NicknamePicker.@Nullable Problem problem = NicknamePicker.trySetSkin(player, skin);
            if (problem == null) {
                MutableComponent success = TextComponents.setSkin(skin);
                source.sendSuccess(() -> success, true);
                return 1;
            } else {
                source.sendFailure(problem.error.get());
                return 0;
            }
        }
    }
}
