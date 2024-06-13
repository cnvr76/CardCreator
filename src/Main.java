import com.formdev.flatlaf.themes.FlatMacLightLaf;
import views.MainWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatMacLightLaf());
                new MainWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
