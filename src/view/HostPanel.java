package view;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HostPanel extends JPanel {

    private String subnetTitle;

    private DefaultListModel<String> hostModel = new DefaultListModel<>();
    private DefaultListModel<String> notesModel = new DefaultListModel<>();

    public HostPanel(String network, String subnet, JSONArray data) {
        subnetTitle = subnet;

        for (int i = 0; i < data.size(); i++) {
            JSONObject networkObject = (JSONObject) data.get(i);

            if (networkObject.get("id").toString().equals(network)) {
                JSONArray subnets = (JSONArray) networkObject.get("subnets");
                for (int j = 0; j < subnets.size(); j++) {


                    JSONObject subnetObject = (JSONObject) subnets.get(j);
                    String subnetString = subnetObject.get("subnet").toString();

                    if (subnetString.equals(subnet)) {
                        JSONArray hosts = (JSONArray) subnetObject.get("hosts");
                        for (int k = 0; k < hosts.size(); k++) {
                            JSONObject hostObject = (JSONObject) hosts.get(k);

                            hostModel.addElement(hostObject.get("host").toString());
                            notesModel.addElement(hostObject.get("note").toString());
                        }
                    }
                }
            }
        }

        this.setLayout(new BorderLayout());

        JPanel listContent = new JPanel();
        listContent.setLayout(new GridLayout(1, 2));

        JScrollPane hostScrollPane = new JScrollPane();
        JList<String> hostList =  new JList<>(hostModel);
        hostScrollPane.setViewportView(hostList);

        JScrollPane notesScrollPane = new JScrollPane();
        JList<String> notesList =  new JList<>(notesModel);
        notesScrollPane.setViewportView(notesList);

        notesList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // TODO Function to open new User Input Field
                    JFrame frame = new JFrame();
                    String note = JOptionPane.showInputDialog(frame, "Notiz:");
                    if (note != null) {
                        notesModel.setElementAt(note, notesList.getSelectedIndex());
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

        if (hostModel.size() == 0) {
            for (int i = 0; i < 10; i++) {
                String testData = String.valueOf(i);
                hostModel.addElement(testData);
                notesModel.setSize(hostModel.getSize());
                if (notesModel.getElementAt(i) == null) {
                    notesModel.setElementAt("frei", i);
                }
            }
        }

        listContent.add(hostScrollPane);
        listContent.add(notesScrollPane);

        this.add(listContent, BorderLayout.CENTER);
    }

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
