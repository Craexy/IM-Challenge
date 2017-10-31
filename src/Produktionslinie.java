import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Produktionslinie {
	private int id;
	
	private LinkedList <Time> belegtvon;
	private LinkedList <Time> belegtbis;
	private Map<Time, Integer> belegtMitMenge;
	private Map<Time, Integer> urspruenglicheBedarfe;
	
	private Map<Time, Integer> belegtMitMed; //0=Med60;1=Med120;2=Med250;3=Med500 //kann mit methode changeIndexToTypeOfMed umgewandelt werden
	
	HashMap<Time, LinkedList<Integer>> ueberschuesse; //LinkedList(0)=medTyp;LinkedList(1)=ueberschuss für die Produktion zu einem best. Zeitpunkt (->Key)
	

	public Produktionslinie(int id){
		this.id = id;
		
		belegtvon = new LinkedList<Time>();
		belegtbis = new LinkedList<Time>();
		
		belegtMitMenge = new HashMap<Time, Integer>();
		belegtMitMed  = new HashMap<Time, Integer>();
		
		urspruenglicheBedarfe = new HashMap<Time,Integer>();
		
		ueberschuesse = new HashMap<Time,LinkedList<Integer>>();
	}

	public LinkedList<Integer> assign(LinkedList<Time> produktionsstart,LinkedList<Time> produktionsende, LinkedList<Integer> produktionsmenge, LinkedList<Integer> bedarfe){
		
		LinkedList<Integer> indizes = getIndexMaximaleProduktionsmenge(produktionsmenge);
		
		try{
			if(indizes!=null){
				int durchlauf = indizes.size();
				for(int c=0;c<durchlauf;c++){
					
					if(belegtvon.size()==0){
						
							belegtvon.add(produktionsstart.get(indizes.getFirst()));
							belegtbis.add(produktionsende.get(indizes.getFirst()));
							
							belegtMitMenge.put(produktionsstart.get(indizes.getFirst()), produktionsmenge.get(indizes.getFirst()));
							belegtMitMed.put(produktionsstart.get(indizes.getFirst()), indizes.getFirst());
							
							urspruenglicheBedarfe.put(produktionsstart.get(indizes.getFirst()), bedarfe.get(indizes.getFirst()));
							
							addUeberschuesse(bedarfe.get(indizes.getFirst()),0);
							
							System.out.println("\tDie Maschine "+this.id+" wurde von "+produktionsstart.get(indizes.getFirst())+" bis "+produktionsende.get(indizes.getFirst())+" mit "+produktionsmenge.get(indizes.getFirst())+" Einheiten des Medikamentes "+changeIndexToTypeOfMed(indizes.getFirst())+" belegt!");

							
							indizes.removeFirst();
						
					}else{
						if(indizes!=null){
							
							//Testen ob zeitliche Überlappung vorliegt
							int check = checkForConflicts(produktionsstart.get(indizes.getFirst()),produktionsende.get(indizes.getFirst()));
							if(check!=-1){
								//Sortiert einfügen
								belegtvon.add(check, produktionsstart.get(indizes.getFirst()));
								belegtbis.add(check, produktionsende.get(indizes.getFirst()));
								
								belegtMitMenge.put(produktionsstart.get(indizes.getFirst()), produktionsmenge.get(indizes.getFirst()));
								belegtMitMed.put(produktionsstart.get(indizes.getFirst()), indizes.getFirst());
								
								urspruenglicheBedarfe.put(produktionsstart.get(indizes.getFirst()), bedarfe.get(indizes.getFirst()));
								
								addUeberschuesse(bedarfe.get(indizes.getFirst()),check);
								
								System.out.println("\tDie Maschine "+this.id+" wurde von "+produktionsstart.get(indizes.getFirst())+" bis "+produktionsende.get(indizes.getFirst())+" mit "+produktionsmenge.get(indizes.getFirst())+" Einheiten des Medikamentes "+changeIndexToTypeOfMed(indizes.getFirst())+" belegt!");

								indizes.removeFirst();
								
							}else{
								indizes.offer(indizes.poll()); //Das Element an erster Stelle passt nicht, daher wird es übersprungen indem es
															   //an die letzte Stelle der Prioritätsliste "indizes" gesetzt wird
							}
							
							
							}else{
							return null;
						}
					
				}
			}
			}else{
				System.out.println("Es wurden keine Produktionsmengen angefragt.");
				return null;
			}
			
			
				
		}catch (NullPointerException e){
			
		}
		
		//Umwandeln der Reste in eine neue Produktionsmengenanfrage die der nächsten Produktionslinie übergeben werden kann
		LinkedList<Integer> nochZuProduzieren = new LinkedList<Integer>();
		if(!indizes.isEmpty()){
			for(int y=0;y<produktionsmenge.size();y++){
				nochZuProduzieren.add(0);
			}
			for(int i=0;i<indizes.size();i++){
				nochZuProduzieren.set(indizes.get(i), produktionsmenge.get(indizes.get(i)));
			}
		}
		
	
		return nochZuProduzieren;
		
	}
	
	public int checkForConflicts(Time produktionsstart,Time produktionsende){
		//muss ne Map zurückgeben mit <boolean,int>
		//der boolean wert ist ob konflikt vorliegt, der int wert der index
		//Die map in einer LinkedList speichern sodass jede produktionslinie alle Konflikte gespeichert hat
		//Dann ne getconflictindex() methode damit im produktionsplaner transform() gemacht werden kann
		//und die geringere Menge des konflikts umgewandelt werden kann (denn umwandeln = teurer)
		
		//ps. den index des confliktes herauszufinden ist gar nicht so leicht, daher erstmal immer die nochZuProduzieren 
		//werte transformieren
		int index=-1;
		
		if(produktionsende.isEarlierThan(belegtvon.getFirst())){
			return 0;
		}
		if(produktionsstart.isLaterThan(belegtbis.getLast())){
			return belegtbis.size();
		}
		
		for(int z=0;z<belegtbis.size();z++){
			if(produktionsstart.isLaterThan(belegtbis.get(z))){
				index = z+1;
				
				if(produktionsende.isEarlierThan(belegtvon.get(index))){
					return index;
				}
			}
		}
		
		return -1;
	
	}
	
	public void addProduction(int position, Time startZeitpunkt, Time endZeitpunkt,int menge,int med,int ueberschuesse,int urspruenglicheBedarfe){
		//Fügt der Produktionslinie eine Belegung inklusive der Überschüsse hinzu
		
		belegtvon.add(position, startZeitpunkt);
		belegtbis.add(position, endZeitpunkt);
		
		belegtMitMenge.put(startZeitpunkt,menge);
		belegtMitMed.put(startZeitpunkt, med);
		this.urspruenglicheBedarfe.put(startZeitpunkt, urspruenglicheBedarfe);
		
		LinkedList<Integer> tempDaten = new LinkedList<Integer>();
		
		tempDaten.clear();
		tempDaten.add(med);
		tempDaten.add(ueberschuesse);
		this.ueberschuesse.put(startZeitpunkt, tempDaten);

	}
	
	public void removeProduction(boolean all,Time startZeitpunkt, Time endZeitpunkt,int ueberschuesse){
		//Löscht eine, oder alle Belegungen der Produktionslinie (siehe removeAllProductions())
		if(all==true){
			removeAllProductions();
		}else{

			belegtvon.remove(startZeitpunkt);
			belegtbis.remove(endZeitpunkt);
			
			belegtMitMenge.remove(startZeitpunkt);
			belegtMitMed.remove(startZeitpunkt);
			urspruenglicheBedarfe.remove(startZeitpunkt);
			this.ueberschuesse.remove(startZeitpunkt);
		}
		
	}
	
	public void removeAllProductions(){
		//Löscht alle Belegungen einer Produktionslinie
		belegtvon.clear();
		belegtbis.clear();
		belegtMitMenge.clear();
		belegtMitMed.clear();
		ueberschuesse.clear();
		urspruenglicheBedarfe.clear();
	}
	
	//@todo: neue Variante die zuerst die menge produziert die am ehesten dem Bedarf entspricht
	
	//Gibt die Indizes der Produktionsmengen, absteigend sortiert, zurück
	//Produktionsmengen von 0 ausschließen und die linkedlist in ner forschleife benutzen -> solange assign bis nichts mehr in iMax
	public LinkedList<Integer> getIndexMaximaleProduktionsmenge(LinkedList<Integer> produktionsmenge){
		int max=0;
		LinkedList <Integer> iMax = new LinkedList<Integer>();
		System.out.println("\nZu Produzierende Mengen: "+produktionsmenge);
		
		for(int i=0;i<produktionsmenge.size();i++){
			if(produktionsmenge.get(i)>max){
				max = produktionsmenge.get(i);
				iMax.addFirst(i);
			}else{
				if(produktionsmenge.get(i)<max && produktionsmenge.get(i)!=0){ //Fehler in der Logik!!
					if(iMax.size()==1){
						iMax.addLast(i);
					}else{
						for(int l=1;l<iMax.size();l++){
							if(produktionsmenge.get(i)>produktionsmenge.get(iMax.get(l))){
								iMax.add(l, i);
								break; //endlosschleife vermeiden
							}
							if(l==(iMax.size()-1)){
								iMax.addLast(i);
								break; //endlosschleife vermeiden
							}
						}
						}
						
					}
					
				}
		}
			System.out.println("Die Reihenfolge der Belegung (Greedy-Prinzip): "+iMax);
			return iMax;	
	}
	
	//Für die Ausgabe in der Konsole; wandelt 0-3 in die Strings der jeweiligen Medikament-Typen um
	public String changeIndexToTypeOfMed(int index){
		String value="";
		switch(index){
		case 0: value = "Med60";
				break;
		case 1: value = "Med120";
				break;
		case 2: value = "Med250";
				break;
		case 3: value = "Med500";
				break;
		default: System.out.println("Fehler bezüglich des Indizes in getIndexMaximaleProduktionsmenge!");
		}
		return value;
	}
	
	public void addUeberschuesse(int bedarf, int index){
		LinkedList<Integer> tempDaten = new LinkedList<Integer>();

		Time startProduktion = belegtvon.get(index);
		int medTyp = belegtMitMed.get(startProduktion);
		int ueberschuss = belegtMitMenge.get(startProduktion)-bedarf;
			
		tempDaten.clear();
		tempDaten.add(medTyp);
		tempDaten.add(ueberschuss);
			
		ueberschuesse.put(startProduktion, tempDaten);
	}
	
	public HashMap<Time, LinkedList<Integer>> getUeberschuesse(){
		return ueberschuesse;
	}
	
	public boolean isUsed(){
		if(belegtvon.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	public LinkedList <Time> getBelegtvon(){
		return belegtvon;
	}
	
	public LinkedList <Time> getBelegtbis(){
		return belegtbis;
	}
	
	public Map<Time,Integer> getBelegtMitMenge(){
		return belegtMitMenge;
	}
	
	public Map<Time,Integer> getBelegtMitMed(){
		return belegtMitMed;
	}
	
	public Map<Time,Integer> getUrspruenglicheBedarfe(){
		return urspruenglicheBedarfe;
	}
	
	public int getKosten(){
		//Gibt die variablen Kosten für die Produktionslinie zurück
		int dauer =0;
		for(int i=0;i<belegtvon.size();i++){
			dauer += Time.getDifferenceInMinutes(belegtvon.get(i), belegtbis.get(i));
		}
		int kosten = (dauer/60)*1200; //1200 pro stunde
		return kosten;
	}
	
}
