import javax.swing.*;
import java.awt.*;
class DrawPanelLab01a extends JPanel {
   public void paintComponent(final Graphics g)
   {
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      String[] fonts = ge.getAvailableFontFamilyNames();
      g.setColor(Color.BLACK);
      for(int x=0;x<fonts.length;x++)
      {
        g.setFont(new Font(fonts[x], Font.BOLD, 20));
        g.drawString(fonts[x], (x/30)*300, (((x)%30)+1)*20);
      }

   }
}
public class FontGraphics {  
   public static void main(final String[] arg) {
      final JFrame frame = new JFrame("Lab01a");  
      frame.setSize(1043,339);   
      frame.setLocation(0,0);  
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(new DrawPanelLab01a());                                                                 
      frame.setVisible(true); 
      System.err.println("Finished");
   }
}