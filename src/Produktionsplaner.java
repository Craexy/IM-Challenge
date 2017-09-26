import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Produktionsplaner {
	
	private LinkedList <Fahrzeug> fahrzeuge;
	private int bedarfe[] = new int[4];
	private Time zeitpunkte[] = new Time[4];
	private int produktionsdauerGesamt = 0;
	private int produktionsdauer[]=new int[4];

	
	
	public Produktionsplaner(LinkedList<Fahrzeug> fahrzeuge){
		this.fahrzeuge = fahrzeuge;
		planeProduktion();
		
	}
	
	public void planeProduktion(){
		getBedarfeUndZeit();
		getProduktionsdauer();
		getStartzeitProduktion();
	}
	
	public void getBedarfeUndZeit(){
		
		for(int i=0;i<fahrzeuge.size();i++){
			bedarfe[0] = fahrzeuge.get(i).get60();
			bedarfe[1] = fahrzeuge.get(i).get120();
			bedarfe[2] = fahrzeuge.get(i).get250();
			bedarfe[3] = fahrzeuge.get(i).get500();
			
			zeitpunkte[0] = fahrzeuge.get(i).getStartzeitBeladung60();
			zeitpunkte[1] = fahrzeuge.get(i).getStartzeitBeladung120();
			zeitpunkte[2] = fahrzeuge.get(i).getStartzeitBeladung250();
			zeitpunkte[3] = fahrzeuge.get(i).getStartzeitBeladung500();
			
		}
		
	}
	
	public void getProduktionsdauer(){
		int dauer60 = (int)Math.ceil((double)bedarfe[0]/150)*15;
		int dauer120 = (int)Math.ceil((double)bedarfe[1]/150)*30;
		int dauer250 = (int)Math.ceil((double)bedarfe[2]/150)*60;
		int dauer500 = (int)Math.ceil((double)bedarfe[3]/150)*120;
		
		produktionsdauer[0] = dauer60;
		produktionsdauer[1] = dauer120;
		produktionsdauer[2] = dauer250;
		produktionsdauer[3] = dauer500;
		
		for(int i=0;i<produktionsdauer.length;i++){
			if(produktionsdauer[i]>produktionsdauerGesamt){
				produktionsdauerGesamt = produktionsdauer[i];
			}
		}
		System.out.println("Die Gesamtproduktionsdauer beträgt "+produktionsdauerGesamt+" Minuten");
	}
	
	public LinkedList getStartzeitProduktion(){
		Time spMed60 = new Time(zeitpunkte[0].getStunden(), zeitpunkte[0].getMinuten(),0).reduceTime(produktionsdauer[0]);
		Time spMed120 = new Time(zeitpunkte[1].getStunden(), zeitpunkte[1].getMinuten(),0).reduceTime(produktionsdauer[1]);
		Time spMed250 = new Time(zeitpunkte[2].getStunden(), zeitpunkte[2].getMinuten(),0).reduceTime(produktionsdauer[2]);
		Time spMed500 = new Time(zeitpunkte[3].getStunden(), zeitpunkte[3].getMinuten(),0).reduceTime(produktionsdauer[3]);
	
		LinkedList <Time> startzeitenProduktion = new LinkedList<Time>();
		startzeitenProduktion.add(spMed60);
		System.out.println("Die Startzeit der Produktion von Med60: "+spMed60.toString());
		startzeitenProduktion.add(spMed120);
		System.out.println("Die Startzeit der Produktion von Med120: "+spMed120.toString());
		startzeitenProduktion.add(spMed250);
		System.out.println("Die Startzeit der Produktion von Med250: "+spMed250.toString());
		startzeitenProduktion.add(spMed500);
		System.out.println("Die Startzeit der Produktion von Med500: "+spMed500.toString());
		
		return startzeitenProduktion;
	}
	
}
