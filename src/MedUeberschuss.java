
public class MedUeberschuss {
	
	private int medTyp;
	private int anzahlMeds;
	private Time startProduktion;
	
	public MedUeberschuss(int medTyp,  int anzahlMeds, Time startProduktion) {
		this.medTyp=medTyp;
		this.anzahlMeds=anzahlMeds;
		this.startProduktion=startProduktion.getNewInstance();
	}
	
	public int getMedTyp() {
		return medTyp;
	}
	
	public int getAnzahlMeds() {
		return anzahlMeds;
	}
	
	public Time getStartProduktion() {
		return startProduktion;
	}
	
	

}
