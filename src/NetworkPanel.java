import javax.swing.*;
import java.awt.*;

public class NetworkPanel extends JPanel {

    private JButton[] myButton = new JButton[6];

    public NetworkPanel() {

        for (int i = 0; i < myButton.length; i++) {
            myButton[i] = new JButton();
            myButton[i].setSize(new Dimension(100, 100));
            add(myButton[i]);
        }

    }

}
