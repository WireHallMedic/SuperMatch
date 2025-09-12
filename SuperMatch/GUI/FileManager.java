package SuperMatch.GUI;

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.imageio.*;

public class FileManager
{
   // File I/O
   public static BufferedReader loadTextFile(String fileName)
   {
      BufferedReader bReader = null;
      try
      {
         InputStream is = FileManager.class.getResourceAsStream(fileName);
         bReader = new BufferedReader(new InputStreamReader(is));
      }
      catch(Exception ex)
      {
         System.out.println(String.format("Unable to load file %s: %s\n%s", fileName, ex.toString(),
            "If running as a .jar, make sure the file's location is listed in the manifest file."));
      }
      return bReader;
   }
   
   // load image from file
   public static BufferedImage loadImageFile(String fileName, int w, int t)
   {
      // load the image
      BufferedImage image = null;
      try
      {
         InputStream is = FileManager.class.getResourceAsStream(fileName);
         image = ImageIO.read(is);
      }
      catch(Exception ex)
      {
         System.out.println(String.format("Unable to load file %s: %s\n%s", fileName, ex.toString(),
            "If running as a .jar, make sure the file's location is listed in the manifest file."));
      }
      return image;
   }
}