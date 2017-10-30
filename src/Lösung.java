import java.util.List;

public class Lösung {
	
	private int gesamtkosten;
	private int distributionskostenZeit;
	private int distributionskostenStrecke;
	private int produktionskosten;
	private int anzahlÜbrigerBedarfe;
	private int genutzteFahrzeuge;
	private int genutzteProduktionslinien;
	private int strafkostenSatz;
	
	private List<Fahrzeug> fahrzeuge;
	
	private String strecke;
	
	
	
	public Lösung() {
		
	}
	
	public Lösung(int strafkostensatz, int distributionskostenZeit, int distributionskostenStrecke, int produktionskosten, int anzahlÜbrigerBedarfe, List<Fahrzeug> fahrzeuge, int anzahlProduktionslinien, String strecke) {
		this.distributionskostenZeit = distributionskostenZeit;
		this.distributionskostenStrecke = distributionskostenStrecke;
		this.produktionskosten = produktionskosten;
		this.fahrzeuge = fahrzeuge;
		this.genutzteProduktionslinien = anzahlProduktionslinien;
		this.strecke = strecke;
		this.anzahlÜbrigerBedarfe = anzahlÜbrigerBedarfe;
		this.strafkostenSatz=strafkostensatz;
		
		genutzteFahrzeuge = fahrzeuge.size();
		
		gesamtkosten = this.distributionskostenZeit + this.distributionskostenStrecke + this.produktionskosten + this.anzahlÜbrigerBedarfe
				+ this.genutzteFahrzeuge*1000 + this.genutzteProduktionslinien*3000 + this.anzahlÜbrigerBedarfe*this.strafkostenSatz;
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
	
	public int getStrafkostenSatz() {
		return this.strafkostenSatz;
	}
	
	public void setStrafkostenSatz(int strafkostenSatz) {
		this.strafkostenSatz = strafkostenSatz;
	}
	
	public String getStrecke() {
		return this.strecke;
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
				+"\nDiese entfallen auf Produktionskosten(" + this.produktionskosten+") und Distributionskosten für Strecke("
				+this.distributionskostenStrecke+") und Zeit("+this.distributionskostenZeit+") der Fahrten."
				+"\nZusätzlich fallen " + this.anzahlÜbrigerBedarfe*this.strafkostenSatz + " an Strafkosten an da "
				+this.anzahlÜbrigerBedarfe+" Bedarfe nicht gedeckt wurden.";
				
	}

}
