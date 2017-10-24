import java.util.List;

public class Lösung {
	
	private int gesamtkosten;
	private int distributionskostenZeit;
	private int distributionskostenStrecke;
	private int produktionskosten;
	private int strafkosten;
	private int genutzteFahrzeuge;
	private int genutzteProduktionslinien;
	
	private List<Fahrzeug> fahrzeuge;
	
	private String strecke;
	
	
	
	public Lösung() {
		
	}
	
	public Lösung(int distributionskostenZeit, int distributionskostenStrecke, int produktionskosten, int strafkosten, List<Fahrzeug> fahrzeuge, int anzahlProduktionslinien, String strecke) {
		this.distributionskostenZeit = distributionskostenZeit;
		this.distributionskostenStrecke = distributionskostenStrecke;
		this.produktionskosten = produktionskosten;
		this.fahrzeuge = fahrzeuge;
		this.genutzteProduktionslinien = anzahlProduktionslinien;
		this.strecke = strecke;
		this.strafkosten = strafkosten;
		
		genutzteFahrzeuge = fahrzeuge.size();
		
		gesamtkosten = this.distributionskostenZeit + this.distributionskostenStrecke + this.produktionskosten + this.strafkosten
				+ this.genutzteFahrzeuge*1000 + this.genutzteProduktionslinien*3000;
	}
	
	public int getGesamtkosten() {
		return gesamtkosten;
	}
	public void setGesamtkosten(int gesamtkosten) {
		this.gesamtkosten = gesamtkosten;
	}
	public int getDistributionskostenZeit() {
		return distributionskostenZeit;
	}
	public void setDistributionskostenZeit(int distributionskostenZeit) {
		this.distributionskostenZeit = distributionskostenZeit;
	}
	public int getDistributionskostenStrecke() {
		return distributionskostenStrecke;
	}
	public void setDistributionskostenStrecke(int distributionskostenStrecke) {
		this.distributionskostenStrecke = distributionskostenStrecke;
	}
	public int getProduktionskosten() {
		return produktionskosten;
	}
	public void setProduktionskosten(int produktionskosten) {
		this.produktionskosten = produktionskosten;
	}
	public List<Fahrzeug> getFahrzeuge() {
		return fahrzeuge;
	}
	public void setFahrzeuge(List<Fahrzeug> fahrzeuge) {
		this.fahrzeuge = fahrzeuge;
	}
	public int getProduktionslinien() {
		return genutzteProduktionslinien;
	}
	public void setAnzahlProduktionslinien(int anzahlProduktionslinien) {
		this.genutzteProduktionslinien = anzahlProduktionslinien;
	}
	
	public String toString() {
		return "Insgesamt wurden " + this.genutzteFahrzeuge + " Fahrzeuge verwendet."
				+" Es wurden " + this.genutzteProduktionslinien + " Produktionslinien genutzt."
				+" Es sind Gesamtkosten in Höhe von " + this.gesamtkosten + " entstanden."
				+" Diese entfallen auf Produktionskosten(" + this.produktionskosten+") und Distributionskosten für Strecke("
				+this.distributionskostenStrecke+") und Zeit("+this.distributionskostenZeit+") der Fahrten."
				+" Zusätzlich fallen " + this.strafkosten + " an Strafkosten an.";
				
	}

}
