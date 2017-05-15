import javax.swing.*;

public class NetworkCalculator extends JFrame {

    JTabbedPane tabbedPane = new JTabbedPane();
    NetworkPanel networkPanel = new NetworkPanel();
    SubnetPanel subnetPanel = new SubnetPanel();
    HostPanel hostPanel = new HostPanel();

    public NetworkCalculator(String title) {

        tabbedPane.addTab("Netzwerke", networkPanel);
        tabbedPane.add("Subnetze", subnetPanel);
        tabbedPane.add("Hosts", hostPanel);

        setSize(400, 400);
        setLocationRelativeTo(null);
        setTitle(title);

        add(tabbedPane);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

}
