import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SubnetPanel extends JPanel {

    private JTabbedPane tabbedPane;
    private String networkTitle;

    private DefaultListModel<String> model = new DefaultListModel<>();



    public SubnetPanel(String network, NetworkCalculator networkCalculator, JSONArray data) {

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




        networkTitle = network;

        // set the Subnet-Panel Layout to BorderLayout
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
        JList<String> subnetList =  new JList<>(model);
        scrollPane.setViewportView(subnetList);



        // -------------------------------------------------------------------------------------------------------------
        // Create Open Button and Delete Button and add Action Listeners to open the from the list selected Network
        // in a new Tab or to delete the selected Network from the list
        // -------------------------------------------------------------------------------------------------------------

        // Open Selected Network Button
        JButton openSubnetPanelButton = new JButton();
        openSubnetPanelButton.setText("Open");
        openSubnetPanelButton.addActionListener(e -> {
            String subnet = subnetList.getSelectedValue();
            if (subnet != null && networkCalculator.getTabIndexFromTitle(tabbedPane, subnet) == 0) {
                HostPanel hostPanel = new HostPanel(subnet);
                tabbedPane.add(subnet, hostPanel);
                tabbedPane.setSelectedIndex(networkCalculator.getTabIndexFromTitle(tabbedPane, subnet));
                tabbedPane.add("X", new JPanel());
            }
        });

        // Delete Selected Network Button
        JButton deleteSubnetPanelButton = new JButton();
        deleteSubnetPanelButton.setText("Delete");
        deleteSubnetPanelButton.addActionListener(e -> model.removeElement(subnetList.getSelectedValue()));



        // -------------------------------------------------------------------------------------------------------------
        // Create InputFields and set Preferred Size
        // -------------------------------------------------------------------------------------------------------------

        JTextField amountOfHostsTextField = new JTextField();
        amountOfHostsTextField.setPreferredSize(new Dimension(40, 20));
        JTextField amountOfSubnetsTextField = new JTextField();
        amountOfSubnetsTextField.setPreferredSize(new Dimension(40, 20));





        // -------------------------------------------------------------------------------------------------------------
        // Create createNewSubnetButton
        // -------------------------------------------------------------------------------------------------------------

        JButton createNewSubnetButton = new JButton();
        createNewSubnetButton.setText("Create");
        createNewSubnetButton.addActionListener(e -> {
            model.addElement("Test");
            System.out.println("worked");
        });



        // -------------------------------------------------------------------------------------------------------------
        // Adding Elements to the different Panels
        // -------------------------------------------------------------------------------------------------------------

        add(scrollPane, BorderLayout.CENTER);
        openDeleteButtonPanel.add(openSubnetPanelButton);
        openDeleteButtonPanel.add(deleteSubnetPanelButton);
        interactionPanel.add(openDeleteButtonPanel);
        numberFieldsPanel.add(amountOfHostsTextField);
        numberFieldsPanel.add(amountOfSubnetsTextField);
        numberFieldsPanel.add(createNewSubnetButton);
        interactionPanel.add(numberFieldsPanel);
        add(interactionPanel, BorderLayout.PAGE_END);

    }

    public String getNetworkTitle() {
        return networkTitle;
    }

    public DefaultListModel<String> getModel() {
        return model;
    }
}
