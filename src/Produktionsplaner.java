import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;

public class Produktionsplaner {
	
	//int groe�eDerArrays = 4; unwichtig geworden?
	
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
		
		//Pr�fe auf Konflikte der Bedarfe und der Produktionskapazit�ten
	    pr�feAufKonflikte();
	}
	
	public void getNecessaryParameters(){

		LinkedList<Integer> produktionsdauer= new LinkedList<Integer>();
		 
		// Berechnung f�r alle Fahrzeuge
		for(int i=0;i<fahrzeuge.size();i++){
			
			//Initialisierung der Zwischenspeicher f�r die relevanten Produktionsparameter
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
			
			//Berechne tats�chlich produzierte Mengen (Batches)
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
			
			
			//�bergeordnetete Datenstruktur zum Zugriff auf alle Fahrzeuge
			fahrzeugBedarfe.add(bedarfe);
			fahrzeugProduzierteMengen.add(produzierteMengen);
			produktionsstartZeitpunkte.add(produktionsstart);
			bedarfszeitpunkte.add(produktionsende);
			
		}
		
	}
	
	public void pr�feAufKonflikte(){
		//Liegt ein Konflikt vor?
		//...Konflikt liegt vor, wenn zwei Fahrzeuge in �berlappenden Zeitr�umen die Produktion eines gleichen Medikamentes in Anspruch nehmen
		//Schritt 1 zur Identifikation eines Konfliktes:
			//schauen ob die �berlappung gro� genug ist -> Produktion hintereinander m�glich und im zeitlichen Rahmen?
			// ... Hier mit Christopher reden wie die Fahrzeuge die 30 minuten Verwendbarkeit ber�cksichtigen als "Spielraum" f�r die Produktion
		
		//Es liegt ein Konflikt vor:
		//Schritt 1: 
			//Ortung des Problems (60,120,250 oder 500er) -> pr�fen, ob es freie Kapazit�ten auf anderen Produktionslinien gibt
		//Schritt 2:
			//Wenn sich nicht alle Produktionsanfragen umlagern lassen -> umwandlung des Medikamentes auf n�chst h�here Stufe bei freien Kapazit�ten in sp�teren Fahrzeugen / oder niedrigere Stufe bei freien Kapazit�ten in vorhergehenden Fahrzeugen
			//..60->120, 120->250, 250->500 / oder umgekehrt
		//Schritt 3:
			//Auch das nicht ausreichend -> R�ckgabe an Tourenplaner -> Andere Tour?
	
		//Wann ist eine Produktionslinie frei? -> Wenn nicht ausgelastet
		//... Ausgelastet wenn gesamtProduktionsdauer - produktionsdauer[i]=0 
		//... Besser wenn in der Time Klasse eine Differenz funktion genutzt wird -> produktionsdauerGesamtList[i] - Differenz(startzeitpunkte[i],bedarfszeitpunkte[i]) | produktionsdauergesamt muss dann wahrscheinlich zu einem Array mit 4 mal dem gleichen Wert gemacht werden
		//																																								  Ergebnis der Rechenoperation kann dann als int Auslastung[] gespeichert werden
		//... Das Ergebnis der Rechnung sind Kapazit�ten in denen Produktion f�r n�chstes Fahrzeug m�glich ist (In der Form speichern dass erkenntlich wird wieviel von den jeweiligen Medikamenten maximal produziert werden k�nnen)
	}
	
	public void assignProductionLines(){
		
		/*LinkedList<Time> test = produktionsstartZeitpunkte.get(1);
		LinkedList<Time> test2 = bedarfszeitpunkte.get(1);
		
	    System.out.println(test);	
	    System.out.println(test2);	*/
		//Daten in den �u�eren LinkedLists stimmen
		
		Produktionslinie p1 =  new Produktionslinie(this,1);
		Produktionslinie p2 =  new Produktionslinie(this,2);
		Produktionslinie p3 =  new Produktionslinie(this,3);
		Produktionslinie p4 =  new Produktionslinie(this,4);
		
		for(int k=0;k<fahrzeuge.size();k++){
			System.out.println("Fahrzeug "+(k+1)+" /////////////////////////");
			LinkedList<Integer> produktionsmenge = fahrzeugProduzierteMengen.get(k);
			LinkedList<Integer> bedarfe = fahrzeugBedarfe.get(k);
			LinkedList<Time> produktionsstart = produktionsstartZeitpunkte.get(k);
			LinkedList<Time> produktionsende = bedarfszeitpunkte.get(k);
			
			Map<Integer, Integer> reste;
		    reste = p1.assignControl(produktionsstart,produktionsende,produktionsmenge);
			if(!reste.isEmpty()){
				System.out.println("Es wird eine weitere Produktionslinie ben�tigt!");
				//Nicht produzierbare Restauftr�ge stehen in reste
				//�bersch�sse mit switch-anweisung ermitteln mit for schleife durch die reste linked list gehen f�r switch-wert 
				//bspw. case 0 -> produktionsmenge[0]-bedarfe[0]
				//p1.transform meds
				//if reste!=null -> p2.assignControl usw.
			}else{
				System.out.println("Der gesamte Auftrag konnte auf einer Produktionslinie produziert werden");
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
