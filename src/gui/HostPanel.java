package gui;

import logic.Converter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class HostPanel extends JPanel {

    private String subnetTitle;
    private DefaultListModel<String> hostModel = new DefaultListModel<>();
    private DefaultListModel<String> notesModel = new DefaultListModel<>();

    public HostPanel(String network, String subnet, JSONArray data) {
        subnetTitle = subnet;

        // Add Data to the model
        for (Object aData : data) {
            JSONObject networkObject = (JSONObject) aData;
            if (networkObject.get("id").toString().equals(network)) {
                JSONArray subnets = (JSONArray) networkObject.get("subnets");

                for (Object subnet1 : subnets) {
                    JSONObject subnetObject = (JSONObject) subnet1;
                    String subnetString = subnetObject.get("subnet").toString();
                    if (subnetString.equals(subnet)) {
                        JSONArray hosts = (JSONArray) subnetObject.get("hosts");

                        for (Object host : hosts) {
                            JSONObject hostObject = (JSONObject) host;

                            hostModel.addElement(hostObject.get("host").toString());
                            notesModel.addElement(hostObject.get("note").toString());
                        }

                    }
                }

            }
        }

        // Get old Data from the Host Panels
        ArrayList<HostPanel> allHostPanels = SubnetPanel.getHostPanels();
        for (int i = 0; i < allHostPanels.size(); i++) {
            if (allHostPanels.get(i).getSubnetTitle().equals(subnet)) {
                hostModel = allHostPanels.get(i).getHostModel();
                notesModel = allHostPanels.get(i).getNotesModel();
                SubnetPanel.removeEntryFromArrayList(i);
            }
        }

        // Set Host Panel Layout
        this.setLayout(new BorderLayout());

        // Initialise new Panel and set Layout
        JPanel listContent = new JPanel();
        listContent.setLayout(new GridLayout(1, 2));

        // Initialise new Lists and add to Panel
        JList<String> hostList =  new JList<>(hostModel);
        JList<String> notesList =  new JList<>(notesModel);
        listContent.add(hostList);
        listContent.add(notesList);

        // Scroll Pane for the Panel to scroll the lists
        JScrollPane scrollPane = new JScrollPane(listContent);

        // Add Mouse Listener to the host List to view more Information about the host
        hostList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    NetworkCalculator.showMoreInformationAboutTheHostIP(hostList.getSelectedValue());
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

        // Add Mouse Listener to the notes List to change notes
        notesList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // Dont Change the Note of the Network ID and the Broadcast Entry
                    if (notesList.getSelectedIndex() != 0 && notesList.getSelectedIndex() != notesList.getModel().getSize() - 1) {
                        JFrame frame = new JFrame();
                        // Open new Dialogue for User Input
                        String note = JOptionPane.showInputDialog(frame, "Notiz:");
                        if (note != null) {
                            notesModel.setElementAt(note, notesList.getSelectedIndex());
                        }
                    }
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

        // if hostModel is empty generate the IP Addresses and set default Note for each of them
        if (hostModel.size() == 0) {
            String[] allIPs = Converter.getAllIPsInNetwork(subnet);
            for (int i = 0; i < allIPs.length; i++) {
                hostModel.addElement(allIPs[i]);
                notesModel.setSize(hostModel.getSize());
                if (notesModel.getElementAt(i) == null) {
                    if (i == 0) {
                        notesModel.setElementAt("Netzwerk ID", i);
                    } else if (i == allIPs.length - 1) {
                        notesModel.setElementAt("Broadcast", i);
                    } else {
                        notesModel.setElementAt("frei", i);
                    }
                }
            }
        }

        // Add scroll Pane to HostPanel
        this.add(scrollPane, BorderLayout.CENTER);
    }

    /*
        Getter Methods for subnetTitle, hostModel & notesModel
     */

    public String getSubnetTitle() {
        return subnetTitle;
    }

    public DefaultListModel<String> getHostModel() {
        return hostModel;
    }

    public DefaultListModel<String> getNotesModel() {
        return notesModel;
    }
}
