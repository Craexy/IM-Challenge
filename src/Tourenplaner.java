import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

public class Tourenplaner {
	
	private Routenplaner routenplaner;
	private LinkedList<Fahrzeug> fahrzeuge;
	
	private String route;
	
	//Kosten
	private int genutzteFahrzeuge = 0;
	private int streckenkostenFahrt = 0;
	private int zeitkostenFahrt = 0;
	private int kostenProMeile = 5;
	private int kostenProStundeFahrt = 10;
	private int strafkostensatz;
	
	private int gesamtBedarf = 0;
	
	//Bedarfe nach Standorten
	private Map<String, LinkedList<Time>> bedarfe = new HashMap<String, LinkedList<Time>>();
	private LinkedList<Time> bedarfA = new LinkedList<Time>();
	private LinkedList<Time> bedarfB = new LinkedList<Time>();
	private LinkedList<Time> bedarfC = new LinkedList<Time>();
	private LinkedList<Time> bedarfD = new LinkedList<Time>();
	private LinkedList<Time> bedarfE = new LinkedList<Time>();
	private LinkedList<Time> bedarfF = new LinkedList<Time>();
	private LinkedList<Time> bedarfG = new LinkedList<Time>();
	private LinkedList<Time> bedarfH = new LinkedList<Time>();
	
	//Entladezeiten der Standorte
	private Map<String, Integer> entladezeiten = new HashMap<String, Integer>();
	private int entladezeitA = 15;
	private int entladezeitB = 30;
	private int entladezeitC = 30;
	private int entladezeitD = 15;
	private int entladezeitE = 30;
	private int entladezeitF = 15;
	private int entladezeitG = 30;
	private int entladezeitH = 15;
	
	//startNutzMed60: Minuten nach Produktion, ab denen Medikament60 benutzt werden kann 
	//endNutzMed60: Minuten nach Produktion, bis zu denen Medikament60 benutzt werden kann 
	//minus 30, da die Medikamente 30 Minuten vor Benutzung vorliegen müssen
	//--> es stehen also 30 Minuten weniger zur Entladung zur Verfügung
	private int startNutzMed60 = 270;
	private int endNutzMed60 = 450-30;
	
	private int startNutzMed120 = 390;
	private int endNutzMed120 = 600-30;

	private int startNutzMed250 = 540;
	private int endNutzMed250 = 720-30;

	private int startNutzMed500 = 660;
	private int endNutzMed500 = 840-30;
	
	public Tourenplaner(int strafkostensatz, int variante) {
		routenplaner = new Routenplaner();
		route = routenplaner.getRoute();
		this.strafkostensatz = strafkostensatz;
		fahrzeuge = new LinkedList<Fahrzeug>();
		
		//Die Listen der Bedarfe der einzelnen Standorte werden in einer Map gespeichert um dynamischen 
		//Zugriff zu ermöglichen
		bedarfe.put("A", bedarfA);
		bedarfe.put("B", bedarfB);
		bedarfe.put("C", bedarfC);
		bedarfe.put("D", bedarfD);
		bedarfe.put("E", bedarfE);
		bedarfe.put("F", bedarfF);
		bedarfe.put("G", bedarfG);
		bedarfe.put("H", bedarfH);
		
		//gleiches Verfahren wie bei Bedarfe (s.o.)
		entladezeiten.put("A", entladezeitA);
		entladezeiten.put("B", entladezeitB);
		entladezeiten.put("C", entladezeitC);
		entladezeiten.put("D", entladezeitD);
		entladezeiten.put("E", entladezeitE);
		entladezeiten.put("F", entladezeitF);
		entladezeiten.put("G", entladezeitG);
		entladezeiten.put("H", entladezeitH);
		
		//Daten mit Uhrzeiten der Bedarfe werden eingelesen
		this.befülleDaten();
	   
		//Variante 1
		if (variante==1) {
			System.out.println("+++   Variante 1   +++");
			this.variante1();
			System.out.println("Gesamtkosten betragen: "+getGesamtkosten());}

		//Variante 2
		if (variante==2) {
			System.out.println("+++   Variante 2   +++");
			this.befülleDaten();
			this.variante2();}

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
	
	
	private void variante1(){
		//erstes Fahrzeug fährt optimierte Route
		Fahrzeug fahrzeug1  = this.neuesFahrzeugSchicken(route);
		fahrzeuge.add(fahrzeug1);
		
		//zweites Fahrzeug fährt umgekehrte Route
		String strecke2 = "";
		for (int i=0; i<7; i++) {
			strecke2 = strecke2+routenplaner.getRoute().split(",")[7-i]+",";
			}
		Fahrzeug fahrzeug2 = this.neuesFahrzeugSchicken(strecke2);
		fahrzeuge.add(fahrzeug2);
	}
	
	@SuppressWarnings("unused")
	private void variante2() {
		//erstes Auto fährt erste Hälfte der Strecke
		String strecke1 = "";
		for(int i=0;i<4;i++) {
			if(i<3) {
					strecke1 = strecke1 +routenplaner.getRoute().split(",")[i]+ ",";}
					else strecke1 = strecke1 +routenplaner.getRoute().split(",")[i];
			}
		System.out.println(strecke1);
		fahrzeuge.add(this.neuesFahrzeugSchicken(strecke1));
		
		//zweites Auto fährt zweite Hälfte der Strecke
		String strecke2 = "";
		for(int i=4;i<8;i++) {
			if(i<7) {
					strecke2 = strecke2 +routenplaner.getRoute().split(",")[i]+ ",";} 
					else strecke2 = strecke2 +routenplaner.getRoute().split(",")[i];
			}
		System.out.println(strecke2);
		Fahrzeug fahrzeug2 = this.neuesFahrzeugSchicken(strecke2);
		fahrzeuge.add(fahrzeug2);
	}
	
	//Array von Überschüssen wird einzeln verteilt
	public void verteileReste(MedUeberschuss[] überschüsse) {
		for (int i=0;i<überschüsse.length;i++) {
			this.verteileReste(überschüsse[i]);
		}
	}
	
	//Mit Medikamenten, die bei Produktion überproduziert wurden,
	//wird versucht noch offene Bedarfe mit neuen (oder sogar alten) Fahrzeugen zu decken
	public void verteileReste(MedUeberschuss überschuss) {
		Time startNutzung = null;
		Time endNutzung = null;
		LinkedList<Time> aktuellerBedarf;
		
		//Map mit allen möglichen, deckbaren Bedarfen
		Map<String, Integer> deckbareBedarfe = new HashMap<String,Integer>();
		
		//Je nach Typ des Medikaments das überproduziert wurde, sind andere Nutzungszeiten zu beachten
		if (überschuss.getMedTyp()==60) {
			startNutzung = überschuss.getStartProduktion().getNewInstance().addTime(startNutzMed60);
			endNutzung = startNutzung.getNewInstance().addTime(endNutzMed60);
		}
		
		if (überschuss.getMedTyp()==120) {
			startNutzung = überschuss.getStartProduktion().addTime(startNutzMed120);
			endNutzung = startNutzung.getNewInstance().addTime(endNutzMed120);
		}
		
		if (überschuss.getMedTyp()==250) {
			startNutzung = überschuss.getStartProduktion().addTime(startNutzMed250);
			endNutzung = startNutzung.getNewInstance().addTime(endNutzMed250);	
		}
		
		if (überschuss.getMedTyp()==500) {
			startNutzung = überschuss.getStartProduktion().addTime(startNutzMed60);
			endNutzung = startNutzung.getNewInstance().addTime(endNutzMed500);
		}
		
		//Bedarfe, die augfgrund der Nutzungszeit von Überschuss gedeckt werden könnten, werden ermittelt
		for (Entry<String, LinkedList<Time>> e : bedarfe.entrySet()){
			aktuellerBedarf = (LinkedList<Time>)e.getValue();
			//Deckbarer Bedarf in aktuell betrachtetem Standort
			int lokalDeckbareBedarfe = 0;
			for (int j=0;j<aktuellerBedarf.size();j++) {
				if (aktuellerBedarf.get(j).isLaterThan(startNutzung.getNewInstance().reduceTime(1))
					&&aktuellerBedarf.get(j).isEarlierThan(endNutzung.getNewInstance().reduceTime(1))) {
					lokalDeckbareBedarfe = lokalDeckbareBedarfe + 1;	
				}
			deckbareBedarfe.put(e.getKey(), lokalDeckbareBedarfe);
			}
			
		}
		
		//Standort, an dem die meisten Bedarfe durch Überschuss gedeckt werden können wird ermittelt
		//Dieser wird zuerst angefahren
		int lokalDeckbareBedarfe=0;
		String anzufahrenderOrt ="";
		for (Entry<String, Integer> e : deckbareBedarfe.entrySet()){
			if (e.getValue()>lokalDeckbareBedarfe) { anzufahrenderOrt = e.getKey();
			lokalDeckbareBedarfe = e.getValue();}
			System.out.println(e.getValue()+" Bedarfe in Standort "+e.getKey()+" abdeckbar.");
		}
		
		//Wenn an keinem Standort Bedarfe gedeckt werden können wird kein neues Fahrzeug losgeschickt
		if (anzufahrenderOrt.equals("")) {
			System.out.println("Rest nicht verteilbar.");
			return;
		}
		System.out.println("Ort "+anzufahrenderOrt+" wird zuerst angefahren.");
		//Nach dem Versuch übrig bleibende Bedarfe zu decken wird der restliche Überschuss wieder zurückgegeben
		Object[] temp = this.resteFahrzeugSchicken(anzufahrenderOrt, überschuss);
		fahrzeuge.add((Fahrzeug)temp[0]);
		
		//prüfen ob einzelnes Decken verbleibender Bedarfe billiger als Strafkosten ist
		
		//Zeiten für Bedarf "i" ausgeben und zwischenspeichern
		//Fahrzeug für Standort "i" losschicken mit vorhandenem Überschuss
		//Kosten des neuen Fahrzeugs berechenen und mit Strafkosten vergleichen
		//Wenn Strafkosten geringer: Bedarf wiederherstellen und Fahrzeug löschen
		//Wenn Strafkosten höher: Fahrzeugen zu Fahrzeugen hinzufügen
		
		String[] buchstaben = new String[8];
		buchstaben = "A,B,C,D,E,F,G,H".split(",");
		
		LinkedList<Time> betrachteterBedarf = new LinkedList<Time>();
		Object[] optional;
		int fahrzeugKosten;
		int altStrafkosten;
		for (int i=0;i<8;i++) {
		betrachteterBedarf.addAll(bedarfe.get(buchstaben[i]));
		optional = neuesFahrzeugSchicken(buchstaben[i], ((MedUeberschuss)temp[1]).getNewInstance());
		//Kosten wenn Fahrzeug genutzt wird
		fahrzeugKosten = 1000 + Routenplaner.getFahrstrecke("A", buchstaben[i])*5 + Routenplaner.getFahrtzeit("A", buchstaben[i])*10;
		//Kosten wenn Fahrzeug nicht geschickt wird und stattdessen Strafkosten in Kauf genommen werden
		try {
			altStrafkosten = (((Fahrzeug) optional[0]).get60()+ ((Fahrzeug) optional[0]).get120()+ ((Fahrzeug) optional[0]).get250()+ ((Fahrzeug) optional[0]).get500())*strafkostensatz;	
		if (fahrzeugKosten<altStrafkosten) {
			temp = optional;
			fahrzeuge.add((Fahrzeug)optional[0]);
			}
		else {
			bedarfe.get(buchstaben[i]).addAll(betrachteterBedarf);
			} 
		}
		catch (NullPointerException e) {
			System.out.println("Bedarf "+buchstaben[i]+" bereits gedeckt.");
			}
		}
		
	}

	private Fahrzeug neuesFahrzeugSchicken(String strecke) {
		String tempStrecke = strecke;
		strecke="";
		for (int i=0;i<tempStrecke.split(",").length;i++) {
			if (!bedarfe.get(tempStrecke.split(",")[i]).isEmpty()) {
				strecke = strecke+tempStrecke.split(",")[i]+",";
			}
		}
		//Zeit zu der das Fahrzeug wieder zurück im Depot ist --> wird später befüllt
		Time endzeit = new Time(0,0,0);
		
		//Streckenkosten dieser Fahrt berechnen
		//Kosten für Fahrt von Anfang bis Ende
		for (int i = 0;i<strecke.split(",").length-1;i++) {
		streckenkostenFahrt = streckenkostenFahrt + 
				Routenplaner.getFahrstrecke(strecke.split(",")[i],strecke.split(",")[i+1])*kostenProMeile;
		}
		//Kosten für Fahrt von letztem Stopp zu Depot
		streckenkostenFahrt = streckenkostenFahrt + 
				Routenplaner.getFahrstrecke(strecke.split(",")[strecke.split(",").length-1],strecke.split(",")[0])*kostenProMeile;
		Fahrzeug fahrzeug = null;
		String aktuellerStopp = "A";
		Time ersterBedarf = bedarfe.get(strecke.split(",")[0]).getFirst().getNewInstance();
		int fahrtzeit = Routenplaner.getFahrtzeit("A", strecke.split(",")[0]);
	    Time zeitpunktAktuell = ersterBedarf.getNewInstance().reduceTime(fahrtzeit);
	    
	    //Wenn zurückgekehrtes Fahrzeug im Depot steht wird dieses genutzt
	    if (!fahrzeuge.isEmpty()) {
	    	for (Fahrzeug e:fahrzeuge) {
	    		if (e.getRückkehrZeit()!=null) {
	    			if (zeitpunktAktuell.isLaterThan(e.getRückkehrZeit())) {
	    				fahrzeug = new Fahrzeug();
	    				System.out.println("Altes Fahrzeug startet");
	    			    fahrzeug.setStrecke(strecke);
	    			    fahrzeug.setStartzeitFahrt(zeitpunktAktuell.getNewInstance());
	    			}
	    		}
	    	}
	    //wenn kein zurückgekehrtes Fahrzeug im Depot steht wir ein neues losgeschickt
	    } else {
	    fahrzeug = new Fahrzeug();
	    genutzteFahrzeuge = genutzteFahrzeuge +1;
		System.out.println("Neues Fahrzeug startet");
	    fahrzeug.setStrecke(strecke);
	    fahrzeug.setStartzeitFahrt(zeitpunktAktuell.getNewInstance()); }
	    if (fahrzeug==null) {
	    	fahrzeug = new Fahrzeug();
		    genutzteFahrzeuge = genutzteFahrzeuge +1;
			System.out.println("Neues Fahrzeug startet");
		    fahrzeug.setStrecke(strecke);
		    fahrzeug.setStartzeitFahrt(zeitpunktAktuell.getNewInstance()); 
	    }

	    LinkedList<Time> aktuellerBedarf = new LinkedList<Time>();
	    boolean deckbarerBedarfVorhanden;

	    fahrzeug.setStartzeitBeladung60(zeitpunktAktuell.getNewInstance().reduceTime(startNutzMed60));	    
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
						if (!aktuellerBedarf.getFirst()
								.isEarlierThan(fahrzeug.getStartzeitBeladung60().getNewInstance().addTime(endNutzMed60))) bedarfVorhanden = false;
					} catch (Exception e) {
						bedarfVorhanden = false;
					}
				}
				while (bedarfVorhanden);

				
				deckbarerBedarfVorhanden=true;
				do {
					try {
					if (frühBedarf.getFirst().isLaterThan(zeitpunktAktuell.getNewInstance().reduceTime(1))) {	
						/*if (!checkBadgeGroeße(fahrzeug.getStartzeitBeladung60().getNewInstance(), 60, frühBedarf.getFirst())) {
							System.out.println("Badgegröße berücksichtigen!");
							throw new IllegalArgumentException();
						}*/
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

	    Time start120 = null;
	    for (int i = 0; i<8; i++) {
	    try {
	    	bedarfAnErstemStopp = bedarfe.get(fahrzeug.getStrecke().split(",")[i]);
	    	start120 = (bedarfAnErstemStopp.getFirst().getNewInstance().reduceTime(startNutzMed120));
	    	if (start120!=null) break;
	    	}
	    catch (Exception e) {
	    	}
	    }
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
					try {
						frühBedarf.addFirst(aktuellerBedarf.removeFirst());
						if (!aktuellerBedarf.getFirst()
								.isEarlierThan(fahrzeug.getStartzeitBeladung120().getNewInstance().addTime(endNutzMed120))) bedarfVorhanden = false;
					} catch (Exception e) {
						bedarfVorhanden = false;
					}
				}
				while (bedarfVorhanden);
				
				deckbarerBedarfVorhanden=true;
				do {
					try {
					if (frühBedarf.getFirst().isLaterThan(zeitpunktAktuell)) {	
						/*if (!checkBadgeGroeße(fahrzeug.getStartzeitBeladung120().getNewInstance(),120, frühBedarf.getFirst())) {
							System.out.println("Badgegröße berücksichtigen!");
							throw new IllegalArgumentException();
						}*/
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
	    
	    Time start250 = null;
	    for (int i = 0; i<8; i++) {
	    try {
	    	bedarfAnErstemStopp = bedarfe.get(fahrzeug.getStrecke().split(",")[i]);
	    	start250 = (bedarfAnErstemStopp.getFirst().getNewInstance().reduceTime(startNutzMed250));
	    	if (start250!=null) break;
	    	}
	    catch (NoSuchElementException e) {
	    	}
	    }
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
					try {
					frühBedarf.addFirst(aktuellerBedarf.removeFirst());
					if (!aktuellerBedarf.getFirst()
							.isEarlierThan(fahrzeug.getStartzeitBeladung250().getNewInstance().addTime(endNutzMed250))) bedarfVorhanden = false;}
					catch (Exception e) {
						bedarfVorhanden = false;
					}
				}
				while (bedarfVorhanden);
				
				deckbarerBedarfVorhanden=true;
				do {
					try {
					if (frühBedarf.getFirst().isLaterThan(zeitpunktAktuell)) {	
						/*if (!checkBadgeGroeße(fahrzeug.getStartzeitBeladung250().getNewInstance(), 250, frühBedarf.getFirst())) {
							System.out.println("Badgegröße berücksichtigen!");
							throw new IllegalArgumentException();
						}*/
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
					.isEarlierThan(fahrzeug.getStartzeitBeladung500().getNewInstance().addTime(endNutzMed500))) {
						deckbarerBedarfVorhanden=false;
					}} catch (Exception e) {deckbarerBedarfVorhanden=false;}
				}
				while (deckbarerBedarfVorhanden);
				
				deckbarerBedarfVorhanden=true;
				do {
					try {
					if (frühBedarf.getFirst().isLaterThan(zeitpunktAktuell)) {	
						/*if (!checkBadgeGroeße(fahrzeug.getStartzeitBeladung500().getNewInstance(), 500, frühBedarf.getFirst())) {
							System.out.println("Badgegröße berücksichtigen!");
							throw new IllegalArgumentException();
						}*/
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

	    
		System.out.println("Standort A benötigt "+bedarfA.size()+" weitere Medikamente");
		System.out.println("Standort B benötigt "+bedarfB.size()+" weitere Medikamente");
		System.out.println("Standort C benötigt "+bedarfC.size()+" weitere Medikamente");
		System.out.println("Standort D benötigt "+bedarfD.size()+" weitere Medikamente");
		System.out.println("Standort E benötigt "+bedarfE.size()+" weitere Medikamente");
		System.out.println("Standort F benötigt "+bedarfF.size()+" weitere Medikamente");
		System.out.println("Standort G benötigt "+bedarfG.size()+" weitere Medikamente");
		System.out.println("Standort H benötigt "+bedarfH.size()+" weitere Medikamente");
		int gesamtBedarf = bedarfA.size()+bedarfB.size()+bedarfC.size()+bedarfD.size()+bedarfE.size()+bedarfF.size()+bedarfG.size()+bedarfH.size();
		System.out.println("Insgesamt werden noch "+gesamtBedarf+" Einheiten benötigt.");
		
		System.out.println("Das Fahrzeug kehrt um "+endzeit+" in das Depot zurück." );
		fahrzeug.setRückkehrZeit(endzeit.getNewInstance());
		zeitkostenFahrt = zeitkostenFahrt + Time.getDifferenceInMinutes
			(endzeit,fahrzeug.getStartzeitFahrt())*kostenProStundeFahrt;
		
		return fahrzeug;
	}

	private Object[] neuesFahrzeugSchicken(String strecke, MedUeberschuss überschuss){
		String tempStrecke = strecke;
		strecke="";
		for (int i=0;i<tempStrecke.split(",").length;i++) {
			if (!bedarfe.get(tempStrecke.split(",")[i]).isEmpty()) {
				strecke = strecke+tempStrecke.split(",")[i]+",";
			}
		}
		if (strecke.equals("")) {
			Object[] temp = new Object[2];
			temp[0]=null;
			temp[1]=überschuss;
			return temp;
		}
		
		
		
		//Zeit zu der das Fahrzeug wieder zurück im Depot ist
		Time endzeit = new Time(0,0,0);
		Fahrzeug fahrzeug = null;
		String aktuellerStopp = "A";
		Time ersterBedarf = bedarfe.get(strecke.split(",")[0]).getFirst().getNewInstance();
		int fahrtzeit = Routenplaner.getFahrtzeit("A", strecke.split(",")[0]);
		//Fahrzeug soll so losfahren, dass es zum Zeitpunkt des ersten Bedarfs ankommt
	    Time zeitpunktAktuell = ersterBedarf.getNewInstance().reduceTime(fahrtzeit);
	    //Vor erstem Bedarf muss Medikament noch entladen werden --> Fahrzeug muss um diese Zeit früher ankommen
	    zeitpunktAktuell.reduceTime(entladezeiten.get(strecke.split(",")[0]));
	    if (!fahrzeuge.isEmpty()) {
	    	for (Fahrzeug e:fahrzeuge) {
	    		if (e.getRückkehrZeit()!=null) {
	    			if (zeitpunktAktuell.isLaterThan(e.getRückkehrZeit())) {
	    				fahrzeug = new Fahrzeug();
	    				System.out.println("Altes Fahrzeug startet");
	    			    fahrzeug.setStrecke(strecke);
	    			    fahrzeug.setStartzeitFahrt(zeitpunktAktuell.getNewInstance());
	    			}
	    		}
	    	}
	    } else {
	    fahrzeug = new Fahrzeug();
	    genutzteFahrzeuge = genutzteFahrzeuge +1;
		System.out.println("Neues Fahrzeug startet");
	    fahrzeug.setStrecke(strecke);
	    fahrzeug.setStartzeitFahrt(zeitpunktAktuell.getNewInstance()); }
	    if (fahrzeug==null) {
	    	fahrzeug = new Fahrzeug();
		    genutzteFahrzeuge = genutzteFahrzeuge +1;
			System.out.println("Neues Fahrzeug startet");
		    fahrzeug.setStrecke(strecke);
		    fahrzeug.setStartzeitFahrt(zeitpunktAktuell.getNewInstance()); 
	    }
		
		//Streckenkosten dieser Fahrt berechnen
		//Kosten für Fahrt von Anfang bis Ende
		for (int i = 0;i<strecke.split(",").length-1;i++) {
		streckenkostenFahrt = streckenkostenFahrt + 
				Routenplaner.getFahrstrecke(strecke.split(",")[i],strecke.split(",")[i+1])*kostenProMeile;
		}
		//Kosten für Fahrt von letztem Stopp zu Depot
		streckenkostenFahrt = streckenkostenFahrt + 
				Routenplaner.getFahrstrecke(strecke.split(",")[strecke.split(",").length-1],strecke.split(",")[0])*kostenProMeile;

	    	LinkedList<Time> bedarf = bedarfe.get(strecke.split(",")[0]);
	    	for (int i=0; i<bedarf.size();i++) {
	    		if (bedarf.get(i).isEarlierThan(überschuss.getEndNutzungszeit())&&
	    				bedarf.get(i).isLaterThan(überschuss.getStartNutzungszeit())){
	    			fahrzeug.setStartzeitFahrt(bedarf.get(i).getNewInstance().reduceTime(entladezeiten.get(fahrzeug.getStrecke().split(",")[0])));
	    			break;
	    		}
	    	}
	    LinkedList<Time> aktuellerBedarf = new LinkedList<Time>();
	    boolean deckbarerBedarfVorhanden;
	    
	    //Nur Med60 abladen wenn auch Überschuss dazu vorliegt
	    if(überschuss.getMedTyp()==60) {
	    fahrzeug.setStartzeitBeladung60(zeitpunktAktuell.getNewInstance().reduceTime(startNutzMed60));	    
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
						if (!aktuellerBedarf.getFirst()
								.isEarlierThan(fahrzeug.getStartzeitBeladung60().getNewInstance().addTime(endNutzMed60))) bedarfVorhanden = false;
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
						überschuss.setAnzahlMeds(überschuss.getAnzahlMeds()-1);
						frühBedarf.removeFirst();} 
					else deckbarerBedarfVorhanden = false;}
					catch (Exception e) {deckbarerBedarfVorhanden=false;}
				} 
				while (deckbarerBedarfVorhanden&&überschuss.getAnzahlMeds()>0);
				aktuellerBedarf.addAll(frühBedarf);
				aktuellerBedarf.sort(null);
				if (i==strecke.split(",").length-1) {
					endzeit = zeitpunktAktuell.getNewInstance().addTime(Routenplaner.getFahrtzeit(aktuellerStopp, "A"));
				}
				
				System.out.println("Das Fahrzeug hat nach Stopp "+aktuellerStopp+" insgesamt "+fahrzeug.get60()+" Einheiten von Med60 abgeliefert.");
			}
	    }
	    
	    LinkedList<Time> bedarfAnErstemStopp = new LinkedList<Time>();
	    
	  //Nur Med120 abladen wenn auch Überschuss dazu vorliegt
	    if(überschuss.getMedTyp()==120){
	    Time start120 = null;
	    for (int i = 0; i<8; i++) {
	    try {
	    	bedarfAnErstemStopp = bedarfe.get(fahrzeug.getStrecke().split(",")[i]);
	    	start120 = (bedarfAnErstemStopp.getFirst().getNewInstance().reduceTime(startNutzMed120));
	    	if (start120!=null) break;
	    	}
	    catch (Exception e) {
	    	}
	    }
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
					try {
						frühBedarf.addFirst(aktuellerBedarf.removeFirst());
						if (!aktuellerBedarf.getFirst()
								.isEarlierThan(fahrzeug.getStartzeitBeladung120().getNewInstance().addTime(endNutzMed120))) bedarfVorhanden = false;
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
					überschuss.setAnzahlMeds(überschuss.getAnzahlMeds()-1);
					frühBedarf.removeFirst();} 
					else deckbarerBedarfVorhanden = false;}
					catch (Exception e) {deckbarerBedarfVorhanden=false;}
				} 
				while (deckbarerBedarfVorhanden&&überschuss.getAnzahlMeds()>0);
				aktuellerBedarf.addAll(frühBedarf);
				aktuellerBedarf.sort(null);
				
				System.out.println("Das Fahrzeug hat nach Stopp "+aktuellerStopp+" insgesamt "+fahrzeug.get120()+" Einheiten von Med120 abgeliefert.");
			}
	    }
	    
	    //Nur Med250 abladen wenn auch Überschuss dazu vorliegt
	    if(überschuss.getMedTyp()==250){
	    Time start250 = null;
	    for (int i = 0; i<8; i++) {
	    try {
	    	bedarfAnErstemStopp = bedarfe.get(fahrzeug.getStrecke().split(",")[i]);
	    	start250 = (bedarfAnErstemStopp.getFirst().getNewInstance().reduceTime(startNutzMed250));
	    	if (start250!=null) break;
	    	}
	    catch (NoSuchElementException e) {
	    	}
	    }
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
					try {
					frühBedarf.addFirst(aktuellerBedarf.removeFirst());
					if (!aktuellerBedarf.getFirst()
							.isEarlierThan(fahrzeug.getStartzeitBeladung250().getNewInstance().addTime(endNutzMed250))) bedarfVorhanden = false;}
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
					überschuss.setAnzahlMeds(überschuss.getAnzahlMeds()-1);
					frühBedarf.removeFirst();} 
					else deckbarerBedarfVorhanden = false;}
					catch (Exception e) {deckbarerBedarfVorhanden=false;}
				} 
				while (deckbarerBedarfVorhanden&&überschuss.getAnzahlMeds()>0);
				aktuellerBedarf.addAll(frühBedarf);
				aktuellerBedarf.sort(null);
				
				System.out.println("Das Fahrzeug hat nach Stopp "+aktuellerStopp+" insgesamt "+fahrzeug.get250()+" Einheiten von Med250 abgeliefert.");
			}
	    }
	    
	    
	    //Nur Med500 abladen wenn auch Überschuss dazu vorliegt
	    if(überschuss.getMedTyp()==500) {
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
					.isEarlierThan(fahrzeug.getStartzeitBeladung500().getNewInstance().addTime(endNutzMed500))) {
						deckbarerBedarfVorhanden=false;
					}} catch (Exception e) {deckbarerBedarfVorhanden=false;}
				}
				while (deckbarerBedarfVorhanden);
				
				deckbarerBedarfVorhanden=true;
				do {
					try {
					if (frühBedarf.getFirst().isLaterThan(zeitpunktAktuell)) {	
					fahrzeug.set500(fahrzeug.get500()+1);
					überschuss.setAnzahlMeds(überschuss.getAnzahlMeds()-1);
					frühBedarf.removeFirst();} 
					else deckbarerBedarfVorhanden = false;}
					catch (Exception e) {deckbarerBedarfVorhanden=false;}
				} 
				while (deckbarerBedarfVorhanden&&überschuss.getAnzahlMeds()>0);
				aktuellerBedarf.addAll(frühBedarf);
				aktuellerBedarf.sort(null);
				
				System.out.println("Das Fahrzeug hat nach Stopp "+aktuellerStopp+" insgesamt "+fahrzeug.get500()+" Einheiten von Med500 abgeliefert.");
			}}
	    catch (Exception e) {
	    	System.out.println("Fahrzeug hat kein Med500 abgeliefert.");
	    }
	    }

	    
		System.out.println("Bedarf A benötigt "+bedarfA.size()+" weitere Medikamente");
		System.out.println("Bedarf B benötigt "+bedarfB.size()+" weitere Medikamente");
		System.out.println("Bedarf C benötigt "+bedarfC.size()+" weitere Medikamente");
		System.out.println("Bedarf D benötigt "+bedarfD.size()+" weitere Medikamente");
		System.out.println("Bedarf E benötigt "+bedarfE.size()+" weitere Medikamente");
		System.out.println("Bedarf F benötigt "+bedarfF.size()+" weitere Medikamente");
		System.out.println("Bedarf G benötigt "+bedarfG.size()+" weitere Medikamente");
		System.out.println("Bedarf H benötigt "+bedarfH.size()+" weitere Medikamente");
		gesamtBedarf = bedarfA.size()+bedarfB.size()+bedarfC.size()+bedarfD.size()+bedarfE.size()+bedarfF.size()+bedarfG.size()+bedarfH.size();
		System.out.println("Insgesamt werden noch "+gesamtBedarf+" Einheiten benötigt.");
		
		System.out.println("Das Fahrzeug kehrt um "+endzeit+" in das Depot zurück." );
		fahrzeug.setRückkehrZeit(endzeit.getNewInstance());
		zeitkostenFahrt = zeitkostenFahrt + Time.getDifferenceInMinutes
			(endzeit,fahrzeug.getStartzeitFahrt())*kostenProStundeFahrt;
		Object[] ergebnis = new Object[2];
		ergebnis[0] = fahrzeug;
		ergebnis[1] = überschuss;
		return ergebnis;
	}
	
	public int getGesamtkosten() {
		return getFahrkosten() + getÜbrigeBedarfe() + fahrzeuge.size()*1000;
	}

	public int getStreckenkostenFahrt() {
		return streckenkostenFahrt;
	}

	public int getZeitkostenFahrt() {
		return zeitkostenFahrt;
	}

	public int getFahrkosten() {
		return streckenkostenFahrt + zeitkostenFahrt;
	}
	
	public int getÜbrigeBedarfe() { 
		return gesamtBedarf;
	}

	public String getStrecke() {
		return route;
	}

	//private Fahrzeug resteFahrzeugSchicken(String Stopp, MedUeberschuss überschuss) {
	private Object[] resteFahrzeugSchicken(String Stopp, MedUeberschuss überschuss) {	
	String[] list = routenplaner.getRoute().split(",");		
		int index = -1;
	    for (int i = 0; (i < list.length) && (index == -1); i++) {
	        if (list[i].equals(Stopp)) {
	            index = i;
	        }
	    }
	    
	    String strecke = "";
	    for (int i = index; i<list.length;i++) {
	    	strecke = strecke +list[i]+",";
	    }
	    System.out.println(strecke);
	    
		return this.neuesFahrzeugSchicken(strecke, überschuss);
	}
	
	
	
	//Falls zwei Badges notwendig sind um einen Medikamentenbedarf zu decken, wird geprüft
	//ob dies möglich ist, obwohl der zweite Badge (aufgrund der neuerlichen Produktionsdauer)
	//erst gewisse Zeit später zur Verfügung steht
	@SuppressWarnings("unused")
	private boolean checkBadgeGroeße(Time produktionsende, int medTyp, Time bedarf) {
		switch (medTyp){
			case 60:{
				if(produktionsende.getNewInstance().addTime(startNutzMed60+15).isEarlierThan(bedarf)){
					return true;
				}
			}
			case 120:{
				if(produktionsende.getNewInstance().addTime(startNutzMed120+30).isEarlierThan(bedarf)){
					return true;
				}
			}
			case 250:{
				if(produktionsende.getNewInstance().addTime(startNutzMed250+60).isEarlierThan(bedarf)){
					return true;
				}
			}
			case 500:{
				if(produktionsende.getNewInstance().addTime(startNutzMed500+120).isEarlierThan(bedarf)){
					return true;
				}
			}
		}
		return false;
	}
	
	public LinkedList<Fahrzeug> getFahrzeuge(){
		return fahrzeuge;
	}


}
