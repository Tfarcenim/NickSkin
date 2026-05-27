package tfar.nickskin.attachments;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.ResolvableProfile;
import tfar.nickskin.platform.Services;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommonDataAttachments {

    private static final Map<ResourceLocation,CommonDataAttachment<?>> MAP =new HashMap<>();


    public static final CommonDataAttachment<String> NICKNAME =
            register(CommonDataAttachment.create(o -> (String)null)
                    .codec(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8)
                    .copyOnDeath()
                    .build("nickname"));

    public static final CommonDataAttachment<ResolvableProfile> SKIN = register(CommonDataAttachment.create(o -> (ResolvableProfile)null)
            .codec(ResolvableProfile.CODEC)
            .networkSynchronized(ResolvableProfile.STREAM_CODEC)
            .copyOnDeath()
            .build("skin")
    );

    public static final CommonDataAttachment<Boolean> PREVENT_DEATH = register(CommonDataAttachment.create(o -> false)
            .codec(Codec.BOOL)
            .networkSynchronized(ByteBufCodecs.BOOL)
            .copyOnDeath()
            .build("prevent_death")
    );

    public static CommonDataAttachment<?> lookup(ResourceLocation location) {
        return MAP.get(location);
    }

    static <T> CommonDataAttachment<T> register(CommonDataAttachment<T> type) {
        Services.PLATFORM.registerDataAttachment(type);
        Objects.requireNonNull(type.getAttachment());
        MAP.put(type.name,type);
        return type;
    }

    public static void init() {

    }
}