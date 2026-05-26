package tfar.nickskin;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import tfar.nickskin.attachments.AttachmentHelper;
import tfar.nickskin.datagen.NickSkinDatagen;

@Mod(NickSkin.MOD_ID)
public class NickSkinNeoforge {

    public NickSkinNeoforge(IEventBus eventBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.SERVER,NickSkinConfig.SPEC);
        eventBus.addListener(this::register);
        eventBus.addListener(NickSkinDatagen::gather);
        NeoForge.EVENT_BUS.addListener(this::commands);
        NeoForge.EVENT_BUS.addListener(this::serverStarted);
        NeoForge.EVENT_BUS.addListener(this::tabList);
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        NickSkin.init();

    }

    void tabList(PlayerEvent.TabListNameFormat event) {
        Player player = event.getEntity();
        String nickname = AttachmentHelper.getNickName(player);
        if (nickname != null) {
            event.setDisplayName(Component.literal(nickname));
        }
    }

    void serverStarted(ServerStartedEvent event) {
        NickSkin.serverStarted(event.getServer());
    }

    void commands(RegisterCommandsEvent event) {
        NickSkinCommands.registerCommands(event.getDispatcher());
    }

    void register(RegisterEvent event) {
        if (event.getRegistry() == BuiltInRegistries.BLOCK) {
            NickSkin.registration();
        }
    }
}