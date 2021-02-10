
// Java program to illustrate 
// CrossPlatformLookAndFeel 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.UIManager;
import javax.swing.JFrame;

class Awt implements ActionListener {
    JFrame f;
    JButton addbut, subbut, mulbut, divbut, b5;
    JTextField t1, t2, t3;
    JLabel l, l1;

    Awt() {
        f = new JFrame("Cross Platform Look and Feel");
        t1 = new JTextField("             ");
        t2 = new JTextField("             ");
        t3 = new JTextField("             ");
        addbut = new JButton("Add");
        subbut = new JButton("Sub");
        mulbut = new JButton("Mul");
        divbut = new JButton("Div");
        l = new JLabel();
        l1 = new JLabel();
    }

    public void awt1() {
        f.setLayout(new GridLayout(3, 2));
        f.setVisible(true);
        f.add(t1);
        f.add(t2);
        f.add(t3);
        f.add(addbut);
        f.add(subbut);
        f.add(mulbut);
        f.add(divbut);
        f.add(l);
        f.add(l1);
        addbut.addActionListener(this);
        subbut.addActionListener(this);
        mulbut.addActionListener(this);
        divbut.addActionListener(this);
        f.pack();
    }

    public void actionPerformed(ActionEvent e) {
        String s = new String(e.getActionCommand());
        l.setText(s);
        if ((s).equals("Add")) {
            int a = Integer.parseInt(t1.getText());
            int b = Integer.parseInt(t2.getText());
            Integer c = a + b;
            t3.setText(c.toString());
        } else if ((s).equals("Sub")) {
            int a = Integer.parseInt(t1.getText());
            int b = Integer.parseInt(t2.getText());
            Integer c = a - b;
            t3.setText(c.toString());
        } else if ((s).equals("Mul")) {
            int a = Integer.parseInt(t1.getText());
            int b = Integer.parseInt(t2.getText());
            Integer c = a * b;
            t3.setText(c.toString());
        }
    }

    public static void main(String args[]) {

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Look and Feel not set");
        }
        Awt a = new Awt();
        a.awt1();
    }
}