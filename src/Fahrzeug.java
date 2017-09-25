
public class Fahrzeug {
	
	private int Med60 = 0;
	private int Med120 = 0;
	private int Med250 = 0;
	private int Med500 = 0;
	private String Tour = "";
	private Time startzeitFahrt = new Time(0,0,0);
	private Time startzeitBeladung60 = new Time(0,0,0);
	private Time startzeitBeladung120 = new Time(0,0,0);
	private Time startzeitBeladung250 = new Time(0,0,0);
	private Time startzeitBeladung500 = new Time(0,0,0);
	
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
	
	public void setStartzeitFahrt(Time Startzeit) {
		this.startzeitFahrt = new Time(Startzeit.getStunden(), Startzeit.getMinuten(), 0);
	}
	
	public Time getStartzeitFahrt() {
		return new Time (startzeitFahrt.getStunden(), startzeitFahrt.getMinuten(), 0);
	}
	
	public void setStartzeitBeladung60(Time Startzeit) {
		this.startzeitBeladung60 = new Time(Startzeit.getStunden(), Startzeit.getMinuten(), 0);
	}
	
	public Time getStartzeitBeladung60() {
		return new Time (startzeitBeladung60.getStunden(), startzeitBeladung60.getMinuten(), 0);
	}
	
	public void setStartzeitBeladung120(Time Startzeit) {
		this.startzeitBeladung120 = new Time(Startzeit.getStunden(), Startzeit.getMinuten(), 0);
	}
	
	public Time getStartzeitBeladung120() {
		return new Time (startzeitBeladung120.getStunden(), startzeitBeladung120.getMinuten(), 0);
	}
	
	public void setStartzeitBeladung250(Time Startzeit) {
		this.startzeitBeladung250 = new Time(Startzeit.getStunden(), Startzeit.getMinuten(), 0);
	}
	
	public Time getStartzeitBeladung250() {
		return new Time (startzeitBeladung250.getStunden(), startzeitBeladung250.getMinuten(), 0);
	}
	
	public void setStartzeitBeladung500(Time Startzeit) {
		this.startzeitBeladung500 = new Time(Startzeit.getStunden(), Startzeit.getMinuten(), 0);
	}
	
	public Time getStartzeitBeladung500() {
		return new Time (startzeitBeladung500.getStunden(), startzeitBeladung500.getMinuten(), 0);
	}
	
	
	
	public int get60() {
		return Med60;
	}
	
	public void set60(int Med60) {
		this.Med60=Med60;
	}
	
	public int get120() {
		return Med120;
	}
	
	public void set120(int Med120) {
		this.Med120=Med120;
	}
	
	public int get250(){
		return Med250;
	}
	
	public void set250(int Med250) {
		this.Med250=Med250;
	}
	
	public int get500(){
		return Med500;
	}
	
	public void set500(int Med500) {
		this.Med500=Med500;
	}
	
	
}
