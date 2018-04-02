package me.pokerman99.chatcolors;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.UUID;

public class Utils {
    public static MetaData MetaData(UUID uuid) {
        LuckPermsApi lpapi = LuckPerms.getApi();
        User user = lpapi.getUserSafe(uuid).orElse(null);
        if (user == null) {
            return null;
        }
        Contexts contexts = lpapi.getContextForUser(user).orElse(null);
        if (contexts == null) {
            return null;
        }
        return user.getCachedData().getMetaData(contexts);
    }

    public static String color2(String string) {
        return TextSerializers.FORMATTING_CODE.serialize(TextSerializers.FORMATTING_CODE.deserialize(string));
    }

    public static String color(String string) {
        return TextSerializers.FORMATTING_CODE.serialize(Text.of(string));
    }

    public static void sendMessage(CommandSource sender, String message) {
        if (sender == null) { return; }
        sender.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(color(message)));
    }
}
