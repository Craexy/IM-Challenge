import java.util.LinkedList;

public class Main {
	
	
	
	public static void main(String[] args) {
	
	//drei verschiedene Strafkostensätze --> drei Lösungsobjekte 
	//alle drei am Ende ausgeben
		Lösung lösung1 = LösungErstellen(100);
		Lösung lösung2 = LösungErstellen(200);
		Lösung lösung3 = LösungErstellen(3000);
		
		System.out.println("\n"+lösung1);
		System.out.println("\n"+lösung2);
		System.out.println("\n"+lösung3);
	
	}
	
	private static Lösung LösungErstellen(int strafkostensatz) {
		
		Lösung finaleLösung = null;
		Tourenplaner tourp;
		
		for (int j=0; j<100
				;j++) {
			System.out.println("\n------Start------");
			if (j%2==0) {tourp = new Tourenplaner(strafkostensatz, 1);}
			else {tourp = new Tourenplaner(strafkostensatz, 2);}
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
			try {
			if (finaleLösung.getGesamtkosten()>lösung.getGesamtkosten()) {
				finaleLösung = lösung;
			}} catch (NullPointerException e) {finaleLösung = lösung;}
		}
		
		return finaleLösung;
		
	}


}
