
public class Main {
	
	
	
	public static void main(String[] args) {

		
	Time zeit1 = new Time(8,20,0);	
	Time zeit2 = new Time(7,50,0);
	
	System.out.println(zeit2.compareTo(zeit1));
	
	
	
		
	Tourenplaner tour = new Tourenplaner();
	System.out.println(tour.getFahrzeuge().get(0).getStrecke());
	//Routenplaner route = new Routenplaner();

	
	}


}
