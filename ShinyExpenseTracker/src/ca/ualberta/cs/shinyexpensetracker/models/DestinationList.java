package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DestinationList extends Model<DestinationList> {
	private ArrayList<Destination> destinationList = new ArrayList<Destination>();
	
	public DestinationList(){
		destinationList = new ArrayList<Destination>();
	}
	
	public ArrayList<Destination> getDestinations() {
		return destinationList;
	}
	
	
	public boolean addDestination(Destination destination) {
		String destinationName = destination.getName();
		String destReason = destination.getReasonForTravel();
		if(destinationList.contains(destination)){
			return false;
		}
		if (destinationName == null || destinationName.equals("")){
			return false;
		}
		if (destReason == null || destReason.equals("")){
			return false;
		}
		Pattern p = Pattern.compile("^\\w*$");
		Matcher n = p.matcher(destinationName);
		Matcher r = p.matcher(destReason);
		if (n.matches() && r.matches()) {
			destinationList.add(destination);
			notifyViews();
		return true;
		}
		else{
			return false;
		}
	}
	
	public void removeDestination(Destination d) {
		destinationList.remove(d);
	}

	public void removeDestination(String n, String r) {
		destinationList.remove(new Destination(n, r));
	}
	
	public int size(){
		return destinationList.size();
	}

	public int getCount() {
		return destinationList.size();
	}

	public Destination getDestinationById(int i) {
		return destinationList.get(i);
	}
	
	public boolean contains(Destination destination){
		return destinationList.contains(destination);
	}
}
