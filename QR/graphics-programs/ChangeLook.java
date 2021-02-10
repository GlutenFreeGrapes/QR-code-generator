import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ChangeLook {

  public static void main(String args[]) {
    final JFrame frame = new JFrame("Change Look");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ActionListener actionListener = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        String lafClassName = null;
        lafClassName = actionEvent.getActionCommand();
        String finalLafClassName = lafClassName;
        try {
          UIManager.setLookAndFeel(finalLafClassName);
          SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception exception) {
          JOptionPane.showMessageDialog(frame, "Can't change look and feel", "Invalid PLAF",
              JOptionPane.ERROR_MESSAGE);
        }

      }
    };

    UIManager.LookAndFeelInfo looks[] = UIManager.getInstalledLookAndFeels();

    JComboBox comboBox = new JComboBox(new String[] { "a", "b" });

    JPanel panel = new JPanel();

    for (int i = 0, n = looks.length; i < n; i++) {
      JButton button = new JButton(looks[i].getName());
      button.setActionCommand(looks[i].getClassName());
      button.addActionListener(actionListener);
      panel.add(button);
    }

    frame.add(comboBox, BorderLayout.NORTH);
    frame.add(panel, BorderLayout.SOUTH);
    frame.setSize(350, 150);
    frame.setVisible(true);

  }
}








