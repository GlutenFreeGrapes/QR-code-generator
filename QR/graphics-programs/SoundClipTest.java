import java.net.*;
import javax.sound.sampled.*;
import javax.swing.*;
   
// To play sound using Clip, the process need to be alive.
// Hence, we use a Swing application.
public class SoundClipTest extends JFrame {
   
   // Constructor
   public SoundClipTest() {
      
      try {
         AudioInputStream audioIn = AudioSystem.getAudioInputStream(new URL("https://cdn.discordapp.com/attachments/711352466112774144/811989252928700436/Wilhelm_Scream.ogg.wav"));
         Clip clip = AudioSystem.getClip();
         clip.open(audioIn);
         clip.start();
      } catch (Exception e) {
      }
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setTitle("Test Sound Clip");
      this.setSize(0, 0);
      this.setVisible(true);
   }

   public static void main(String[] args) {
      new SoundClipTest();
   }
}