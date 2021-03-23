//GlutenFreeGrapes, 23 March 2021
//QR Code Tutorial: https://www.thonky.com/qr-code-tutorial/
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.nio.charset.*;
import java.net.*;
import javax.sound.sampled.*;
public class QR
{
   public static void main(String[] args)
   {
      try 
      {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } 
      catch (Exception e) 
      {
      }
      String j = "";
      int l=0;
      ImageIcon normal; 
      ImageIcon warning;
      String error = "https://cdn.discordapp.com/attachments/711352466112774144/819294403792207872/Wilhelm_Scream.ogg.wav";
      try
      {
         normal = new ImageIcon(new ImageIcon(new URL("https://media.discordapp.net/attachments/711352466112774144/812004867915644988/grapesNormal.png")).getImage().getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH));
      }
      catch(Exception e)
      {
         normal = new ImageIcon();
      }
      try
      {
         warning = new ImageIcon(new ImageIcon(new URL("https://media.discordapp.net/attachments/711352466112774144/812004871715946586/grapesAngery.png")).getImage().getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH));
      }
      catch(Exception e)
      {
         warning = new ImageIcon();
      }
      String[] a = {"1","2","3","4"};
      int[] maxlengths = {2953, 2331, 1663, 1273};
      while (empty(j)||j.length()>maxlengths[0])
      {
         JPanel pan = new JPanel();
         JLabel g = new JLabel("What message would you like to encode? Maximum size "+maxlengths[0]+" bytes. ");
         JTextArea input = new JTextArea(4, 40);
         pan.setLayout(new BorderLayout());
         input.setLineWrap(false);
         input.setText(j);
         pan.add(g, BorderLayout.NORTH);
         pan.add(new JScrollPane(input), BorderLayout.CENTER);
         switch (JOptionPane.showConfirmDialog(null, pan, "Message", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, normal))
         {
            case JOptionPane.CLOSED_OPTION:
               System.exit(0);
               break;
            case JOptionPane.OK_OPTION:
               j = input.getText();
               l = j.getBytes(StandardCharsets.UTF_8).length;   
               if(empty(j))
               {
                  play(error);
                  JOptionPane.showMessageDialog(null,"Please enter a message. ","Error",JOptionPane.WARNING_MESSAGE, warning);
               }
               else if(l>maxlengths[0])
               {
                  play(error);
                  JOptionPane.showMessageDialog(null,"Message too long. \nMaximum length "+maxlengths[0]+" bytes, input length "+l+" bytes. ","Error",JOptionPane.WARNING_MESSAGE, warning);
               }
               else if (l>maxlengths[1])
               {
                  a = Arrays.copyOf(a, 1);
               }
               else if (l>maxlengths[2])
               {
                  a = Arrays.copyOf(a, 2);
               }
               else if (l>maxlengths[3])
               {
                  a = Arrays.copyOf(a, 3);
               }
               break;
            case JOptionPane.CANCEL_OPTION:
               System.exit(0);
               break;
         }
      }
      int ec = 0;
      try 
      {
         ec = Integer.parseInt((String)JOptionPane.showInputDialog(null, "Select your error correction level: \n1 - Low\n2 - Medium\n3 - Quartile\n4 - High", "Error Correction Level", JOptionPane.QUESTION_MESSAGE, normal, a, a[0]));
      } 
      catch (NumberFormatException e) 
      {
         System.exit(0);
      }
      int[] qrinfo = new int[3];
      int[][] m = encode(j, ec, qrinfo);
      ContentPanel c = new ContentPanel(m, qrinfo);
      JFrame frame = new JFrame("QR Code");
      frame.setLocation(0,0);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(c);
      frame.setSize(c.framesize()+16, c.framesize()+c.buttonWidth()+c.fontBlock()+39);
      frame.setResizable(false);
      frame.setVisible(true);
   }
   public static boolean empty(String s)
   {
      int[] whitespace = new int[]{9,10,11,12,13,32,133,160,5760,8192,8193,8194,8195,8196,8197,8198,8199,8200,8201,8202,8232,8233,8239,8287,12288,6158,8203,8204,8205,8288,65279};
      String ws = new String(whitespace, 0, whitespace.length);
      for(int x=0;x<whitespace.length;x++)
      {
         s = s.replaceAll(ws.substring(x,x+1), "");
      }
      if (s.equals(""))
      {
         return true;
      }
      else
      {
         return false;
      }
   }
   public static void play(String sound)
   {
      try
      {
         AudioInputStream audioIn = AudioSystem.getAudioInputStream(new URL(sound));
         Clip clip = AudioSystem.getClip();
         clip.open(audioIn);
         clip.start();
      } 
      catch (Exception e) 
      {
      }
   }
   public static int[][] encode(String s, int i, int[] qrinfo)
   {
      QREncoder w = new QREncoder (s,i);
      w.alignAndTimePattern();
      w.reserveSpaces();
      w.encode(); 
      w.errorCorrect(); 
      w.structureFinalMessage();
      w.fillMatrix();
      w.findBestMask();
      qrinfo[0] = w.getEC();
      qrinfo[1] = w.getVersion();
      qrinfo[2] = w.getMask();
      return w.getMatrix();
   }
}
class ContentPanel extends JPanel
{
   static final long serialVersionUID = 69420;
   private int[] qrinfo;
   private int[][] m;
   private int pixelsize = 0;
   private Font f;
   private JButton save;
   public ContentPanel(int[][] n, int[] info)
   {
      m = n;
      pixelsize = (int)Math.ceil(450/(m.length+8));
      qrinfo = info;
      setLayout(null);
      f = new Font("Bahnschrift", Font.BOLD, 20);
      JLabel ec = new JLabel("Error Correction Level: "+qrinfo[0], SwingConstants.CENTER);
      JLabel vs = new JLabel("Version: "+qrinfo[1], SwingConstants.CENTER);
      JLabel mp = new JLabel("Mask Pattern: "+qrinfo[2], SwingConstants.CENTER);
      ec.setFont(f);
      vs.setFont(f);
      mp.setFont(f);
      ec.setForeground(Color.BLACK);
      vs.setForeground(Color.BLACK);
      mp.setForeground(Color.BLACK);
      ec.setLocation(((framesize()/2)-((int)ec.getPreferredSize().getWidth()/2)), (int)(f.getSize()/2));
      vs.setLocation(((framesize()/2)-((int)vs.getPreferredSize().getWidth()/2)), (int)(3*f.getSize()/2));
      mp.setLocation(((framesize()/2)-((int)mp.getPreferredSize().getWidth()/2)), (int)(5*f.getSize()/2));
      ec.setSize((int)ec.getPreferredSize().getWidth(), (int)ec.getPreferredSize().getHeight());
      vs.setSize((int)vs.getPreferredSize().getWidth(), (int)vs.getPreferredSize().getHeight());
      mp.setSize((int)mp.getPreferredSize().getWidth(), (int)mp.getPreferredSize().getHeight());
      add(ec);
      add(vs);
      add(mp);
      save = new JButton("Save as PNG");
      save.addActionListener(new saveListener());
      save.setLocation(0,fontBlock()+framesize());
      save.setSize(framesize()/2, 25);
      add(save);
      JButton quit = new JButton("Quit");
      quit.addActionListener(new quitListener());
      quit.setLocation(framesize()/2,fontBlock()+framesize());
      quit.setSize(framesize()/2, 25);
      add(quit);
   }
   private class quitListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         System.exit(0);
      }
   }
   private class saveListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         BufferedImage temp = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_BYTE_BINARY);
         BufferedImage i = temp.getSubimage(0, fontBlock(), framesize(), framesize());
         Graphics2D gr = temp.createGraphics();
         gr.setColor(Color.WHITE);
         gr.fillRect(0, 0, framesize(), framesize()+fontBlock()+buttonWidth());
         for(int x=0;x<m.length;x++)
         {
            for(int y=0;y<m.length;y++)
            {
               if (m[y][x] == 0)
               {
                  gr.setColor(Color.WHITE);
               }
               else
               {
                  gr.setColor(Color.BLACK);
               }
               gr.fillRect((x+4)*pixelsize, (y+4)*pixelsize+fontBlock(), pixelsize, pixelsize);
            }
         }
         gr.dispose();
	      File directory = new File("./generated-qr-codes");
         if (!directory.exists())
	      {
	         directory.mkdirs();
	      }
         File output = new File("./generated-qr-codes/QR.png");
         int g = 1;
         while (output.exists())
         {
            output = new File("./generated-qr-codes/QR("+g+").png");
            g++;
         }
         try 
         {
            ImageIO.write(i, "png", output);
         } 
         catch (IOException ie)
         {
            ie.printStackTrace();
         }
      }
   }
   public int buttonWidth()
   {
      return save.getHeight();
   }
   public int fontBlock()
   {
      return f.getSize()*4;
   }
   public int framesize()
   {
      return (m.length+8)*pixelsize;
   }
   public void paintComponent(Graphics g)
   {
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, framesize(), framesize()+fontBlock()+buttonWidth());
      g.setColor(Color.BLACK);
      g.drawLine(0,fontBlock(),framesize(),fontBlock());
      g.drawLine(0,0,framesize(),0);
      for(int x=0;x<m.length;x++)
      {
         for(int y=0;y<m.length;y++)
         {
            if (m[y][x] == 0)
            {
               g.setColor(Color.WHITE);
            }
            else
            {
               g.setColor(Color.BLACK);
            }
            g.fillRect((x+4)*pixelsize, (y+4)*pixelsize+fontBlock(), pixelsize, pixelsize);
         }
      }
   }
}
class QREncoder
{
   int[] bytecapac = new int[]{17, 14, 11, 7, 32, 26, 20, 14, 53, 42, 32, 24, 78, 62, 46, 34, 106, 84, 60, 44, 134, 106, 74, 58, 154, 122, 86, 64, 192, 152, 108, 84, 230, 180, 130, 98, 271, 213, 151, 119, 321, 251, 177, 137, 367, 287, 203, 155, 425, 331, 241, 177, 458, 362, 258, 194, 520, 412, 292, 220, 586, 450, 322, 250, 644, 504, 364, 280, 718, 560, 394, 310, 792, 624, 442, 338, 858, 666, 482, 382, 929, 711, 509, 403, 1003, 779, 565, 439, 1091, 857, 611, 461, 1171, 911, 661, 511, 1273, 997, 715, 535, 1367, 1059, 751, 593, 1465, 1125, 805, 625, 1528, 1190, 868, 658, 1628, 1264, 908, 698, 1732, 1370, 982, 742, 1840, 1452, 1030, 790, 1952, 1538, 1112, 842, 2068, 1628, 1168, 898, 2188, 1722, 1228, 958, 2303, 1809, 1283, 983, 2431, 1911, 1351, 1051, 2563, 1989, 1423, 1093, 2699, 2099, 1499, 1139, 2809, 2213, 1579, 1219, 2953, 2331, 1663, 1273};
   int[] codewordcapac = new int[]{7,1,19,0,0,10,1,16,0,0,13,1,13,0,0,17,1,9,0,0,10,1,34,0,0,16,1,28,0,0,22,1,22,0,0,28,1,16,0,0,15,1,55,0,0,26,1,44,0,0,18,2,17,0,0,22,2,13,0,0,20,1,80,0,0,18,2,32,0,0,26,2,24,0,0,16,4,9,0,0,26,1,108,0,0,24,2,43,0,0,18,2,15,2,16,22,2,11,2,12,18,2,68,0,0,16,4,27,0,0,24,4,19,0,0,28,4,15,0,0,20,2,78,0,0,18,4,31,0,0,18,2,14,4,15,26,4,13,1,14,24,2,97,0,0,22,2,38,2,39,22,4,18,2,19,26,4,14,2,15,30,2,116,0,0,22,3,36,2,37,20,4,16,4,17,24,4,12,4,13,18,2,68,2,69,26,4,43,1,44,24,6,19,2,20,28,6,15,2,16,20,4,81,0,0,30,1,50,4,51,28,4,22,4,23,24,3,12,8,13,24,2,92,2,93,22,6,36,2,37,26,4,20,6,21,28,7,14,4,15,26,4,107,0,0,22,8,37,1,38,24,8,20,4,21,22,12,11,4,12,30,3,115,1,116,24,4,40,5,41,20,11,16,5,17,24,11,12,5,13,22,5,87,1,88,24,5,41,5,42,30,5,24,7,25,24,11,12,7,13,24,5,98,1,99,28,7,45,3,46,24,15,19,2,20,30,3,15,13,16,28,1,107,5,108,28,10,46,1,47,28,1,22,15,23,28,2,14,17,15,30,5,120,1,121,26,9,43,4,44,28,17,22,1,23,28,2,14,19,15,28,3,113,4,114,26,3,44,11,45,26,17,21,4,22,26,9,13,16,14,28,3,107,5,108,26,3,41,13,42,30,15,24,5,25,28,15,15,10,16,28,4,116,4,117,26,17,42,0,0,28,17,22,6,23,30,19,16,6,17,28,2,111,7,112,28,17,46,0,0,30,7,24,16,25,24,34,13,0,0,30,4,121,5,122,28,4,47,14,48,30,11,24,14,25,30,16,15,14,16,30,6,117,4,118,28,6,45,14,46,30,11,24,16,25,30,30,16,2,17,26,8,106,4,107,28,8,47,13,48,30,7,24,22,25,30,22,15,13,16,28,10,114,2,115,28,19,46,4,47,28,28,22,6,23,30,33,16,4,17,30,8,122,4,123,28,22,45,3,46,30,8,23,26,24,30,12,15,28,16,30,3,117,10,118,28,3,45,23,46,30,4,24,31,25,30,11,15,31,16,30,7,116,7,117,28,21,45,7,46,30,1,23,37,24,30,19,15,26,16,30,5,115,10,116,28,19,47,10,48,30,15,24,25,25,30,23,15,25,16,30,13,115,3,116,28,2,46,29,47,30,42,24,1,25,30,23,15,28,16,30,17,115,0,0,28,10,46,23,47,30,10,24,35,25,30,19,15,35,16,30,17,115,1,116,28,14,46,21,47,30,29,24,19,25,30,11,15,46,16,30,13,115,6,116,28,14,46,23,47,30,44,24,7,25,30,59,16,1,17,30,12,121,7,122,28,12,47,26,48,30,39,24,14,25,30,22,15,41,16,30,6,121,14,122,28,6,47,34,48,30,46,24,10,25,30,2,15,64,16,30,17,122,4,123,28,29,46,14,47,30,49,24,10,25,30,24,15,46,16,30,4,122,18,123,28,13,46,32,47,30,48,24,14,25,30,42,15,32,16,30,20,117,4,118,28,40,47,7,48,30,43,24,22,25,30,10,15,67,16,30,19,118,6,119,28,18,47,31,48,30,34,24,34,25,30,20,15,61,16};
   int[] alignpatterns = new int[]{6,18,6,22,6,26,6,30,6,34,6,22,38,6,24,42,6,26,46,6,28,50,6,30,54,6,32,58,6,34,62,6,26,46,66,6,26,48,70,6,26,50,74,6,30,54,78,6,30,56,82,6,30,58,86,6,34,62,90,6,28,50,72,94,6,26,50,74,98,6,30,54,78,102,6,28,54,80,106,6,32,58,84,110,6,30,58,86,114,6,34,62,90,118,6,26,50,74,98,122,6,30,54,78,102,126,6,26,52,78,104,130,6,30,56,82,108,134,6,34,60,86,112,138,6,30,58,86,114,142,6,34,62,90,118,146,6,30,54,78,102,126,150,6,24,50,76,102,128,154,6,28,54,80,106,132,158,6,32,58,84,110,136,162,6,26,54,82,110,138,166,6,30,58,86,114,142,170};
   int[] parsedalign;
   int[][] matrix;
   String[] datacodewords;
   String[] eccodewords;
   int[] mespoly;
   int[] genpoly;
   int[] blockinfo = new int[5];
   int[] formatinfo = new int[15];
   int[] versioninfo = new int[18];
   int version = 1;
   int er;
   int mas;
   String t;
   String finalmessage;
   int[] remainderbits = new int[]{0,7,7,7,7,7,0,0,0,0,0,0,0,3,3,3,3,3,3,3,4,4,4,4,4,4,4,3,3,3,3,3,3,3,0,0,0,0,0,0};
   ArrayList<String> erco;
   String[][] mask;
   public QREncoder(String s, int ec)
   {
      er = ec;
      t = s;
      int ddddd = s.getBytes(StandardCharsets.UTF_8).length; 
      while(ddddd>bytecapac[4*(version-1)+ec-1])
      {
         version++;
      }
      int num = version*4+17;
      matrix = new int[num][num];
      mask = new String[num][num];
      for(int x=0;x<num;x++)
      {
         for(int y=0;y<num;y++)
         {
            matrix[x][y] = 2;
         }
      }
      for(int x=0;x<5;x++)
      {
         blockinfo[x] = codewordcapac[20*(version-1)+5*(er-1)+x];  
      }
      eccodewords = new String[blockinfo[0]*(blockinfo[1]+blockinfo[3])];
      if (version>1)
      {
         parsedalign = new int[2];
         for(int x=0;x<parsedalign.length;x++)
         {
            parsedalign[x] = alignpatterns[x+(2*(version-2))];
         }
         if (version>6)
         {
            parsedalign = new int[3];
            for(int x=0;x<parsedalign.length;x++)
            {
               parsedalign[x] = alignpatterns[x+10+(3*(version-7))];
            }
            if (version>13)
            {
               parsedalign = new int[4];
               for(int x=0;x<parsedalign.length;x++)
               {
                  parsedalign[x] = alignpatterns[x+31+(4*(version-14))];
               }
               if (version>20)
               {
                  parsedalign = new int[5];
                  for(int x=0;x<parsedalign.length;x++)
                  {
                     parsedalign[x] = alignpatterns[x+59+(5*(version-21))];
                  }
                  if (version>27)
                  {
                     parsedalign = new int[6];
                     for(int x=0;x<parsedalign.length;x++)
                     {
                        parsedalign[x] = alignpatterns[x+94+(6*(version-28))];
                     }
                     if (version>34)
                     {
                        parsedalign = new int[7];
                        for(int x=0;x<parsedalign.length;x++)
                        {
                           parsedalign[x] = alignpatterns[x+136+(7*(version-35))];
                        }
                     }
                  }
               }
            }
         }
      }
   }
   public int[][] getMatrix()
   {
      return matrix;
   }
   public int getMask()
   {
      return mas;
   }
   public int getEC()
   {
      return er;
   }
   public int getVersion()
   {
      return version;
   }
   private int binToDec(String a)
   {
      return Integer.parseInt(a,2);
   }
   private String pad(int length, String s, char side)
   {
      if (side=='r')
      {
         while (s.length() < length)
         {
            s = s+"0";
         }
      }
      else if (side == 'l')
      {
         while (s.length() < length)
         {
            s = "0"+s;
         }
      }
      return s;
   }
   private int exps(int a)
   {
      int b=1;
      for (int x=0;x<a;x++)
      {
         b*=2;
         if (b>=256)
         {
            b^=285;
         }
      }
      return b;
   }
   private int log(int a)
   {
      int b = 0;
      while (a!=1)
      {
         if (a%2==1)
         {
            a^=285;
         }
         a/=2;
         b++;
      }
      return b;
   }
   public void fillMatrix()
   {
      int place = 0;
      int x = matrix.length-1;
      int y = matrix.length-1;
      int fin = finalmessage.length()-1;
      while (place<=fin)
      {
         for (int fffff = 0;fffff <matrix.length;fffff++)
         {
            if (matrix[y][x]==2)
            {
               matrix[y][x] = Integer.parseInt(Character.toString(finalmessage.charAt(0)));
               finalmessage = finalmessage.substring(1);
               place++;
            }
            x--;
            if (matrix[y][x]==2)
            {
               matrix[y][x] = Integer.parseInt(Character.toString(finalmessage.charAt(0)));
               finalmessage = finalmessage.substring(1);
               place++;
            }
            x++;
            y--;
         }
         x-=2;
         y++;
         if (x==6)
         {
            x--;
         }
         for (int eeeee = 0;eeeee <matrix.length;eeeee++)
         {
            if (matrix[y][x]==2)
            {
               matrix[y][x] = Integer.parseInt(Character.toString(finalmessage.charAt(0)));
               finalmessage = finalmessage.substring(1);
               place++;
            }
            x--;
            if (matrix[y][x]==2)
            {
               matrix[y][x] = Integer.parseInt(Character.toString(finalmessage.charAt(0)));
               finalmessage = finalmessage.substring(1);
               place++;
            }
            x++;
            y++;
         }
         x-=2;
         y-=1;
      }
   }
   public void structureFinalMessage()
   {
      finalmessage = "";
      for(int a=0;a<blockinfo[2];a++)
      {
         for (int b=0;b<blockinfo[1];b++)
         {
            finalmessage = finalmessage+datacodewords[a+b*blockinfo[2]];
            datacodewords[a+b*blockinfo[2]]="";
         }
         for(int c=0;c<blockinfo[3];c++)
         {
            finalmessage = finalmessage+datacodewords[a+blockinfo[1]*blockinfo[2]+c*blockinfo[4]];
            datacodewords[a+blockinfo[1]*blockinfo[2]+c*blockinfo[4]]="";
         }
      }
      for(int d=0;d<blockinfo[3];d++)
      {
         finalmessage = finalmessage+datacodewords[blockinfo[1]*blockinfo[2]+d*blockinfo[4]+blockinfo[4]-1];
         datacodewords[blockinfo[1]*blockinfo[2]+d*blockinfo[4]+blockinfo[4]-1]="";
      }
      for(int e=0;e<blockinfo[0];e++)
      {
         for(int f=0;f<(blockinfo[1]+blockinfo[3]);f++)
         {
            finalmessage = finalmessage+eccodewords[e+f*blockinfo[0]];
            eccodewords[e+f*blockinfo[0]]="";
         }
      }
      for(int abcdefghijklmnopqrstuvwxyz = 0; abcdefghijklmnopqrstuvwxyz<remainderbits[version-1];abcdefghijklmnopqrstuvwxyz++)
      {
         finalmessage = finalmessage+"0";
      }
      finalmessage = finalmessage.replaceAll("0","4");
      finalmessage = finalmessage.replaceAll("1","5");
   }
   private int mod255(int a)
   {
      if (a>=0)
      {
         while (!(a<255&&a>=0))
         {
            a-=255;
         }
      }
      else if (a<0)
      {
         while (!(a<255&&a>=0))
         {
            a+=255;
         }
      }
      return a;
   }
   public void alignAndTimePattern() 
   {
      for(int x=0;x<8;x++)
      {
         for(int y=0;y<8;y++)
         {
            matrix[x][y] = 0;
            matrix[matrix.length-1-x][y] = 0;
            matrix[x][matrix.length-1-y] = 0;
         }
      }
      for(int x=0;x<7;x++)
      {
         for(int y=0;y<7;y++)
         {
            matrix[x][y] = 1;
            matrix[matrix.length-1-x][y] = 1;
            matrix[x][matrix.length-1-y] = 1;
         }
      }
      for(int x=1;x<6;x++)
      {
         for(int y=1;y<6;y++)
         {
            matrix[x][y] = 0;
            matrix[matrix.length-1-x][y] = 0;
            matrix[x][matrix.length-1-y] = 0;
         }
      }
      for(int x=2;x<5;x++)
      {
         for(int y=2;y<5;y++)
         {
            matrix[x][y] = 1;
            matrix[matrix.length-1-x][y] = 1;
            matrix[x][matrix.length-1-y] = 1;
         }
      }
      if (version>1)
      {
         for(int x=0;x<parsedalign.length;x++)
         {
            for(int y=0;y<parsedalign.length;y++)
            {
               if (matrix[parsedalign[x]][parsedalign[y]] == 2)
               {
                  for(int a=0;a<5;a++)
                  {
                     for(int z=0;z<5;z++)
                     {
                        matrix[parsedalign[x]+a-2][parsedalign[y]+z-2] = 1;
                     }
                  }
                  for(int a=0;a<3;a++)
                  {
                     for(int z=0;z<3;z++)
                     {
                        matrix[parsedalign[x]+a-1][parsedalign[y]+z-1] = 0;
                     }
                  }
                  matrix[parsedalign[x]][parsedalign[y]] = 1;
               }
            }
         }
      }
      for(int x=6;x<matrix.length-6;x++)
      {
         if(matrix[x][6]==2&&matrix[6][x]==2)
            if(x%2==0)
            {
               matrix[x][6] = 1;
               matrix[6][x] = 1;
            }
            else
            {
               matrix[x][6] = 0;
               matrix[6][x] = 0;
            }
      }
      matrix[matrix.length-8][8] = 1;
   }
   public void reserveSpaces()
   {
      for(int x=0;x<7;x++)
      {
         matrix[matrix.length-1-x][8]=3;
      }
      for(int x=0;x<8;x++)
      {
         matrix[8][matrix.length-1-x]=3;
      }
      for(int x=0;x<9;x++)
      {
         if (matrix[x][8]==2)
         {
            matrix[8][x]=3;
            matrix[x][8]=3;
         }
      }
      if (version>=7)
      {
         for(int x=0;x<3;x++)
         {
            for(int y=0;y<6;y++)
            {
               matrix[y][matrix.length-9-x]=3;
               matrix[matrix.length-9-x][y]=3;
            }
         }
      }
   }
   private String binXOR(String e, String f)
   {
      int aa = binToDec(e);
      int bb = binToDec(f);
      aa^=bb;
      String cc = Integer.toBinaryString(aa);
      return cc;
   }
   public void printAsCharArray()
   {
      char[][] matrix1 = new char[matrix.length][matrix.length];
      for(int x=0;x<matrix1.length;x++)
      {
         for(int y=0;y<matrix1.length;y++)
         {
            if (matrix[x][y] == 1)
            {
               matrix1[x][y] = 'B';
            }
            else if (matrix[x][y] == 0)
            {
               matrix1[x][y] = ' ';
            }
            else if (matrix[x][y] == 4)
            {
               matrix1[x][y] = '.';
            }
            else if (matrix[x][y] == 5)
            {
               matrix1[x][y] = 'x';
            }
            else if (matrix[x][y] == 3)
            {
               matrix1[x][y] = 'R';
            }
            else
            {
               matrix1[x][y] = '-';
            }
         }
      }
      for(int x=0;x<matrix1.length;x++)
      {
         for(int y=0;y<matrix1.length;y++)
         {
            System.out.print(matrix1[x][y]+" ");
         }
         System.out.println();
      }
   }
   public void encode()
   {
      String[] message = new String[t.getBytes(StandardCharsets.UTF_8).length];
      byte[] ms = t.getBytes();
      try
      {
         ms = t.getBytes(StandardCharsets.UTF_8);
      }
      catch(Exception e)
      {
      }
      for(int x=0;x<ms.length;x++)
      {
         message[x] = Integer.toBinaryString((int)(ms[x]&255));
         
      }   
      for(int x=0;x<message.length;x++)
      {
         message[x] = pad(8, message[x], 'l');
      }
      String tomp = "0100";
      String teemp = Integer.toBinaryString(ms.length);
      if (version<10)
      {
         while (teemp.length()<8)
         {
            teemp = "0"+teemp;
         }
      }
      else
      {
         while (teemp.length()<16)
         {
            teemp = "0"+teemp;
         }
      }
      String term = "0000";
      int temp = blockinfo[1]*blockinfo[2]+blockinfo[3]*blockinfo[4]-message.length-1-(teemp.length()/8);
      String fill = tomp+teemp;
      for(int x=0;x<message.length;x++)
      {
         fill = fill+message[x];
      }
      fill = fill+term;
      String tempus = paddingbytes(temp);
      fill = fill+tempus;
      String[] messages = new String[fill.length()/8];
      for(int x=0;x<messages.length;x++)
      {
         messages[x] = fill.substring(8*x,8*(x+1));
      }
      datacodewords = messages;
   }
   private String paddingbytes(int a)
   {
      String temp="";
      for (int x=0;x<a;x++)
      {
         if (x%2 == 0)
         {
            temp = temp+"11101100";
         }
         else
         {
            temp = temp+"00010001";
         }
      }
      return temp;
   }
   public void print()
   {
      for(int x=0;x<matrix.length;x++)
      {
         for(int y=0;y<matrix.length;y++)
         {
            System.out.print(matrix[x][y]+" ");
         }
         System.out.println();
      }
   }
   private int[] shift(int[] array)
   {
      int[] temp = new int[array.length-1];
      for(int x=1;x<array.length;x++)
      {
         temp[x-1] = array[x];
      }
      return temp;
   }
   private void generate(int n)
   {
      genpoly = new int[]{1,1};
      for(int x=1;x<n;x++)
      {
         int[] y=new int[]{1,exps(mod255(x))};
         genpoly = polymult(genpoly,y);
      }
   }  
   private int GFmultiply(int a, int b)
   {
      String c = Integer.toBinaryString(a);
      String d = Integer.toBinaryString(b);
      String[] f = new String[d.length()];
      int e = 0;
      for(int x=0;x<d.length();x++)
      {
         if (d.substring(x,x+1).equals("1"))
         {
            String z="";
            for(int v=0;v<d.length()-1-x;v++)
            {
               z+="0";
            }
            f[x] = c+z;
         }
         else
         {
            f[x] = "0";
         }
      }
      for (int y=0;y<f.length;y++)
      {
         e^=binToDec(f[y]);
      }
      e = mod2divremainder(285, e);
      return e;
   }
   private int mod2divremainder(int divisor, int dividend)
   {
      String quotient="";
      String remainder="";
      String c = Integer.toBinaryString(dividend);
      String d = pad(c.length(),Integer.toBinaryString(divisor),'r');
      while (c.length()>=d.length())
      {
         c = pad(c.length()-1,binXOR(c,d),'l');
         if (c.substring(0,1).equals("1"))
         {
            d = pad(c.length(),Integer.toBinaryString(divisor),'r');
            quotient = quotient+"1";
         }
         else
         {
            d = pad(c.length(),"",'r');
            quotient = quotient+"0";
         }
      }
      remainder = c;
      return binToDec(remainder);
   }
   private int[] polymult(int[] a, int[] b)
   { 
      int[] prod = new int[a.length + b.length - 1]; 
      for (int i = 0; i < a.length; i++)  
      { 
         for (int j = 0; j < b.length; j++)  
         { 
            prod[i+j] ^= GFmultiply(a[i], b[j]);
         } 
      } 
      return prod;
   }
   public void errorCorrect()
   {
      erco = new ArrayList<String>();
      for(int x=0;x<blockinfo[1];x++)
      {
         mespoly = new int[blockinfo[2]+blockinfo[0]];
         for(int y=0;y<blockinfo[2];y++)
         {
            mespoly[y] = binToDec(""+datacodewords[y+x*blockinfo[2]]); 
         }
         errorCorrection();
      }
      for(int x=0;x<blockinfo[3];x++)
      {
         mespoly = new int[blockinfo[4]+blockinfo[0]];
         for(int y=0;y<blockinfo[4];y++)
         {
            mespoly[y] = binToDec(""+datacodewords[y+x*blockinfo[4]+blockinfo[1]*blockinfo[2]]); 
         }
         errorCorrection();
      }
      for(int x=0;x<erco.size();x++)
      {
         eccodewords[x] = erco.get(x);
      }
   }
   private void errorCorrection()
   {
      while(mespoly.length!=blockinfo[0]) 
      {
         generate(blockinfo[0]);            
         for (int x=0;x<genpoly.length;x++) 
         {
            genpoly[x] = exps(mod255(log(mod255(genpoly[x]))+log(mod255(mespoly[0]))));
         }
         for(int x=0;x<genpoly.length;x++) 
         {
            mespoly[x] ^= genpoly[x];
         }
         mespoly = shift(mespoly);  
         while (!(mespoly[0]>0||mespoly.length==blockinfo[0]))
         {
            mespoly = shift(mespoly);
         }
      }
      for(int x=1;x<=blockinfo[0];x++) 
      {
         erco.add(pad(8,Integer.toBinaryString(mespoly[x-1]),'l'));
      }
   }
   public void findBestMask()
   {
      int[] maskpenalties = new int[8];
      for(int x=0;x<8;x++)
      {
         int[][] temp = new int[matrix.length][matrix.length];
         for (int xx=0;xx<matrix.length;xx++)
         {
            for (int y=0;y<matrix.length;y++)
            {
               temp[xx][y] = matrix[xx][y];
            }
         }
         maskpenalties[x] = maskpenalty(mask(temp,x));
      }
      int xxx = 99999;
      int index = 0;
      {
         for(int xx=0;xx<8;xx++)
         {
            if (maskpenalties[xx] < xxx)
            {
               xxx = maskpenalties[xx];
               index = xx;
            }
         }
         mas = index;
         matrix = mask(matrix,index);
      }
   }
   private int[][] mask(int[][] array, int a)
   {
      mask = new String[array.length][array.length];
      for (int x=0;x<array.length;x++)
      {
         for (int y=0;y<array.length;y++)
         {
            if (array[x][y] == 4 || array[x][y] == 5)
            {
               if (a==0)
               {
                  if ((x+y)%2==0)
                  {
                     swap(array,x,y);
                     mask[x][y]="X";
                  }
               }
               else if (a==1)
               {
                  if (x%2==0)
                  {
                     swap(array,x,y);
                     mask[x][y]="X";
                  }
               }
               else if (a==2)
               {
                  if (y%3==0)
                  {
                     swap(array,x,y);
                     mask[x][y]="X";
                  }
               }
               else if (a==3)
               {
                  if ((x+y)%3==0)
                  {
                     swap(array,x,y);
                     mask[x][y]="X";
                  }
               }
               else if (a==4)
               {
                  if (((int)Math.floor(x/2)+(int)Math.floor(y/3))%2==0)
                  {
                     swap(array,x,y);
                     mask[x][y]="X";
                  }
               }
               else if (a==5)
               {
                  if (((x*y)%3)+((x*y)%2)==0)
                  {
                     swap(array,x,y);
                     mask[x][y]="X";
                  }
               }
               else if (a==6)
               {
                  if ((((x*y)%3)+((x*y)%2))%2==0)
                  {
                     swap(array,x,y);
                     mask[x][y]="X";
                  }
               }
               else if (a==7)
               {
                  if ((((x*y)%3)+((x+y)%2))%2==0)
                  {
                     swap(array,x,y);
                     mask[x][y]="X";
                  }
               }
            }
         }
      }
      for (int x=0;x<array.length;x++)
      {
         for (int y=0;y<array.length;y++)
         {
            if (array[x][y] == 4)
            {
               array[x][y] = 0;
               mask[x][y]=" ";
            }
            else if (array[x][y] == 5)
            {
               array[x][y] = 1;
               mask[x][y]=" ";
            }
         }
      }
      formatVersionInfo(a,array);
      return array;
   }
   private void swap(int[][] array, int x, int y)
   {
      if (array[x][y] == 4)
      {
         array[x][y] = 1;
      }
      if (array[x][y] == 5)
      {
         array[x][y] = 0;
      }
   }
   private int maskpenalty(int[][] array)
   {
      int pen1=0;
      int pen2=0;
      int pen3=0;
      int pen4=0;
      int cons = 0;
      int darksquares=0;
      for(int x=0;x<array.length;x++)
      {
         cons = 1;
         for(int y=0;y<array.length-1;y++)
         {
            if (array[x][y] == array[x][y+1])
            {
               cons++;
            }
            else
            {
               cons = 1;
            }
            if (cons==5)
            {
               pen1+=3;
            }
            else if (cons>5)
            {
               pen1+=1;
            }
         }
      }
      for(int x=0;x<array.length-1;x++)
      {
         cons = 1;
         for(int y=0;y<array.length;y++)
         {
            if (array[x][y] == array[x+1][y])
            {
               cons++;
            }
            else
            {
               cons = 1;
            }
            if (cons==5)
            {
               pen1+=3;
            }
            else if (cons>5)
            {
               pen1+=1;
            }
         }
      }
      for(int x=0;x<array.length-1;x++)
      {
         for(int y=0;y<array.length-1;y++)
         {
            String temp = "";
            for(int z=0;z<1;z++)
            {
               for(int a=0;a<1;a++)
               {
                  temp = temp+""+array[x+a][y+z];
               }
            }
            if (temp.equals("1111")||temp.equals("0000"))
            {
               pen2+=3;
            }
         }
      }
      for(int x=0;x<array.length;x++)
      {
         for(int y=0;y<array.length-11;y++)
         {
            String temp = "";
            for(int z=0;z<9;z++)
            {
               temp = temp+""+array[x][y+z];
            }
            if (temp.equals("10111010000")||temp.equals("00001011101"))
            {
               pen3+=40;
            }
         }
      }
      for(int x=0;x<array.length-11;x++)
      {
         for(int y=0;y<array.length;y++)
         {
            String temp = "";
            for(int z=0;z<9;z++)
            {
               temp = temp+""+array[x+z][y];
            }
            if (temp.equals("10111010000")||temp.equals("00001011101"))
            {
               pen3+=40;
            }
         }
      }
      for(int x=0;x<array.length;x++)
      {
         for(int y=0;y<array.length;y++)
         {
            if (array[x][y]==1)
            {
               darksquares++;
            }
         }
      }
      int area = array.length*array.length;
      int temp = darksquares*100/area;
      int temp2 = (int)Math.floor(temp/5);
      temp = temp2*5; 
      temp2 = temp+5;
      temp = Math.abs(temp-50);
      temp2 = Math.abs(temp2-50);
      temp/=5;
      temp2/=5;
      if (temp>temp2)
      {
         pen4 += 10*temp2;
      }
      else
      {
         pen4 += 10*temp;
      }
      return pen1+pen2+pen3+pen4;
   }
   private void formatVersionInfo(int mask, int[][] array)
   {
      String a="";
      if (er == 1)
      {
         a = "01";
      }
      else if (er == 2)
      {
         a = "00";
      }
      else if (er == 3)
      {
         a = "11";
      }
      else if (er == 4)
      {
         a = "10";
      }
      String b = Integer.toBinaryString(mask);
      while (b.length()!=3)
      {
         b = "0"+b;
      }
      String e = a+b;
      e=e+"0000000000";
      while (e.charAt(0) == '0'&&e.length()>1)
      {
         e = e.substring(1);
      }
      while (e.length()>10)
      {
         e = binXOR(e, pad(e.length(), "10100110111", 'r'));
      }
      while (e.length()<10)
      {
         e = "0"+e;
      }
      e = a+b+e;
      e=binXOR(e,"101010000010010");
      while (e.length()<15)
      {
         e = "0"+e;
      }
      for(int ycy = 0; ycy < 15; ycy++)
      {
         formatinfo[ycy] = Integer.parseInt(e.substring(ycy,ycy+1));
      }
      for(int cc = 0;cc<6;cc++)
      {
         array[8][cc] = formatinfo[cc];
         array[cc][8] = formatinfo[14-cc];
         array[array.length-1-cc][8] = formatinfo[cc];
         array[8][array.length-1-cc] = formatinfo[14-cc];
      }
      array[array.length-7][8] = formatinfo[6];
      array[8][7] = formatinfo[6];
      array[8][array.length-8] = formatinfo[7];
      array[8][8] = formatinfo[7];
      array[8][array.length-7] = formatinfo[8];
      array[7][8] = formatinfo[8];
      if (version>=7)
      {
         String x = Integer.toBinaryString(version);
         x = x+"000000000000";
         String y = "1111100100101";
         while (x.length()>12)
         {
            x = binXOR(x, pad(x.length(),y,'r'));
         }
         while (12 > x.length())
         {
            x = "0"+x;
         }
         x = pad(6,Integer.toBinaryString(version),'l')+x;
         for(int yy = 0; yy < 18; yy++)
         {
            versioninfo[yy] = Integer.parseInt(x.substring(17-yy,18-yy));
         }
         for(int zz=0;zz<versioninfo.length;zz++)
         {
            array[(int)Math.floor(zz/3)][array.length+(zz%3)-11] = versioninfo[zz];
            array[array.length+(zz%3)-11][(int)Math.floor(zz/3)] = versioninfo[zz]; 
         }
      }
   }
}