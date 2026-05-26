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
    public NickSkinConfig(ModConfigSpec.Builder builder) {
        builder.push("general");
        nicknames = builder.defineList("random_nicknames",() -> createDefaultList(),o -> true);
        builder.pop();
    }

    static List<String> createDefaultList() {
        List<String> list = new ArrayList<>();
        list.add("Dream");
        return list;
    }

}
