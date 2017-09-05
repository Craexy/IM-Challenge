import java.util.HashSet;
import java.util.Set;
import java.sql.Time;

public class Tourenplaner {
	
	private Routenplaner routenplaner;
	private String route;
	private int routenzeit;
	private Set<Fahrzeug> fahrzeuge;
	
	//Zeitpunkt, zu dem das erste Medikament zugestellt wird
	private String startzeit = "6:30";
	
	private Time[] bedarf0 = new Time[20];
	private Time[] bedarf1 = new Time[31];
	private Time[] bedarf2 = new Time[45];
	private Time[] bedarf3 = new Time[45];
	private Time[] bedarf4 = new Time[45];
	private Time[] bedarf5 = new Time[45];
	private Time[] bedarf6 = new Time[45];
	private Time[] bedarf7 = new Time[45];
	
	private int entladezeit0 = 15;
	private int entladezeit1 = 30;
	private int entladezeit2 = 30;
	private int entladezeit3 = 15;
	private int entladezeit4 = 30;
	private int entladezeit5 = 15;
	private int entladezeit6 = 30;
	private int entladezeit7 = 15;
	
	
	public Tourenplaner() {
		routenplaner = new Routenplaner();
		route = routenplaner.getRoute();
		//routenzeit = routenplaner.getFahrtzeit(route.split(",")[0], route.split(",")[route.length()]);
		
		fahrzeuge = new HashSet<Fahrzeug>();
		
		this.befülleDaten();
	}
	
	@SuppressWarnings("deprecation")
	private void befülleDaten() {
		String tempStr = "7:30 7:40 8:0 8:10 8:30 8:40 8:50 9:0 9:30 9:50 10:30 10:50 11:30 11:50 12:30 12:50 13:30 14:0 14:30 15:0 15:40 16:0";
		for (int i=0; i<tempStr.split(":").length; i++) {
			bedarf0[i] = new Time(Integer.parseInt(tempStr.split(" ")[i].split(":")[0]),
					Integer.parseInt(tempStr.split(" ")[i].split(":")[1]), 0);
		}
		
		tempStr = "7:10 7:20 7:45 8:5 8:15 8:35 8:50 8:55 9:20 9:25 9:45 10:10 10:15 10:25 10:55 11:55 13:15 13:20 13:25 13:35 14:5 14:10 14:35 14:40 14:45 14:50 14:55 15:0 15:20 16:5 16:55";
		for (int i=0; i<tempStr.split(":").length; i++) {
			bedarf1[i] = new Time(Integer.parseInt(tempStr.split(" ")[i].split(":")[0]),
					Integer.parseInt(tempStr.split(" ")[i].split(":")[1]), 0);
		}
		
		tempStr = "6:30 6:35 6:40 7:5 7:30 8:0 8:15 8:40 8:55 9:0 9:5 9:50 10:15 10:25 10:30 10:35 10:45 11:0 11:10 11:20 11:35 11:40 11:45 11:55 12:20 12:25 12:50 13:5 13:20 13:25 13:30 13:40 13:45 14:10 14:15 14:20 14:55 15:30 15:35 15:40 16:30 16:35 16:40 17:5 17:20";
		for (int i=0; i<tempStr.split(":").length; i++) {
			bedarf2[i] = new Time(Integer.parseInt(tempStr.split(" ")[i].split(":")[0]),
					Integer.parseInt(tempStr.split(" ")[i].split(":")[1]), 0);
		}
		
		tempStr = "6:50 8:0 8:15 8:20 9:0 9:30 9:35 9:40 10:5 10:10 10:15 10:25 10:30 11:0 11:15 11:30 11:40 11:55 12:30 12:50 13:20 13:30 13:45 14:15 14:35 14:45 15:0 15:10 15:15 15:20 15:55 16:10 16:20 16:25 16:30 16:35 17:15";
		for (int i=0; i<tempStr.split(":").length; i++) {
			bedarf3[i] = new Time(Integer.parseInt(tempStr.split(" ")[i].split(":")[0]),
					Integer.parseInt(tempStr.split(" ")[i].split(":")[1]), 0);
		}
		
		tempStr = "6:35 6:40 6:55 7:5 7:25 7:40 7:50 8:20 8:30 8:40 8:55 9:5 9:10 9:15 9:50 10:10 10:35 10:40 10:50 11:0 11:15 11:30 11:35 11:40 12:30 12:40 12:45 13:10 13:15 13:20 13:30 13:35 13:50 14:10 14:40 14:45 15:10 15:15 15:55 16:0 16:25 16:40 17:0 17:10 17:20";
		for (int i=0; i<tempStr.split(":").length; i++) {
			bedarf4[i] = new Time(Integer.parseInt(tempStr.split(" ")[i].split(":")[0]),
					Integer.parseInt(tempStr.split(" ")[i].split(":")[1]), 0);
		}
		
		tempStr = "6:30 6:40 6:45 6:50 6:55 7:0 7:10 7:15 7:20 8:15 8:20 8:25 8:35 9:30 9:45 10:35 10:45 10:50 11:5 11:30 11:35 11:40 11:55 12:10 12:15 12:20 12:30 12:35 12:40 13:15 13:20 13:35 13:55 14:5 14:20 14:25 14:30 14:40 15:5 15:25 15:55 16:0 16:5 16:15 16:40 16:55 17:15";
		for (int i=0; i<tempStr.split(":").length; i++) {
			bedarf5[i] = new Time(Integer.parseInt(tempStr.split(" ")[i].split(":")[0]),
					Integer.parseInt(tempStr.split(" ")[i].split(":")[1]), 0);
		}
		
		tempStr = "6:50 7:5 7:15 7:45 7:50 7:55 8:5 8:15 8:50 8:55 9:5 9:15 9:40 9:45 10:0 10:5 10:10 10:15 10:25 10:40 11:10 11:35 11:45 12:5 12:10 12:30 12:40 13:20 13:25 13:50 14:10 14:15 14:25 14:35 14:40 14:50 15:0 15:15 15:20 15:45 16:5 16:10 16:15 16:35 16:45 17:5 17:20";
		for (int i=0; i<tempStr.split(":").length; i++) {
			bedarf6[i] = new Time(Integer.parseInt(tempStr.split(" ")[i].split(":")[0]),
					Integer.parseInt(tempStr.split(" ")[i].split(":")[1]), 0);
		}
		
		tempStr = "6:50 6:55 7:0 7:40 7:45 8:0 9:30 9:50 9:55 10:0 10:10 10:15 10:20 10:30 10:45 10:50 11:0 11:15 11:30 11:40 11:45 12:45 13:0 13:15 13:35 13:40 13:50 14:20 14:25 14:35 14:55 15:20 15:55 16:0 16:20 16:30 16:50 17:0 17:5 17:15";
		for (int i=0; i<tempStr.split(":").length; i++) {
			bedarf7[i] = new Time(Integer.parseInt(tempStr.split(" ")[i].split(":")[0]),
					Integer.parseInt(tempStr.split(" ")[i].split(":")[1]), 0);
		}
	}
	
	private void berechneRouten() {
		
		//Start bei 0
		Fahrzeug fahrzeug1 = new Fahrzeug();
			fahrzeug1.set60(fahrzeug1.get60()+1);
			fahrzeug1.setStartzeit(bedarf0[0]);
		//		if (bedarf0[1].before(new Time(null, null, null))) {
					
			//	}
			
	}
	
	private double berechneVerblHaltbarkeit30(double Radioaktivität) {
		return (0.85*Radioaktivität);
	}
	
	
	public Set<Fahrzeug> getFahrzeuge(){
		return fahrzeuge;
	}

}
