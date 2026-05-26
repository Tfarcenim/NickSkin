package tfar.nickskin;

import net.minecraft.world.level.GameRules;

public class NickSkinGameRules {
    public static final GameRules.Key<GameRules.IntegerValue> MAX_NICKNAME_LENGTH = GameRules.register(
            "nickskin:max_nickname_length", GameRules.Category.PLAYER,
            GameRules.IntegerValue.create(16,1,64,(s,v)->{})
    );

    public static final GameRules.Key<GameRules.BooleanValue> ALLOW_DUPLICATE_NICKNAMES = GameRules.register(
            "nickskin:allow_duplicate_nicknames", GameRules.Category.PLAYER,
            GameRules.BooleanValue.create(false)
    );

    public static final GameRules.Key<GameRules.BooleanValue> ALLOW_PLAYER_NAMES_AS_NICKNAMES = GameRules.register(
            "nickskin:allow_player_names_as_nicknames", GameRules.Category.PLAYER,
            GameRules.BooleanValue.create(false)
    );

    public static void init(){

    }
}
