package gui;

import logic.Converter;
import logic.NetworkAddressValidator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class NetworkPanel extends JPanel {

    private JTabbedPane tabbedPane;
    private static DefaultListModel<String> model = new DefaultListModel<>();
    private static ArrayList<SubnetPanel> subnetPanels = new ArrayList<>();
    private JSONArray data;

    public NetworkPanel(NetworkCalculator networkCalculator, JSONArray data) {
        this.data = data;

        // Get Data an write to listModel
        for (Object aData : data) {
            JSONObject networkObject = (JSONObject) aData;
            model.addElement(networkObject.get("id").toString());
        }

        // set the Network-Panel Layout to BorderLayout
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

        // Subnetmask Panel
        JPanel subnetmaskPanel = new JPanel();
        subnetmaskPanel.setLayout(new FlowLayout());

        /*
         Create JList with ListModel
         JList for all Networks
        */
        JScrollPane scrollPane = new JScrollPane();
        JList<String> networkList =  new JList<>(model);
        scrollPane.setViewportView(networkList);
        networkList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openNewSubnet(networkList, networkCalculator);
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
        JButton openNetworkPanelButton = new JButton();
        openNetworkPanelButton.setText("Öffnen");
        openNetworkPanelButton.addActionListener(e -> openNewSubnet(networkList, networkCalculator));

        // Delete Selected Network Button
        JButton deleteNetworkPanelButton = new JButton();
        deleteNetworkPanelButton.setText("Löschen");
        deleteNetworkPanelButton.addActionListener(e -> {
            String currentSelectedTitle = networkList.getSelectedValue();
            String title = "Subnetz: " + currentSelectedTitle;
            int selectedTabIndex = networkCalculator.getTabIndexFromTitle(tabbedPane, title);
            if (selectedTabIndex != 0) {
                tabbedPane.remove(selectedTabIndex);
                tabbedPane.remove(selectedTabIndex);
            }
            model.removeElement(currentSelectedTitle);
        });

        // Show more Information Button
        JButton showData = new JButton("Informationen");
        showData.addActionListener(e -> {
            if (networkList.getSelectedValue() != null) {
                NetworkCalculator.showMoreInformationAboutNetwork(networkList.getSelectedValue());
            }

        });

        /*
         Create InputFields with Labels for the Optical View and adding all of them to the NumberField-Panel
         Create New Network Button
        */
        JButton createNewNetworkButton = new JButton();
        numberFieldsPanel.add(createNewNetworkButton);
        numberFieldsPanel.add(new JLabel("eines neuen Netzwerkes:"));

        // TextFields
        JTextField[] textFields = new JTextField[5];
        for (int i = 0; i < textFields.length; i++) {
            textFields[i] = new JTextField();
            if (i < textFields.length - 1) {
                textFields[i].setPreferredSize(new Dimension(40, 20));
            } else {
                textFields[i].setPreferredSize(new Dimension(30, 20));
            }
            numberFieldsPanel.add(textFields[i]);

            if (i < textFields.length - 2) {
                numberFieldsPanel.add(new JLabel("."));
            } else if (i == textFields.length - 2) {
                numberFieldsPanel.add(new JLabel("/"));
            }
        }

        // Create Create New Network Button and add Action Listener to create new Networks
        createNewNetworkButton.setText("Anlegen");
        createNewNetworkButton.addActionListener(e -> {

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < textFields.length; i++) {
                String currentBlock = textFields[i].getText();

                if (i < textFields.length - 2) {
                    stringBuilder.append(currentBlock);
                    stringBuilder.append(".");
                } else if (i == textFields.length - 2) {
                    stringBuilder.append(currentBlock);
                    stringBuilder.append("/");
                } else {
                    stringBuilder.append(currentBlock);
                }
                // Set TextField Content to Default
                textFields[i].setText("");
            }

            String newNetwork = stringBuilder.toString();

            if (NetworkAddressValidator.validate(newNetwork)) {
                if (!model.contains(newNetwork)) {
                    String[] allOldNetworks = new String[model.size()];
                    for (int i = 0; i < model.size(); i++) {
                        allOldNetworks[i] = model.getElementAt(i);
                    }
                    if (Converter.checkIfPossibleNewNetwork(allOldNetworks, newNetwork)) {
                        model.addElement(newNetwork);
                        // sort the elements from the model
                        String[] sortedElements = Converter.sortNetworksInModel(model);
                        model.removeAllElements();
                        for (String sortedElement : sortedElements) {
                            model.addElement(sortedElement);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Netzwerke überlagern sich",
                                "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Netwerk bereits vorhanden",
                            "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ungültiges Netzwerk",
                        "Eingabefehler", JOptionPane.ERROR_MESSAGE);
            }

        });

        // Subnetmask Button
        JButton subnetmaskButton = new JButton("Subnetmaske -> Präfix");
        subnetmaskPanel.add(subnetmaskButton);

        JTextField[] subnetmaskArray = new JTextField[4];
        for (int i = 0; i < subnetmaskArray.length; i++) {
            subnetmaskArray[i] = new JTextField();
            subnetmaskArray[i].setPreferredSize(new Dimension(40, 20));
            if (i < subnetmaskArray.length - 1) {
                subnetmaskPanel.add(subnetmaskArray[i]);
                subnetmaskPanel.add(new JLabel("."));
            } else {
                subnetmaskPanel.add(subnetmaskArray[i]);
            }
        }

        // Action Listener for SubnetMask Converter Button
        subnetmaskButton.addActionListener(e -> {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < subnetmaskArray.length; i++) {
                if (i < subnetmaskArray.length - 1) {
                    stringBuilder.append(subnetmaskArray[i].getText());
                    stringBuilder.append(".");
                } else {
                    stringBuilder.append(subnetmaskArray[i].getText());
                }
            }

            String subnetmaskAsString = stringBuilder.toString();

            if (NetworkAddressValidator.validateSubnetMask(subnetmaskAsString)) {
                textFields[textFields.length - 1].setText(String.valueOf(Converter.maskToPrefix(subnetmaskAsString)));
            } else {
                JOptionPane.showMessageDialog(null, "Ungültige Subnetzmaske",
                        "Eingabefehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Adding Elements to the different Panels
        add(scrollPane, BorderLayout.CENTER);
        openDeleteButtonPanel.add(openNetworkPanelButton);
        openDeleteButtonPanel.add(deleteNetworkPanelButton);
        openDeleteButtonPanel.add(showData);
        interactionPanel.add(openDeleteButtonPanel);
        interactionPanel.add(numberFieldsPanel);
        interactionPanel.add(subnetmaskPanel);
        add(interactionPanel, BorderLayout.PAGE_END);
    }

    /**
     * open a new SubnetPanel & add to the tabbedPane
     * @param networkList parent List of Networks
     * @param networkCalculator parent NetworkCalculator
     */
    private void openNewSubnet(JList networkList, NetworkCalculator networkCalculator){
        String network = (String) networkList.getSelectedValue();
        String title = "Subnetz: " + network;
        if (network != null && networkCalculator.getTabIndexFromTitle(tabbedPane, title) == 0) {
            SubnetPanel subnetPanel = new SubnetPanel(network, networkCalculator, data);
            subnetPanels.add(subnetPanel);
            tabbedPane.add(title, subnetPanel);
            tabbedPane.setSelectedIndex(networkCalculator.getTabIndexFromTitle(tabbedPane, title));
            tabbedPane.add("X", new JPanel());
        }
    }

    /**
     * remove Entry with the given index in subnetPanels
     * @param index parent int
     */
    public static void removeEntryFromArrayList(int index) {
        subnetPanels.remove(index);
    }

    public static void addEntryToArrayList(SubnetPanel subnetPanel) {
        subnetPanels.add(subnetPanel);
    }

    /*
        Getter Methods for model & subnetPanels
     */

    public static DefaultListModel<String> getModel() {
        return model;
    }

    public static ArrayList<SubnetPanel> getSubnetPanels() {
        return subnetPanels;
    }

}
