package tfar.nickskin.datagen;

import com.mojang.authlib.GameProfile;
import net.minecraft.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.common.util.FakePlayer;
import tfar.nickskin.NickSkin;
import tfar.nickskin.TextComponents;

public class NickSkinLang extends LanguageProvider {
    public NickSkinLang(PackOutput output) {
        super(output, NickSkin.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addTranslatableComponent(TextComponents.setNickName(""),"Your nickname was set to \"%s\"");
        addTranslatableComponent(TextComponents.setSkin(new GameProfile(Util.NIL_UUID,"")),"Your skin was set to \"%s\"");
        addTranslatableComponent(TextComponents.setRandomNickName(""),"Your nickname was randomly set to \"%s\"");
        addTranslatableComponent(TextComponents.CLEAR_NICKNAME,"Your nickname was cleared");
        addTranslatableComponent(TextComponents.CLEAR_SKIN,"Your skin was cleared");
        addTranslatableComponent(TextComponents.listNicknamedUsers(""),"Nicknamed users: %s");
        addTranslatableComponent(TextComponents.NICKNAME_ALREADY_SET,"Nickname already set");
        addTranslatableComponent(TextComponents.SKIN_ALREADY_SET,"Skin already set");
        //addTranslatableComponent(TextComponents.nicknameTooLong(0),"Nickname too long: %s");
        addTranslatableComponent(TextComponents.NICKNAME_TOO_LONG,"Nickname too long");
        addTranslatableComponent(TextComponents.INVALID_NICKNAME,"Invalid nickname");
        addTranslatableComponent(TextComponents.NICKNAME_ALREADY_TAKEN,"Nickname already taken");
        addTranslatableComponent(TextComponents.NICKNAME_MATCHES_PLAYER_NAME,"Nickname matches player name");
        addTranslatableComponent(TextComponents.RANDOM_NICKNAME_FAILURE,"Couldn't set random nickname");
        addTranslatableComponent(TextComponents.RANDOM_NICKNAME_SUCCESS,"Successfully set random nickname");
    }

    protected void addTranslatableComponent(Component component, String value) {
        if (component.getContents() instanceof TranslatableContents translatableContents){
            add(translatableContents.getKey(), value);
        } else {
            throw new RuntimeException(component +" is not translatable");
        }
    }
}
