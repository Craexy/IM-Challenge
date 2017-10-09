import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Produktionslinie {
	int id;
	Produktionsplaner prodp;
	LinkedList <Time> belegtvon;
	LinkedList <Time> belegtbis;
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
		//belegtmitMed = new LinkedList<Integer>();
		//belegtmitMenge = new LinkedList<Integer>();
		
		belegtMitMenge = new HashMap<Time, Integer>();
		belegtMitMed  = new HashMap<Time, Integer>();
	}
	
	public  boolean assignControl(LinkedList<Time> produktionsstart,LinkedList<Time> produktionsende, LinkedList<Integer> produktionsmenge){
		LinkedList<Integer> indizes = getIndexMaximaleProduktionsmenge(produktionsmenge);
		LinkedList<Integer> rest = assign(produktionsstart,produktionsende,produktionsmenge,indizes);
		//solange die aufaddierten werte von produktionsmenge >0
		return true;
		
		//for schleife die abbricht wenn die iMax linked list nicht abgearbeitet werden kann wegen kapzitätsproblemen
		//die indizes aus der iMax sind die Reste! diese werden zurückgegeben
	}

	public LinkedList <Integer> assign(LinkedList<Time> produktionsstart,LinkedList<Time> produktionsende, LinkedList<Integer> produktionsmenge, LinkedList<Integer> indizes){
		//In dieser Funktion schon Umwandeln von 60 in 120 oder ähnliches, um alle zu produzieren.
		//wenn die Ladung nicht komplett produziert werden kann -> neue Produktionslinie -> Übergabe der Medikamente die diese produktionslinie nicht produzieren konnte
		try{
			//belegtvon und belegtbis sortieren und dann prüfen ob produktionsstart earlierthan belegtbis einträge
			//produktionsstart
			//statt index indizes.getFirst benutzen und die returnwerte ändern
			if(belegtvon.size()==0){
				//Test ob es Produktionsmengen gibt, wenn ja dann wird maximale genommen
				
				if(indizes!=null){
					//Hier noch testen ob maximale kapazität mit dem einen Auftrag schon überschritten wird
					//also wieviele 60er,120er... überhaupt roduziert werden können (switch-anweisung)
					//produktionsdauer mit an diese Methode übergeben und dann hochrechnen
					
					belegtvon.add(produktionsstart.get(indizes.getFirst()));
					belegtbis.add(produktionsende.get(indizes.getFirst()));
					
					belegtMitMenge.put(produktionsstart.get(indizes.getFirst()), produktionsmenge.get(indizes.getFirst()));
					belegtMitMed.put(produktionsstart.get(indizes.getFirst()), indizes.getFirst());
					
					//belegtmitMed.add(index);
					//belegtmitMenge.add(produktionsmenge.get(index));
					
					System.out.println("Die Maschine "+this.id+" wurde von "+produktionsstart.get(indizes.getFirst())+" bis "+produktionsende.get(indizes.getFirst())+" mit "+produktionsmenge.get(indizes.getFirst())+" Einheiten des Medikamentes "+changeIndexToTypeOfMed(indizes.getFirst())+" belegt!");
					//Den Werte in Produktionsmenge zu 0 machen, damit sie nicht nochmal betrachtet werden und eine neue maximale produktionsmenge gefunden werden kann
					//Die LinkedList produktionsmenge wird quasi abgearbeitet bis alles = 0 ist
					//Die mengen bleiben aber in der Hashmap belegtmitMenge erhalten
					produktionsmenge.set(indizes.getFirst(), 0);
					return indizes;
					//assign(produktionsstart,produktionsende,produktionsmenge); //Aufpassen beim rückgabewert der methode 
				}else{
					System.out.println("Es wurden keine Produktionsmengen angefragt.");
					return indizes;
				}
				
			}else{
				//Hier wenn schon aufträge vorhanden sind -> passt alles oder muss angepasst werden?
				System.out.println("Es gibt eine Belegung der Produktionslinie, passen noch weitere?");
				if(indizes!=null){
					//Hier noch testen ob maximale kapazität mit dem einen Auftrag schon überschritten wird
					//also wieviele 60er,120er... überhaupt roduziert werden können (switch-anweisung)
					//produktionsdauer mit an diese Methode übergeben und dann hochrechnen
					
					//Testen ob zeitliche Überlappung vorliegt
					//-> checkForConflicts()
					
					//Bei belegung auf Sortierung achten! compareTo()
					//belegtvon.add(produktionsstart.get(index));
				    //belegtbis.add(produktionsende.get(index));

					//belegtMitMenge.put(produktionsstart.get(index), produktionsmenge.get(index));
					//belegtMitMed.put(produktionsstart.get(index), index);
					
					
					//System.out.println("Die Maschine "+this.id+" wurde von "+produktionsstart.get(index)+" bis "+produktionsende.get(index)+" mit "+produktionsmenge.get(index)+" Einheiten des Medikamentes "+changeIndexToTypeOfMed(index)+" belegt!");
					//Den Werte in Produktionsmenge zu 0 machen, damit sie nicht nochmal betrachtet werden und eine neue maximale produktionsmenge gefunden werden kann
					//Die LinkedList produktionsmenge wird quasi abgearbeitet bis alles = 0 ist
					//Die mengen bleiben aber in der Hashmap belegtmitMenge erhalten
					//produktionsmenge.set(index, 0);
					//assign(produktionsstart,produktionsende,produktionsmenge); //Aufpassen beim rückgabewert der methode
					//besprechen ob es theoretisch klappt die methode immer wieder selbst aufzurufen bis problem gelöst ist
				}else{
					System.out.println("Alles Produktionsanfragen wurden an die Produktionslinien verteilt!");
					return indizes;
				}
				
			}
			
		}catch (NullPointerException e){
			
		}
		System.out.println("Ende");
		return indizes;
		
	}
	
	public boolean checkForConflict(Time produktionsstart,Time produktionsende){
		System.out.println("hier");
		//Was muss hier zurückgegeben werden? Wahrscheinlich geht boolean
		return false;
	}
	
	//@todo: neue Variante die zuerst die menge produziert die am ehesten dem Bedarf entspricht
	
	//Gibt die Indizes der Produktionsmengen, absteigend sortiert, zurück
	//Produktionsmengen von 0 ausschließen und die linkedlist in ner forschleife benutzen -> solange assign bis nichts mehr in iMax
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
								l = iMax.size(); //endlosschleife vermeiden
							}
							if(l==(iMax.size()-1)){
								iMax.addLast(i);
								l = iMax.size(); //endlosschleife vermeiden
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
		default: System.out.println("Fehler bezüglich des Indizes in getIndexMaximaleProduktionsmenge!");
		}
		return value;
	}
	
}
