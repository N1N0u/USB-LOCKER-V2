import java.io.FileWriter;
import java.io.IOException;

public class LockUnlock {
	
	public static void lockUsb(String name) {
		

	        try {
	            FileWriter writer = new FileWriter("lock"+name+".bat");
	            writer.write("(echo sel vol "+name+" & echo list vol & echo attr disk set readonly & echo detail disk) | diskpart\r\n"
	            		+ "echo.\r\n"
	            		+ "echo.\r\n"
	            		+ "if %ERRORLEVEL% == 0 (\r\n"
	            		+ "  echo SUCCESS! Drive "+name+" should now be READONLY.\r\n"
	            		+ ") else (\r\n"
	            		+ "  echo Failure setting "+name+" to READONLY.\r\n)");
	            writer.close();
	          //  System.out.println("File has been written successfully.");
	        } catch (IOException e) {
	            System.out.println("An error occurred.");
	            e.printStackTrace();
	        }
		
	}
	
	public static void UnlockUsb(String name) {
		 try {
	            FileWriter writer = new FileWriter("Unlock"+name+".bat");
	            writer.write("(echo sel vol "+name+" & echo list vol & echo attr disk clear readonly & echo detail disk) | diskpart\r\n"
	            		+ "echo.\r\n"
	            		+ "echo.\r\n"
	            		+ "if %ERRORLEVEL% == 0 (\r\n"
	            		+ "  echo SUCCESS! Drive "+name+" should now be READONLY.\r\n"
	            		+ ") else (\r\n"
	            		+ "  echo Failure setting "+name+" to READONLY.\r\n)");
	            writer.close();
	            System.out.println("File has been written successfully.");
	        } catch (IOException e) {
	            System.out.println("An error occurred.");
	            e.printStackTrace();
	        }
	}
		

}
