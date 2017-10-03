import java.util.LinkedList;

public class Produktionslinie {
	Produktionsplaner prodp;
	LinkedList <Time> belegtvon;
	LinkedList <Time> belegtbis;
	

	public Produktionslinie(Produktionsplaner prodp){
		this.prodp = prodp;
		
		belegtvon = new LinkedList<Time>();
		belegtbis = new LinkedList<Time>();
		
	}

	public boolean assign(LinkedList<Time> produktionsstart,LinkedList<Time> produktionsende, LinkedList<Integer> bedarfe){
		
		try{
			//belegtvon und belegtbis sortieren und dann prüfen ob produktionsstart earlierthan belegtbis einträge
			//produktionsstart
			
		}catch (NullPointerException e){
			
		}
		
		return true;
		
	}
}
