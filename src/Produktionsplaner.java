import java.io.FileInputStream;
import java.io.IOException;
import java.util.StringTokenizer;

public class Produktionsplaner {
	
	private int Fahrzeuge;
	private int[][] Distanzen;
	private String[] Daten;
	
	
	public Produktionsplaner(){
		Distanzen = new int[7][7];
		leseDaten();
		
	}
	
	private void leseDaten(){
		 String filename = "D:/Dropbox/Studium/Module/6. Semester/IM Challenge/Daten/L_ij.txt";
		 String str = null;
		 
		    byte buffer[] = new byte[ 4000 ];
		    FileInputStream in = null;
		    try
		    {
		      in = new FileInputStream( filename );
		      int len = in.read( buffer, 0, 4000 );
		      str = new String( buffer, 0, len );
		      //System.out.println( str );
		    }
		    catch ( IOException e ) { System.out.println( e ); }
		    finally
		    {
		      try {
		        if ( in != null ) in.close();
		      } catch (IOException e) {}
		    }
		    
		Daten = str.split("\\s");    
		for (int i = 3; i<29*3; i=i+4) {
			System.out.println("Hier wurde unterteilt: " + Daten[i]);
			}
		}
	
	
	
}
