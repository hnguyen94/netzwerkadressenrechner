package view;

import logic.Converter;
import logic.IPv4.IPv4Network;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;


public class NetworkPanel extends JPanel {

    private JTabbedPane tabbedPane;
    private static DefaultListModel<IPv4Network> model = new DefaultListModel<>();

    private static ArrayList<SubnetPanel> subnetPanels = new ArrayList<>();
    private JSONArray data;


    public NetworkPanel(NetworkCalculator networkCalculator, JSONArray data) {
        this.data = data;

        for (int i = 0; i < data.size(); i++) {
            JSONObject networkObject = (JSONObject) data.get(i);
            model.addElement(Converter.convertStringToIpv4Network(networkObject.get("id").toString()));
        }

        // set the Network-Panel Layout to BorderLayout
        this.setLayout(new BorderLayout());
        // Initialise TabbedPane
        tabbedPane = networkCalculator.getTabbedPane();



        // -------------------------------------------------------------------------------------------------------------
        // Create Panels and give them specific Layout
        // -------------------------------------------------------------------------------------------------------------

        // Create Interaction Panel for Open-, Delete-Button, IP-Numberfields and Create Button
        JPanel interactionPanel = new JPanel();
        interactionPanel.setLayout(new BoxLayout(interactionPanel, BoxLayout.PAGE_AXIS));

        // Open and Delete Button Panel
        JPanel openDeleteButtonPanel = new JPanel();
        openDeleteButtonPanel.setLayout(new FlowLayout());

        // Numberfields Panel
        JPanel numberFieldsPanel = new JPanel();
        numberFieldsPanel.setLayout(new FlowLayout());



        // -------------------------------------------------------------------------------------------------------------
        // Create JList with ListModel
        // -------------------------------------------------------------------------------------------------------------

        // JList for all Networks
        JScrollPane scrollPane = new JScrollPane();
        JList<IPv4Network> networkList =  new JList<>(model);
        scrollPane.setViewportView(networkList);
        networkList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {
                    openNewTab(networkList, networkCalculator);
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



        // -------------------------------------------------------------------------------------------------------------
        // Create Open Button and Delete Button and add Action Listeners to open the from the list selected Network
        // in a new Tab or to delete the selected Network from the list
        // -------------------------------------------------------------------------------------------------------------

        // Open Selected Network Button
        JButton openNetworkPanelButton = new JButton();
        openNetworkPanelButton.setText("Open");

        openNetworkPanelButton.addActionListener(e -> openNewTab(networkList, networkCalculator));

        // Delete Selected Network Button
        JButton deleteNetworkPanelButton = new JButton();
        deleteNetworkPanelButton.setText("Delete");
        deleteNetworkPanelButton.addActionListener(e -> {
            IPv4Network currentSelectedTitle = networkList.getSelectedValue();
            if(currentSelectedTitle != null){
                int selectedTabIndex = networkCalculator.getTabIndexFromTitle(tabbedPane, currentSelectedTitle.toString());
                if (selectedTabIndex != 0) {
                    tabbedPane.remove(selectedTabIndex);
                    tabbedPane.remove(selectedTabIndex);
                }
                model.removeElement(currentSelectedTitle);
            }
        });



        // -------------------------------------------------------------------------------------------------------------
        // Create InputFields with Labels for the Optical View and adding all of them to the NumberField-Panel
        // -------------------------------------------------------------------------------------------------------------

        // Create New Network Button
        JButton createNewNetworkButton = new JButton();
        numberFieldsPanel.add(createNewNetworkButton);
        numberFieldsPanel.add(new JLabel("a new Network"));

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



        // -------------------------------------------------------------------------------------------------------------
        // Create Create New Network Button and add Action Listener to create new Networks
        // -------------------------------------------------------------------------------------------------------------

        createNewNetworkButton.setText("Create");
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
            }

            String newNetwork = stringBuilder.toString();

            IPv4Network iPv4Network =  Converter.convertStringToIpv4Network(newNetwork);

            if (NetworkAddressValidator.validate(newNetwork)) {
                if (!model.contains(iPv4Network)) {
                    model.addElement(iPv4Network);
                } else {
                    JOptionPane.showMessageDialog(null, "Netzwerk bereits vorhanden",
                            "Eingabefehler", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ungültiges Netzwerk",
                        "Eingabefehler", JOptionPane.WARNING_MESSAGE);
            }


        });


        // -------------------------------------------------------------------------------------------------------------
        // Adding Elements to the different Panels
        // -------------------------------------------------------------------------------------------------------------

        add(scrollPane, BorderLayout.CENTER);
        openDeleteButtonPanel.add(openNetworkPanelButton);
        openDeleteButtonPanel.add(new JLabel("or"));
        openDeleteButtonPanel.add(deleteNetworkPanelButton);
        openDeleteButtonPanel.add(new JLabel("selected Network from List"));
        interactionPanel.add(openDeleteButtonPanel);
        interactionPanel.add(numberFieldsPanel);
        add(interactionPanel, BorderLayout.PAGE_END);

    }

    public static DefaultListModel<IPv4Network> getModel() {
        return model;
    }

    public static ArrayList<SubnetPanel> getSubnetPanels() {
        return subnetPanels;
    }

    private void openNewTab(JList networkList, NetworkCalculator networkCalculator){
        IPv4Network network = (IPv4Network) networkList.getSelectedValue();
        if (network != null && networkCalculator.getTabIndexFromTitle(tabbedPane, network.toString()) == 0) {
            SubnetPanel subnetPanel = new SubnetPanel(network, networkCalculator, data);
            subnetPanels.add(subnetPanel);
            tabbedPane.add(network.toString(), subnetPanel);
            tabbedPane.setSelectedIndex(networkCalculator.getTabIndexFromTitle(tabbedPane, network.toString()));
            tabbedPane.add("X", new JPanel());
        }
    }

}
