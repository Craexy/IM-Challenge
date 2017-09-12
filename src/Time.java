
public class Time {
	
	private int stunden;
	private int minuten;
	private int sekunden;
	
	public Time(int Stunden, int Minuten, int Sekunden) {
		stunden = Stunden;
		minuten = Minuten;
		sekunden = Sekunden;
	}
	
	public Time reduceTime(int Minuten) {
		if (minuten>=Minuten) minuten = minuten - Minuten; 
		else {
			stunden = stunden - Minuten/60;
			minuten = minuten - Minuten%60;
		}
		return new Time(stunden, minuten, sekunden);
	}
	
	public Time addTime(int Minuten) {
		if (Minuten<=60-minuten) minuten = minuten + Minuten;
		else {
			minuten = minuten + stunden*60;
			minuten = minuten+Minuten;
			stunden = minuten/60;
			minuten = minuten%60;
		}
		return new Time(stunden, minuten, 0);
	}

	public boolean isEarlierThan(Time Zeit) {
		if (Zeit.getStunden()<this.stunden) return false;
		else if (Zeit.getStunden()>this.stunden) return true;
		
		if (Zeit.getMinuten()<this.minuten) return false;
		else if (Zeit.getMinuten()>this.minuten) return false;
		
		return false;
	}
	
	public int getStunden() {
		return stunden;
	}

	public int getMinuten() {
		return minuten;
	}
}
