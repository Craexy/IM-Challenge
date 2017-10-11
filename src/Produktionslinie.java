import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Produktionslinie {
	private int id;
	private Produktionsplaner prodp;
	
	LinkedList <Time> belegtvon;
	LinkedList <Time> belegtbis;
	LinkedList <Integer> ueberschuesse;
	//LinkedList <Integer> belegtmitMenge;	//Veraltet, weil man auf die Mengen und Meds zugreifen muss nachdem belegtvon und belegtbis sortiert wurden
	//LinkedList <Integer> belegtmitMed;
	private Map<Time, Integer> belegtMitMenge;
	//0=Med60;1=Med120;2=Med250;3=Med500 //kann mit methode changeIndexToTypeOfMed umgewandelt werden
	private Map<Time, Integer> belegtMitMed;
	

	public Produktionslinie(Produktionsplaner prodp, int id){
		this.id = id;
		this.prodp = prodp;
		
		belegtvon = new LinkedList<Time>();
		belegtbis = new LinkedList<Time>();
		ueberschuesse = new LinkedList<Integer>();
		
		belegtMitMenge = new HashMap<Time, Integer>();
		belegtMitMed  = new HashMap<Time, Integer>();
		
	}
	
	public  Map<Integer, Integer> assignControl(LinkedList<Time> produktionsstart,LinkedList<Time> produktionsende, LinkedList<Integer> produktionsmenge){
		LinkedList<Integer> indizes = getIndexMaximaleProduktionsmenge(produktionsmenge);
		
		LinkedList<Integer> restTemp = assign(produktionsstart,produktionsende,produktionsmenge,indizes);
		Map<Integer,Integer> reste = new HashMap<Integer,Integer>();
		for(int y=0;y<restTemp.size();y++){
			reste.put(restTemp.get(y), produktionsmenge.get(restTemp.get(y)));
		}
		
		return reste;
	}

	public LinkedList <Integer> assign(LinkedList<Time> produktionsstart,LinkedList<Time> produktionsende, LinkedList<Integer> produktionsmenge, LinkedList<Integer> indizes){
		//In dieser Funktion schon Umwandeln von 60 in 120 oder �hnliches, um alle zu produzieren.
		//wenn die Ladung nicht komplett produziert werden kann -> neue Produktionslinie -> �bergabe der Medikamente die diese produktionslinie nicht produzieren konnte
		
		
		try{
			//belegtvon und belegtbis sortieren und dann pr�fen ob produktionsstart earlierthan belegtbis eintr�ge
			//produktionsstart
			//statt index indizes.getFirst benutzen und die returnwerte �ndern
			if(indizes!=null){
				int durchlauf = indizes.size();
				//For-schleife um alles mit indizes.size()
				for(int c=0;c<durchlauf;c++){
					
					if(belegtvon.size()==0){
							//Hier noch testen ob maximale kapazit�t mit dem einen Auftrag schon �berschritten wird
							//also wieviele 60er,120er... �berhaupt roduziert werden k�nnen (switch-anweisung)
							//produktionsdauer mit an diese Methode �bergeben und dann hochrechnen
							
							belegtvon.add(produktionsstart.get(indizes.getFirst()));
							belegtbis.add(produktionsende.get(indizes.getFirst()));
							
							belegtMitMenge.put(produktionsstart.get(indizes.getFirst()), produktionsmenge.get(indizes.getFirst()));
							belegtMitMed.put(produktionsstart.get(indizes.getFirst()), indizes.getFirst());
							
							System.out.println("Die Maschine "+this.id+" wurde von "+produktionsstart.get(indizes.getFirst())+" bis "+produktionsende.get(indizes.getFirst())+" mit "+produktionsmenge.get(indizes.getFirst())+" Einheiten des Medikamentes "+changeIndexToTypeOfMed(indizes.getFirst())+" belegt!");
							indizes.removeFirst();
						
					}else{
						//Hier wenn schon auftr�ge vorhanden sind -> passt alles oder muss angepasst werden?
						System.out.println("Es gibt eine Belegung der Produktionslinie, passen noch weitere?");
						if(indizes!=null){
							//Hier noch testen ob maximale kapazit�t mit dem einen Auftrag schon �berschritten wird
							//also wieviele 60er,120er... �berhaupt roduziert werden k�nnen (switch-anweisung)
							//produktionsdauer mit an diese Methode �bergeben und dann hochrechnen
							
							//Testen ob zeitliche �berlappung vorliegt
							int check = checkForConflicts(produktionsstart.get(indizes.getFirst()),produktionsende.get(indizes.getFirst()));
							if(check!=-1){
								System.out.println("Ja");
								//Sortiert einf�gen
								belegtvon.add(check, produktionsstart.get(indizes.getFirst()));
								belegtbis.add(check, produktionsende.get(indizes.getFirst()));
								
								belegtMitMenge.put(produktionsstart.get(indizes.getFirst()), produktionsmenge.get(indizes.getFirst()));
								belegtMitMed.put(produktionsstart.get(indizes.getFirst()), indizes.getFirst());
								
								indizes.removeFirst();
								
								System.out.println(belegtvon);
								System.out.println(belegtbis);
							}else{
								System.out.println("Nein");
								//return indizes; //kein return an dieser stelle
								//hier muss element ans ende der liste verschoben werden (sozusagen �berspringen)
							}
							
							
							//System.out.println("Die Maschine "+this.id+" wurde von "+produktionsstart.get(index)+" bis "+produktionsende.get(index)+" mit "+produktionsmenge.get(index)+" Einheiten des Medikamentes "+changeIndexToTypeOfMed(index)+" belegt!");
							}else{
							System.out.println("Alles Produktionsanfragen wurden an die Produktionslinien verteilt!");
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
		return indizes;
		
	}
	
	public int checkForConflicts(Time produktionsstart,Time produktionsende){
		int index=-1;
		
		if(produktionsende.isEarlierThan(belegtvon.getFirst())){
			return 0;
		}
		if(produktionsstart.isLaterThan(belegtbis.getLast())){
			return belegtbis.size();
		}
		
		for(int z=0;z<belegtbis.size();z++){
			if(produktionsstart.isLaterThan(belegtbis.get(z))){
				index = z;
			}
		}
		if(produktionsende.isEarlierThan(belegtvon.get(index+1))){
			return index;
		}

		return -1;
	
	}
	
	//@todo: neue Variante die zuerst die menge produziert die am ehesten dem Bedarf entspricht
	
	//Gibt die Indizes der Produktionsmengen, absteigend sortiert, zur�ck
	//Produktionsmengen von 0 ausschlie�en und die linkedlist in ner forschleife benutzen -> solange assign bis nichts mehr in iMax
	public LinkedList<Integer> getIndexMaximaleProduktionsmenge(LinkedList<Integer> produktionsmenge){
		int max=0;
		LinkedList <Integer> iMax = new LinkedList<Integer>();
		System.out.println(produktionsmenge);
		
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
		
			System.out.println(iMax); 
			return iMax;	
	}
	
	//Vielleicht brauch ich noch die umgekehrte Version dieser Methode. Je nachdem was sih noch ergibt im code
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
		default: System.out.println("Fehler bez�glich des Indizes in getIndexMaximaleProduktionsmenge!");
		}
		return value;
	}
	
}
