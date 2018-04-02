package me.pokerman99.chatcolors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

public class ColorCommand implements CommandExecutor{

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!args.hasAny("colors")) {
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(),
                    "virtualchest open colors " + src.getName());
            return CommandResult.success();
        }
        if (args.hasAny("colors")) {
            String message = args.<String>getOne("colors").get();
            if (message.contains("&l") && !src.hasPermission("chatcolorsec.colors.bold")) {
                Utils.sendMessage(src, "&cYou don't have permission to set your chat color to bold!");
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
                    Utils.sendMessage(src, "&aSuccessfully changed your chat color!");
                    return CommandResult.success();
                }
                if (message.contains("&l")) {
                    String msg = message.replaceAll("&l", "");
                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + src.getName()+ " meta set chat-color " + msg.replace("&", ""));
                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + src.getName()+ " meta set chat-bold " + true);
                    Utils.sendMessage(src, "&aSuccessfully changed your chat color!");
                    return CommandResult.success();
                }
            } else {
                Utils.sendMessage(src, "&cInvalid Color!");
            }


        }

        return CommandResult.success();
    }

}
