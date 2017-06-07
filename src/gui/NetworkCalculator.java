package gui;

import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.*;

public class NetworkCalculator extends JFrame {

    private JTabbedPane tabbedPane = new JTabbedPane();

    public NetworkCalculator(String title, JSONArray data) {

        // -------------------------------------------------------------------------------------------------------------
        // Add Action Listener to the TabbedPane to make it possible to close Tabs via the X-Tab
        // -------------------------------------------------------------------------------------------------------------

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
        setLocationRelativeTo(null);
        setTitle(title);

        add(tabbedPane);

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    // Function to get the Tab Index from the Title
    public int getTabIndexFromTitle(JTabbedPane tabbedPane, String title) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).equals(title)) {
                return i;
            }
        }
        return 0;
    }

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



            try {
                FileWriter file = new FileWriter("data.json");
                file.write(resultObject.toJSONString());
                file.flush();
                file.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }


        }
        super.processWindowEvent(e);
    }

}
