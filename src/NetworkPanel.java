import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NetworkPanel extends JPanel {

    private JTabbedPane tabbedPane;

    public NetworkPanel(NetworkCalculator networkCalculator) {

        // set the Network-Panel Layout to BorderLayout
        this.setLayout(new BorderLayout());



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
        // Create tabbed Pane and add Action Listener
        // -------------------------------------------------------------------------------------------------------------

        // Initialise TabbedPane
        tabbedPane = networkCalculator.getTabbedPane();
        tabbedPane.addChangeListener(e -> {
            // If the Close Tab is clicked...
            if (tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()).equals("X")) {
                int currentIndex = tabbedPane.getSelectedIndex();
                tabbedPane.setSelectedIndex(0);
                tabbedPane.removeTabAt(currentIndex);
                tabbedPane.removeTabAt(currentIndex - 1);
            }
        });



        // -------------------------------------------------------------------------------------------------------------
        // Create JList with ListModel
        // -------------------------------------------------------------------------------------------------------------

        // JList for all Networks
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> networkList =  new JList<>(model);



        // -------------------------------------------------------------------------------------------------------------
        // Create Open Button and Delete Button and add Action Listeners to open the from the list selected Network
        // in a new Tab or to delete the selected Network from the list
        // -------------------------------------------------------------------------------------------------------------

        // Open Selected Network Button
        JButton openNetworkPanelButton = new JButton();
        openNetworkPanelButton.setText("Open");
        openNetworkPanelButton.addActionListener(e -> {
            String index = networkList.getSelectedValue();
            if (index != null && getTabIndexFromTitle(tabbedPane, index) == 0) {
                SubnetPanel subnetPanel = new SubnetPanel(index);
                tabbedPane.add(index, subnetPanel);
                tabbedPane.setSelectedIndex(getTabIndexFromTitle(tabbedPane, index));
                tabbedPane.add("X", new JPanel());
            }
        });

        // Delete Selected Network Button
        JButton deleteNetworkPanelButton = new JButton();
        deleteNetworkPanelButton.setText("Delete");
        deleteNetworkPanelButton.addActionListener(e -> model.removeElement(networkList.getSelectedValue()));



        // -------------------------------------------------------------------------------------------------------------
        // Create InputFields with Labels for the Optical View and adding all of them to the NumberField-Panel
        // -------------------------------------------------------------------------------------------------------------

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

        // Create New Network Button
        JButton createNewNetworkButton = new JButton();
        createNewNetworkButton.setText("Create");
        createNewNetworkButton.addActionListener(e -> {

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < textFields.length; i++) {
                if (i < textFields.length - 2) {
                    stringBuilder.append(textFields[i].getText());
                    stringBuilder.append(".");
                } else if (i == textFields.length - 2) {
                    stringBuilder.append(textFields[i].getText());
                    stringBuilder.append("/");
                } else {
                    stringBuilder.append(textFields[i].getText());
                }
            }

            String newNetwork = stringBuilder.toString();

            if (!model.contains(newNetwork)) {
                model.addElement(newNetwork);
            }
        });



        // -------------------------------------------------------------------------------------------------------------
        // Adding Elements to the different Panels
        // -------------------------------------------------------------------------------------------------------------

        add(networkList, BorderLayout.CENTER);
        openDeleteButtonPanel.add(openNetworkPanelButton);
        openDeleteButtonPanel.add(deleteNetworkPanelButton);
        interactionPanel.add(openDeleteButtonPanel);
        numberFieldsPanel.add(createNewNetworkButton);
        interactionPanel.add(numberFieldsPanel);
        add(interactionPanel, BorderLayout.PAGE_END);

    }

    // Function to get the Tab Index from the Title
    private int getTabIndexFromTitle(JTabbedPane tabbedPane, String title) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).equals(title)) {
                return i;
            }
        }
        return 0;
    }

}
