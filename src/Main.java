import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.stream.events.StartDocument;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.k33ptoo.components.KButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Main extends JFrame {

    static JTable table;
    static  String exist;
    KButton br,bt;
    JTabbedPane tabs;
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
        JButton btnDisable = new JButton("Disable Protection");

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