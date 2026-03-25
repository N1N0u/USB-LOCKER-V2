import java.io.FileWriter;
import java.io.IOException;

public class LockUnlock {
	
	public static int lockUsb(String name) {
		
		int exitCode=0;
		try {
		    String fileName = "lock" + name + ".bat";

		    // ===== CREATE BAT =====
		    FileWriter writer = new FileWriter(fileName);
		    writer.write("(echo sel vol " + name + " & echo list vol & echo attr disk set readonly & echo detail disk) | diskpart\r\n"
		            + "echo.\r\n"
		            + "echo.\r\n"
		            + "if %ERRORLEVEL% == 0 (\r\n"
		            + "  echo SUCCESS! Drive " + name + " is now READONLY.\r\n"
		            + ") else (\r\n"
		            + "  echo Failure setting " + name + " to READONLY.\r\n"
		            + ")");
		    writer.close();

		    // ===== RUN BAT AS ADMIN =====
		    ProcessBuilder pb = new ProcessBuilder(
		            "powershell",
		            "-Command",
		            "Start-Process cmd -ArgumentList '/c " + fileName + "' -Verb runAs"
		    );

		    Process process = pb.start();
		    exitCode = process.waitFor();

		    System.out.println("Executed with code: " + exitCode);
		    return exitCode;

		} catch (Exception e) {
		    e.printStackTrace();
		}
		return exitCode;
		
	}
	
	public static int UnlockUsb(String name) {
		int exitCode=0;
		try {
		    String fileName = "Unlock" + name + ".bat";

		    FileWriter writer = new FileWriter(fileName);
		    writer.write("(echo sel vol " + name + " & echo list vol & echo attr disk clear readonly & echo detail disk) | diskpart\r\n"
		            + "echo.\r\n"
		            + "echo.\r\n"
		            + "if %ERRORLEVEL% == 0 (\r\n"
		            + "  echo SUCCESS! Drive " + name + " should now be READONLY.\r\n"
		            + ") else (\r\n"
		            + "  echo Failure setting " + name + " to READONLY.\r\n"
		            + ")");
		    writer.close();

		    System.out.println("BAT file created.");

		    // ===== RUN THE BAT FILE =====
		    ProcessBuilder pb = new ProcessBuilder(
		            "powershell",
		            "-Command",
		            "Start-Process cmd -ArgumentList '/c " + fileName + "' -Verb runAs"
		    );

		    Process process = pb.start();
		    exitCode = process.waitFor();
		    System.out.println("Executed with code: " + exitCode);
		    return exitCode;
		   

		} catch (Exception e) {
		    e.printStackTrace();
		}
		return exitCode;
	}
		

}
