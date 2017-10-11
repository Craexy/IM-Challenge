
public class Time implements Comparable<Time>{
	
	private int stunden;
	private int minuten;
	private int tage;
	
	public Time(int Stunden, int Minuten, int Tage) {
		stunden = Stunden;
		minuten = Minuten;
		tage = Tage;
	}
	
	public Time getNewInstance() {
		return new Time(stunden, minuten, tage);
	}
	
	public Time reduceTime(int Minuten) throws IllegalArgumentException{
		/*if (Minuten<0) throw new IllegalArgumentException();
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
		//falls Zeit am Vortag liegt --> nur für ein Tag früher möglich
		if (this.stunden<0) {
			tage = tage-1;
			stunden = 24 + stunden;
		}
		return new Time(stunden, minuten, tage);*/
		if (Minuten<0) throw new IllegalArgumentException();
		if (this.minuten>=Minuten) this.minuten = this.minuten - Minuten; 
		else {
			if (Minuten>this.minuten) this.stunden = this.stunden -1;
			int tempStunden = Minuten/60;
			this.stunden = this.stunden - tempStunden;
			int tempMinuten = Minuten-60*tempStunden;
			if (tempMinuten<=this.minuten) this.minuten = this.minuten - tempMinuten;
			else this.minuten = 60 - (tempMinuten-this.minuten);
		}
		//falls Zeit am Vortag liegt --> nur für ein Tag früher möglich
				if (this.stunden<0) {
					tage = tage-1;
					stunden = 24 + stunden;
				}
		return new Time(this.stunden, this.minuten, this.tage);
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
		if(Zeit.getTage()<this.tage) return false;
		else if(Zeit.getTage()>this.tage) return true;
		
		if (Zeit.getStunden()<this.stunden) return false;
		else if (Zeit.getStunden()>this.stunden) return true;
		
		if (Zeit.getMinuten()<this.minuten) return false;
		else if (Zeit.getMinuten()>this.minuten) return true;
		
		return false;
	}
	
	public boolean isLaterThan(Time Zeit) {
		if (Zeit.getTage()<this.tage) return true;
		else if(Zeit.getTage()>this.tage) return false;
		
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
	
	public int getTage() {
		return tage;
	}
	
	public String toString() {
		if (this.tage<0) return ""+this.stunden+":"+this.minuten+" --> "+this.tage+" Tage vorher.";
		else return ""+this.stunden+":"+this.minuten;
	}

	@Override
	public int compareTo(Time Zeit) {
		if (this.isLaterThan(Zeit)) return 1;
		if (this.isEarlierThan(Zeit)) return -1;
		return 0;
	}
}
