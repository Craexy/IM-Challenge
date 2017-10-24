
public class TransformedMed {
	private int produktionslinie;
	private int position;
	private int medTyp;
	private int menge;
	private Time startzeitpunkt;
	private Time endzeitpunkt;
	private int ueberschuesse;
	private int urspruenglicheBedarfe;
	
	public TransformedMed(int produktionslinie,int position, int medTyp, int menge, Time startzeitpunkt, Time endzeitpunkt, int ueberschuesse, int urspruenglicheBedarfe){
		this.produktionslinie = produktionslinie;
		this.position = position;
		this.medTyp = medTyp;
		this.menge = menge;
		this.startzeitpunkt = startzeitpunkt;
		this.endzeitpunkt = endzeitpunkt;
		this.ueberschuesse = ueberschuesse;
		this.urspruenglicheBedarfe = urspruenglicheBedarfe;
		
	}
	
	public int getPL(){
		return produktionslinie;
	}
	public int getPosition(){
		return position;
	}
	public int getMedTyp(){
		return medTyp;
	}
	public int getMenge(){
		return menge;
	}
	public Time getStartzeitpunkt(){
		return startzeitpunkt;
	}
	public Time getEndzeitpunkt(){
		return endzeitpunkt;
	}
	public int getUeberschuesse(){
		return ueberschuesse;
	}
	public int getUrspruenglicheBedarfe(){
		return urspruenglicheBedarfe;
	}
}
