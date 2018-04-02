package me.pokerman99.chatcolors;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Logger;

import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.serializer.TextSerializers;

import br.net.fabiozumbi12.UltimateChat.Sponge.API.SendChannelMessageEvent;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;

@Plugin(id = "chatcolors", name = "ChatColors", version = "1.0", description = "Plugin for Justin's servers providing chat colors", dependencies = {
		@Dependency(id = "ultimatechat", optional = false),
		@Dependency(id = "luckperms", optional = false)})
public class Main {

    @Inject @DefaultConfig(sharedRoot = false)
    private Path defaultConfig;

    @Inject @DefaultConfig(sharedRoot = false)
    public ConfigurationLoader<CommentedConfigurationNode> loader;

    @Inject @org.spongepowered.api.config.ConfigDir(sharedRoot = false)
    private Path ConfigDir;

    public static CommentedConfigurationNode rootNode;

    @Inject
    private Logger logger;

    public Logger getLogger() {
        return logger;
    }

    public static Main instance;

    public static Main getInstance() {
        return instance;
    }

	@Listener
	public void onPreInit(GamePreInitializationEvent event) {
        try{rootNode = loader.load();
        rootNode.getNode("config-version").setValue(1.0);
        loader.save(rootNode);} catch (IOException e){}
		CommandSpec chatcolor = CommandSpec.builder()
				.arguments(GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.string(Text.of("colors")))))
				.permission("chatcolorsec.colors.base")
				.executor(new ColorCommand()).build();
		Sponge.getCommandManager().register(this, chatcolor, "color");
		//Sponge.getEventManager().registerListeners(this, this);
	}

    @Listener
    public void onJoin(ClientConnectionEvent.Join event){
        if (Main.rootNode.getNode(event.getTargetEntity().getIdentifier()).isVirtual()){
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user %player% meta unset chat-color".replace("%player%", event.getTargetEntity().getName()));
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user %player% meta unset chat-bold".replace("%player%", event.getTargetEntity().getName()));
            Main.rootNode.getNode(event.getTargetEntity().getIdentifier()).setValue(true);
            try{loader.save(rootNode);} catch (IOException e){}
        }
    }

	@Listener
	public void onChat(SendChannelMessageEvent event) {
		if (event.getChannel() == null) return;

		boolean chatcolorbool = Utils.MetaData(UUID.fromString(event.getSender().getIdentifier())).getMeta().containsKey("chat-color");

		if (chatcolorbool == true) {
			String chatbold = Utils.MetaData(UUID.fromString(event.getSender().getIdentifier())).getMeta().get("chat-bold");
			String chatcolor = Utils.MetaData(UUID.fromString(event.getSender().getIdentifier())).getMeta().get("chat-color");
			if (chatbold.equals("false")) {
				event.addTag("{color}", "&" + chatcolor);
				return;
			}
			if (chatbold.equals("true")) {
				event.addTag("{color}", "&" + chatcolor + "&l");
			}
			return;
		} else {
			event.addTag("{color}", Utils.color2(event.getChannel().getColor()));
			return;
		}
	}

}
