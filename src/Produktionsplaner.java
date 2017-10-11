import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class Produktionsplaner {
	
	//int groeßeDerArrays = 4; unwichtig geworden?
	
	private LinkedList <Fahrzeug> fahrzeuge;
	
	private LinkedList <LinkedList<Integer>> fahrzeugBedarfe;
	private LinkedList <LinkedList<Integer>> fahrzeugProduzierteMengen;
	
	private LinkedList <LinkedList<Time>> bedarfszeitpunkte;
	private LinkedList <LinkedList<Time>> produktionsstartZeitpunkte;
	
	
	public Produktionsplaner(LinkedList<Fahrzeug> fahrzeuge){
		this.fahrzeuge = fahrzeuge;	
		
		fahrzeugBedarfe = new LinkedList<LinkedList<Integer>>();
		bedarfszeitpunkte = new LinkedList<LinkedList<Time>>();
		produktionsstartZeitpunkte = new LinkedList<LinkedList<Time>>();
		fahrzeugProduzierteMengen = new LinkedList <LinkedList<Integer>>();
		
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
			LinkedList<Integer> produzierteMengen = new LinkedList<Integer>();
			
			LinkedList<Time>  produktionsende= new LinkedList<Time>();
			LinkedList<Time> produktionsstart = new LinkedList<Time>();
			
			//Ein Fahrzeug wird betrachtet und alle die Produktion betreffenden Parameter werden berechnet
			
			//Die Medikamentanfragen eines Fahrzeuges
			bedarfe.add(fahrzeuge.get(i).get60());
			bedarfe.add(fahrzeuge.get(i).get120());
			bedarfe.add(fahrzeuge.get(i).get250());
			bedarfe.add(fahrzeuge.get(i).get500());
			
			//Berechne tatsächlich produzierte Mengen (Batches)
			produzierteMengen.clear();
			produzierteMengen.add((int)Math.ceil((double)bedarfe.get(0)/150)*150);
			produzierteMengen.add((int)Math.ceil((double)bedarfe.get(1)/100)*100);
			produzierteMengen.add((int)Math.ceil((double)bedarfe.get(2)/80)*80);
			produzierteMengen.add((int)Math.ceil((double)bedarfe.get(3)/60)*120);
			
			//Die Endzeitpunkte der Produktion
			produktionsende.clear();
			produktionsende.add(fahrzeuge.get(i).getStartzeitBeladung60().getNewInstance());
			produktionsende.add(fahrzeuge.get(i).getStartzeitBeladung120().getNewInstance());
			produktionsende.add(fahrzeuge.get(i).getStartzeitBeladung250().getNewInstance());
			produktionsende.add(fahrzeuge.get(i).getStartzeitBeladung500().getNewInstance());
			
			//Berechne Produktionsdauer
			produktionsdauer.clear();
			int z1 =((int)Math.ceil((double)produzierteMengen.get(0)/150)*15);
			produktionsdauer.add((int)Math.ceil((double)produzierteMengen.get(0)/150)*15);
			produktionsdauer.add((int)Math.ceil((double)produzierteMengen.get(1)/100)*30);
			produktionsdauer.add((int)Math.ceil((double)produzierteMengen.get(2)/80)*60);
			produktionsdauer.add((int)Math.ceil((double)produzierteMengen.get(3)/60)*120);
			
			//Berechne die Startzeit der Produktion
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
			fahrzeugProduzierteMengen.add(produzierteMengen);
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
		
		Map<Integer,Produktionslinie> produktionslinien = new HashMap<Integer,Produktionslinie>();
		int anzahlDerProduktionslinien = 4; //Hier ist die Anzahl der Produktionslinien anpassbar
		
		for(int i=1;i<=anzahlDerProduktionslinien;i++){
			produktionslinien.put(i,new Produktionslinie(this,i));
		}
		
		for(int k=0;k<fahrzeuge.size();k++){
			System.out.println("");
			System.out.println("/////// "+"Fahrzeug "+(k+1)+" ////////");
			System.out.println("");
			LinkedList<Integer> bedarfe = fahrzeugBedarfe.get(k);
			LinkedList<Time> produktionsstart = produktionsstartZeitpunkte.get(k);
			LinkedList<Time> produktionsende = bedarfszeitpunkte.get(k);
			LinkedList<Integer> nochZuProduzieren = fahrzeugProduzierteMengen.get(k);
			
			int id=1; //Für jedes Fahrzeug beginnt die Belegung bei Produktionslinie 1
			while(!nochZuProduzieren.isEmpty() | id>4){
				nochZuProduzieren = produktionslinien.get(id).assign(produktionsstart,produktionsende,nochZuProduzieren,bedarfe);
				id++;
				if(!nochZuProduzieren.isEmpty()){
					//Hier bevor die nächste Produktionslinie eröffnet wird erstmal transform() probieren
					//Tauschen auch beachten: Also wenn z.B. statt 50, 200 Einheiten produzeirt werden können aber der Zeitslot von dem 50er Auftrag blockiert wird -> den dann gar nicht produzieren oder verschieben
					System.out.println("Es wird eine weitere Produktionslinie benötigt!");
				}
			}
			System.out.println("Alle Produktionsanfragen des Fahrzeuges "+(k+1)+" wurden an die Produktionslinien verteilt!");	
			
				//Nicht produzierbare Restaufträge stehen in reste -> in kleinere mögliche produktionmenge umwandeln transformMeds() -> neu assignen sonst neue produktionslinie
				//Überschüsse mit switch-anweisung ermitteln mit for schleife durch die reste linked list gehen für switch-wert 
				//bspw. case 0 -> produktionsmenge[0]-bedarfe[0]
				//p1.transform meds
			
		}
		//Die MedUeberschuss-Objekte instatiieren
		HashMap<Time, LinkedList<Integer>> ueberschuesse = produktionslinien.get(1).getUeberschuesse();
		
		System.out.println("\n");
		System.out.println("Überschüsse P1:");
		System.out.println(ueberschuesse);
		
			
				
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
