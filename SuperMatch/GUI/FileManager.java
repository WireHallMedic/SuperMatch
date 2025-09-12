package SuperMatch.GUI;

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.imageio.*;
import SuperMatch.SMMain;
import SuperMatch.Board.*;

public class FileManager
{
   public static final int TILE_SIZE = GameBoard.TILE_SIZE;
   
   // File I/O
   public static BufferedReader loadTextFile(String fileName)
   {
      BufferedReader bReader = null;
      try
      {
         InputStream is = SMMain.class.getResourceAsStream(fileName);
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
   public static BufferedImage loadImageFile(String fileName)
   {
      // load the image
      BufferedImage image = null;
      try
      {
         InputStream is = SMMain.class.getResourceAsStream(fileName);
         image = ImageIO.read(is);
      }
      catch(Exception ex)
      {
         System.out.println(String.format("Unable to load file %s: %s\n%s", fileName, ex.toString(),
            "If running as a .jar, make sure the file's location is listed in the manifest file."));
      }
      return image;
   }
   
   public static BufferedImage[] loadTileImages()
   {
      BufferedImage[] imageArr = new BufferedImage[TileType.values().length];
      BufferedImage imageStrip = FileManager.loadImageFile("/SuperMatch/Resources/Tiles.png");
      
      for(int i = 0; i < imageArr.length; i++)
         imageArr[i] = imageStrip.getSubimage(0, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
      return imageArr;
   }
}