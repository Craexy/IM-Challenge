
public class Main {
	
	
	
	public static void main(String[] args) {
	
	Tourenplaner tourp = new Tourenplaner();
	Produktionsplaner produktionp = new Produktionsplaner(tourp.getFahrzeuge());
	
	
	}


}
