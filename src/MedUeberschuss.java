import java.util.HashMap;
import java.util.LinkedList;

public class MedUeberschuss {
	
	private int medTyp;
	private int anzahlMeds;
	private Time startProduktion;
	
	public MedUeberschuss(LinkedList<Integer> medUndUeberschuss, Time startProduktion) {
		
		this.anzahlMeds = medUndUeberschuss.getLast();
		this.startProduktion=startProduktion.getNewInstance();
		
		int medTypTemp = medUndUeberschuss.getFirst();
		
		switch(medTypTemp) {
		case 0: this.medTyp = 60;
			break;
		case 1: this.medTyp = 120;
			break;
		case 2: this.medTyp = 250;
			break;
		case 3: this.medTyp = 500;
			break;
		default: this.medTyp=medTypTemp;
			break;
		}
		
		System.out.println("Dieser MedUeberschuss wurde erzeugt: "+medTyp+"  "+anzahlMeds+"   "+startProduktion);
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
