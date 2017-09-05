import java.sql.Time;

public class Fahrzeug {
	
	private int Med60 = 0;
	private int Med120 = 0;
	private int Med250 = 0;
	private int Med500 = 0;
	private String Tour = "";
	private Time Startzeit = new Time(0,0,0);
	
	public Fahrzeug(int Medikament60, int Medikament120, int Medikament250, int Medikament500) {
		Med60 = Medikament60;
		Med120 = Medikament120;
		Med250 = Medikament250;
		Med500 = Medikament500;
	}
	
	public Fahrzeug(int Medikament60, int Medikament120, int Medikament250, int Medikament500, String Strecke) {
		Med60 = Medikament60;
		Med120 = Medikament120;
		Med250 = Medikament250;
		Med500 = Medikament500;
		this.Tour = Strecke;
	}
	
	public Fahrzeug() {
		this(0, 0, 0, 0);
	}
	
	public String getStrecke(){
		return Tour;
	}
	
	public void setStrecke(String Strecke) {
		this.Tour = Strecke;
	}
	
	public void setStartzeit(Time Startzeit) {
		this.Startzeit = Startzeit;
	}
	
	public Time getStartzeit() {
		return Startzeit;
	}
	
	public int get60() {
		return Med60;
	}
	
	public void set60(int Med60) {
		this.Med60=Med60;
	}
	
	public int getMed120() {
		return Med120;
	}
	
	public void set120(int Med120) {
		this.Med120=Med120;
	}
	
	public int getMed250(){
		return Med250;
	}
	
	public void set250(int Med250) {
		this.Med250=Med250;
	}
	
	public int getMed500(){
		return Med500;
	}
	
	public void set500(int Med500) {
		this.Med500=Med500;
	}
	
	
}
