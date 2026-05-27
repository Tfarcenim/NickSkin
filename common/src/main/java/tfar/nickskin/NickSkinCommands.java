package tfar.nickskin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
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
import java.util.Optional;

public class NickSkinCommands {
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("nick")
                .then(Commands.argument("nickname", StringArgumentType.greedyString())
                        .executes(NickSkinCommands::setOwnNickname)
                        .then(Commands.literal("set")
                                .requires(cs -> cs.hasPermission(Commands.LEVEL_ADMINS))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                                .executes(NickSkinCommands::setOtherNickname)
                                        )
                                )
                        )
                )
        );

        dispatcher.register(Commands.literal("nickskin")
                .then(Commands.argument("skin", StringArgumentType.string())
                        .executes(NickSkinCommands::setOwnSkin))
                .then(Commands.literal("set")
                        .requires(cs -> cs.hasPermission(Commands.LEVEL_ADMINS))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("skin", StringArgumentType.string())
                                        .executes(NickSkinCommands::setOtherSkin)
                                )
                        )
                )
        );

        dispatcher.register(Commands.literal("unnick")
                .executes(NickSkinCommands::resetOwnNickname).then(Commands.literal("remove")
                        .requires(src -> src.hasPermission(Commands.LEVEL_ADMINS))
                        .then(Commands.argument("players", GameProfileArgument.gameProfile())
                                .executes(NickSkinCommands::resetNicknameByGameProfile))));

        dispatcher.register(Commands.literal("unnickskin")
                .executes(NickSkinCommands::resetOwnSkin).then(Commands.literal("remove")
                        .requires(src -> src.hasPermission(Commands.LEVEL_ADMINS))
                        .then(Commands.argument("players", GameProfileArgument.gameProfile())
                                .executes(NickSkinCommands::resetSkinByGameProfile))));

        dispatcher.register(Commands.literal("nickrandom").executes(NickSkinCommands::randomNicknameSelf)
                .then(Commands.literal("set")
                        .requires(cs -> cs.hasPermission(Commands.LEVEL_ADMINS))
                        .then(Commands.argument("players", EntityArgument.players())
                                .executes(NickSkinCommands::randomNickPlayers)
                        )
                )
        );
        dispatcher.register(Commands.literal("prevent_death")
                .requires(cs -> cs.hasPermission(Commands.LEVEL_ADMINS))
                .then(Commands.argument("prevent_death", BoolArgumentType.bool())
                        .then(Commands.argument("players", EntityArgument.players())
                                .executes(NickSkinCommands::preventDeath)
                        )
                )
        );
    }

    private static int preventDeath(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        boolean preventDeath = BoolArgumentType.getBool(ctx, "prevent_death");
        Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx,"players");
        for (ServerPlayer player : players) {
            AttachmentHelper.setShouldPreventDeath(player, preventDeath);
        }
        return 0;
    }

    private static int resetNicknameByGameProfile(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        Collection<GameProfile> gameProfiles = GameProfileArgument.getGameProfiles(context, "players");

        for (GameProfile gameProfile : gameProfiles) {
            ServerPlayer player = source.getServer().getPlayerList().getPlayer(gameProfile.getId());
            if (player != null) {
                AttachmentHelper.clearNickName(player);
            } else {
                MinecraftServer server = source.getServer();
                ServerLevel level = source.getLevel();
                ServerPlayer fakePlayer = Services.PLATFORM.getFakePlayer(level, gameProfile);

                PlayerDataStorage saveHandler = server.playerDataStorage;
                CompoundTag tag = saveHandler.load(fakePlayer).orElseThrow(EntityArgument.NO_PLAYERS_FOUND::create);
                AttachmentHelper.clearNickName(fakePlayer);
                saveHandler.save(fakePlayer);
            }
        }

        return 1;
    }


    private static int resetSkinByGameProfile(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        Collection<GameProfile> gameProfiles = GameProfileArgument.getGameProfiles(context, "players");

        for (GameProfile gameProfile : gameProfiles) {
            ServerPlayer player = source.getServer().getPlayerList().getPlayer(gameProfile.getId());
            if (player != null) {
                AttachmentHelper.clearSkin(player);
            } else {
                MinecraftServer server = source.getServer();
                ServerLevel level = source.getLevel();
                ServerPlayer fakePlayer = Services.PLATFORM.getFakePlayer(level, gameProfile);

                PlayerDataStorage saveHandler = server.playerDataStorage;
                CompoundTag tag = saveHandler.load(fakePlayer).orElseThrow(EntityArgument.NO_PLAYERS_FOUND::create);
                AttachmentHelper.clearSkin(fakePlayer);
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
            NicknamePicker.@Nullable Problem problem = NicknamePicker.trySetNickname(target, randomNickname, false);
            if (problem == null) {
                target.sendSystemMessage(TextComponents.setRandomNickName(randomNickname), false);
                AttachmentHelper.setSkin(target, new ResolvableProfile(Optional.of(randomNickname), Optional.empty(), new PropertyMap()));
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

    private static int setOwnNickname(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        String nickname = StringArgumentType.getString(context, "nickname");

        return setNickName(source, player, nickname, false);
    }

    private static int setNickName(CommandSourceStack source, ServerPlayer target, String nickname, boolean fromAdmin) {
        if (nickname.equals(AttachmentHelper.getNickName(target))) {
            source.sendFailure(TextComponents.NICKNAME_ALREADY_SET);
            return 0;
        } else {
            NicknamePicker.@Nullable Problem problem = NicknamePicker.trySetNickname(target, nickname, fromAdmin);
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

    private static int setOtherNickname(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        String nickname = StringArgumentType.getString(context, "nickname");
        return setNickName(source, player, nickname, true);
    }

    private static int setOwnSkin(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        String skin = StringArgumentType.getString(context, "skin");
        return setSkin(source, player, skin, false);
    }

    private static int setSkin(CommandSourceStack source, ServerPlayer player, String skin, boolean fromAdmin) {
        ResolvableProfile currentSkin = AttachmentHelper.getSkin(player);
        if (currentSkin.name().isPresent() && currentSkin.name().get().equals(skin)) {
            source.sendFailure(TextComponents.SKIN_ALREADY_SET);
            return 0;
        } else {
            NicknamePicker.@Nullable Problem problem = NicknamePicker.trySetSkin(player, skin, fromAdmin);
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

    private static int setOtherSkin(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        String skin = StringArgumentType.getString(context, "skin");
        return setSkin(source, player, skin, true);
    }
}
