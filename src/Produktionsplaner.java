import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Produktionsplaner {
	
	//int groeßeDerArrays = 4; unwichtig geworden?
	
	private LinkedList <Fahrzeug> fahrzeuge;
	
	private LinkedList <LinkedList<Integer>> fahrzeugBedarfe;
	private LinkedList <LinkedList<Time>> bedarfszeitpunkte;
	private LinkedList <LinkedList<Time>> produktionsstartZeitpunkte;
	
	
	public Produktionsplaner(LinkedList<Fahrzeug> fahrzeuge){
		this.fahrzeuge = fahrzeuge;	
		
		fahrzeugBedarfe = new LinkedList<LinkedList<Integer>>();
		bedarfszeitpunkte = new LinkedList<LinkedList<Time>>();
		produktionsstartZeitpunkte = new LinkedList<LinkedList<Time>>();
		
		planeProduktion();
		
	}
	
	public void planeProduktion(){
		getNecessaryParameters();
		assignProductionLines();
		
		//Prüfe auf Konflikte der Bedarfe und der Produktionskapazitäten
	    prüfeAufKonflikte();
	}
	
	public void getNecessaryParameters(){

		LinkedList<Integer> produktionsdauer= new LinkedList<Integer>();
		 
		// Berechnung für alle Fahrzeuge
		for(int i=0;i<fahrzeuge.size();i++){
			
			//Initialisierung der Zwischenspeicher für die relevanten Produktionsparameter
			LinkedList<Integer> bedarfe = new LinkedList<Integer>();
			LinkedList<Time>  produktionsende= new LinkedList<Time>();
			LinkedList<Time> produktionsstart = new LinkedList<Time>();
			
			//Ein Fahrzeug wird betrachtet und alle die Produktion betreffenden Parameter werden berechnet
			
			bedarfe.add(fahrzeuge.get(i).get60());
			bedarfe.add(fahrzeuge.get(i).get120());
			bedarfe.add(fahrzeuge.get(i).get250());
			bedarfe.add(fahrzeuge.get(i).get500());
			
			produktionsende.clear();
			produktionsende.add(fahrzeuge.get(i).getStartzeitBeladung60());
			produktionsende.add(fahrzeuge.get(i).getStartzeitBeladung120());
			produktionsende.add(fahrzeuge.get(i).getStartzeitBeladung250());
			produktionsende.add(fahrzeuge.get(i).getStartzeitBeladung500());
			
			
			//getProduktionsdauer und getStartzeitProduktion
			int dauer60 = (int)Math.ceil((double)bedarfe.get(0)/150)*15;
			int dauer120 = (int)Math.ceil((double)bedarfe.get(1)/150)*30;
			int dauer250 = (int)Math.ceil((double)bedarfe.get(2)/150)*60;
			int dauer500 = (int)Math.ceil((double)bedarfe.get(3)/150)*120;
			
			produktionsdauer.clear();
			produktionsdauer.add(dauer60);
			produktionsdauer.add(dauer120);
			produktionsdauer.add(dauer250);
			produktionsdauer.add(dauer500);
			
			
			Time spMed60 = produktionsende.get(0).getNewInstance().reduceTime(produktionsdauer.get(0));
			Time spMed120 = produktionsende.get(1).getNewInstance().reduceTime(produktionsdauer.get(1));
			Time spMed250 = produktionsende.get(2).getNewInstance().reduceTime(produktionsdauer.get(2));
			Time spMed500 = produktionsende.get(3).getNewInstance().reduceTime(produktionsdauer.get(3));
			
			
			produktionsstart.clear();
			produktionsstart.add(spMed60);
			produktionsstart.add(spMed120);
			produktionsstart.add(spMed250);
			produktionsstart.add(spMed500);
			
			
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
	
	public void assignProductionLines(){
		
		/*LinkedList<Time> test = produktionsstartZeitpunkte.get(1);
		LinkedList<Time> test2 = bedarfszeitpunkte.get(1);
		
	    System.out.println(test);	
	    System.out.println(test2);	*/
		//Daten in den äußeren LinkedLists stimmen
		
		Produktionslinie p1 =  new Produktionslinie(this);
		Produktionslinie p2 =  new Produktionslinie(this);
		Produktionslinie p3 =  new Produktionslinie(this);
		Produktionslinie p4 =  new Produktionslinie(this);
		
		for(int k=0;k<fahrzeuge.size();k++){
			
			LinkedList<Integer> bedarfe = fahrzeugBedarfe.get(k);
			LinkedList<Time> produktionsstart = produktionsstartZeitpunkte.get(k);
			LinkedList<Time> produktionsende = bedarfszeitpunkte.get(k);
			
		    if(p1.assign(produktionsstart,produktionsende,bedarfe)){
		    	System.out.println("Maschine P1 konnte belegt werden!");
		    }
			
		}
				
	}
	
	public LinkedList<LinkedList<Integer>> getFahrzeugBedarfe() {
		return fahrzeugBedarfe;
	}
	
	public LinkedList <LinkedList<Time>> getProduktionsstartZeitpunkte() {
		return produktionsstartZeitpunkte;
	}
	
	public LinkedList <LinkedList<Time>> getBedarfszeitpunkte() {
		return bedarfszeitpunkte;
	}
	
}
