package github.t1ra.renminbi;

import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.io.BufferedWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.StatCollector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

import github.t1ra.renminbi.Commands.RmbCommand;

@Mod(modid = Globals.MOD_ID, name = Globals.MOD_NAME, version = Globals.MOD_VERSION, acceptedMinecraftVersions = Globals.MOD_VERSIONS)
public class Renminbi {
	
	public static FileWriter logFileWriter = null;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					byte[] last_bytes = new byte[9];
					RandomAccessFile raf = new RandomAccessFile(new File(Globals.logFileAbsolute), "r");
					raf.seek(raf.length()-9);
					raf.read(last_bytes);
					if (!new String(last_bytes).equals("</server>")) {
						logFileWriter.write("</server>");
					}
					logFileWriter.write("</renminbi>");
					logFileWriter.flush();
					raf.close();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
		
		String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd.HH:mm:ss")); // 2020-01-01.10:10:10
		String fancyTime = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)); // 1 Jun 2020 01:01:01
		
		Globals.logDirectory = event.getModConfigurationDirectory().getParentFile().getAbsolutePath() + "/renminbi";
		Globals.logFile = currentTime + ".log";
		Globals.logFileAbsolute = Globals.logDirectory + "/" + Globals.logFile;
			
		new File(Globals.logDirectory).mkdirs();
		
		try {
			final File logFile = new File(Globals.logFileAbsolute);
			if (logFile.isFile()) {
				Globals.logger.warn(String.format("Log file %s already exists?! Deleting.", Globals.logFileAbsolute));
				logFile.delete();
			} else {
				logFile.createNewFile();
			}
			logFileWriter = new FileWriter(Globals.logFileAbsolute);
			logFileWriter.write(String.format("<renminbi version=\"%s\" created=\"%s\">", Globals.MOD_VERSION, fancyTime));
		} catch (Throwable e) {
			Globals.logger.error(String.format("An error occured creating the log file %s, probably bad permissions. "
					+ "Giving up.\nError was: %s", Globals.logFileAbsolute, e));
		}
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new RmbCommand());
	}

	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent event) {
		String content = event.message.getUnformattedText();
		if (Globals.isLogging) {
			if (content.contentEquals(StatCollector.translateToLocal("command.rmb.loggingEnabled"))) {
				return;
			}
			try {
				String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)); // 1 Jun 2020 01:01:01
				logFileWriter.write(String.format("<msg time=\"%s\">%s</msg>\n", currentTime, content.replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;")));
				logFileWriter.flush();
			} catch (Throwable e) {
				Globals.logger.error(String.format("An error occured while trying to write a log to %s. Giving up.\n"
						+ "Error was: %s", Globals.logFileAbsolute, e));
			}
		}
	}
	
	@SubscribeEvent
    public void onPlayerJoinedServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
	Minecraft.getMinecraft().addScheduledTask(new Runnable() {
	    @Override
	    public void run() {
	    	ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
	    	try {
	    		logFileWriter.write(String.format("<server address=\"%s\">", event.isLocal ? "Local" : serverData.serverIP));
	    		logFileWriter.flush();
	    	} catch (Throwable e) {
	    		Globals.logger.error(String.format("An error curred while trying to write a log to %s. Giving up.\n"
	    				+ "Error was: %s", Globals.logFileAbsolute, e));
	    	}
	    }
	});
    }
	
	@SubscribeEvent
    public void onPlayerLeftServer(ClientDisconnectionFromServerEvent event) {
		Minecraft.getMinecraft().addScheduledTask(new Runnable() {
		    @Override
		    public void run() {
		    	try {
		    		logFileWriter.write("</server>");
		    		logFileWriter.flush();
		    	} catch (Throwable e) {
		    		Globals.logger.error(String.format("An error curred while trying to write a log to %s. Giving up.\n"
		    				+ "Error was: %s", Globals.logFileAbsolute, e));
		    	}
		    }
		});
    }
}