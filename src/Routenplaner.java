import org.graphstream.graph.implementations.*;
import java.util.LinkedList;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;


public class Routenplaner {
	

	private String route;
	private static Graph g1;
	private LinkedList<String> best = new LinkedList<String>();
	private int tourLaenge;
	

	public Routenplaner(){
		g1 =  new DefaultGraph("Fahrtzeiten");
		erzeugeDaten();
		planeRoute();
	}
	

	public String getRoute(){
		transformRouteToString();
		return route;
	}
	
	public void transformRouteToString(){
		route = "";
		for (int i = 0; i < best.size(); i++) {
			if(i==best.size()-1){
				route += best.get(i);
			}else{
				route += best.get(i)+",";
			}
      
        }
	}

	
	private void planeRoute() {
		simmulatedAnnealing();
		System.out.println("|| TSP mit Simulated Annealing ||\n");
		System.out.println("Optimale Route wurde wie folgt berechnet: "+this.best+ " - Die Tourl�nge betr�gt "+tourLaenge+" Meilen\n");
	}
	
	public static int getFahrtzeit(String Anfangspunkt, String Endpunkt) {
		if (Anfangspunkt.equals(Endpunkt)) return 0;
		try{
			return g1.getEdge(Anfangspunkt+Endpunkt).getAttribute("Fahrtzeit",Integer.class);
		}catch (NullPointerException e)
		{
			return g1.getEdge(Endpunkt+Anfangspunkt).getAttribute("Fahrtzeit",Integer.class);
		}
	}
	
	public static int getFahrstrecke(String Anfangspunkt, String Endpunkt) {
		if (Anfangspunkt.equals(Endpunkt)) return 0;
		try{
			return g1.getEdge(Anfangspunkt+Endpunkt).getAttribute("Fahrstrecke",Integer.class);
		}catch (NullPointerException e)
		{
			return g1.getEdge(Endpunkt+Anfangspunkt).getAttribute("Fahrstrecke",Integer.class);
		}
	}


	public static int getNumberOfNodes(){
		return g1.getNodeCount();
	}
	 
	public static String getCity(int cityIndex){
		return g1.getNode(cityIndex).getId();
		
	}
	
	
	public void simmulatedAnnealing(){
		//W�hle die Variante der Distanzfunktion: Zeit=1/Strecke=2
		int variante = 1;
		
		// Systemtemperatur
        double temp = 10000;

        // Abk�hlungsrate
        double coolingRate = 0.003;
        
     // Die erste Tour wird zuf�llig erstellt (Stadt "A" jedoch immer an erster Stelle)
        Tour currentSolution = new Tour();
        currentSolution.eineTourErstellen();
        
     // Zuf�llig erstellte Tour als bisher beste Tour festlegen
        Tour best = new Tour(currentSolution.getTour());
        
     // Solange ausf�hren bis Temperatur nicht mehr gr��er 1
        while (temp > 1) {
            // Erstelle eine neue Tour als Kopie der alten
            Tour newSolution = new Tour(currentSolution.getTour());

            // W�hle zwei Positionen der Tour
            int tourPos1 = (int) ((newSolution.tourSize()-1) * Math.random()+1);
            int tourPos2 = (int) ((newSolution.tourSize()-1) * Math.random()+1);

            // W�hle die St�dte zu den Positionen
            String citySwap1 = newSolution.getCity(tourPos1);
            String citySwap2 = newSolution.getCity(tourPos2);

            // Tausche die St�dte
            newSolution.setCity(tourPos2, citySwap1);
            newSolution.setCity(tourPos1, citySwap2);
            
            // Berechne die Energie (-> L�nge) der Touren
            int currentEnergy = currentSolution.getDistance(variante);
            int neighbourEnergy = newSolution.getDistance(variante);

            // Akzeptanzfunktion aufrufen
            if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
                currentSolution = new Tour(newSolution.getTour());
            }

            // Falls die neue Tour k�rzer ist, wird sie als beste Tour gespeichert
            // Dazu muss sie allerdings akzeptiert worden sein, 
            // da sonst die currentSolution nicht der newSolution sondern der vorhergehenden L�sung entspricht
            if (currentSolution.getDistance(variante) < best.getDistance(variante)) {
                best = new Tour(currentSolution.getTour());
            }
            
            // System k�hlt ab
            temp *= 1-coolingRate;
        }
        this.best = best.getTour();
        tourLaenge = best.getDistance(variante);
	}
	

	 // Berechne die Akzeptanzwahrscheinlichkeit
    public static double acceptanceProbability(int energy, int newEnergy, double temperature) {
    	// (Energy = Gesamtdistanz der Route)
    	
        // Wenn die neue Tour besser ist, wird sie akzeptiert (-> Akzeptanzwahrscheinlichkeit von 1,0 entspricht 100%)
        if (newEnergy < energy) {
            return 1.0;
        }
        // Wenn die neue Tour schlechter ist, wird eine Akzeptanzwahrscheinlichkeit zwischen 0 und 1 zur�ckgegeben
        return Math.exp((energy - newEnergy) / temperature);
    }
    
    
    
    private void erzeugeDaten() {
		
		
		Node A = g1.addNode("A");		
		Node B = g1.addNode("B");
		Node C = g1.addNode("C");
		Node D = g1.addNode("D");
		Node E = g1.addNode("E");
		Node F = g1.addNode("F");
		Node G = g1.addNode("G");
		Node H = g1.addNode("H");
		
		
		Edge AB = g1.addEdge("AB", A, B);
			AB.addAttribute("Fahrtzeit", 27);
			AB.addAttribute("Fahrstrecke", 27);
		Edge AC = g1.addEdge("AC", A, C);
			AC.addAttribute("Fahrtzeit", 57);
			AC.addAttribute("Fahrstrecke", 43);
		Edge AD = g1.addEdge("AD", A, D);
			AD.addAttribute("Fahrtzeit", 88);
			AD.addAttribute("Fahrstrecke", 80);
		Edge AE = g1.addEdge("AE", A, E);
			AE.addAttribute("Fahrtzeit", 65);
			AE.addAttribute("Fahrstrecke", 53);
		Edge AF = g1.addEdge("AF", A, F);
			AF.addAttribute("Fahrtzeit", 85);
			AF.addAttribute("Fahrstrecke", 75);
		Edge AG = g1.addEdge("AG", A, G);
			AG.addAttribute("Fahrtzeit", 73);
			AG.addAttribute("Fahrstrecke", 61);
		Edge AH = g1.addEdge("AH", A, H);
			AH.addAttribute("Fahrtzeit", 93);
			AH.addAttribute("Fahrstrecke", 91);
		
		Edge BC = g1.addEdge("BC", B, C);
			BC.addAttribute("Fahrtzeit", 43);
			BC.addAttribute("Fahrstrecke", 31);
		Edge BD = g1.addEdge("BD", B, D);
			BD.addAttribute("Fahrtzeit", 60);
			BD.addAttribute("Fahrstrecke", 53);
		Edge BE = g1.addEdge("BE", B, E);
			BE.addAttribute("Fahrtzeit", 90);
			BE.addAttribute("Fahrstrecke", 84);
		Edge BF = g1.addEdge("BF", B, F);
			BF.addAttribute("Fahrtzeit", 95);
			BF.addAttribute("Fahrstrecke", 91);
		Edge BG = g1.addEdge("BG", B, G);
			BG.addAttribute("Fahrtzeit", 70);
			BG.addAttribute("Fahrstrecke", 70);
		Edge BH = g1.addEdge("BH", B, H);
			BH.addAttribute("Fahrtzeit", 82);
			BH.addAttribute("Fahrstrecke", 73);
		
		Edge CD = g1.addEdge("CD", C, D);
			CD.addAttribute("Fahrtzeit", 68);
			CD.addAttribute("Fahrstrecke", 39);
		Edge CE = g1.addEdge("CE", C, E);
			CE.addAttribute("Fahrtzeit", 107);
			CE.addAttribute("Fahrstrecke", 99);
		Edge CF = g1.addEdge("CF", C, F);
			CF.addAttribute("Fahrtzeit", 112);
			CF.addAttribute("Fahrstrecke", 107);
		Edge CG = g1.addEdge("CG", C, G);
			CG.addAttribute("Fahrtzeit", 87);
			CG.addAttribute("Fahrstrecke", 86);
		Edge CH = g1.addEdge("CH", C, H);
			CH.addAttribute("Fahrtzeit", 115);
			CH.addAttribute("Fahrstrecke", 86);
		
		Edge DE = g1.addEdge("DE", D, E);
			DE.addAttribute("Fahrtzeit", 134);
			DE.addAttribute("Fahrstrecke", 137);
		Edge DF = g1.addEdge("DF", D, F);
			DF.addAttribute("Fahrtzeit", 140);
			DF.addAttribute("Fahrstrecke", 144);
		Edge DG = g1.addEdge("DG", D, G);
			DG.addAttribute("Fahrtzeit", 121);
			DG.addAttribute("Fahrstrecke", 123);
		Edge DH = g1.addEdge("DH", D, H);
			DH.addAttribute("Fahrtzeit", 124);
			DH.addAttribute("Fahrstrecke", 106);
		
		Edge EF = g1.addEdge("EF", E, F);
			EF.addAttribute("Fahrtzeit", 65);
			EF.addAttribute("Fahrstrecke", 49);
		Edge EG = g1.addEdge("EG", E, G);
			EG.addAttribute("Fahrtzeit", 74);
			EG.addAttribute("Fahrstrecke", 67);
		Edge EH = g1.addEdge("EH", E, H);
			EH.addAttribute("Fahrtzeit", 98);
			EH.addAttribute("Fahrstrecke", 93);
		

		Edge FG = g1.addEdge("FG", F, G);
			FG.addAttribute("Fahrtzeit", 105);
			FG.addAttribute("Fahrstrecke", 107);
		Edge FH = g1.addEdge("FH", F, H);
			FH.addAttribute("Fahrtzeit", 133);
			FH.addAttribute("Fahrstrecke", 134);
		

		Edge GH = g1.addEdge("GH", G, H);
			GH.addAttribute("Fahrtzeit", 45);
			GH.addAttribute("Fahrstrecke", 34);

	}


	
}
