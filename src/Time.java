
public class Time implements Comparable<Time>{
	
	private int stunden;
	private int minuten;
	private int sekunden;
	
	public Time(int Stunden, int Minuten, int Sekunden) {
		stunden = Stunden;
		minuten = Minuten;
		sekunden = Sekunden;
	}
	
	public Time reduceTime(int Minuten) throws IllegalArgumentException{
		if (Minuten<0) throw new IllegalArgumentException();
		if (this.minuten>=Minuten) this.minuten = this.minuten - Minuten; 
		else {
			if (Minuten%60==0){ this.stunden = this.stunden-Minuten/60;
								
			}
			else {this.stunden = this.stunden - Minuten/60 - 1;
			this.minuten = this.minuten + 60 - Minuten%60;}
			if (this.minuten==60) {
				minuten = 0;
				stunden = stunden + 1;
			}
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
		else if (Zeit.getMinuten()>this.minuten) return true;
		
		return false;
	}
	
	public boolean isLaterThan(Time Zeit) {
		if (Zeit.getStunden()<this.stunden) return true;
		else if(Zeit.getStunden()>this.stunden) return false;
		
		if (Zeit.getMinuten()<this.minuten) return true;
		else if(Zeit.getMinuten()>this.minuten) return false;
		
		return false;
	}
	
	public int getStunden() {
		return stunden;
	}

	public int getMinuten() {
		return minuten;
	}
	
	public String toString() {
		return ""+this.stunden+":"+this.minuten+":"+this.sekunden;
	}

	@Override
	public int compareTo(Time Zeit) {
		if (this.isLaterThan(Zeit)) return 1;
		if (this.isEarlierThan(Zeit)) return -1;
		return 0;
	}
}
