import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Produktionsplaner {
	
	private LinkedList <Fahrzeug> fahrzeuge;
	
	private LinkedList<int[]> fahrzeugBedarfe;
	private LinkedList <Time[]> bedarfszeitpunkte;
	private LinkedList <Time[]> produktionsstartZeitpunkte;
	
	
	public Produktionsplaner(LinkedList<Fahrzeug> fahrzeuge){
		this.fahrzeuge = fahrzeuge;	
		
		fahrzeugBedarfe = new LinkedList<int[]>();
		bedarfszeitpunkte = new LinkedList<Time[]>();
		produktionsstartZeitpunkte = new LinkedList<Time[]>();
		
		planeProduktion();
		
	}
	
	public void planeProduktion(){
		getNecessaryParameters();
		
		//Prüfe auf Konflikte der Bedarfe und der Produktionskapazitäten
	    prüfeAufKonflikte();
	}
	
	public void getNecessaryParameters(){
		
		//Initialisierung der Zwischenspeicher für die relevanten Produktionsparameter
		 int[] bedarfe = new int[4];
		 Time[]  produktionsende= new Time[4];
		 Time[] produktionsstart = new Time[4];
		 int[] produktionsdauer= new int[4];
		 
		// Berechnung für alle Fahrzeuge
		for(int i=0;i<fahrzeuge.size();i++){
			
			//Ein Fahrzeug wird betrachtet und alle die Produktion betreffenden Parameter werden berechnet
			bedarfe[0] = fahrzeuge.get(i).get60();
			bedarfe[1] = fahrzeuge.get(i).get120();
			bedarfe[2] = fahrzeuge.get(i).get250();
			bedarfe[3] = fahrzeuge.get(i).get500();
			
			produktionsende[0] = fahrzeuge.get(i).getStartzeitBeladung60();
			produktionsende[1] = fahrzeuge.get(i).getStartzeitBeladung120();
			produktionsende[2] = fahrzeuge.get(i).getStartzeitBeladung250();
			produktionsende[3] = fahrzeuge.get(i).getStartzeitBeladung500();
			
			
			//getProduktionsdauer und getStartzeitProduktion
			int dauer60 = (int)Math.ceil((double)bedarfe[0]/150)*15;
			int dauer120 = (int)Math.ceil((double)bedarfe[1]/150)*30;
			int dauer250 = (int)Math.ceil((double)bedarfe[2]/150)*60;
			int dauer500 = (int)Math.ceil((double)bedarfe[3]/150)*120;
			
			produktionsdauer[0] = dauer60;
			produktionsdauer[1] = dauer120;
			produktionsdauer[2] = dauer250;
			produktionsdauer[3] = dauer500;
			
			
			Time spMed60 = new Time(produktionsende[0].getStunden(), produktionsende[0].getMinuten(),0).reduceTime(produktionsdauer[0]);
			Time spMed120 = new Time(produktionsende[1].getStunden(), produktionsende[1].getMinuten(),0).reduceTime(produktionsdauer[1]);
			Time spMed250 = new Time(produktionsende[2].getStunden(), produktionsende[2].getMinuten(),0).reduceTime(produktionsdauer[2]);
			Time spMed500 = new Time(produktionsende[3].getStunden(), produktionsende[3].getMinuten(),0).reduceTime(produktionsdauer[3]);
		
			produktionsstart[0] = spMed60;
			produktionsstart[1] = spMed120;
			produktionsstart[2] = spMed250;
			produktionsstart[3] = spMed500;
			
			
			System.out.println("\n");
			System.out.println("Produktionsanfrage Fahrzeug "+(i+1)+"\n");
			
			System.out.println("Startzeit der Produktion von Med60: "+spMed60.toString());
			//startzeitenProduktion.add(spMed120);
			System.out.println("Startzeit der Produktion von Med120: "+spMed120.toString());
			//startzeitenProduktion.add(spMed250);
			System.out.println("Startzeit der Produktion von Med250: "+spMed250.toString());
			//startzeitenProduktion.add(spMed500);
			System.out.println("Startzeit der Produktion von Med500: "+spMed500.toString());
			
			System.out.println("\n");
			
			
			//Übergeordnetete Datenstruktur zum Zugriff auf alle Fahrzeuge
			fahrzeugBedarfe.add(bedarfe);
			produktionsstartZeitpunkte.add(produktionsstart);
			bedarfszeitpunkte.add(produktionsende);
			
			
			//Testen der Datenstruktur; Auf diese Weise muss Zugriff auf Arrays in den LinkedLists erfolgen
			int[] tester = fahrzeugBedarfe.getFirst();
			System.out.println(tester[0]);
			System.out.println(tester[1]);
			System.out.println(tester[2]);
			System.out.println(tester[3]);
			
			System.out.println("\n");

			Time[] tester2 = produktionsstartZeitpunkte.getFirst();
			System.out.println(tester2[0].toString());
			System.out.println(tester2[1].toString());
			System.out.println(tester2[2].toString());
			System.out.println(tester2[3].toString());
			
			System.out.println("\n");
			
			Time[] tester3 = bedarfszeitpunkte.getFirst();
			System.out.println(tester3[0].toString());
			System.out.println(tester3[1].toString());
			System.out.println(tester3[2].toString());
			System.out.println(tester3[3].toString());
			
			System.out.println("\n");
			
		}
		
	}
	
	public void prüfeAufKonflikte(){
		//Liegt ein Konflikt vor?
		//...Konflikt liegt vor, wenn zwei Fahrzeuge in überlappenden Zeiträumen die Produktion eines gleichen Medikamentes in Anspruch nehmen
		//Schritt 1 zur Identifikation eines Konfliktes:
			//schauen ob die Überlappung groß genug ist -> Produktion hintereinander möglich und im zeitlichen Rahmen?
			// ... Hier mit Christopher reden wie die Fahrzeuge die 30 minuten Verwendbarkeit berücksichtigen als "Spielraum" für die Produktion
		
		//Es liegt ein Konflikt vor:
		//Schritt 1: 
			//Ortung des Problems (60,120,250 oder 500er) -> prüfen, ob es freie Kapazitäten auf anderen Produktionslinien gibt
		//Schritt 2:
			//Wenn sich nicht alle Produktionsanfragen umlagern lassen -> umwandlung des Medikamentes auf nächst höhere Stufe bei freien Kapazitäten in späteren Fahrzeugen / oder niedrigere Stufe bei freien Kapazitäten in vorhergehenden Fahrzeugen
			//..60->120, 120->250, 250->500 / oder umgekehrt
		//Schritt 3:
			//Auch das nicht ausreichend -> Rückgabe an Tourenplaner -> Andere Tour?
	
		//Wann ist eine Produktionslinie frei? -> Wenn nicht ausgelastet
		//... Ausgelastet wenn gesamtProduktionsdauer - produktionsdauer[i]=0 
		//... Besser wenn in der Time Klasse eine Differenz funktion genutzt wird -> produktionsdauerGesamtList[i] - Differenz(startzeitpunkte[i],bedarfszeitpunkte[i]) | produktionsdauergesamt muss dann wahrscheinlich zu einem Array mit 4 mal dem gleichen Wert gemacht werden
		//																																								  Ergebnis der Rechenoperation kann dann als int Auslastung[] gespeichert werden
		//... Das Ergebnis der Rechnung sind Kapazitäten in denen Produktion für nächstes Fahrzeug möglich ist (In der Form speichern dass erkenntlich wird wieviel von den jeweiligen Medikamenten maximal produziert werden können)
	}
	
}
