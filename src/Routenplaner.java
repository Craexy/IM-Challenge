import org.graphstream.graph.implementations.*;


import java.util.ArrayList;
import java.util.Collection;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;


public class Routenplaner {
	

	private String route = "A,B,C,D,E,F,G,H";
	private static Graph g1 = new DefaultGraph("Fahrtzeiten");
	private ArrayList best = new ArrayList<String>();
	private int tourLaenge;
	

	public Routenplaner(){
		erzeugeDaten();
		planeRoute();
	}
	

	public String getRoute(){
		transformTourToString();
		return route;
	}
	
	public void transformTourToString(){
		route = "";
		for (int i = 0; i < best.size(); i++) {
			if(i==best.size()-1){
				route += best.get(i);
			}else{
				route += best.get(i)+",";
			}
      
        }
		System.out.println(route);
	}

	
	private void planeRoute() {
		simmulatedAnnealing();
		System.out.println(tourLaenge);
	}
	
	public static int getFahrtzeit(String Anfangspunkt, String Endpunkt) {
		try{
			return g1.getEdge(Anfangspunkt+Endpunkt).getAttribute("Fahrtzeit",Integer.class);
		}catch (NullPointerException e)
		{
			return g1.getEdge(Endpunkt+Anfangspunkt).getAttribute("Fahrtzeit",Integer.class);
		}
	}


	public static int getNumberOfNodes(){
		return g1.getNodeCount();
	}
	 
	public static String getCity(int cityIndex){
		return g1.getNode(cityIndex).getId();
		
	}
	
	
	public void simmulatedAnnealing(){
		// Set initial temp
        double temp = 10000;

        // Cooling rate
        double coolingRate = 0.003;
        
     // Initialize intial solution
        Tour currentSolution = new Tour();
        currentSolution.eineTourErstellen();
        
     // Set as current best
        Tour best = new Tour(currentSolution.getTour());
        
     // Loop until system has cooled
        while (temp > 1) {
            // Create new neighbour tour
            Tour newSolution = new Tour(currentSolution.getTour());

            // Get a random positions in the tour
            int tourPos1 = (int) (newSolution.tourSize() * Math.random());
            int tourPos2 = (int) (newSolution.tourSize() * Math.random());

            // Get the cities at selected positions in the tour
            String citySwap1 = newSolution.getCity(tourPos1);
            String citySwap2 = newSolution.getCity(tourPos2);

            // Swap them
            newSolution.setCity(tourPos2, citySwap1);
            newSolution.setCity(tourPos1, citySwap2);
            
            // Get energy of solutions
            int currentEnergy = currentSolution.getDistance();
            int neighbourEnergy = newSolution.getDistance();

            // Decide if we should accept the neighbour
            if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
                currentSolution = new Tour(newSolution.getTour());
            }

            // Keep track of the best solution found
            if (currentSolution.getDistance() < best.getDistance()) {
                best = new Tour(currentSolution.getTour());
            }
            
            // Cool system
            temp *= 1-coolingRate;
        }
        this.best = best.getTour();
        tourLaenge = best.getDistance();
	}
	

	 // Berechne die acceptance probability
    public static double acceptanceProbability(int energy, int newEnergy, double temperature) {
        // If the new solution is better, accept it (Energy = Gesamtdistanz der Route)
        if (newEnergy < energy) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
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
		Edge AC = g1.addEdge("AC", A, C);
			AC.addAttribute("Fahrtzeit", 57);
		Edge AD = g1.addEdge("AD", A, D);
			AD.addAttribute("Fahrtzeit", 88);
		Edge AE = g1.addEdge("AE", A, E);
			AE.addAttribute("Fahrtzeit", 65);
		Edge AF = g1.addEdge("AF", A, F);
			AF.addAttribute("Fahrtzeit", 85);
		Edge AG = g1.addEdge("AG", A, G);
			AG.addAttribute("Fahrtzeit", 73);
		Edge AH = g1.addEdge("AH", A, H);
			AH.addAttribute("Fahrtzeit", 93);
		
		Edge BC = g1.addEdge("BC", B, C);
			BC.addAttribute("Fahrtzeit", 43);
		Edge BD = g1.addEdge("BD", B, D);
			BD.addAttribute("Fahrtzeit", 60);
		Edge BE = g1.addEdge("BE", B, E);
			BE.addAttribute("Fahrtzeit", 90);
		Edge BF = g1.addEdge("BF", B, F);
			BF.addAttribute("Fahrtzeit", 95);
		Edge BG = g1.addEdge("BG", B, G);
			BG.addAttribute("Fahrtzeit", 70);
		Edge BH = g1.addEdge("BH", B, H);
			BH.addAttribute("Fahrtzeit", 82);
		
		Edge CD = g1.addEdge("CD", C, D);
			CD.addAttribute("Fahrtzeit", 68);
		Edge CE = g1.addEdge("CE", C, E);
			CE.addAttribute("Fahrtzeit", 107);
		Edge CF = g1.addEdge("CF", C, F);
			CF.addAttribute("Fahrtzeit", 112);
		Edge CG = g1.addEdge("CG", C, G);
			CG.addAttribute("Fahrtzeit", 87);
		Edge CH = g1.addEdge("CH", C, H);
			CH.addAttribute("Fahrtzeit", 115);
		
		Edge DE = g1.addEdge("DE", D, E);
			DE.addAttribute("Fahrtzeit", 134);
		Edge DF = g1.addEdge("DF", D, F);
			DF.addAttribute("Fahrtzeit", 140);
		Edge DG = g1.addEdge("DG", D, G);
			DG.addAttribute("Fahrtzeit", 121);
		Edge DH = g1.addEdge("DH", D, H);
			DH.addAttribute("Fahrtzeit", 124);
		
		Edge EF = g1.addEdge("EF", E, F);
			EF.addAttribute("Fahrtzeit", 65);
		Edge EG = g1.addEdge("EG", E, G);
			EG.addAttribute("Fahrtzeit", 74);
		Edge EH = g1.addEdge("EH", E, H);
			EH.addAttribute("Fahrtzeit", 98);
		

		Edge FG = g1.addEdge("FG", F, G);
			FG.addAttribute("Fahrtzeit", 105);
		Edge FH = g1.addEdge("FH", F, H);
			FH.addAttribute("Fahrtzeit", 133);
		

		Edge GH = g1.addEdge("GH", G, H);
			GH.addAttribute("Fahrtzeit", 45);
			
			
		
		
		//g1.display();
		

	}

	
}
