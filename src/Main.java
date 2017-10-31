import java.util.LinkedList;

public class Main {
	
	
	
	public static void main(String[] args) {
	
	//drei verschiedene Strafkostens�tze --> drei L�sungsobjekte 
	//alle drei am Ende ausgeben
		L�sung l�sung1 = L�sungErstellen(100);
		L�sung l�sung2 = L�sungErstellen(200);
		L�sung l�sung3 = L�sungErstellen(3000);
		
		System.out.println("\n"+l�sung1);
		System.out.println("\n"+l�sung2);
		System.out.println("\n"+l�sung3);
	
	}
	
	private static L�sung L�sungErstellen(int strafkostensatz) {
		
		L�sung finaleL�sung = null;
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
			L�sung l�sung = new L�sung(strafkostensatz, tourp.getZeitkostenFahrt(), tourp.getStreckenkostenFahrt(), produktp.calculateCostsForProductionTime(), tourp.get�brigeBedarfe(), fahrzeuge, produktp.numberOfActiveProductionLines(), tourp.getStrecke());	
			try {
			if (finaleL�sung.getGesamtkosten()>l�sung.getGesamtkosten()) {
				finaleL�sung = l�sung;
			}} catch (NullPointerException e) {finaleL�sung = l�sung;}
		}
		
		return finaleL�sung;
		
	}


}
