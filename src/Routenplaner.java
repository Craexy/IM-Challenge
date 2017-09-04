import org.graphstream.graph.implementations.*;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;


public class Routenplaner {
	
	private String Route;
	private static Graph g1 = new DefaultGraph("Fahrtzeiten");

	public Routenplaner(){
		erzeugeDaten();
		planeRoute();
	}
	
	private void erzeugeDaten() {
		
		
		Node A = g1.addNode("A");
		A.addAttribute("Name", "A");
		Node B = g1.addNode("B");
		A.addAttribute("Name", "B");
		Node C = g1.addNode("C");
		A.addAttribute("Name", "C");
		Node D = g1.addNode("D");
		A.addAttribute("Name", "D");
		Node E = g1.addNode("E");
		A.addAttribute("Name", "E");
		Node F = g1.addNode("F");
		A.addAttribute("Name", "F");
		Node G = g1.addNode("G");
		A.addAttribute("Name", "G");
		Node H = g1.addNode("H");
		A.addAttribute("Name", "H");
		
		
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
				
		g1.display();
		

	}

	public String getRoute(){
		return Route;
	}

	
	private void planeRoute() {

	}
	
	public static int getFahrtzeit(String Anfangspunkt, String Endpunkt) {
		return g1.getEdge(Anfangspunkt+Endpunkt).getAttribute("Fahrtzeit");
	}

	public static int getNumberOfNodes(){
		return g1.getNodeCount();
	}
	 
	public static String getCity(int cityIndex){
		return g1.getNode(cityIndex).getAttribute("Name");
		
	}

	
}
