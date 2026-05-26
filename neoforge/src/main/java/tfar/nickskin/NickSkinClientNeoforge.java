package tfar.nickskin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.TriState;
import tfar.nickskin.attachments.AttachmentHelper;
import tfar.nickskin.platform.Services;

@Mod(value = NickSkin.MOD_ID,dist = Dist.CLIENT)
public class NickSkinClientNeoforge {

    public NickSkinClientNeoforge(IEventBus bus) {
        NeoForge.EVENT_BUS.addListener(this::forceNameTag);
    }

    void forceNameTag(RenderNameTagEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player player) {
            String nickname = AttachmentHelper.getNickName(player);
            if (nickname != null) {
                event.setContent(Component.literal(nickname));
            }
            if (Services.PLATFORM.isDevelopmentEnvironment()) {
                event.setCanRender(TriState.TRUE);
            }
        }
    }
}
