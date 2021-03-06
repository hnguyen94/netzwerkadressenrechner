package gui;

import logic.Converter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Objects;

public class SubnetPanel extends JPanel {

    private JTabbedPane tabbedPane;
    private String networkTitle;
    private DefaultListModel<String> model = new DefaultListModel<>();
    private static ArrayList<HostPanel> hostPanels = new ArrayList<>();
    private JSONArray data;
    private JLabel freeAddressesLabel = new JLabel("Freie Adressen: XXX");


    public SubnetPanel(String network, NetworkCalculator networkCalculator, JSONArray data) {
        this.data = data;

        // Get Data an write to listModel
        for (int i = 0; i < data.size(); i++) {
            JSONObject networkObject = (JSONObject) data.get(i);

            if (networkObject.get("id").toString().equals(network)) {
                JSONArray subnets = (JSONArray) networkObject.get("subnets");
                for (int j = 0; j < subnets.size(); j++) {
                    JSONObject subnetObject = (JSONObject) subnets.get(j);
                    model.addElement(subnetObject.get("subnet").toString());
                }
            }
        }

        // Get Data from the old models
        ArrayList<SubnetPanel> allSubnetPanels = NetworkPanel.getSubnetPanels();
        for (int i = 0; i < allSubnetPanels.size(); i++) {
            if (allSubnetPanels.get(i).getNetworkTitle().equals(network)) {
                model = allSubnetPanels.get(i).getModel();
                NetworkPanel.removeEntryFromArrayList(i);
            }
        }

        // Set networkTitle
        networkTitle = network;

        // set the Subnet-Panel Layout to BorderLayout
        this.setLayout(new BorderLayout());
        // Initialise TabbedPane
        tabbedPane = networkCalculator.getTabbedPane();

        /*
         Create Panels and give them specific Layout
         Create Interaction Panel for Open-, Delete-Button, IP-Numberfields and Create Button
        */
        JPanel interactionPanel = new JPanel();
        interactionPanel.setLayout(new BoxLayout(interactionPanel, BoxLayout.PAGE_AXIS));

        // Open and Delete Button Panel
        JPanel openDeleteButtonPanel = new JPanel();
        openDeleteButtonPanel.setLayout(new FlowLayout());

        // Numberfields Panel
        JPanel numberFieldsPanel = new JPanel();
        numberFieldsPanel.setLayout(new FlowLayout());

        /*
         Create JList with ListModel
         JList for all Networks
        */
        JScrollPane scrollPane = new JScrollPane();
        JList<String> subnetList =  new JList<>(model);
        scrollPane.setViewportView(subnetList);

        //  Add SubnetList Mouse Listener to open new Subnets in Tabs via double click
        subnetList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openNewHostPanel(subnetList, networkCalculator);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        /*
         Create Open Button and Delete Button and add Action Listeners to open the from the list selected Network
         in a new Tab or to delete the selected Network from the list
         Open Selected Network Button
        */
        JButton openSubnetPanelButton = new JButton();
        openSubnetPanelButton.setText("Öffnen");
        openSubnetPanelButton.addActionListener(e -> {
            openNewHostPanel(subnetList, networkCalculator);
        });

        // Delete Selected Network Button
        JButton deleteSubnetPanelButton = new JButton();
        deleteSubnetPanelButton.setText("Löschen");
        deleteSubnetPanelButton.addActionListener(e -> {
            String currentSelectedTitle = subnetList.getSelectedValue();
            String title = "Hosts: " + currentSelectedTitle;
            int selectedTabIndex = networkCalculator.getTabIndexFromTitle(tabbedPane, title);
            if (selectedTabIndex != 0) {
                tabbedPane.remove(selectedTabIndex);
                tabbedPane.remove(selectedTabIndex);
            }
            model.removeElement(currentSelectedTitle);
            updateFreeAddressesLabel();
        });

        // Show more Information from selected Subnet
        JButton showData = new JButton("Informationen");
        showData.addActionListener(e -> {
            if (subnetList.getSelectedValue() != null) {
                NetworkCalculator.showMoreInformationAboutNetwork(subnetList.getSelectedValue());
            }


        });

        // Create InputFields and set Preferred Size
        JTextField amountOfHostsTextField = new JTextField();
        amountOfHostsTextField.setPreferredSize(new Dimension(40, 20));

        // Create createNewSubnetButton
        JButton createNewSubnetButton = new JButton();
        createNewSubnetButton.setText("Anlegen");
        createNewSubnetButton.addActionListener(e -> {
            if (!Objects.equals(amountOfHostsTextField.getText(), "")) {
                int minAmountOfHosts = Integer.valueOf(amountOfHostsTextField.getText());
                int prefixAccordingToTheHosts = Converter.getPrefixFromAmountOfHosts(minAmountOfHosts);

                // if model is not empty
                if (model.getSize() != 0) {
                    boolean checker = false;
                    String[] allCurrentSubnets = new String[model.getSize()];
                    for (int i = 0; i < model.getSize(); i++) {
                        allCurrentSubnets[i] = model.getElementAt(i);
                    }
                    String[] allPossibleNewNetworks = new String[allCurrentSubnets.length];
                    for (int i = 0; i < allCurrentSubnets.length; i++) {
                        allPossibleNewNetworks[i] = Converter.getNewFreeIPAfterNetwork(allCurrentSubnets[i]);
                    }

                    for (int i = 0; i < allPossibleNewNetworks.length; i++) {
                        String possibleNewNetwork = allPossibleNewNetworks[i] + "/" + prefixAccordingToTheHosts;
                        if (!checker && Converter.checkIfPossibleNewNetwork(allCurrentSubnets, possibleNewNetwork)) {
                            if (Converter.ipToLong(Converter.getBroadcastFromNetwork(network)) >= Converter.ipToLong(Converter.getBroadcastFromNetwork(possibleNewNetwork))) {

                                model.addElement(possibleNewNetwork);
                                // sort the elements from the model
                                String[] sortedElements = Converter.sortNetworksInModel(model);
                                model.removeAllElements();
                                for (String sortedElement : sortedElements) {
                                    model.addElement(sortedElement);
                                }
                                checker = true;
                            }
                        }
                    }

                    if (!checker) {
                        long lastPossibleNetwork = Converter.ipToLong(model.getElementAt(model.size() - 1));
                        long broadCastFromNetworkAsLong = Converter.ipToLong(Converter.getBroadcastFromNetwork(network));

                        for (int i = (int) lastPossibleNetwork; i < broadCastFromNetworkAsLong; i++) {
                            String possibleNewNetwork = Converter.longToIP(i) + "/" + prefixAccordingToTheHosts;
                            if (Converter.checkIfPossibleNewNetwork(allCurrentSubnets, possibleNewNetwork)) {
                                if (Converter.ipToLong(Converter.getBroadcastFromNetwork(network)) >= Converter.ipToLong(Converter.getBroadcastFromNetwork(possibleNewNetwork))) {

                                    model.addElement(possibleNewNetwork);
                                    // sort the elements from the model
                                    String[] sortedElements = Converter.sortNetworksInModel(model);
                                    model.removeAllElements();
                                    for (String sortedElement : sortedElements) {
                                        model.addElement(sortedElement);
                                    }
                                    checker = true;
                                    break;
                                }
                            }
                        }
                    }

                    // if not possible to generate new subnet network
                    if (!checker) {
                        JOptionPane.showMessageDialog(null, "Subnetz kann nicht angelegt werden!",
                                "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                    }

                } else { // if model is empty
                    String newNetworkBuilder = network.split("/")[0] + "/" + prefixAccordingToTheHosts;
                    model.addElement(newNetworkBuilder);
                }

                // Set TextField Content to Default
                amountOfHostsTextField.setText("");

                // Update FreeAdresses Label
                updateFreeAddressesLabel();
            }
        });

        // JPanel for SubnetInformation
        JPanel subnetInformation = new JPanel();
        subnetInformation.add(freeAddressesLabel);
        updateFreeAddressesLabel();


        // Adding Elements to the different Panels
        add(subnetInformation, BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
        openDeleteButtonPanel.add(openSubnetPanelButton);
        openDeleteButtonPanel.add(deleteSubnetPanelButton);
        openDeleteButtonPanel.add(showData);
        interactionPanel.add(openDeleteButtonPanel);
        numberFieldsPanel.add(createNewSubnetButton);
        numberFieldsPanel.add(new JLabel("eines neuen Subnetzes mit:"));
        numberFieldsPanel.add(amountOfHostsTextField);
        numberFieldsPanel.add(new JLabel("Hosts"));
        interactionPanel.add(numberFieldsPanel);
        add(interactionPanel, BorderLayout.PAGE_END);

    }

    /**
     * open a new HostPanel & add to the tabbedPane
     * @param subnetList parent List of Subnets
     * @param networkCalculator parent NetworkCalculator
     */
    public void openNewHostPanel(JList subnetList, NetworkCalculator networkCalculator) {

        String subnet = (String) subnetList.getSelectedValue();
        String title = "Hosts: " + subnet;
        if (subnet != null && networkCalculator.getTabIndexFromTitle(tabbedPane, title) == 0) {
            HostPanel hostPanel = new HostPanel(networkTitle, subnet, data);
            hostPanels.add(hostPanel);
            tabbedPane.add(title, hostPanel);
            tabbedPane.setSelectedIndex(networkCalculator.getTabIndexFromTitle(tabbedPane, title));
            tabbedPane.add("X", new JPanel());
        }
    }

    /**
     * remove Entry with the given index in hostPanels
     * @param index parent int
     */
    public static void removeEntryFromArrayList(int index) {
        hostPanels.remove(index);
    }

    public static void addEntryToArrayList(HostPanel hostPanel) {
        hostPanels.add(hostPanel);
    }

    /**
     * Updates the Free Adress Label on the top
     */
    private void updateFreeAddressesLabel() {
        try {
            String[] allIPsInNetwork = Converter.getAllIPsInNetwork(networkTitle);
            int amountOfFreeAddressesInNetwork = allIPsInNetwork.length;


            for (int i = 0; i < model.size(); i++) {
                String currentSubnet = model.getElementAt(i);
                String[] allIPsInCurrentSubnet = Converter.getAllIPsInNetwork(currentSubnet);
                long amountOfAddressesInCurrentSubnet = allIPsInCurrentSubnet.length;

                amountOfFreeAddressesInNetwork -= amountOfAddressesInCurrentSubnet;
            }

            String contentText = "<html>Freie IP Adressen: <b>" + String.valueOf(amountOfFreeAddressesInNetwork) + "</b></html>";
            freeAddressesLabel.setText(contentText);
        } catch (Exception e) {
            String contentText = "<html>Freie IP Adressen: <b>viele</b>";
            freeAddressesLabel.setText(contentText);
        }



    }

    /*
        Getter Methods for networkTitle, model & hostPanels
     */

    public String getNetworkTitle() {
        return networkTitle;
    }

    public DefaultListModel<String> getModel() {
        return model;
    }

    public static ArrayList<HostPanel> getHostPanels() {
        return hostPanels;
    }


}
