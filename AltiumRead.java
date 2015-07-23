import org.apache.poi.poifs.filesystem.*;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Paint;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;


public class AltiumRead {
	static int[][] wires = new int[4096][4];
	
	private Frame mainFrame;
	   private Label headerLabel;
	   private Label statusLabel;
	   private Panel controlPanel;

	   public AltiumRead(){
	      prepareGUI();
	   }
	
	
	  public static void main(String[] args) throws IOException {
		    if (args.length==0 || args.length>2) {
		      System.out.println("Please specify valid file");
		      return;
		    }

		    
		    List<String> argsList = Arrays.asList(args);
		    if (!argsList.contains("-c")) {
		      unpackFile(new File(args[0]));
		    } else {
		      ParametricFile parametricFile = new ParametricFile();
		      parametricFile.setWires(wires);
		      POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(new File(args[args.length - 1])));
		      if (parametricFile.load(new DocumentInputStream((DocumentEntry) fs.getRoot().getEntry("FileHeader")))) {
		        System.out.println("Successful!");
//		        System.out.println(parametricFile.getJSON());
//		        System.out.println(parametricFile.getJSON().toString());
		      }
		    }
		    
		    AltiumRead  altiumRead = new AltiumRead();
		    altiumRead.showImageDemo();
		  }
		  
		  private static void unpackFile(File file) throws IOException {
		    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
		    System.out.println( file.getName() + ".export");
//		    exportDirEntry(fs.getRoot(), new File(file.getParentFile(), file.getName() + ".export"));
		    exportDirEntry(fs.getRoot(), new File(file.getParentFile(), "test.export"));
		  }

		  private static void exportDirEntry(DirectoryEntry dirEntry, File dir) throws IOException {
		    if (!dir.mkdirs()) throw new IOException("Can't create dir");

		    for (Entry entry : dirEntry) {
		      if (entry.isDirectoryEntry()) {
		        exportDirEntry((DirectoryEntry) entry, new File(dir, entry.getName()));
		      } else if (entry.isDocumentEntry()) {
		        exportFileEntry(entry, dir);
		      }
		    }
		  }

		  private static void exportFileEntry(Entry entry, File dir) throws IOException {
		    OutputStream output = new FileOutputStream(new File(dir, entry.getName()));
		    InputStream input = new DocumentInputStream((DocumentEntry) entry);

		    byte[] buffer = new byte[4096]; // Adjust if you want
		    int bytesRead;
		    while ((bytesRead = input.read(buffer)) != -1)
		      output.write(buffer, 0, bytesRead);
		  }
		  
		  


			   private void prepareGUI(){
			      mainFrame = new Frame("Altium Reader");
			      mainFrame.setSize(1200,800);
			      mainFrame.setLayout(new GridLayout(1, 1));
			      mainFrame.addWindowListener(new WindowAdapter() {
			         public void windowClosing(WindowEvent windowEvent){
			            System.exit(0);
			         }        
			      });    
			      headerLabel = new Label();
			      headerLabel.setAlignment(Label.CENTER);
			      statusLabel = new Label();        
			      statusLabel.setAlignment(Label.CENTER);
			      statusLabel.setSize(350,100);

			      controlPanel = new Panel();
			      controlPanel.setLayout(new FlowLayout());

//			      mainFrame.add(headerLabel);
//			      mainFrame.add(controlPanel);
//			      mainFrame.add(statusLabel);
			      mainFrame.setVisible(true);  
			   }

			   private void showImageDemo(){
			      headerLabel.setText("Control in action: Image"); 

			      mainFrame.add(new ImageComponent());
//			      controlPanel.add(new ImageComponent());
//			      controlPanel.add(new ImageComponent("resources/java.jpg"));
			      mainFrame.setVisible(true);  
			   }
				
			   class ImageComponent extends Component {

//			      BufferedImage img;

			      public void paint(Graphics g) {
			    	  g.setColor( Color.RED);
			    	  g.drawLine( 100, 200, 200, 100 );
			    	  g.drawLine( 100, 100, 200, 200 );
			    	  g.drawLine(50,  50,  250,  50 );
			    	  g.drawLine(250,  50,  250,  250);
			    	  g.drawLine(250,  250,  50,  250);
			    	  g.drawLine(50,  250,  50,  50);
			    	  
			    	  int nextWire = 0;
			    	  
			    	  while( wires[nextWire][0] != 0 )
			    		  g.drawLine( wires[nextWire][0],  wires[nextWire][1], wires[nextWire][2], wires[nextWire][3]);
			      }

//			      public ImageComponent(String path) {
//			         try {
//			            img = ImageIO.read(new File(path));
//			         } catch (IOException e) {
//			            e.printStackTrace();
//			         }
//			      }

			      public Dimension getPreferredSize() {
			    	  return new Dimension( 1200, 800);
//			         if (img == null) {
//			            return new Dimension(100,100);
//			         } else {
//			            return new Dimension(img.getWidth(), img.getHeight());
//			         }
			      }
			   }
		  }


