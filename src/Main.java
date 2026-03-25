import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.stream.events.StartDocument;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.k33ptoo.components.KButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Main extends JFrame {

    static JTable table;
    static  String exist;
    KButton br,bt;
    JTabbedPane tabs;
    private LockUnlock lu;
    
    public Main() {
        initialize();
        exist=checkUserExist();
        StartRecon(exist);
        
    }

    private void StartRecon(String exist) {
    	exist=exist;
		// TODO Auto-generated method stub
    	if(exist.equals("0"))
        {
        	bt.setEnabled(false);
        	JOptionPane.showMessageDialog(null, "Please start Recon First to save your face");
        	tabs.setEnabledAt(1, false);
        }
    	else
    	{
    		bt.setEnabled(true);
    		File folder = new File("trainer");

    		if (folder.exists() && folder.isDirectory()) {
    			tabs.setEnabledAt(1, true);
    		} else {
    		    System.out.println("Folder does NOT exist");
    		}
    		
    		
    	}
	}

	private String checkUserExist() {
		// TODO Auto-generated method stub
    	try (BufferedReader reader = new BufferedReader(new FileReader("check.txt"))) {
            return reader.readLine(); // returns null if file is empty
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
   

	private void initialize() {
        setTitle("USB-LOCKER V2.0");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        setResizable(false);

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel(new GridLayout(1, 1));
        topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel title = new JLabel("Usb Controller V2", SwingConstants.CENTER);
        title.setFont(new Font("Palatino Linotype", Font.BOLD | Font.ITALIC, 14));
        topPanel.add(title);

        getContentPane().add(topPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        table = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Usb Drive", "Status"}
        ));

        JScrollPane scrollPane = new JScrollPane(table);

        // ===== BUTTON PANEL =====
        JPanel btnPanel = new JPanel(new GridLayout(1, 2));

        JButton btnEnable = new JButton("Enable Protection");
        btnEnable.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		DefaultTableModel model=(DefaultTableModel) table.getModel();
        		int row=table.getSelectedRow();
        		
        		//get name
        		String name=(String) model.getValueAt(row, 0);
        		
        		//remove 2 last carcters
        		String result = name.substring(0, name.length() - 2);
      		
        			lu=new LockUnlock();
        			int exitCode=lu.lockUsb(result);
        			
        			if(exitCode!=0) JOptionPane.showMessageDialog(null, "please check lock function");
        		
        		
        	}
        });
        JButton btnDisable = new JButton("Disable Protection");
        btnDisable.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		
        		try {
        			ProcessBuilder pb = new ProcessBuilder(
            		        "python",
            		        "src/pyFiles/face_recognition.py"
            		);

            		pb.redirectErrorStream(true);

            		Process process = pb.start();

            		BufferedReader reader = new BufferedReader(
            		        new InputStreamReader(process.getInputStream()));

            		String line;

            		while ((line = reader.readLine()) != null) {
            		    System.out.println(line);

            		    if (line.contains("AUTHORIZED")) {
            		        System.out.println("Face recognized → Unlock USB");

                    		DefaultTableModel mod=(DefaultTableModel) table.getModel();
                    		int row=table.getSelectedRow();
                    		
                    		//get name
                    		String name=(String) mod.getValueAt(row, 0);
                    		
                    		//remove 2 last carcters
                    		String result = name.substring(0, name.length() - 2);
                    		
                    		lu=new LockUnlock();
                    		int exitCode=lu.UnlockUsb(result);
                    		
                    		if(exitCode!=0) JOptionPane.showMessageDialog(null, "please check unlock function");
                    		else mod.setValueAt("Protecion disabled", row, 1);
            		        break;
            		    }
            		}

					process.waitFor();
				} catch (InterruptedException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		

        		
        	}
        });

        btnPanel.add(btnEnable);
        btnPanel.add(btnDisable);

        // ===== USB TAB PANEL =====
        JPanel usbPanel = new JPanel(new BorderLayout());
        usbPanel.add(scrollPane, BorderLayout.CENTER);
        usbPanel.add(btnPanel, BorderLayout.SOUTH);

        // ===== TABS =====
        tabs = new JTabbedPane();
        
                // ===== Tab 3: Activation =====
                JPanel activationPanel = new JPanel();
                
                        tabs.addTab("Starting Position", activationPanel);
                        activationPanel.setLayout(new GridLayout(2, 0, 0, 0));
                        
                        br = new KButton();
                        br.addActionListener(new ActionListener() {
                        	public void actionPerformed(ActionEvent e) {

                        		 int exitCode=0;
                                 // Start process
                                
                                 try {
                                	    ProcessBuilder pb = new ProcessBuilder(
                                	            "python",
                                	            "src/pyFiles/face_dataset.py"
                                	    );

                                	    pb.redirectErrorStream(true); // merge error + output

                                	    Process process = pb.start();

                                	    BufferedReader reader = new BufferedReader(
                                	            new InputStreamReader(process.getInputStream()));

                                	    String line;
                                	    while ((line = reader.readLine()) != null) {
                                	        System.out.println(line);
                                	    }

                                	    
                                	    exitCode = process.waitFor();
                                	    System.out.println("Exited with code: " + exitCode);
                                	    
                                	    

                                	} catch (Exception e1) {
                                	    e1.printStackTrace();
                                	}
                                 
                                 if(exitCode==0)
                                 {
                                	 Path path = Paths.get("dataset");

                                	 try {
                                	     if (Files.exists(path) && Files.isDirectory(path) &&
                                	         Files.list(path).findAny().isPresent()) {
                                	    	 FileWriter writer;
                                	    	 writer = new FileWriter("check.txt", false);
    										 writer.write("1");
    	                                     writer.close();

    	                                     System.out.println("File updated successfully");
    	                                     bt.setEnabled(true);
    	                                     
    	                                     JOptionPane.showMessageDialog(null, "Run Tarining Now Pls !!");
                                	     } else {
                                	         System.out.println("Folder missing or empty");
                                	     }
                                	 } catch (IOException e1) {
                                	     e1.printStackTrace();
                                	 }
                                	 
							
                                    
                                 }
                              

                              
                        		
                        	}
                        });
                        br.kHoverForeGround = Color.WHITE;
                        br.kHoverEndColor = Color.WHITE;
                        br.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 30));
                        br.setText("Recon");
                        br.setkStartColor(Color.BLUE);
                        br.setkPressedColor(Color.BLUE);
                        br.setkHoverStartColor(Color.BLUE);
                        br.setkHoverEndColor(Color.BLUE);
                        br.setkEndColor(Color.BLUE);
                        activationPanel.add(br);
                        
                        bt = new KButton();
                        bt.addActionListener(new ActionListener() {
                        	public void actionPerformed(ActionEvent e) {
                        		 int exitCode=0;
                                 // Start process
                                
                                 try {
                                	 ProcessBuilder pb = new ProcessBuilder(
                                		        "python",
                                		        "src/pyFiles/training.py"
                                		);

                                		pb.redirectErrorStream(true);

                                		Process process = pb.start();

                                		BufferedReader reader = new BufferedReader(
                                		        new InputStreamReader(process.getInputStream()));

                                		String line;
                                		while ((line = reader.readLine()) != null) {
                                		    System.out.println("[PYTHON] " + line);
                                		}

                                		exitCode = process.waitFor();
                                		System.out.println("Exited with code: " + exitCode);
                                	    
                                	    

                                	} catch (Exception e1) {
                                	    e1.printStackTrace();
                                	}
                                 
                                 if(exitCode==0)
                                 {
                                	 tabs.setEnabledAt(1, true);
                                 }
                                 
                        	}
                        });
                        bt.setkHoverForeGround(Color.WHITE);
                        bt.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 30));
                        bt.setText("Train");
                        bt.setkStartColor(Color.ORANGE);
                        bt.setkPressedColor(Color.ORANGE);
                        bt.setkHoverStartColor(Color.ORANGE);
                        bt.setkHoverEndColor(Color.ORANGE);
                        bt.setkEndColor(Color.ORANGE);
                        activationPanel.add(bt);

        // Tab 1: USB Detection
        tabs.addTab("USB Detection", usbPanel);

        // ===== ADD TABS TO FRAME =====
        getContentPane().add(tabs, BorderLayout.CENTER);

        // ===== OPTIONAL: Start AutoDetect =====
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        AutoDetect autoDetect = new AutoDetect(model);
        autoDetect.detect();
       
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}