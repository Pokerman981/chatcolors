package me.pokerman99.chatcolors;

import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
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
public class Main implements CommandExecutor {

	@Listener
	public void onPreInit(GamePreInitializationEvent event) {
		CommandSpec chatcolor = CommandSpec.builder()
				.arguments(GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.string(Text.of("colors")))))
				.permission("chatcolorsec.colors.base")
				.executor(this).build();
		Sponge.getCommandManager().register(this, chatcolor, "color");
		//Sponge.getEventManager().registerListeners(this, this);
	}

	@Listener
	public void onChat(SendChannelMessageEvent event) {
		boolean chatcolorbool = MetaData(UUID.fromString(event.getSender().getIdentifier())).getMeta().containsKey("chat-color");
		if (event.getChannel() == null) return;
		if (chatcolorbool == false) {
			event.addTag("{color}", color2(event.getChannel().getColor()));
			return;
		}
			
		if (chatcolorbool == true) {
			String chatbold = MetaData(UUID.fromString(event.getSender().getIdentifier())).getMeta().get("chat-bold");
			String chatcolor = MetaData(UUID.fromString(event.getSender().getIdentifier())).getMeta().get("chat-color");
			if (chatbold.equals("false")) {
			event.addTag("{color}", "&" + chatcolor);
			return;
			}
			if (chatbold.equals("true")) {
			event.addTag("{color}", "&" + chatcolor + "&l");
			}
			return;
		}
	}

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

	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!args.hasAny("colors")) {
			Sponge.getCommandManager().process(Sponge.getServer().getConsole(),
					"virtualchest open colors " + src.getName());
			return CommandResult.success();
		}
		if (args.hasAny("colors")) {
			String message = args.<String>getOne("colors").get();
				if (message.contains("&l") && !src.hasPermission("chatcolorsec.colors.bold")) {
					sendMessage(src, "&cYou don't have permission to set your chat color to bold!");
					return CommandResult.empty();
				}
				if (message.equals("&5") 
						|| message.equals("&2") 
						|| message.equals("&3") 
						|| message.equals("&6")
						|| message.equals("&9") 
						|| message.equals("&a") 
						|| message.equals("&b") 
						|| message.equals("&d")
						|| message.equals("&e") 
						|| message.equals("&5&l") 
						|| message.equals("&2&l") 
						|| message.equals("&3&l") 
						|| message.equals("&6&l")
						|| message.equals("&9&l") 
						|| message.equals("&a&l") 
						|| message.equals("&b&l") 
						|| message.equals("&d&l")
						|| message.equals("&e&l") ) {
					if (!message.contains("&l")) {
						String msg = message.replaceAll("&l", "");
						Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + src.getName()+ " meta set chat-color " + message.replace("&", ""));
						Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + src.getName()+ " meta set chat-bold " + false);
						sendMessage(src, "&aSuccessfully changed your chat color!");
						return CommandResult.success();
					} 
					if (message.contains("&l")) {
						String msg = message.replaceAll("&l", "");
						Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + src.getName()+ " meta set chat-color " + msg.replace("&", ""));
						Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + src.getName()+ " meta set chat-bold " + true);
						sendMessage(src, "&aSuccessfully changed your chat color!");
						return CommandResult.success();
					}
				} else {
					sendMessage(src, "&cInvalid Color!");
				}
				
			
		}

		return CommandResult.success();
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