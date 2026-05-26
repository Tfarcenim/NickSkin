package tfar.nickskin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;

public interface TextComponents {

    static MutableComponent setNickName(String nickname) {
        return Component.translatable("nickskin.commands.set_nickname.success",nickname);
    }
    MutableComponent CLEAR_NICKNAME = Component.translatable("nickskin.commands.clear_nickname.success");

    static MutableComponent listNicknamedUsers(String nickname) {
        return Component.translatable("nickskin.commands.list_nicknames.success",nickname);
    }

    MutableComponent NICKNAME_ALREADY_SET =Component.translatable("nickskin.commands.nickname_already_set.failure");

    static MutableComponent nicknameTooLong(int maxLength) {
        return Component.translatable("nickskin.commands.nickname_too_long.failure",maxLength);
    }

    MutableComponent NICKNAME_TOO_LONG = Component.translatable("nickskin.commands.nickname_too_long.failure");
    MutableComponent INVALID_NICKNAME = Component.translatable("nickskin.commands.invalid_nickname.failure");
    MutableComponent NICKNAME_ALREADY_TAKEN = Component.translatable("nickskin.commands.nickname_already_taken.failure");
    MutableComponent NICKNAME_MATCHES_PLAYER_NAME = Component.translatable("nickskin.commands.nickname_matches_player_name.failure");

    MutableComponent RANDOM_NICKNAME_FAILURE = Component.translatable("nickskin.command.random_nickname.failure");
    MutableComponent RANDOM_NICKNAME_SUCCESS = Component.translatable("nickskin.command.random_nickname.succsss");

    static MutableComponent setRandomNickName(String nickname){
        return Component.translatable("nickskin.commands.set_random_nickname.success",nickname);
    }

}
