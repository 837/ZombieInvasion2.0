package ch.zombieInvasion.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class LOGGER {
	public static void LOG(String txt) {
		try {
			Date date = new Date();
			BufferedWriter writer = new BufferedWriter(new FileWriter("DebugLog.txt", true));
			writer.write(date + "  " + txt + "\n");
			writer.close();
		} catch (IOException e) {
			System.out.println("ERROR in LOGGER");
		}
	};

	static {
		LOG("\n\n");
	}

}
