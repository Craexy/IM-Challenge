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
	private int strafkosten;
	
	private LinkedList <Fahrzeug> fahrzeuge;
	
	private LinkedList <LinkedList<Integer>> fahrzeugBedarfe;
	private LinkedList <LinkedList<Integer>> fahrzeugProduzierteMengen;
	
	private LinkedList <LinkedList<Time>> bedarfszeitpunkte;
	private LinkedList <LinkedList<Time>> produktionsstartZeitpunkte;
	
	private Map<Integer,Produktionslinie> produktionslinien;
	private LinkedList <MedUeberschuss> medUeberschuesse;
	
	
	public Produktionsplaner(LinkedList<Fahrzeug> fahrzeuge, int strafkosten){
		this.fahrzeuge = fahrzeuge;	
		this.strafkosten = strafkosten;
		
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
		isItWorthIt();
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
			
			int id=1; //Für jedes Fahrzeug beginnt die Belegung bei Produktionslinie 1
			while(!nochZuProduzieren.isEmpty() | id>anzahlDerProduktionslinien){
				nochZuProduzieren = produktionslinien.get(id).assign(produktionsstart,produktionsende,nochZuProduzieren,bedarfe);
				//Tauschen auch beachten: Also wenn z.B. statt 50, 200 Einheiten produziert werden können aber der Zeitslot von dem 50er Auftrag blockiert wird -> den dann gar nicht produzieren oder verschieben
				//swap();
				
				if(!nochZuProduzieren.isEmpty()){
					//vor transform teilmengen versuchen reinzulegen
					//transform auf alle bereits belegten Produktionslinien anwenden, nicht nur auf momentan betrachtete (d.h. id übergeben und in transform() dann for i=0;i<=id;i++... und dann transformieren versuchen
									
					System.out.println("Es wird eine weitere Produktionslinie benötigt!");
					id++;
				}
			}
			
			
			
			if(!nochZuProduzieren.isEmpty()){
				System.out.println("Alle Produktionslinien sind zu den angefragten Zeitpunkten bereits belegt, die Anfrage des Fahrzeuges "+(k+1)+" konnte nicht -oder nicht vollständig- produziert werden");
			}else{
				System.out.println("Alle Produktionsanfragen des Fahrzeuges "+(k+1)+" wurden an die Produktionslinien verteilt!");	
			}
			
				//Nicht produzierbare Restaufträge stehen in reste -> in kleinere mögliche produktionmenge umwandeln transformMeds() -> neu assignen sonst neue produktionslinie
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
	
//	public void cutAndTry(LinkedList<Time> produktionsstart,LinkedList<Time> produktionsende, LinkedList<Integer> nochZuProduzieren, int id){
//		LinkedList<Integer> index = produktionslinien.get(id).getIndexMaximaleProduktionsmenge(nochZuProduzieren);
//		
//		for(int i : index){
//			System.out.println("Der Iterator hat diese Werte: "+i);
//			switch(i){
//			case 3: 
//					
//					break;
//			
//			case 2: 
//					
//					break;
//					
//			case 1: 
//					break;
//					
//			case 0: 
//					
//					break;
//					
//			default: break;
//				
//				//zu 120er Meds machen, zeitpunkte ändern, etc.
//				//...Siehe Tourenplaner für zeitenverschiebungen
//			}
//		}
//		
//	}
	
	//nach transform testweise verschieben und gucken wieviel die letzte PL jeweils Produziert und auf basis davon entscheiden ob Strafkosten in kauf genommen werden
	public void transform(){ //Betrachtet bis jetzt nur transformierung in eine Richtung (60->120 etc.) //Kofnlikte innerhalb der TransformedMed-Objekte beachten
		int numberOfProdLines = numberOfActiveProductionLines();
		
		for(int i=numberOfProdLines;i>1;i--){	//Die letzte genutzte PL wird betrachtet | es muss mehr als eine PL geben um die Produktionen auf die vorherige zu schieben
			
			Produktionslinie zumUmverteilen = produktionslinien.get(numberOfProdLines);
			LinkedList<Time> belegtvon = zumUmverteilen.getBelegtvon();
			LinkedList<Time> belegtbis = zumUmverteilen.getBelegtbis();
			Map<Time,Integer> belegtMitMenge = zumUmverteilen.getBelegtMitMenge();
			Map<Time,Integer> belegtMitMed = zumUmverteilen.getBelegtMitMed();
			Map<Time,Integer> urspruenglicheBedarfe = zumUmverteilen.getUrspruenglicheBedarfe();
			boolean allSuccessfull=false;
			
			LinkedList<TransformedMed> transformedMedObjects = new LinkedList<TransformedMed>();
			
			for(int k=0;k<belegtvon.size();k++){ //Die Umverteilung geschieht für jede Belegung in der letzten PL
				
				Time endzeitAktuell = belegtbis.get(k);
				Time startzeitAktuell = null;
				int medTypAktuell = belegtMitMed.get(belegtvon.get(k));
				int mengeAktuell = belegtMitMenge.get(belegtvon.get(k));
				int produktionsdauer = 0;
				int ueberschuesseAktuell = 0;
				int urspruenglicheBedarfeAktuell = 0;
				boolean successfull=true;
				
				switch(medTypAktuell){
				case 0: //umwandlung zu 120er
						medTypAktuell=1;
						mengeAktuell = ((int)Math.ceil((double)urspruenglicheBedarfe.get(belegtvon.get(k))/100)*100);
						ueberschuesseAktuell = mengeAktuell - urspruenglicheBedarfe.get(belegtvon.get(k));
						urspruenglicheBedarfeAktuell = urspruenglicheBedarfe.get(belegtvon.get(k)); 
						endzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(120);
						produktionsdauer = ((int)Math.ceil((double)mengeAktuell/100)*30);
						startzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(produktionsdauer);
						
						for(int j=1;j<(numberOfProdLines-1);j++){ //Probieren der Belegung des transformierten Meds auf eine der vorhergehenden PL's
							int check = produktionslinien.get(j).checkForConflicts(startzeitAktuell, endzeitAktuell );
							if(check!=-1){
								transformedMedObjects.add(new TransformedMed(j,check,medTypAktuell,mengeAktuell,startzeitAktuell,endzeitAktuell,ueberschuesseAktuell,urspruenglicheBedarfeAktuell)); 
								break;
							}else{
								successfull =false;
							}
						}
						
						//Umwandlung zu 250er
						if(!successfull){
							medTypAktuell=2;
							mengeAktuell = ((int)Math.ceil((double)urspruenglicheBedarfe.get(belegtvon.get(k))/80)*80);
							ueberschuesseAktuell = mengeAktuell - urspruenglicheBedarfe.get(belegtvon.get(k));
							endzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(150);
							produktionsdauer = ((int)Math.ceil((double)mengeAktuell/80)*60);
							startzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(produktionsdauer);
							
							for(int j=1;j<(numberOfProdLines-1);j++){ //Probieren der Belegung des transformierten Meds auf eine der vorhergehenden PL's
								int check = produktionslinien.get(j).checkForConflicts(startzeitAktuell, endzeitAktuell );
								if(check!=-1){
									transformedMedObjects.add(new TransformedMed(j,check,medTypAktuell,mengeAktuell,startzeitAktuell,endzeitAktuell,ueberschuesseAktuell,urspruenglicheBedarfeAktuell));
									successfull=true;
									break;
								}else{
									successfull =false;
								}
							}
						}
						
						//Umwandlung zu 500er
						if(!successfull){
							medTypAktuell=3;
							mengeAktuell = ((int)Math.ceil((double)urspruenglicheBedarfe.get(belegtvon.get(k))/60)*120);
							ueberschuesseAktuell = mengeAktuell - urspruenglicheBedarfe.get(belegtvon.get(k));
							endzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(120);
							produktionsdauer = ((int)Math.ceil((double)mengeAktuell/60)*120);
							startzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(produktionsdauer);
							
							for(int j=1;j<(numberOfProdLines-1);j++){ //Probieren der Belegung des transformierten Meds auf eine der vorhergehenden PL's
								int check = produktionslinien.get(j).checkForConflicts(startzeitAktuell, endzeitAktuell );
								if(check!=-1){
									transformedMedObjects.add(new TransformedMed(j,check,medTypAktuell,mengeAktuell,startzeitAktuell,endzeitAktuell,ueberschuesseAktuell,urspruenglicheBedarfeAktuell)); 
									successfull=true;
									break;
								}else{
									successfull =false;
								}
							}
						}
						
						break;
						
				case 1: //Umwandlung zu 60er
						medTypAktuell=0;
						mengeAktuell = ((int)Math.ceil((double)urspruenglicheBedarfe.get(belegtvon.get(k))/150)*150);
						ueberschuesseAktuell = mengeAktuell - urspruenglicheBedarfe.get(belegtvon.get(k));
						endzeitAktuell = endzeitAktuell.getNewInstance().addTime(120);
						produktionsdauer = ((int)Math.ceil((double)mengeAktuell/150)*15);
						startzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(produktionsdauer);
						
						for(int j=1;j<(numberOfProdLines-1);j++){ //Probieren der Belegung des transformierten Meds auf eine der vorhergehenden PL's
							int check = produktionslinien.get(j).checkForConflicts(startzeitAktuell, endzeitAktuell );
							if(check!=-1){
								transformedMedObjects.add(new TransformedMed(j,check,medTypAktuell,mengeAktuell,startzeitAktuell,endzeitAktuell,ueberschuesseAktuell,urspruenglicheBedarfeAktuell)); 
								break;
							}else{
								successfull =false;
							}
						}
						
						//Umwandlung zu 250er
						if(!successfull){
							medTypAktuell=2;
							mengeAktuell = ((int)Math.ceil((double)urspruenglicheBedarfe.get(belegtvon.get(k))/80)*80);
							ueberschuesseAktuell = mengeAktuell - urspruenglicheBedarfe.get(belegtvon.get(k));
							endzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(270);
							produktionsdauer = ((int)Math.ceil((double)mengeAktuell/80)*60);
							startzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(produktionsdauer);
							
							for(int j=1;j<(numberOfProdLines-1);j++){ //Probieren der Belegung des transformierten Meds auf eine der vorhergehenden PL's
								int check = produktionslinien.get(j).checkForConflicts(startzeitAktuell, endzeitAktuell );
								if(check!=-1){
									transformedMedObjects.add(new TransformedMed(j,check,medTypAktuell,mengeAktuell,startzeitAktuell,endzeitAktuell,ueberschuesseAktuell,urspruenglicheBedarfeAktuell)); 
									successfull=true;
									break;
								}else{
									successfull =false;
								}
							}
						}
						
						//Umwandlung zu 500er
						if(!successfull){
							medTypAktuell=3;
							mengeAktuell = ((int)Math.ceil((double)urspruenglicheBedarfe.get(belegtvon.get(k))/60)*120);
							ueberschuesseAktuell = mengeAktuell - urspruenglicheBedarfe.get(belegtvon.get(k));
							endzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(120);
							produktionsdauer = ((int)Math.ceil((double)mengeAktuell/60)*120);
							startzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(produktionsdauer);
							
							for(int j=1;j<(numberOfProdLines-1);j++){ //Probieren der Belegung des transformierten Meds auf eine der vorhergehenden PL's
								int check = produktionslinien.get(j).checkForConflicts(startzeitAktuell, endzeitAktuell );
								if(check!=-1){
									transformedMedObjects.add(new TransformedMed(j,check,medTypAktuell,mengeAktuell,startzeitAktuell,endzeitAktuell,ueberschuesseAktuell,urspruenglicheBedarfeAktuell)); 
									successfull=true;
									break;
								}else{
									successfull =false;
								}
							}
						}
						
						break;
						
				case 2: //Umwandlung zu 60er //addTime anpassen //reduceTime zu addtime ändern
						medTypAktuell=0;
						mengeAktuell = ((int)Math.ceil((double)urspruenglicheBedarfe.get(belegtvon.get(k))/150)*150);
						ueberschuesseAktuell = mengeAktuell - urspruenglicheBedarfe.get(belegtvon.get(k));
						endzeitAktuell = endzeitAktuell.getNewInstance().addTime(270);
						produktionsdauer = ((int)Math.ceil((double)mengeAktuell/150)*15);
						startzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(produktionsdauer);
						
						for(int j=1;j<(numberOfProdLines-1);j++){ //Probieren der Belegung des transformierten Meds auf eine der vorhergehenden PL's
							int check = produktionslinien.get(j).checkForConflicts(startzeitAktuell, endzeitAktuell );
							if(check!=-1){
								transformedMedObjects.add(new TransformedMed(j,check,medTypAktuell,mengeAktuell,startzeitAktuell,endzeitAktuell,ueberschuesseAktuell,urspruenglicheBedarfeAktuell));
								break;
							}else{
								successfull =false;
							}
						}
						
						if(!successfull){
							//umwandlung zu 120er
							medTypAktuell=1;
							mengeAktuell = ((int)Math.ceil((double)urspruenglicheBedarfe.get(belegtvon.get(k))/100)*100);
							ueberschuesseAktuell = mengeAktuell - urspruenglicheBedarfe.get(belegtvon.get(k));
							endzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(120);
							produktionsdauer = ((int)Math.ceil((double)mengeAktuell/100)*30);
							startzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(produktionsdauer);
							
							for(int j=1;j<(numberOfProdLines-1);j++){ //Probieren der Belegung des transformierten Meds auf eine der vorhergehenden PL's
								int check = produktionslinien.get(j).checkForConflicts(startzeitAktuell, endzeitAktuell );
								if(check!=-1){
									transformedMedObjects.add(new TransformedMed(j,check,medTypAktuell,mengeAktuell,startzeitAktuell,endzeitAktuell,ueberschuesseAktuell,urspruenglicheBedarfeAktuell));
									successfull=true;
									break;
								}else{
									successfull =false;
								}
							}
							
						}
						
						//Umwandlung zu 500er
						if(!successfull){
							medTypAktuell=3;
							mengeAktuell = ((int)Math.ceil((double)urspruenglicheBedarfe.get(belegtvon.get(k))/60)*120);
							ueberschuesseAktuell = mengeAktuell - urspruenglicheBedarfe.get(belegtvon.get(k));
							endzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(270);
							produktionsdauer = ((int)Math.ceil((double)mengeAktuell/60)*120);
							startzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(produktionsdauer);
							
							for(int j=1;j<(numberOfProdLines-1);j++){ //Probieren der Belegung des transformierten Meds auf eine der vorhergehenden PL's
								int check = produktionslinien.get(j).checkForConflicts(startzeitAktuell, endzeitAktuell );
								if(check!=-1){
									transformedMedObjects.add(new TransformedMed(j,check,medTypAktuell,mengeAktuell,startzeitAktuell,endzeitAktuell,ueberschuesseAktuell,urspruenglicheBedarfeAktuell)); 
									successfull=true;
									break;
								}else{
									successfull =false;
								}
							}
						}
						
						break;
					
				case 3:	 //Umwandlung zu 60er //addTime anpassen //reduceTime zu addtime ändern
						medTypAktuell=0;
						mengeAktuell = ((int)Math.ceil((double)urspruenglicheBedarfe.get(belegtvon.get(k))/150)*150);
						ueberschuesseAktuell = mengeAktuell - urspruenglicheBedarfe.get(belegtvon.get(k));
						endzeitAktuell = endzeitAktuell.getNewInstance().addTime(390);
						produktionsdauer = ((int)Math.ceil((double)mengeAktuell/150)*15);
						startzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(produktionsdauer);
						
						for(int j=1;j<(numberOfProdLines-1);j++){ //Probieren der Belegung des transformierten Meds auf eine der vorhergehenden PL's
							int check = produktionslinien.get(j).checkForConflicts(startzeitAktuell, endzeitAktuell );
							if(check!=-1){
								transformedMedObjects.add(new TransformedMed(j,check,medTypAktuell,mengeAktuell,startzeitAktuell,endzeitAktuell,ueberschuesseAktuell,urspruenglicheBedarfeAktuell));
								break;
							}else{
								successfull =false;
							}
						}
						
						//umwandlung zu 120er
						if(!successfull){
							medTypAktuell=1;
							mengeAktuell = ((int)Math.ceil((double)urspruenglicheBedarfe.get(belegtvon.get(k))/100)*100);
							ueberschuesseAktuell = mengeAktuell - urspruenglicheBedarfe.get(belegtvon.get(k));
							endzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(120);
							produktionsdauer = ((int)Math.ceil((double)mengeAktuell/100)*30);
							startzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(produktionsdauer);
							
							for(int j=1;j<(numberOfProdLines-1);j++){ //Probieren der Belegung des transformierten Meds auf eine der vorhergehenden PL's
								int check = produktionslinien.get(j).checkForConflicts(startzeitAktuell, endzeitAktuell );
								if(check!=-1){
									transformedMedObjects.add(new TransformedMed(j,check,medTypAktuell,mengeAktuell,startzeitAktuell,endzeitAktuell,ueberschuesseAktuell,urspruenglicheBedarfeAktuell)); 
									successfull=true;
									break;
								}else{
									successfull =false;
								}
							}
							
						}
						
						//Umwandlung zu 250er
						if(!successfull){
							medTypAktuell=2;
							mengeAktuell = ((int)Math.ceil((double)urspruenglicheBedarfe.get(belegtvon.get(k))/80)*80);
							ueberschuesseAktuell = mengeAktuell - urspruenglicheBedarfe.get(belegtvon.get(k));
							endzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(150);
							produktionsdauer = ((int)Math.ceil((double)mengeAktuell/80)*60);
							startzeitAktuell = endzeitAktuell.getNewInstance().reduceTime(produktionsdauer);
							
							for(int j=1;j<(numberOfProdLines-1);j++){ //Probieren der Belegung des transformierten Meds auf eine der vorhergehenden PL's
								int check = produktionslinien.get(j).checkForConflicts(startzeitAktuell, endzeitAktuell );
								if(check!=-1){
									transformedMedObjects.add(new TransformedMed(j,check,medTypAktuell,mengeAktuell,startzeitAktuell,endzeitAktuell,ueberschuesseAktuell,urspruenglicheBedarfeAktuell));
									successfull=true;
									break;
								}else{
									successfull =false;
								}
							}
						}
							
						
				default: 
						
						break;
				}
				
				if(!successfull){
					allSuccessfull=false;
					break;
				}	

			}
			System.out.println("Konnte die PL gespart werden?");
			if(allSuccessfull){
				System.out.println("Ja");
				
				produktionslinien.get(numberOfProdLines).removeProduction(true,null, null, 0);
				for(int z=0;z<transformedMedObjects.size();z++){
					TransformedMed tO = transformedMedObjects.get(i);
					produktionslinien.get(tO.getPL()).addProduction(tO.getPosition(), tO.getStartzeitpunkt(), tO.getEndzeitpunkt(), tO.getMenge(), tO.getMedTyp(), tO.getUeberschuesse(),tO.getUrspruenglicheBedarfe());
				}
				
			}else{
				System.out.println("Nein");
			}
		}
	
	}
	
	public int calculateCostsForProductionTime(){
		int costs = 0;
		int nrOfProdLines = numberOfActiveProductionLines();
		for(int i=1;i<=nrOfProdLines;i++){
			costs += produktionslinien.get(i).getKosten();
		}
		return costs;
	}
	
	public void isItWorthIt(){
		int nrOfProdLines = numberOfActiveProductionLines();
		for(int i=1;i<=nrOfProdLines;i++){
			int cost = produktionslinien.get(i).getKosten();
			cost = cost+3000;
			Map<Time, Integer> mengen = produktionslinien.get(i).getBelegtMitMenge();
			for(Time j : mengen.keySet()){
				cost += mengen.get(j)*strafkosten;
			}
			if(cost>strafkosten){
				produktionslinien.get(i).removeAllProductions();
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
	
	public LinkedList<MedUeberschuss> getMedUeberschuesse() {
		return medUeberschuesse;
	}
	
}