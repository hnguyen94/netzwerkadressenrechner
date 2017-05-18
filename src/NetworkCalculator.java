import javax.swing.*;

public class NetworkCalculator extends JFrame {

    private JTabbedPane tabbedPane = new JTabbedPane();

    private NetworkPanel networkPanel = new NetworkPanel(this);

    public NetworkCalculator(String title) {

        tabbedPane.addTab("Netzwerke", networkPanel);

        setSize(600, 600);
        setLocationRelativeTo(null);
        setTitle(title);

        add(tabbedPane);

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

}
