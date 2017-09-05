import java.util.ArrayList;
import java.util.Collections;



public class Tour {

	private ArrayList tour = new ArrayList<String>();
	private int distance = 0;
	
	
    public Tour(){
        for (int i = 0; i < Routenplaner.getNumberOfNodes(); i++) {
            tour.add(null);
        }
    }
    
    public Tour(ArrayList tour){
        this.tour = (ArrayList) tour.clone();
    }
    
 
    public ArrayList getTour(){
        return tour;
    }
    
    public void eineTourErstellen() {
    	
        for (int cityIndex = 0; cityIndex < Routenplaner.getNumberOfNodes(); cityIndex++) {
          setCity(cityIndex, Routenplaner.getCity(cityIndex));
        }
        // Tour zufällig anordnen
        Collections.shuffle(tour);
        System.out.println(tour);
    }
    

    public String getCity(int tourPosition) {
        return (String) tour.get(tourPosition);
    }

    
    public void setCity(int tourPosition, String city) {
        tour.set(tourPosition, city);
        // If the tours been altered we need to reset the fitness and distance
        distance = 0;
    }
    
    public int tourSize() {
        return tour.size();
    }
    
    public int getDistance(){
        if (distance == 0) {
            int tourDistance = 0;
            // Loop through our tour's cities
            for (int cityIndex=0; cityIndex < tourSize(); cityIndex++) {
                // Get city we're traveling from
                String fromCity = getCity(cityIndex);
                // City we're traveling to
                String destinationCity;
                // Check we're not on our tour's last city, if we are set our 
                // tour's final destination city to our starting city
                if(cityIndex+1 < tourSize()){
                    destinationCity = getCity(cityIndex+1);
                }
                else{
                    destinationCity = getCity(0);
                }
                // Get the distance between the two cities
                tourDistance += Routenplaner.getFahrtzeit(fromCity,destinationCity);
            }
            distance = tourDistance;
        }
        return distance;
    }
}
