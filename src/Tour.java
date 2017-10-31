import java.util.Collections;
import java.util.LinkedList;



public class Tour {

	private LinkedList<String> tour = new LinkedList<String>();
	private int distance = 0;
	
	
    public Tour(){
        for (int i = 0; i < Routenplaner.getNumberOfNodes(); i++) {
            tour.add(null);
        }
    }
    
    public Tour(LinkedList<String> tour){
        this.tour = (LinkedList<String>) tour.clone();
    }
    
 
    public LinkedList<String> getTour(){
        return tour;
    }
    
    public void eineTourErstellen() {
    	
        for (int cityIndex = 0; cityIndex < Routenplaner.getNumberOfNodes(); cityIndex++) {
          setCity(cityIndex, Routenplaner.getCity(cityIndex));
        }
        // Tour zufällig anordnen
        String firstElement = (String) tour.getFirst();  
        tour.removeFirst();
        Collections.shuffle(tour);
        tour.addFirst(firstElement);
    }
    

    public String getCity(int tourPosition) {
        return (String) tour.get(tourPosition);
    }

    
    public void setCity(int tourPosition, String city) {
        tour.set(tourPosition, city);
        // Bei neuer Tour wird die Distanz auf 0 um in getDistance die Teilstrecken aufzuaddieren
        distance = 0;
    }
    
    public int tourSize() {
        return tour.size();
    }
    
    public int getDistance(int variante){
    	switch(variante){
    	case 1:
    		if (distance == 0) {
                int tourDistance = 0;
                // For-Schleife über alle Städte in einer Tour
                for (int cityIndex=0; cityIndex < tourSize(); cityIndex++) {
                    // Teilroute: Startstadt
                    String fromCity = getCity(cityIndex);
                    // Teilroute: Zielstadt
                    String destinationCity;
                    // Befindet sich der Algorithmus auf der letzten Stadt als Startstadt
                    // wird die 1. Stadt als Zielstadt festgelegt (->Rundreise)
                    if(cityIndex+1 < tourSize()){
                        destinationCity = getCity(cityIndex+1);
                    }
                    else{
                        destinationCity = getCity(0);
                    }
                    // Addiert die Distanz der Teilstrecke zu der Gesamtlänge der Tour
                    tourDistance += Routenplaner.getFahrtzeit(fromCity,destinationCity);
                }
                distance = tourDistance;
            }
            return distance;
            
    	case 2:
    		if (distance == 0) {
                int tourDistance = 0;
                // For-Schleife über alle Städte in einer Tour
                for (int cityIndex=0; cityIndex < tourSize(); cityIndex++) {
                	// Teilroute: Startstadt
                    String fromCity = getCity(cityIndex);
                    // Teilroute: Zielstadt
                    String destinationCity;
                    // Befindet sich der Algorithmus auf der letzten Stadt als Startstadt
                    // wird die 1. Stadt als Zielstadt festgelegt (->Rundreise)
                    if(cityIndex+1 < tourSize()){
                        destinationCity = getCity(cityIndex+1);
                    }
                    else{
                        destinationCity = getCity(0);
                    }
                    // Addiert die Distanz der Teilstrecke zu der Gesamtlänge der Tour
                    tourDistance += Routenplaner.getFahrstrecke(fromCity,destinationCity);
                }
                distance = tourDistance;
            }
            return distance;
    	default:System.out.println("Es wurde keine Variante festgelegt!"); 
    			return distance;
    	}
    	
        
    }
}
