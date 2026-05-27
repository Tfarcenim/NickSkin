package tfar.nickskin.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.ResolvableProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.nickskin.NickSkinClient;
import tfar.nickskin.attachments.AttachmentHelper;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {


    @Inject(method = "getTextureLocation(Lnet/minecraft/client/player/AbstractClientPlayer;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    private void handleDisguise(AbstractClientPlayer player, CallbackInfoReturnable<ResourceLocation> cir) {
        ResolvableProfile skin = AttachmentHelper.getSkin(player);
        if (skin != null) {
            cir.setReturnValue(NickSkinClient.getPlayerSkin(skin.gameProfile()));
        }
    }

    @Unique
    PlayerModel<AbstractClientPlayer> original;

    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD"))
    private void preRender(AbstractClientPlayer entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {

        ResolvableProfile skin = AttachmentHelper.getSkin(entity);

        if (skin != null) {
            PlayerSkin.Model modelInfo = NickSkinClient.getModelInfo(skin.gameProfile());
            original = model;
            PlayerModel<?> newPlayerModel = ((PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().playerRenderers.get(modelInfo)).getModel();
            model = (PlayerModel<AbstractClientPlayer>) newPlayerModel;
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
    at = @At("RETURN"))
    private void postRender(AbstractClientPlayer entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        ResolvableProfile skin = AttachmentHelper.getSkin(entity);
        if (skin != null) {
            model = original;
        }
    }

    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

}
