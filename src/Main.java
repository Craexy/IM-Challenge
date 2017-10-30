import java.util.LinkedList;

public class Main {
	
	
	
	public static void main(String[] args) {
	int strafkostensatz = 100;
		
	Tourenplaner tourp = new Tourenplaner(strafkostensatz,2);
	LinkedList<Fahrzeug> fahrzeuge = tourp.getFahrzeuge();
	
	Produktionsplaner produktp = new Produktionsplaner(fahrzeuge, strafkostensatz);
	MedUeberschuss[] ueberschuesse = new MedUeberschuss[produktp.getMedUeberschuesse().size()];
	
	int i = 0;
	for (MedUeberschuss med : produktp.getMedUeberschuesse()) {
		ueberschuesse[i] = med;
		i++;
	}
	tourp.verteileReste(ueberschuesse);
	
	Lösung lösung = new Lösung(strafkostensatz, tourp.getZeitkostenFahrt(), tourp.getStreckenkostenFahrt(), produktp.calculateCostsForProductionTime(), tourp.getÜbrigeBedarfe(), fahrzeuge, produktp.numberOfActiveProductionLines(), tourp.getStrecke());	
	System.out.println(lösung);
	}


}
