import java.util.LinkedList;

public class Main {
	
	
	
	public static void main(String[] args) {
	Tourenplaner tourp = new Tourenplaner();
	LinkedList<Fahrzeug> fahrzeuge = tourp.getFahrzeuge();
	
	Produktionsplaner produktp = new Produktionsplaner(fahrzeuge);
	MedUeberschuss[] ueberschuesse = new MedUeberschuss[produktp.getMedUeberschuesse().size()];
	
	int i = 0;
	for (MedUeberschuss med : produktp.getMedUeberschuesse()) {
		ueberschuesse[i] = med;
		i++;
	}
	tourp.verteileReste(ueberschuesse);
	
	Lösung lösung = new Lösung(tourp.getZeitkostenFahrt(), tourp.getStreckenkostenFahrt(), 0, tourp.getStrafkosten(), fahrzeuge, null, tourp.getStrecke());	
	System.out.println(lösung);
	}


}
