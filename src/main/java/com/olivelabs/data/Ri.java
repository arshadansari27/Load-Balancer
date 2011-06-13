package com.olivelabs.data;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Ri {
	
	public Long hashCode;
	public double requestCount = 0;
	public static Double totalRequestCount = 0d;
	protected Double averageJobServiceTime = Double.valueOf(1L);
	public static Double averageServiceTime = Double.valueOf(1D);
	
	public Double getProbability(){
		return requestCount/totalRequestCount;
	}
	
	public void setAverageJobServiceTime(Long milliSeconds){
		double totalRequest = totalRequestCount.doubleValue();
		averageJobServiceTime = (((totalRequest - 1) * averageJobServiceTime) +  ((1.0)*milliSeconds)/1000)/totalRequest;
		averageServiceTime = (((totalRequest - 1) * averageServiceTime) +  ((1.0)*milliSeconds)/1000)/totalRequest;
	}
	
	public Double getAverageJobServiceTime(){
		return averageJobServiceTime;
	}
	public Priority getPriority(){
		Double classifier = getProbability() / (averageJobServiceTime/averageServiceTime);
		
		if(classifier > 0.66d){
			return Priority.HIGH;
		}
		else if(classifier > 0.33d && classifier < 0.66d){
			return Priority.MEDIUM;
		}
		else{
			return Priority.LOW;
		}
	}
}

