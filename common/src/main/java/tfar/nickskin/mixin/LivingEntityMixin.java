package tfar.nickskin.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.nickskin.attachments.AttachmentHelper;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract void setHealth(float health);

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "checkTotemDeathProtection",at = @At(value = "RETURN",ordinal = 1),cancellable = true)
    private void onTotemUsed(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue())return;
        MinecraftServer server = getServer();
        if (server != null)  {
            boolean preventDeath = (Object)this instanceof ServerPlayer player && AttachmentHelper.getShouldPreventDeath(player);
            if (preventDeath) {
                setHealth(1);
                cir.setReturnValue(true);
            }
        }
    }
}
