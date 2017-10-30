import java.util.HashMap;
import java.util.LinkedList;

public class MedUeberschuss {
	
	private int medTyp;
	private int anzahlMeds;
	private Time startProduktion;
	
	private int startNutzMed60 = 270;
	private int endNutzMed60 = 450-30;
	
	private int startNutzMed120 = 390;
	private int endNutzMed120 = 600-30;

	private int startNutzMed250 = 540;
	private int endNutzMed250 = 720-30;

	private int startNutzMed500 = 660;
	private int endNutzMed500 = 840-30;
	
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
		
	}
	
	public MedUeberschuss getNewInstance() {
		LinkedList<Integer> temp = new LinkedList<Integer>();
		temp.addFirst(medTyp);
		temp.addLast(anzahlMeds);
		return new MedUeberschuss(temp, this.startProduktion);
	}
	
	public int getMedTyp() {
		return medTyp;
	}
	
	public int getAnzahlMeds() {
		return anzahlMeds;
	}
	
	public void setAnzahlMeds(int anzahl) {
		this.anzahlMeds = anzahl;
	}
	
	public Time getStartProduktion() {
		return startProduktion;
	}
	
	public Time getStartNutzungszeit() {
		switch (medTyp) {
			case 60: return startProduktion.getNewInstance().addTime(startNutzMed60);
			case 120: return startProduktion.getNewInstance().addTime(startNutzMed120);
			case 250: return startProduktion.getNewInstance().addTime(startNutzMed250);
			case 500: return startProduktion.getNewInstance().addTime(startNutzMed500);
			default: return null;
				}
	}
	
	public Time getEndNutzungszeit() {
		switch (medTyp) {
			case 60: return startProduktion.getNewInstance().addTime(endNutzMed60);
			case 120: return startProduktion.getNewInstance().addTime(endNutzMed120);
			case 250: return startProduktion.getNewInstance().addTime(endNutzMed250);
			case 500: return startProduktion.getNewInstance().addTime(endNutzMed500);
			default: return null;
				}
	}
	
	public String toString() {
		return anzahlMeds+" Einheiten Überschuss vom Typ Med"+medTyp+", die zum Zeitpunkt "+startProduktion.toString()+" produziert werden.";
	}
	

}
