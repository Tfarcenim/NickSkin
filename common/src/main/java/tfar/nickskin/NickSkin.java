package tfar.nickskin;

import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.nickskin.attachments.CommonDataAttachments;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class NickSkin {

    public static final String MOD_ID = "nickskin";
    public static final String MOD_NAME = "NickSkin";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {

    }

    public static void registration() {
        CommonDataAttachments.init();
        NickSkinGameRules.init();
    }

    public static NickSkinData nickSkinData;

    public static void serverStarted(MinecraftServer server) {
        nickSkinData = server.overworld().getDataStorage().computeIfAbsent(NickSkinData.factory(server.overworld()),MOD_ID);
    }


}