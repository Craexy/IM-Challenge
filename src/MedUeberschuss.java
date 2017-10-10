
public class MedUeberschuss {
	
	private int medTyp;
	private int anzahlMeds;
	private Time startProduktion;
	
	public MedUeberschuss(int medTyp,  int anzahlMeds, Time startProduktion) {
		switch(medTyp) {
		case 0: this.medTyp = 60;
			break;
		case 1: this.medTyp = 120;
			break;
		case 2: this.medTyp = 250;
			break;
		case 3: this.medTyp = 500;
			break;
		default: this.medTyp=medTyp;
			break;
		}
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
	
	public String toString() {
		return anzahlMeds+" Einheiten Überschuss vom Typ Med"+medTyp+", die zum Zeitpunkt "+startProduktion.toString()+" produziert werden.";
	}
	

}
