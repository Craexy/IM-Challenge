import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Tourenplaner {
	
	private Routenplaner routenplaner;
	private String route;
	private int routenzeit;
	private LinkedList<Fahrzeug> fahrzeuge;
	
	//Zeitpunkt, zu dem das erste Medikament zugestellt wird
	private String startzeit = "6:30";
	
	private Map<String, LinkedList<Time>> bedarfe = new HashMap<String, LinkedList<Time>>();
	private LinkedList<Time> bedarfA = new LinkedList<Time>();
	private LinkedList<Time> bedarfB = new LinkedList<Time>();
	private LinkedList<Time> bedarfC = new LinkedList<Time>();
	private LinkedList<Time> bedarfD = new LinkedList<Time>();
	private LinkedList<Time> bedarfE = new LinkedList<Time>();
	private LinkedList<Time> bedarfF = new LinkedList<Time>();
	private LinkedList<Time> bedarfG = new LinkedList<Time>();
	private LinkedList<Time> bedarfH = new LinkedList<Time>();
	
	private Map<String, Integer> entladezeiten = new HashMap<String, Integer>();
	private int entladezeitA = 15;
	private int entladezeitB = 30;
	private int entladezeitC = 30;
	private int entladezeitD = 15;
	private int entladezeitE = 30;
	private int entladezeitF = 15;
	private int entladezeitG = 30;
	private int entladezeitH = 15;
	
	//Minuten nach Produktion, ab denen Medikament60 benutzt werden kann 
	private int startNutzMed60 = 270;
	//Minuten nach Produktion, bis zu denen Medikament60 benutzt werden kann 
	//minus 30, da die Medikamente 30 Minuten vor Benutzung vorliegen müssen
	//--> es stehen also 30 Minuten weniger zur Entladung zur Verfügung
	private int endNutzMed60 = 450-30;
	private int dauerNutzMed60 = endNutzMed60-startNutzMed60;
	
	private int startNutzMed120 = 390;
	private int endNutzMed120 = 600-30;
	private int dauerNutzMed120 = endNutzMed120 - startNutzMed120;

	private int startNutzMed250 = 540;
	private int endNutzMed250 = 720-30;
	private int dauerNutzMed250 = endNutzMed250 - startNutzMed250;

	private int startNutzMed500 = 660;
	private int endNutzMed500 = 840-30;
	private int dauerNutzMed500 = endNutzMed500 - startNutzMed500;
	
	public Tourenplaner() {
		routenplaner = new Routenplaner();
		route = routenplaner.getRoute();//routenzeit = routenplaner.getFahrtzeit(route.split(",")[0], route.split(",")[route.length()]);
		
		fahrzeuge = new LinkedList<Fahrzeug>();
		
		bedarfe.put("A", bedarfA);
		bedarfe.put("B", bedarfB);
		bedarfe.put("C", bedarfC);
		bedarfe.put("D", bedarfD);
		bedarfe.put("E", bedarfE);
		bedarfe.put("F", bedarfF);
		bedarfe.put("G", bedarfG);
		bedarfe.put("H", bedarfH);
		
		entladezeiten.put("A", entladezeitA);
		entladezeiten.put("B", entladezeitB);
		entladezeiten.put("C", entladezeitC);
		entladezeiten.put("D", entladezeitD);
		entladezeiten.put("E", entladezeitE);
		entladezeiten.put("F", entladezeitF);
		entladezeiten.put("G", entladezeitG);
		entladezeiten.put("H", entladezeitH);
		
		//Daten mit Uhrzeiten der Bedarfe werden eingelesen
		//this.befülleDaten();
	    //Variante 1
		//this.variante1();
	    
		//Variante 2
		this.befülleDaten();
		this.variante2();
	   
	    
	    
	    //Fahrzeuge fahren alternativ nur zur Hälfte und kehren dann um
	    
	 /*   boolean bedarfGedeckt = false;
	    do {
	    	//welche Medikamente sind zu welchem Zeitpunkt noch übrig?
	    	//ab wann/bis wann sind diese Medikamente nutzbar?
	    	//wo sind (unter Einbezug der Fahrtzeit) 
	    	//starte Fahrzeug mit diesem Medikament und
	    } 
	    while (!bedarfGedeckt);*/
	}
	
	private void befülleDaten() {
		String tempStr = "7:30 7:40 8:0 8:10 8:30 8:40 8:50 9:0 9:30 9:50 10:30 10:50 11:30 11:50 12:30 12:50 13:30 14:0 14:30 15:0 15:40 16:0";
		for (int i=0; i<tempStr.split(":").length-1; i++) {
			bedarfA.add(new Time(Integer.parseInt(tempStr.split(" ")[i].split(":")[0]),
					Integer.parseInt(tempStr.split(" ")[i].split(":")[1]), 0));
		}
		
		tempStr = "7:10 7:20 7:45 8:5 8:15 8:35 8:50 8:55 9:20 9:25 9:45 10:10 10:15 10:25 10:55 11:55 13:15 13:20 13:25 13:35 14:5 14:10 14:35 14:40 14:45 14:50 14:55 15:0 15:20 16:5 16:55";
		for (int i=0; i<tempStr.split(":").length-1; i++) {
			bedarfB.add(new Time(Integer.parseInt(tempStr.split(" ")[i].split(":")[0]),
					Integer.parseInt(tempStr.split(" ")[i].split(":")[1]), 0));
		}
		
		tempStr = "6:30 6:35 6:40 7:5 7:30 8:0 8:15 8:40 8:55 9:0 9:5 9:50 10:15 10:25 10:30 10:35 10:45 11:0 11:10 11:20 11:35 11:40 11:45 11:55 12:20 12:25 12:50 13:5 13:20 13:25 13:30 13:40 13:45 14:10 14:15 14:20 14:55 15:30 15:35 15:40 16:30 16:35 16:40 17:5 17:20";
		for (int i=0; i<tempStr.split(":").length-1; i++) {
			bedarfC.add(new Time(Integer.parseInt(tempStr.split(" ")[i].split(":")[0]),
					Integer.parseInt(tempStr.split(" ")[i].split(":")[1]), 0));
		}
		
		tempStr = "6:50 8:0 8:15 8:20 9:0 9:30 9:35 9:40 10:5 10:10 10:15 10:25 10:30 11:0 11:15 11:30 11:40 11:55 12:30 12:50 13:20 13:30 13:45 14:15 14:35 14:45 15:0 15:10 15:15 15:20 15:55 16:10 16:20 16:25 16:30 16:35 17:15";
		for (int i=0; i<tempStr.split(":").length-1; i++) {
			bedarfD.add(new Time(Integer.parseInt(tempStr.split(" ")[i].split(":")[0]),
					Integer.parseInt(tempStr.split(" ")[i].split(":")[1]), 0));
		}
		
		tempStr = "6:35 6:40 6:55 7:5 7:25 7:40 7:50 8:20 8:30 8:40 8:55 9:5 9:10 9:15 9:50 10:10 10:35 10:40 10:50 11:0 11:15 11:30 11:35 11:40 12:30 12:40 12:45 13:10 13:15 13:20 13:30 13:35 13:50 14:10 14:40 14:45 15:10 15:15 15:55 16:0 16:25 16:40 17:0 17:10 17:20";
		for (int i=0; i<tempStr.split(":").length-1; i++) {
			bedarfE.add(new Time(Integer.parseInt(tempStr.split(" ")[i].split(":")[0]),
					Integer.parseInt(tempStr.split(" ")[i].split(":")[1]), 0));
		}
		
		tempStr = "6:30 6:40 6:45 6:50 6:55 7:0 7:10 7:15 7:20 8:15 8:20 8:25 8:35 9:30 9:45 10:35 10:45 10:50 11:5 11:30 11:35 11:40 11:55 12:10 12:15 12:20 12:30 12:35 12:40 13:15 13:20 13:35 13:55 14:5 14:20 14:25 14:30 14:40 15:5 15:25 15:55 16:0 16:5 16:15 16:40 16:55 17:15";
		for (int i=0; i<tempStr.split(":").length-1; i++) {
			bedarfF.add(new Time(Integer.parseInt(tempStr.split(" ")[i].split(":")[0]),
					Integer.parseInt(tempStr.split(" ")[i].split(":")[1]), 0));
		}
		
		tempStr = "6:50 7:5 7:15 7:45 7:50 7:55 8:5 8:15 8:50 8:55 9:5 9:15 9:40 9:45 10:0 10:5 10:10 10:15 10:25 10:40 11:10 11:35 11:45 12:5 12:10 12:30 12:40 13:20 13:25 13:50 14:10 14:15 14:25 14:35 14:40 14:50 15:0 15:15 15:20 15:45 16:5 16:10 16:15 16:35 16:45 17:5 17:20";
		for (int i=0; i<tempStr.split(":").length-1; i++) {
			bedarfG.add(new Time(Integer.parseInt(tempStr.split(" ")[i].split(":")[0]),
					Integer.parseInt(tempStr.split(" ")[i].split(":")[1]), 0));
		}
		
		tempStr = "6:50 6:55 7:0 7:40 7:45 8:0 9:30 9:50 9:55 10:0 10:10 10:15 10:20 10:30 10:45 10:50 11:0 11:15 11:30 11:40 11:45 12:45 13:0 13:15 13:35 13:40 13:50 14:20 14:25 14:35 14:55 15:20 15:55 16:0 16:20 16:30 16:50 17:0 17:5 17:15";
		for (int i=0; i<tempStr.split(":").length-1; i++) {
			bedarfH.add(new Time(Integer.parseInt(tempStr.split(" ")[i].split(":")[0]),
					Integer.parseInt(tempStr.split(" ")[i].split(":")[1]), 0));
		}
	}
	
	private void berechneTouren() {
		String aktuellerStopp = "A";
		LinkedList<Time> aktuellerBedarf;
		//aktueller Zeitpunkt
		//wird bei jedem Schritt der Zeit in Anspruch nimmt verändert
		Time zeitpunktAktuell;
		
		//Start bei 0
		Fahrzeug fahrzeug1 = new Fahrzeug();
			fahrzeug1.setStrecke("A");
			fahrzeug1.set60(fahrzeug1.get60()+1);
			
			System.out.println("Fahrzeug 1 startet bei A.");
			
			Time temp = bedarfA.getFirst();
			//Zeitpunkt, zu dem Medikament ausgeladen sein muss 
			//--> Medikament muss 30 Minuten vor Benutzung ausgeladen sein
			Time zeitpunktAusgeladen = temp.reduceTime(30);
			//Zeitpunkt zu dem mit ausladen begonnen werden muss
			Time zeitpunktAusladen = new Time(zeitpunktAusgeladen.getStunden(), zeitpunktAusgeladen.getMinuten(), 0);
			zeitpunktAusladen.reduceTime(entladezeitA);
			zeitpunktAktuell = new Time(zeitpunktAusladen.getStunden(), zeitpunktAusladen.getMinuten(), 0);
			//Fahrzeug wird so beladen, dass es zu Beginn der nutzbaren Zeit vorhanden ist
			//Also Beladezeitpunkt = Ausgeladenzeitpunkt - Dauer bis Med60 verwendet werden kann
			fahrzeug1.setStartzeitBeladung60(zeitpunktAusgeladen);
			fahrzeug1.setStartzeitBeladung60(fahrzeug1.getStartzeitBeladung60().reduceTime(startNutzMed60));
			//Fahrzeug fährt los wenn es ausgeladen werden muss, da keine Fahrtzeit von 0 zu 0
			fahrzeug1.setStartzeitFahrt(zeitpunktAusladen);	
			//erster Bedarf gedeckt
			bedarfA.removeFirst();
			
			
			//prüfen welche weiteren Med60 für 0 bereits jetzt abgeladen werden können
			boolean deckbarerBedarfVorhanden = true;
				do {
					Time temp1 = bedarfA.getFirst();
					Time temp2 = fahrzeug1.getStartzeitBeladung60().addTime(endNutzMed60);
					if (temp1.isEarlierThan(temp2)) {
						fahrzeug1.set60(fahrzeug1.get60()+1);
						bedarfA.removeFirst();
					}
					else deckbarerBedarfVorhanden = false;
				}
				while (deckbarerBedarfVorhanden);
				
			
				
		//Alle möglichen Medikamente wurden ausgelande
		zeitpunktAktuell = zeitpunktAusgeladen;
		System.out.println("Fahrzeug 1 hat bei A "+fahrzeug1.get60()+" Einheit(en) von Med60 abgeliefert "
				+ "und fährt um "+zeitpunktAktuell.toString()+" Uhr zum nächsten Stopp weiter.");
		
		for (int i=1; i<8;i++) {
		//Weiterfahrt zu erstem Stopp
			int temp1 = Routenplaner.getFahrtzeit(aktuellerStopp, routenplaner.getRoute().split(",")[i]);
			aktuellerStopp=routenplaner.getRoute().split(",")[i];
			zeitpunktAktuell = zeitpunktAktuell.addTime(temp1);
			System.out.println("Fahrzeug 1 kommt bei Stopp "+aktuellerStopp+" um "+zeitpunktAktuell.toString()+" Uhr an.");
			fahrzeug1.setStrecke(fahrzeug1.getStrecke()+aktuellerStopp);
			aktuellerBedarf = (LinkedList<Time>) bedarfe.get(aktuellerStopp);
		
		//Solange Bedarf vorhanden ist, der durch das aktuelle Fahrzeug durch Med60 gedeckt werden kann 
		//soll dies getan werden
		//Geprüft ob Bedarf vorhanden ist, nachdem das Fahrzeug beim Standort ankommt UND bevor das Medikament abgelaufen ist
			LinkedList<Time> frühBedarf = new LinkedList<Time>();
			do {
				frühBedarf.addFirst(aktuellerBedarf.removeFirst());
			}
			while (aktuellerBedarf.getFirst()
					.isEarlierThan(fahrzeug1.getStartzeitBeladung60().addTime(endNutzMed60)));
			
			deckbarerBedarfVorhanden=true;
			do {
				if (frühBedarf.getFirst().isLaterThan(zeitpunktAktuell)) {	
				fahrzeug1.set60(fahrzeug1.get60()+1);
				frühBedarf.removeFirst();
				}
				else deckbarerBedarfVorhanden = false;
			} 
			while (deckbarerBedarfVorhanden);
			aktuellerBedarf.addAll(frühBedarf);
			aktuellerBedarf.sort(null);
			
			System.out.println("Fahrzeug1 hat nach Stopp "+aktuellerStopp+" insgesamt "+fahrzeug1.get60()+" Einheiten von Med60 abgeliefert.");
		}
		
		//prüfen welche Med120 abgeladen werden können
		aktuellerStopp = "A";
		zeitpunktAktuell = new Time(fahrzeug1.getStartzeitFahrt().getStunden(), fahrzeug1.getStartzeitFahrt().getMinuten(),0);
		//Med120 soll so eingeladen werden, dass es zum ersten Bedarf, der nicht mehr von Med60
		//gedeckt werden kann zur Verfügung steht --> nur wenn dieser Zeitpunkt nicht nach 
		//Start des Fahrzeuges ist
		Time start120 = (new Time(bedarfA.getFirst().getStunden(), bedarfA.getFirst().getMinuten(), 0).reduceTime(startNutzMed120));
		if (start120.isEarlierThan(fahrzeug1.getStartzeitFahrt())) {
		fahrzeug1.setStartzeitBeladung120(start120);}
		else fahrzeug1.setStartzeitBeladung120(fahrzeug1.getStartzeitFahrt());
		for (int i=0; i<8;i++) {
			int temp1 = Routenplaner.getFahrtzeit(aktuellerStopp, routenplaner.getRoute().split(",")[i]);
			aktuellerStopp=routenplaner.getRoute().split(",")[i];
			zeitpunktAktuell = zeitpunktAktuell.addTime(temp1);
			aktuellerBedarf = (LinkedList<Time>) bedarfe.get(aktuellerStopp);
			//Solange Bedarf vorhanden ist, der durch das aktuelle Fahrzeug durch Med120 gedeckt werden kann 
			//soll dies getan werden
			//Geprüft wird ob Bedarf vorhanden ist, nachdem das Fahrzeug beim Standort ankommt UND bevor das Medikament abgelaufen ist
				LinkedList<Time> frühBedarf = new LinkedList<Time>();
				do {
					frühBedarf.addFirst(aktuellerBedarf.removeFirst());
				}
				while (aktuellerBedarf.getFirst()
						.isEarlierThan(fahrzeug1.getStartzeitBeladung120().addTime(endNutzMed120)));
				
				deckbarerBedarfVorhanden=true;
				do {
					try {
					if (frühBedarf.getFirst().isLaterThan(zeitpunktAktuell)) {	
					fahrzeug1.set120(fahrzeug1.get120()+1);
					frühBedarf.removeFirst();
					}
					else deckbarerBedarfVorhanden = false;}
					catch (Exception e) {deckbarerBedarfVorhanden = false;}
				} 
				while (deckbarerBedarfVorhanden);
				aktuellerBedarf.addAll(frühBedarf);
				aktuellerBedarf.sort(null);
				
				System.out.println("Fahrzeug1 hat nach Stopp "+aktuellerStopp+" insgesamt "+fahrzeug1.get120()+" Einheiten von Med120 abgeliefert.");
			}
			
		
				//prüfen welche Med250 abgeladen werden können
				aktuellerStopp = "A";
				zeitpunktAktuell = new Time(fahrzeug1.getStartzeitFahrt().getStunden(), fahrzeug1.getStartzeitFahrt().getMinuten(),0);
				//Med250 soll so eingeladen werden, dass es zum ersten Bedarf, der nicht mehr von Med250
				//gedeckt werden kann zur Verfügung steht --> nur wenn dieser Zeitpunkt nicht nach 
				//Start des Fahrzeuges ist
				Time start250 = (new Time(bedarfA.getFirst().getStunden(), bedarfA.getFirst().getMinuten(), 0).reduceTime(startNutzMed250));
				if (start250.isEarlierThan(fahrzeug1.getStartzeitFahrt())) {
				fahrzeug1.setStartzeitBeladung250(start250);}
				else fahrzeug1.setStartzeitBeladung250(fahrzeug1.getStartzeitFahrt());
				
				for (int i=0; i<8;i++) {
						int temp1 = Routenplaner.getFahrtzeit(aktuellerStopp, routenplaner.getRoute().split(",")[i]);
						aktuellerStopp=routenplaner.getRoute().split(",")[i];
						zeitpunktAktuell = zeitpunktAktuell.addTime(temp1);
						aktuellerBedarf = (LinkedList<Time>) bedarfe.get(aktuellerStopp);
					
					//Solange Bedarf vorhanden ist, der durch das aktuelle Fahrzeug durch Med120 gedeckt werden kann 
					//soll dies getan werden
					//Geprüft wird ob Bedarf vorhanden ist, nachdem das Fahrzeug beim Standort ankommt UND bevor das Medikament abgelaufen ist
						LinkedList<Time> frühBedarf = new LinkedList<Time>();
						deckbarerBedarfVorhanden = true;
						do {
							frühBedarf.addFirst(aktuellerBedarf.removeFirst());
							try {if (!aktuellerBedarf.getFirst()
							.isEarlierThan(fahrzeug1.getStartzeitBeladung250().addTime(endNutzMed250))) {
								deckbarerBedarfVorhanden=false;
							}} catch (Exception e) {deckbarerBedarfVorhanden=false;}
						}
						while (deckbarerBedarfVorhanden);
						
						deckbarerBedarfVorhanden=true;
						do {
							try {
							if (frühBedarf.getFirst().isLaterThan(zeitpunktAktuell)) {	
							fahrzeug1.set250(fahrzeug1.get250()+1);
							frühBedarf.removeFirst();
							}
							else deckbarerBedarfVorhanden = false;}
							catch (Exception e) {deckbarerBedarfVorhanden = false;}
						} 
						while (deckbarerBedarfVorhanden);
						aktuellerBedarf.addAll(frühBedarf);
						aktuellerBedarf.sort(null);
						
						System.out.println("Fahrzeug1 hat nach Stopp "+aktuellerStopp+" insgesamt "+fahrzeug1.get250()+" Einheiten von Med250 abgeliefert.");
					}
				
				//prüfen welche Med500 abgeladen werden können
				zeitpunktAktuell = new Time(fahrzeug1.getStartzeitFahrt().getStunden(), fahrzeug1.getStartzeitFahrt().getMinuten(),0);
				//Med500 soll so eingeladen werden, dass es zum ersten Bedarf, der nicht mehr von Med250
				//gedeckt werden kann zur Verfügung steht --> nur wenn dieser Zeitpunkt nicht nach 
				//Start des Fahrzeuges ist
				LinkedList<Time> zweiterBedarf = (LinkedList<Time>)bedarfe.get(routenplaner.getRoute().split(",")[1]);
				Time start500 = (new Time(zweiterBedarf.getFirst().getStunden(), zweiterBedarf.getFirst().getMinuten(), 0).reduceTime(startNutzMed500).addTime(Routenplaner.getFahrtzeit("A", routenplaner.getRoute().split(",")[1])));
				if (start500.isEarlierThan(fahrzeug1.getStartzeitFahrt())) {
				fahrzeug1.setStartzeitBeladung500(start500);}
				else fahrzeug1.setStartzeitBeladung500(fahrzeug1.getStartzeitFahrt());
				
				for (int i=1; i<8;i++) {
					int temp1 = Routenplaner.getFahrtzeit(aktuellerStopp, routenplaner.getRoute().split(",")[i]);
					aktuellerStopp=routenplaner.getRoute().split(",")[i];
					zeitpunktAktuell = zeitpunktAktuell.addTime(temp1);
					aktuellerBedarf = (LinkedList<Time>) bedarfe.get(aktuellerStopp);
					
					//Solange Bedarf vorhanden ist, der durch das aktuelle Fahrzeug durch Med500 gedeckt werden kann 
					//soll dies getan werden
					//Geprüft wird ob Bedarf vorhanden ist, nachdem das Fahrzeug beim Standort ankommt UND bevor das Medikament abgelaufen ist
						LinkedList<Time> frühBedarf = new LinkedList<Time>();
						deckbarerBedarfVorhanden = true;
						do {
							frühBedarf.addFirst(aktuellerBedarf.removeFirst());
							try {if (!aktuellerBedarf.getFirst()
							.isEarlierThan(fahrzeug1.getStartzeitBeladung500().addTime(endNutzMed500))) {
								deckbarerBedarfVorhanden=false;
							}} catch (Exception e) {deckbarerBedarfVorhanden=false;}
						}
						while (deckbarerBedarfVorhanden);
						
						deckbarerBedarfVorhanden=true;
						do {
							try {
							if (frühBedarf.getFirst().isLaterThan(zeitpunktAktuell)) {	
							fahrzeug1.set500(fahrzeug1.get500()+1);
							frühBedarf.removeFirst();
							}
							else deckbarerBedarfVorhanden = false;}
							catch (Exception e) {deckbarerBedarfVorhanden = false;}
						} 
						while (deckbarerBedarfVorhanden);
						aktuellerBedarf.addAll(frühBedarf);
						aktuellerBedarf.sort(null);
						
						System.out.println("Fahrzeug1 hat nach Stopp "+aktuellerStopp+" insgesamt "+fahrzeug1.get500()+" Einheiten von Med500 abgeliefert.");
					}
    fahrzeuge.add(fahrzeug1);
}
	
	private void variante1(){
		//erstes Fahrzeug fährt optimierte Route
				Fahrzeug fahrzeug1  = this.neuesFahrzeugSchicken(routenplaner.getRoute());
				fahrzeuge.add(fahrzeug1);
				//zweites Fahrzeug fährt umgekehrte Route
				String strecke2 = "";
			    for (int i=0; i<7; i++) {
			    	strecke2 = strecke2+routenplaner.getRoute().split(",")[7-i]+",";
			    	}
			    Fahrzeug fahrzeug2 = this.neuesFahrzeugSchicken(strecke2);
			    fahrzeuge.add(fahrzeug2);
	}
	
	private void variante2() {
		String strecke1 = "";
		for(int i=0;i<4;i++) {
			if(i<3) {
		strecke1 = strecke1 +routenplaner.getRoute().split(",")[i]+ ",";}
			else strecke1 = strecke1 +routenplaner.getRoute().split(",")[i];
		}
		System.out.println(strecke1);
		fahrzeuge.add(this.neuesFahrzeugSchicken(strecke1));
		
		String strecke2 = "";
		for(int i=4;i<8;i++) {
			if(i<7) {
		strecke2 = strecke2 +routenplaner.getRoute().split(",")[i]+ ",";
			} else strecke2 = strecke2 +routenplaner.getRoute().split(",")[i];
			}
		System.out.println(strecke2);
		Fahrzeug fahrzeug2 = this.neuesFahrzeugSchicken(strecke2);
		fahrzeuge.add(fahrzeug2);
	}
	
	private Fahrzeug neuesFahrzeugSchicken(String strecke) {
		//Zeit zu der das Fahrzeug wieder zurück im Depot ist
		Time endzeit = new Time(0,0,0);
		Fahrzeug fahrzeug = new Fahrzeug();
		System.out.println("Neues Fahrzeug startet");
		fahrzeug.setStrecke(strecke);
		String aktuellerStopp = "A";
		Time ersterBedarf = bedarfe.get(strecke.split(",")[0]).getFirst().getNewInstance();
		int fahrtzeit = Routenplaner.getFahrtzeit("A", strecke.split(",")[0]);
	    Time zeitpunktAktuell = ersterBedarf.getNewInstance().reduceTime(fahrtzeit);
	    fahrzeug.setStartzeitFahrt(zeitpunktAktuell.getNewInstance());
	    fahrzeug.setStartzeitBeladung60(zeitpunktAktuell.getNewInstance().reduceTime(startNutzMed60));
	    LinkedList<Time> aktuellerBedarf = new LinkedList<Time>();
	    boolean deckbarerBedarfVorhanden;
	    for (int i=0; i<strecke.split(",").length;i++) {
			//Fahrt zu erstem Stopp
				int naechsteFahrtzeit = Routenplaner.getFahrtzeit(aktuellerStopp, routenplaner.getRoute().split(",")[i]);
				aktuellerStopp=fahrzeug.getStrecke().split(",")[i];
				zeitpunktAktuell = zeitpunktAktuell.addTime(naechsteFahrtzeit);
				//Entladezeit muss berücksichtigt werden
				zeitpunktAktuell = zeitpunktAktuell.addTime(entladezeiten.get(aktuellerStopp));
				aktuellerBedarf = (LinkedList<Time>) bedarfe.get(aktuellerStopp);
			
			//Solange Bedarf vorhanden ist, der durch das aktuelle Fahrzeug durch Med60 gedeckt werden kann 
			//soll dies getan werden
			//Geprüft ob Bedarf vorhanden ist, nachdem das Fahrzeug beim Standort ankommt UND bevor das Medikament abgelaufen ist
				
				LinkedList<Time> frühBedarf = new LinkedList<Time>();
				boolean bedarfVorhanden = true;
				do {
					frühBedarf.addFirst(aktuellerBedarf.removeFirst());
					try {
						if (aktuellerBedarf.getFirst()
								.isEarlierThan(fahrzeug.getStartzeitBeladung60().addTime(endNutzMed60)))
							bedarfVorhanden=false;
					} catch (Exception e) {
						bedarfVorhanden = false;
					}
					
				}
				while (bedarfVorhanden);
				
				deckbarerBedarfVorhanden=true;
				do {
					try {
					if (frühBedarf.getFirst().isLaterThan(zeitpunktAktuell.getNewInstance().reduceTime(1))) {	
					fahrzeug.set60(fahrzeug.get60()+1);
					frühBedarf.removeFirst();} 
					else deckbarerBedarfVorhanden = false;}
					catch (Exception e) {deckbarerBedarfVorhanden=false;}
				} 
				while (deckbarerBedarfVorhanden);
				aktuellerBedarf.addAll(frühBedarf);
				aktuellerBedarf.sort(null);
				
				if (i==strecke.split(",").length-1) {
					endzeit = zeitpunktAktuell.getNewInstance().addTime(Routenplaner.getFahrtzeit(aktuellerStopp, "A"));
				}
				
				System.out.println("Das Fahrzeug hat nach Stopp "+aktuellerStopp+" insgesamt "+fahrzeug.get60()+" Einheiten von Med60 abgeliefert.");
			}
	    
	    LinkedList<Time> bedarfAnErstemStopp = new LinkedList<Time>();
	    bedarfAnErstemStopp = bedarfe.get(strecke.split(",")[0]);
	    Time start120 = (bedarfAnErstemStopp.getFirst().getNewInstance().reduceTime(startNutzMed120));
		if (start120.isEarlierThan(fahrzeug.getStartzeitFahrt())) {
		fahrzeug.setStartzeitBeladung120(start120);}
		else fahrzeug.setStartzeitBeladung120(fahrzeug.getStartzeitFahrt());	    
	    aktuellerStopp = "A";
	    zeitpunktAktuell = ersterBedarf.getNewInstance().reduceTime(Routenplaner.getFahrtzeit("A", strecke.split(",")[0]));
	    for (int i=0; i<strecke.split(",").length;i++) {
			//Fahrt zu erstem Stopp
				int naechsteFahrtzeit = Routenplaner.getFahrtzeit(aktuellerStopp, routenplaner.getRoute().split(",")[i]);
				aktuellerStopp=fahrzeug.getStrecke().split(",")[i];
				zeitpunktAktuell = zeitpunktAktuell.addTime(naechsteFahrtzeit);
				aktuellerBedarf = (LinkedList<Time>) bedarfe.get(aktuellerStopp);
			
			//Solange Bedarf vorhanden ist, der durch das aktuelle Fahrzeug durch Med120 gedeckt werden kann 
			//soll dies getan werden
			//Geprüft ob Bedarf vorhanden ist, nachdem das Fahrzeug beim Standort ankommt UND bevor das Medikament abgelaufen ist
				LinkedList<Time> frühBedarf = new LinkedList<Time>();
				boolean bedarfVorhanden = true;
				do {
					frühBedarf.addFirst(aktuellerBedarf.removeFirst());
					try {
						if (aktuellerBedarf.getFirst()
								.isEarlierThan(fahrzeug.getStartzeitBeladung120().addTime(endNutzMed120))) bedarfVorhanden = false;
					} catch (Exception e) {
						bedarfVorhanden = false;
					}
				}
				while (bedarfVorhanden);
				
				deckbarerBedarfVorhanden=true;
				do {
					try {
					if (frühBedarf.getFirst().isLaterThan(zeitpunktAktuell)) {	
					fahrzeug.set120(fahrzeug.get120()+1);
					frühBedarf.removeFirst();} 
					else deckbarerBedarfVorhanden = false;}
					catch (Exception e) {deckbarerBedarfVorhanden=false;}
				} 
				while (deckbarerBedarfVorhanden);
				aktuellerBedarf.addAll(frühBedarf);
				aktuellerBedarf.sort(null);
				
				System.out.println("Das Fahrzeug hat nach Stopp "+aktuellerStopp+" insgesamt "+fahrzeug.get120()+" Einheiten von Med120 abgeliefert.");
			}
	    bedarfAnErstemStopp = new LinkedList<Time>();
	    bedarfAnErstemStopp = bedarfe.get(strecke.split(",")[0]);
	    Time start250 = (bedarfAnErstemStopp.getFirst().getNewInstance().reduceTime(startNutzMed250));
		if (start250.isEarlierThan(fahrzeug.getStartzeitFahrt())) {
		fahrzeug.setStartzeitBeladung250(start250);}
		else fahrzeug.setStartzeitBeladung250(fahrzeug.getStartzeitFahrt());
	    aktuellerStopp = "A";
	    zeitpunktAktuell = ersterBedarf.getNewInstance().reduceTime(Routenplaner.getFahrtzeit("A", strecke.split(",")[0]));
	    for (int i=0; i<strecke.split(",").length;i++) {
			//Fahrt zu erstem Stopp
				int naechsteFahrtzeit = Routenplaner.getFahrtzeit(aktuellerStopp, routenplaner.getRoute().split(",")[i]);
				aktuellerStopp=fahrzeug.getStrecke().split(",")[i];
				zeitpunktAktuell = zeitpunktAktuell.addTime(naechsteFahrtzeit);
				aktuellerBedarf = (LinkedList<Time>) bedarfe.get(aktuellerStopp);
			
			//Solange Bedarf vorhanden ist, der durch das aktuelle Fahrzeug durch Med250 gedeckt werden kann 
			//soll dies getan werden
			//Geprüft ob Bedarf vorhanden ist, nachdem das Fahrzeug beim Standort ankommt UND bevor das Medikament abgelaufen ist
				LinkedList<Time> frühBedarf = new LinkedList<Time>();
				boolean bedarfVorhanden = true;
				do {
					frühBedarf.addFirst(aktuellerBedarf.removeFirst());
					try {
					if (!aktuellerBedarf.getFirst()
							.isEarlierThan(fahrzeug.getStartzeitBeladung250().addTime(endNutzMed250))) bedarfVorhanden = false;}
					catch (Exception e) {
						bedarfVorhanden = false;
					}
				}
				while (bedarfVorhanden);
				
				deckbarerBedarfVorhanden=true;
				do {
					try {
					if (frühBedarf.getFirst().isLaterThan(zeitpunktAktuell)) {	
					fahrzeug.set250(fahrzeug.get250()+1);
					frühBedarf.removeFirst();} 
					else deckbarerBedarfVorhanden = false;}
					catch (Exception e) {deckbarerBedarfVorhanden=false;}
				} 
				while (deckbarerBedarfVorhanden);
				aktuellerBedarf.addAll(frühBedarf);
				aktuellerBedarf.sort(null);
				
				System.out.println("Das Fahrzeug hat nach Stopp "+aktuellerStopp+" insgesamt "+fahrzeug.get250()+" Einheiten von Med250 abgeliefert.");
			}
	    
	    bedarfAnErstemStopp = new LinkedList<Time>();
	    bedarfAnErstemStopp = bedarfe.get(strecke.split(",")[0]);
	    try {
	    Time start500 = (bedarfAnErstemStopp.getFirst().getNewInstance().reduceTime(startNutzMed500));
		if (start500.isEarlierThan(fahrzeug.getStartzeitFahrt())) {
		fahrzeug.setStartzeitBeladung500(start500);}
		else fahrzeug.setStartzeitBeladung500(fahrzeug.getStartzeitFahrt());
	    aktuellerStopp = "A";
	    zeitpunktAktuell = ersterBedarf.getNewInstance().reduceTime(Routenplaner.getFahrtzeit("A", strecke.split(",")[0]));
	    fahrzeug.setStartzeitBeladung500(zeitpunktAktuell.getNewInstance().reduceTime(startNutzMed500));
	    for (int i=0; i<strecke.split(",").length;i++) {
			//Fahrt zu erstem Stopp
				int naechsteFahrtzeit = Routenplaner.getFahrtzeit(aktuellerStopp, routenplaner.getRoute().split(",")[i]);
				aktuellerStopp=fahrzeug.getStrecke().split(",")[i];
				zeitpunktAktuell = zeitpunktAktuell.addTime(naechsteFahrtzeit);
				aktuellerBedarf = (LinkedList<Time>) bedarfe.get(aktuellerStopp);
			
			//Solange Bedarf vorhanden ist, der durch das aktuelle Fahrzeug durch Med500 gedeckt werden kann 
			//soll dies getan werden
			//Geprüft ob Bedarf vorhanden ist, nachdem das Fahrzeug beim Standort ankommt UND bevor das Medikament abgelaufen ist
				LinkedList<Time> frühBedarf = new LinkedList<Time>();
				deckbarerBedarfVorhanden = true;
				do {
					frühBedarf.addFirst(aktuellerBedarf.removeFirst());
					try {if (!aktuellerBedarf.getFirst()
					.isEarlierThan(fahrzeug.getStartzeitBeladung500().addTime(endNutzMed500))) {
						deckbarerBedarfVorhanden=false;
					}} catch (Exception e) {deckbarerBedarfVorhanden=false;}
				}
				while (deckbarerBedarfVorhanden);
				
				deckbarerBedarfVorhanden=true;
				do {
					try {
					if (frühBedarf.getFirst().isLaterThan(zeitpunktAktuell)) {	
					fahrzeug.set500(fahrzeug.get500()+1);
					frühBedarf.removeFirst();} 
					else deckbarerBedarfVorhanden = false;}
					catch (Exception e) {deckbarerBedarfVorhanden=false;}
				} 
				while (deckbarerBedarfVorhanden);
				aktuellerBedarf.addAll(frühBedarf);
				aktuellerBedarf.sort(null);
				
				System.out.println("Das Fahrzeug hat nach Stopp "+aktuellerStopp+" insgesamt "+fahrzeug.get500()+" Einheiten von Med500 abgeliefert.");
			}}
	    catch (Exception e) {
	    	System.out.println("Fahrzeug hat kein Med500 abgeliefert.");
	    }

	    
		System.out.println("Bedarf A benötigt "+bedarfA.size()+" weitere Medikamente");
		System.out.println("Bedarf B benötigt "+bedarfB.size()+" weitere Medikamente");
		System.out.println("Bedarf C benötigt "+bedarfC.size()+" weitere Medikamente");
		System.out.println("Bedarf D benötigt "+bedarfD.size()+" weitere Medikamente");
		System.out.println("Bedarf E benötigt "+bedarfE.size()+" weitere Medikamente");
		System.out.println("Bedarf F benötigt "+bedarfF.size()+" weitere Medikamente");
		System.out.println("Bedarf G benötigt "+bedarfG.size()+" weitere Medikamente");
		System.out.println("Bedarf H benötigt "+bedarfH.size()+" weitere Medikamente");
		
		System.out.println("Das Fahrzeug kehrt um "+endzeit+" in das Depot zurück." );
		return fahrzeug;
	}
	
	private double berechneVerblHaltbarkeit30(double Radioaktivität) {
		return (0.85*Radioaktivität);
	}
	
	
	public LinkedList<Fahrzeug> getFahrzeuge(){
		return fahrzeuge;
	}

}
