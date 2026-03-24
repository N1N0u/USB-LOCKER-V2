

import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

public class RemovableDrive {

    public static void CheckUsbDrives() {
        FileSystemView fsv = FileSystemView.getFileSystemView();

        File[] drives = File.listRoots();
        if (drives != null && drives.length > 0) {
            for (File aDrive : drives) {
                String driveType = fsv.getSystemTypeDescription(aDrive);
                boolean isRemovable = driveType.contains("Removable") || driveType.contains("USB");
                System.out.println("Drive Letter: " + aDrive);
                System.out.println("\tType: " + driveType);
                System.out.println("\tIs Removable: " + isRemovable);
                System.out.println();
                String aString=(aDrive.toString());
               
             if(isRemovable)
             {
            	 DefaultTableModel model=(DefaultTableModel) Main.table.getModel();
           		model.addRow(new Object[] {aString,"Protection is On"});
               aString=  aString.substring(0, aString.length() - 2);
            	// System.out.println("oooo");
            	 LockUnlock.lockUsb(aString);
            	 try {
     				Runtime.
     				   getRuntime().
     				   exec("powershell.exe Start-Process Lock"+aString+".bat -verb RunAs");
     	        } catch (IOException e) {
     	            e.printStackTrace();
     	        }
            	

            	 
             }
            }
        }
    }
}