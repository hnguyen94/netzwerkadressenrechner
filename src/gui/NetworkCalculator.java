package gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.sun.jmx.remote.security.JMXPluggableAuthenticator;
import logic.Converter;
import org.json.simple.*;
import sun.nio.ch.Net;
import sun.swing.SwingAccessor;

public class NetworkCalculator extends JFrame {

    private JTabbedPane tabbedPane = new JTabbedPane();

    public NetworkCalculator(String title, JSONArray data) {

        for (int i = 0; i < data.size(); i++) {
            JSONObject networkObject = (JSONObject) data.get(i);
            String networkString = networkObject.get("id").toString();

            JSONArray subnets = (JSONArray) networkObject.get("subnets");
            for (int j = 0; j < subnets.size(); j++) {
                JSONObject subnetObject = (JSONObject) subnets.get(j);
                String subnetString = subnetObject.get("subnet").toString();

                SubnetPanel subnetPanel = new SubnetPanel(networkString, this, data);
                NetworkPanel.addEntryToArrayList(subnetPanel);

                HostPanel hostPanel = new HostPanel(networkString, subnetString, data);
                SubnetPanel.addEntryToArrayList(hostPanel);

            }
        }



        // Add Action Listener to the TabbedPane to make it possible to close Tabs via the X-Tab
        tabbedPane.addChangeListener(e -> {
            // If the Close Tab is clicked...
            if (tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()).equals("X")) {
                int currentIndex = tabbedPane.getSelectedIndex();
                tabbedPane.setSelectedIndex(0);
                tabbedPane.removeTabAt(currentIndex);
                tabbedPane.removeTabAt(currentIndex - 1);
                if (tabbedPane.getTitleAt(currentIndex - 2).equals("X")) {
                    tabbedPane.setSelectedIndex(currentIndex - 3);
                } else {
                    tabbedPane.setSelectedIndex(currentIndex - 2);
                }
            }
        });

        NetworkPanel networkPanel = new NetworkPanel(this, data);
        tabbedPane.addTab("Netzwerke", networkPanel);

        // Set default Windows specifications
        setSize(600, 600);
        setMinimumSize(new Dimension(600, 600));
        setLocationRelativeTo(null);
        setTitle(title);

        add(tabbedPane);

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    /**
     *
     * @param tabbedPane parent Tabs
     * @param title parent String title
     * @return index of the selected title in the tabbedpane
     */
    public int getTabIndexFromTitle(JTabbedPane tabbedPane, String title) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).equals(title)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Save all the data when closing the program
     * @param e parent WindowEvent
     */
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if(e.getID() == WindowEvent.WINDOW_CLOSING){
            JSONArray networks = new JSONArray();
            DefaultListModel networkList = NetworkPanel.getModel();
            ArrayList<SubnetPanel> subnetPanels = NetworkPanel.getSubnetPanels();
            ArrayList<HostPanel> hostPanels = SubnetPanel.getHostPanels();

            for (int i = 0; i < networkList.getSize(); i++) {
                JSONObject network = new JSONObject();
                String networkString = networkList.getElementAt(i).toString();
                network.put("id", networkString);
                JSONArray subnets = new JSONArray();
                for (SubnetPanel subnetPanel : subnetPanels) {
                    String networkTitle = subnetPanel.getNetworkTitle();
                    if (networkString.equals(networkTitle)) {
                        DefaultListModel subnetList = subnetPanel.getModel();
                        for (int j = 0; j < subnetList.getSize(); j++) {
                            String subnetString = subnetList.getElementAt(j).toString();

                            JSONArray hosts = new JSONArray();
                            for (HostPanel hostPanel: hostPanels) {
                                String subnetTitle = hostPanel.getSubnetTitle();

                                if (subnetString.equals(subnetTitle)) {
                                    DefaultListModel hostList = hostPanel.getHostModel();
                                    DefaultListModel noteList = hostPanel.getNotesModel();

                                    for (int k = 0; k < hostList.getSize(); k++) {
                                        String hostString = hostList.getElementAt(k).toString();
                                        String noteString = noteList.getElementAt(k).toString();

                                        JSONObject host = new JSONObject();
                                        host.put("host", hostString);
                                        host.put("note", noteString);
                                        hosts.add(host);
                                    }
                                }
                            }
                            JSONObject subnet = new JSONObject();
                            subnet.put("subnet", subnetString);
                            subnet.put("hosts", hosts);

                            subnets.add(subnet);
                        }
                    }
                }
                network.put("subnets", subnets);
                networks.add(network);

            }

            JSONObject resultObject = new JSONObject();
            resultObject.put("data", networks);

            // Write JSONObject to file
            try {
                FileWriter file = new FileWriter("data.json");
                file.write(resultObject.toJSONString());
                file.flush();
                file.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
        // Close the Window
        super.processWindowEvent(e);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public static void showMoreInformationAboutNetwork(String network) {
        String networkIDDecimal = network.split("/")[0];
        String broadcastDecimal = Converter.getBroadcastFromNetwork(network);
        String subnetmaskDecimal = Converter.getSubnetMaskFromNetwork(network);
        String networkIDBinary = Converter.convertIpToBinary(networkIDDecimal);
        String broadcastBinary = Converter.convertIpToBinary(broadcastDecimal);
        String subnetmaskBinary = Converter.convertIpToBinary(subnetmaskDecimal);
        String networdIDHex = Converter.convertIpToHex(networkIDDecimal);
        String broadcastHex = Converter.convertIpToHex(broadcastDecimal);
        String subnetmaskHex = Converter.convertIpToHex(subnetmaskDecimal);
        String amountOfIPAddresses = String.valueOf(Converter.getAmountOfIPsFromPrefix(Converter.getPrefixFromCompleteNetwork(network)));
        String amountOfHosts = String.valueOf(Integer.valueOf(amountOfIPAddresses) - 2);

        JDialog dialog = new JDialog();
        // Set Parameter
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.PAGE_AXIS));
        dialog.setTitle("Informationen zum Netz");
        dialog.setSize(550,500);
        dialog.setLocationRelativeTo(null);

        // Generate Content for Decimal
        JPanel decimalContent = new JPanel();
        decimalContent.setLayout(new GridLayout(0, 2));
        decimalContent.add(new JLabel("Netzwerk ID:"));
        decimalContent.add(new JLabel(networkIDDecimal));
        decimalContent.add(new JLabel("Subnetzmaske:"));
        decimalContent.add(new JLabel(subnetmaskDecimal));
        decimalContent.add(new JLabel("Broadcast:"));
        decimalContent.add(new JLabel(broadcastDecimal));

        // Generate Content for Binary
        JPanel binaryContent = new JPanel();
        binaryContent.setLayout(new GridLayout(0, 2));
        binaryContent.add(new JLabel("Netzwerk ID:"));
        binaryContent.add(new JLabel(networkIDBinary));
        binaryContent.add(new JLabel("Subnetzmaske:"));
        binaryContent.add(new JLabel(subnetmaskBinary));
        binaryContent.add(new JLabel("Broadcast:"));
        binaryContent.add(new JLabel(broadcastBinary));

        // Generate Content for Hex
        JPanel hexContent = new JPanel();
        hexContent.setLayout(new GridLayout(0, 2));
        hexContent.add(new JLabel("Netzwerk ID:"));
        hexContent.add(new JLabel(networdIDHex));
        hexContent.add(new JLabel("Subnetzmaske:"));
        hexContent.add(new JLabel(subnetmaskHex));
        hexContent.add(new JLabel("Broadcast:"));
        hexContent.add(new JLabel(broadcastHex));

        // Generate Content for extra Information
        JPanel extraContent = new JPanel();
        extraContent.setLayout(new GridLayout(0, 2));
        extraContent.add(new JLabel("Anzahl an Adressen:"));
        extraContent.add(new JLabel(amountOfIPAddresses));
        extraContent.add(new JLabel("Anzahl an Hosts:"));
        extraContent.add(new JLabel(amountOfHosts));

        // Title Panels
        JPanel decimalTitle = new JPanel();
        JLabel decimalTitleLabel = new JLabel("<html><u>Dezimal</u></html>");
        decimalTitle.add(decimalTitleLabel);
        JPanel binaryTitle = new JPanel();
        JLabel binaryTitleLabel = new JLabel("<html><u>Binär</u></html>");
        binaryTitle.add(binaryTitleLabel);
        JPanel hexTitle = new JPanel();
        JLabel hexTitleLable = new JLabel("<html><u>Hexadezimal</u></html>");
        hexTitle.add(hexTitleLable);
        JPanel extraTitle = new JPanel();
        JLabel extraTitleLabel = new JLabel("<html><u>Sonstiges</u></html>");
        extraTitle.add(extraTitleLabel);

        // Build the whole Content for Deciaml
        JPanel decimal = new JPanel();
        decimal.setLayout(new BorderLayout());
        decimal.add(decimalTitle, BorderLayout.PAGE_START);
        decimal.add(decimalContent, BorderLayout.CENTER);

        // Build the whole Content for Binary
        JPanel binary = new JPanel();
        binary.setLayout(new BorderLayout());
        binary.add(binaryTitle, BorderLayout.PAGE_START);
        binary.add(binaryContent, BorderLayout.CENTER);

        // Build the whole Content for Hex
        JPanel hex = new JPanel();
        hex.setLayout(new BorderLayout());
        hex.add(hexTitle, BorderLayout.PAGE_START);
        hex.add(hexContent, BorderLayout.CENTER);

        // Build the whole content for extra Information
        JPanel extra = new JPanel();
        extra.setLayout(new BorderLayout());
        extra.add(extraTitle, BorderLayout.PAGE_START);
        extra.add(extraContent, BorderLayout.CENTER);

        // Add Content to the dialog window
        dialog.add(decimal);
        dialog.add(binary);
        dialog.add(hex);
        dialog.add(extra);

        // Set Parameter
        dialog.setVisible(true);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public static void showMoreInformationAboutTheHostIP(String hostAsDecimal) {
        String hostAsBinary = Converter.convertIpToBinary(hostAsDecimal);
        String hostAsHex = Converter.convertIpToHex(hostAsDecimal);

        JDialog dialog = new JDialog();
        dialog.setSize(550, 160);
        dialog.setTitle("Informationen zum Host");
        dialog.setLocationRelativeTo(null);

        JPanel content = new JPanel();
        content.setLayout(new GridLayout(0, 2));
        content.add(new JLabel("Dezimal"));
        content.add(new JLabel(hostAsDecimal));
        content.add(new JLabel("Binär"));
        content.add(new JLabel(hostAsBinary));
        content.add(new JLabel("Hexadezimal"));
        content.add(new JLabel(hostAsHex));

        JPanel title = new JPanel();
        title.add(new JLabel("<html><u>IP Adresse</u></html>"));

        JPanel data = new JPanel();
        data.setLayout(new BorderLayout());
        data.add(title, BorderLayout.PAGE_START);
        data.add(content, BorderLayout.CENTER);

        dialog.add(data);

        dialog.setVisible(true);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

}
