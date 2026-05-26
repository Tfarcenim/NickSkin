package tfar.nickskin.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.PlayerScoreEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerScoreEntry.class)
public class PlayerScoreEntryMixin {

    @ModifyReturnValue(
            method = {"ownerName()Lnet/minecraft/network/chat/Component;"},
            at = {@At("RETURN")}
    )
    private static Component modifyReturnValue(Component original) {
        return original;
    }
}
