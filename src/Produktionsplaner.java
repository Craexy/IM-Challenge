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
	
	//int groe�eDerArrays = 4; unwichtig geworden?
	
	private LinkedList <Fahrzeug> fahrzeuge;
	
	private LinkedList <LinkedList<Integer>> fahrzeugBedarfe;
	private LinkedList <LinkedList<Integer>> fahrzeugProduzierteMengen;
	
	private LinkedList <LinkedList<Time>> bedarfszeitpunkte;
	private LinkedList <LinkedList<Time>> produktionsstartZeitpunkte;
	
	private Map<Integer,Produktionslinie> produktionslinien;
	private LinkedList <MedUeberschuss> medUeberschuesse;
	
	
	public Produktionsplaner(LinkedList<Fahrzeug> fahrzeuge){
		this.fahrzeuge = fahrzeuge;	
		
		fahrzeugBedarfe = new LinkedList<LinkedList<Integer>>();
		bedarfszeitpunkte = new LinkedList<LinkedList<Time>>();
		produktionsstartZeitpunkte = new LinkedList<LinkedList<Time>>();
		fahrzeugProduzierteMengen = new LinkedList <LinkedList<Integer>>();
		
		produktionslinien = new HashMap<Integer,Produktionslinie>();
		medUeberschuesse = new LinkedList<MedUeberschuss>();
		
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
			
			int id=1; //F�r jedes Fahrzeug beginnt die Belegung bei Produktionslinie 1
			while(!nochZuProduzieren.isEmpty() | id>anzahlDerProduktionslinien){
				nochZuProduzieren = produktionslinien.get(id).assign(produktionsstart,produktionsende,nochZuProduzieren,bedarfe);
				//Tauschen auch beachten: Also wenn z.B. statt 50, 200 Einheiten produziert werden k�nnen aber der Zeitslot von dem 50er Auftrag blockiert wird -> den dann gar nicht produzieren oder verschieben
				//swap();
				
				if(!nochZuProduzieren.isEmpty()){
					//vor transform teilmengen versuchen reinzulegen
					//transform auf alle bereits belegten Produktionslinien anwenden, nicht nur auf momentan betrachtete (d.h. id �bergeben und in transform() dann for i=0;i<=id;i++... und dann transformieren versuchen
									
					System.out.println("Es wird eine weitere Produktionslinie ben�tigt!");
					id++;
				}
			}
			
			
			
			if(!nochZuProduzieren.isEmpty()){
				System.out.println("Alle Produktionslinien sind zu den angefragten Zeitpunkten bereits belegt, die Anfrage des Fahrzeuges "+(k+1)+" konnte nicht -oder nicht vollst�ndig- produziert werden");
			}else{
				System.out.println("Alle Produktionsanfragen des Fahrzeuges "+(k+1)+" wurden an die Produktionslinien verteilt!");	
			}
			
				//Nicht produzierbare Restauftr�ge stehen in reste -> in kleinere m�gliche produktionmenge umwandeln transformMeds() -> neu assignen sonst neue produktionslinie
				//p1.transform meds
			
		}
		System.out.println("Attempt transformation to save a production line.");
		transform();
		
		//Die MedUeberschuss-Objekte instanziieren
		for(int i=1;i<=anzahlDerProduktionslinien;i++){
			for(Time key : produktionslinien.get(i).getUeberschuesse().keySet()){
				medUeberschuesse.add(new MedUeberschuss(produktionslinien.get(i).getUeberschuesse().get(key),key));
			}
		}
					
	}
	
	public int numberOfActiveProductionLines(){
		int numberOf=0;
		for(int i=1;i<=produktionslinien.size();i++){
			if(produktionslinien.get(i).isUsed()){
				numberOf++;
			}
		}
		
		return numberOf;
	}
	
	public void cutAndTry(LinkedList<Time> produktionsstart,LinkedList<Time> produktionsende, LinkedList<Integer> nochZuProduzieren, int id){
		LinkedList<Integer> index = produktionslinien.get(id).getIndexMaximaleProduktionsmenge(nochZuProduzieren);
		
		for(int i : index){
			System.out.println("Der Iterator hat diese Werte: "+i);
			switch(i){
			case 3: 
					
					break;
			
			case 2: 
					
					break;
					
			case 1: 
					break;
					
			case 0: 
					
					break;
					
			default: break;
				
				//zu 120er Meds machen, zeitpunkte �ndern, etc.
				//...Siehe Tourenplaner f�r zeitenverschiebungen
			}
		}
		
	}
	
	public void transform(){
		int numberOfProdLines = numberOfActiveProductionLines();
		
		for(int i=numberOfProdLines;i>1;i--){	//Die letzte genutzte PL wird betrachtet | es muss mehr als eine PL geben um die Produktionen auf die vorherige zu schieben
			
			Produktionslinie zumUmverteilen = produktionslinien.get(numberOfProdLines);
			LinkedList<Time> belegtvon = zumUmverteilen.getBelegtvon();
			LinkedList<Time> belegtbis = zumUmverteilen.getBelegtbis();
			HashMap<Time, LinkedList<Integer>> ueberschuesse = zumUmverteilen.getUeberschuesse();
			Map<Time,Integer> belegtMitMenge = zumUmverteilen.getBelegtMitMenge();
			Map<Time,Integer> belegtMitMed = zumUmverteilen.getBelegtMitMed();
			Map<Time,Integer> urspruenglicheBedarfe = zumUmverteilen.getUrspruenglicheBedarfe();
			
			LinkedList<TransformedMed> transformedMedObjects = new LinkedList<TransformedMed>();
			
			for(int k=0;k<belegtvon.size();k++){ //Die Umverteilung geschieht f�r jede Belegung in der letzten PL
				
				boolean successfull = false;
				Time endzeitAktuell = belegtbis.get(k);
				int medTypAktuell = belegtMitMed.get(belegtvon.get(k));
				int mengeAktuell = belegtMitMenge.get(belegtvon.get(k));
				
				//Hier noch die anderen Werte die durch die umwandlung ge�ndert werden �ndern (Zeiten, Mengen)
				//sp�ter einf�gen dass maximal 1 mal transformiert werden darf (ne forschleife innerhalb der switch anweisung) und erst sp�ter eine 2. und 3. transformationen ausgef�hrt wird um zu vermeiden dass mehr 2er/3er-transformationen(mehr kosten) existieren als n�tig
				while(medTypAktuell<3 && !successfull){ //Solange transformieren bis keine transformation mehr m�glich ist oder es geklappt hat
					
					//�bersch�sse noch reinbringen/umverteilen
					
					switch(medTypAktuell){
					case 0: //umwandlung zu 120er
							medTypAktuell++;
							mengeAktuell = ((int)Math.ceil((double)urspruenglicheBedarfe.get(belegtvon.get(k))/100)*100);
							//endzeitAktuell =...
							
							for(int j=1;j<(numberOfProdLines-1);j++){ //Probieren der Belegung des transformierten Meds auf eine der vorhergehenden PL's
								//checkforconflicts (mit "aktuellen"/neuen Zeitwerten)
								//int check = produktionslinien.get(j).checkForConflicts(, );
								//Erstelle TransformedMed-Objekt mit den ganze neuen/aktuellen werten
								//--> addProduction & removeProduction ABER: nur die informationen speichern (in TransformedMed-Objekten, die tats�chliche adduund remove funktion erst ausf�hren wenn alle eintr�ge aus belegtvon verteilt werden konnten
								//denn nur dann kann eine Produktionslinie gespart werden
								//wenn erfolgreich: successfull == true und break;
								
							}
							
							
							
							break;
							
					case 1: //Umwandlung zu 250er
							medTypAktuell++;
							mengeAktuell = ((int)Math.ceil((double)urspruenglicheBedarfe.get(belegtvon.get(k))/80)*80);
							
							for(int j=1;j<(numberOfProdLines-1);j++){ //Probieren der Belegung des transformierten Meds auf eine der vorhergehenden PL's
								//siehe oben
							}
							
							break;
							
					case 2: //Umwandlung zu 500er
							medTypAktuell++;
							mengeAktuell = ((int)Math.ceil((double)urspruenglicheBedarfe.get(belegtvon.get(k))/60)*120);
							
							for(int j=1;j<(numberOfProdLines-1);j++){ //Probieren der Belegung des transformierten Meds auf eine der vorhergehenden PL's
								//siehe oben
							}
							
							break;
						
					default: //500er k�nnen nicht weiter umgewandelt werden
							medTypAktuell++;
							break;
					}
					
				}

			}
			//Hier dann testen ob die transformedMedObjects.size()==belegtvon.size()
			//wenn ja sind alle transformationen m�glich und k�nnen durchgef�hrt werden
			//d.h. alle belegungen der letzten PL entfernen und in TransformedMed Objekten steht die neue PL, der MedTyp, die Menge, die Start-und Endzeitpunkte und die �bersch�sse
			//--> addProduction()
				//removeProduction()	
			System.out.println("Konnte die PL gespart werden?");
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
	
	public LinkedList<MedUeberschuss> getMedUeberschuesse() {
		return medUeberschuesse;
	}
	
}