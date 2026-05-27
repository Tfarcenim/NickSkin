package tfar.nickskin.platform.services;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tfar.nickskin.attachments.CommonDataAttachment;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    <T> void registerDataAttachment(CommonDataAttachment<T> attachment);

    @Nullable
    <T> T getAttachedValue(Object object, CommonDataAttachment<T> attachment);

    default <T> T getOrCreateAttachedValue(Entity entity, CommonDataAttachment<T> attachment) {
        T value = getAttachedValue(entity, attachment);
        if (value != null) {
            return value;
        }
        value = attachment.getDefaultValueSupplier().apply(entity);
        setAttachedValue(entity, attachment, value);
        return value;
    }

    <T> void setAttachedValue(Object object, CommonDataAttachment<T> attachment, @Nullable T value);

    void refreshDisplayName(Player player);
    void refreshTabNameList(Player player);

    ServerPlayer getFakePlayer(ServerLevel level, GameProfile gameProfile);
}