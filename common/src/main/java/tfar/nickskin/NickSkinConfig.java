package tfar.nickskin;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class NickSkinConfig {

    public static final ModConfigSpec SPEC;
    public static final NickSkinConfig CONFIG;

    static {
        ModConfigSpec.Builder configBuilder = new ModConfigSpec.Builder();
        CONFIG = new NickSkinConfig(configBuilder);
        SPEC = configBuilder.build();
    }

    public final ModConfigSpec.ConfigValue<List<? extends String>> nicknames;
   // public final ModConfigSpec.ConfigValue<List<? extends String>> skins;
    public NickSkinConfig(ModConfigSpec.Builder builder) {
        builder.push("general");
        nicknames = builder.defineList("random_nicknames",() -> createDefaultList(),o -> true);
      //  skins = builder.defineList("random_skins",() -> createDefaultList(),o -> true);
        builder.pop();
    }

    static List<String> createDefaultList() {
        List<String> list = new ArrayList<>();

        list.add("7exists");
        list.add("Apple");
        list.add("Baldi");
        list.add("Banana");
        list.add("Battery");
        list.add("bike");
        list.add("candidature");
        list.add("Chat");
        list.add("ChatGPT");
        list.add("Creeper");
        list.add("Craft");
        list.add("Cycle");
        list.add("Death");
        list.add("Dream");
        list.add("eXist");
        list.add("Game");
        list.add("Github");
        list.add("Friend");
        list.add("iPhone");
        list.add("Java");
        list.add("jeb_");
        list.add("KrissaNXD");
        list.add("Level");
        list.add("list");
        list.add("Mine");
        list.add("Minecraft");
        list.add("mixin");
        list.add("Nick");
        list.add("Notch");
        list.add("Option");
        list.add("Orange");
        list.add("paid");
        list.add("Piston");
        list.add("Player");
        list.add("power");
        list.add("Sand");
        list.add("Sent");
        list.add("Server");
        list.add("Shift");
        list.add("System");
        list.add("Technoblade");
        list.add("UnityForsaken");
        list.add("Villager");
        list.add("Void");
        list.add("Volume");
        return list;
    }

}
