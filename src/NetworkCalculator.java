import javax.swing.*;
import java.awt.event.WindowEvent;
public class NetworkCalculator extends JFrame {

    private JTabbedPane tabbedPane = new JTabbedPane();

    public NetworkCalculator(String title) {

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
            }
        });

        NetworkPanel networkPanel = new NetworkPanel(this);
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
            System.out.println("Windows is closing");
            //TODO Save data to JSON File




        }
        super.processWindowEvent(e);
    }

}
