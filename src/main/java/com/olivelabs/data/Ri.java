package com.olivelabs.data;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Ri {
	
	public Long hashCode;
	public BigInteger requestCount;
	public static BigInteger totalRequestCount;
	protected Long averageJobServiceTime;
	
	
	public BigDecimal getProbability(){
		return new BigDecimal(requestCount).divide(new BigDecimal(totalRequestCount));
	}
	
	public void setAverageJobServiceTime(Long milliSeconds){
		averageJobServiceTime = totalRequestCount.subtract(BigInteger.ONE)
									.multiply(new BigInteger(averageJobServiceTime.toString()))
									.add(new BigInteger(milliSeconds.toString()))
									.divide(totalRequestCount)
									.longValue();
	}
	
	public Long getAverageJobServiceTime(){
		return averageJobServiceTime;
	}
	public Priority getPriority(){
		Double classifier = getProbability()
								.divide(new BigDecimal(averageJobServiceTime.intValue()))
								.doubleValue();
		
		if(classifier < 0.33d){
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

