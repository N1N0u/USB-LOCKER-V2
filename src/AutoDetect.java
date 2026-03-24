import java.io.File;
import java.io.IOException;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class AutoDetect {

    private DefaultTableModel model;

    public AutoDetect(DefaultTableModel model) {
        this.model = model;
    }

    public void detect() {

        Thread thread = new Thread(() -> {
            File[] roots = File.listRoots();

            while (true) {
                File[] newRoots = File.listRoots();

                // ===== NEW DRIVE DETECTED =====
                if (roots.length < newRoots.length) {
                    for (File root : newRoots) {
                        if (!contains(roots, root)) {

                            String path = root.getAbsolutePath();

                            // UI update must be on EDT
                            SwingUtilities.invokeLater(() -> {
                                model.addRow(new Object[]{path, "Protection is On"});
                            });

                            String drive = path.substring(0, path.length() - 2);

                            LockUnlock.lockUsb(drive);

                            try {
                                Runtime.getRuntime().exec(
                                    "powershell.exe Start-Process Lock" + drive + ".bat -verb RunAs"
                                );
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    roots = newRoots;
                }

                // ===== DRIVE REMOVED =====
                else if (roots.length > newRoots.length) {
                    for (File root : roots) {
                        if (!contains(newRoots, root)) {

                            String path = root.getAbsolutePath();

                            SwingUtilities.invokeLater(() -> {
                                for (int i = 0; i < model.getRowCount(); i++) {
                                    if (model.getValueAt(i, 0).equals(path)) {
                                        model.removeRow(i);
                                        break;
                                    }
                                }
                            });

                            System.out.println("Drive removed: " + path);
                        }
                    }
                    roots = newRoots;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private boolean contains(File[] roots, File root) {
        for (File r : roots) {
            if (r.equals(root)) {
                return true;
            }
        }
        return false;
    }
}