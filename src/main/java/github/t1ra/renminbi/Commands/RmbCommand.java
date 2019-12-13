package github.t1ra.renminbi.Commands;

import java.io.File;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.server.MinecraftServer;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.actors.threadpool.Arrays;
import github.t1ra.renminbi.Globals;

public class RmbCommand extends CommandBase {
	
	@Override
	public String getCommandName() {
		return "rmb";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return StatCollector.translateToLocal("command.rmb.usage");
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}
	
	/* Errors */
	public static String usageError = StatCollector.translateToLocal("command.rmb.usageError");
	public static String noDirectoryProvidedError = StatCollector.translateToLocal("command.rmb.noDirectoryProvidedError");
	public static String directoryContainsSpacesError = StatCollector.translateToLocal("command.rmb.directoryContainsSpacesError");
	
	/* Notifications */
	public static String loggingToEnabled = StatCollector.translateToLocal("command.rmb.loggingToEnabled");
	public static String loggingToDisabled = StatCollector.translateToLocal("command.rmb.loggingToDisabled");
	public static String enabledLogging = StatCollector.translateToLocal("command.rmb.loggingEnabled");
	public static String disabledLogging = StatCollector.translateToLocal("command.rmb.loggingDisabled");
	
	/* Usage */
	public static String usage = StatCollector.translateToLocal("command.rmb.usage");
	public static String help = StatCollector.translateToLocal("command.rmb.help");
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		IChatComponent message = new ChatComponentText(null);
		
		if (args != null && args.length > 0) {
			switch(args[0]) {
			case "status":
				
				if (Globals.isLogging) {
					message = new ChatComponentText(String.format("%s %s", loggingToEnabled, Globals.logFileAbsolute));
				} else {
					message = new ChatComponentText(String.format("%s %s", loggingToDisabled, Globals.logFileAbsolute));
				}
				
				message.getChatStyle().setColor(EnumChatFormatting.GREEN);
			
				break;
			case "toggle":
				Globals.isLogging = !Globals.isLogging;
				
				if (Globals.isLogging) {
					message = new ChatComponentText(enabledLogging);
					message.getChatStyle().setColor(EnumChatFormatting.GREEN);
				} else {
					message = new ChatComponentText(disabledLogging);
					message.getChatStyle().setColor(EnumChatFormatting.RED);
				}
				
				break;
			case "help":
				/* Hacky - Minecraft doesn't support printing newlines, so I have to split
				 * and send them all as individual messages, finally printing the final line
				 * with the message call at the bottom of the switch.
				 */
				String lines[] = help.split("\\\\n+");
				
				for (int i = 0; i < lines.length - 1; i++) {
					sender.addChatMessage(new ChatComponentText(lines[i]));
				}
				
				message = new ChatComponentText(lines[lines.length - 1]);
				
				break;
			default:
				message = new ChatComponentText(usageError);
				message.getChatStyle().setColor(EnumChatFormatting.RED);
			}
		} else {
			message = new ChatComponentText(usageError);
			message.getChatStyle().setColor(EnumChatFormatting.RED);
		}
		sender.addChatMessage(message);
	}
}
