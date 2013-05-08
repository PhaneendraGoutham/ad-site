package pl.stalkon.ad.core;

import java.util.Calendar;
import java.util.Date;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	      long lDateTime = new Date().getTime();
	      System.out.println("Date() - Time in milliseconds: " + lDateTime);
	 
	      Calendar lCDateTime = Calendar.getInstance();
	      System.out.println("Calender - Time in milliseconds :" + lCDateTime.getTimeInMillis());

	}

}
