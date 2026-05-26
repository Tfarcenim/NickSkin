package tfar.nickskin;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class NickSkinData extends SavedData {

    private final Map<String,String> usernamesToNicknames = new HashMap<>();

    public static SavedData.Factory<NickSkinData> factory(ServerLevel level) {
        return new SavedData.Factory<>(() -> new NickSkinData(), (p_294039_, p_324123_) -> load(level, p_294039_),
                null);
    }

    public boolean nicknameInUse(String nickname) {
        return this.usernamesToNicknames.containsValue(nickname);
    }

    public boolean isPlayerName(String nickname) {
        return this.usernamesToNicknames.containsKey(nickname);
    }

    public Map<String, String> getUsernamesToNicknames() {
        return usernamesToNicknames;
    }

    public static NickSkinData load(ServerLevel level, CompoundTag tag) {
        NickSkinData nickSkinData = new NickSkinData();
        CompoundTag nicknames = tag.getCompound("nicknames");
        for (String key : nicknames.getAllKeys()) {
            nickSkinData.usernamesToNicknames.put(key, nicknames.getString(key));
        }
        return nickSkinData;
    }


    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag compoundTag = new CompoundTag();
        for (Map.Entry<String, String> entry : this.usernamesToNicknames.entrySet()) {
            compoundTag.putString(entry.getKey(),entry.getValue());
        }

        tag.put("nicknames",compoundTag);
        return tag;
    }

    public void update(Player player, @Nullable String nickname) {

        if (nickname == null) {
            usernamesToNicknames.remove(player.getGameProfile().getName());
        } else {
            usernamesToNicknames.put(player.getGameProfile().getName(), nickname);
        }

        setDirty();
    }
}
