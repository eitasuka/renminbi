package github.t1ra.renminbi;

import java.io.BufferedWriter;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Globals implements Cloneable {
	/* Constants for Forge */
	public static final String
		MOD_ID = "renminbi",
		MOD_NAME = "Renminbi",
		MOD_VERSION = "1.1.0",
		MOD_VERSIONS = "[1.8, 1.8.9]";
	
	public static String
	logFileAbsolute = null,
	logFile = null,
	logDirectory = null;
	public static boolean isLogging = true;
	
	public static final Logger logger = LogManager.getLogger(MOD_ID);
}
