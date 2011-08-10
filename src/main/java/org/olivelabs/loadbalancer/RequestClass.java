package org.olivelabs.loadbalancer;


public class RequestClass {
	public String URL;
	public Double requestProbability =  0D;
	public Double averageServiceTime = Double.MAX_VALUE;
	public static Long TOTAL_REQUEST_RECEIVED = 0L;
	public Long SUM_THIS_REQUEST = 0L;
	
	
	public RequestClass(String url){
		URL = url;
	}
	
	public void increaseRequestCount(){
		TOTAL_REQUEST_RECEIVED++;
		SUM_THIS_REQUEST++;
		updateProbability();
	}
	
	public void updateAverageServiceTime(Long newServiceTime){
		if(SUM_THIS_REQUEST==0L) return;
		if(averageServiceTime == Double.MAX_VALUE || SUM_THIS_REQUEST == 1L) averageServiceTime = newServiceTime/1.0;
		else{
			averageServiceTime = (
						(
							(averageServiceTime * (SUM_THIS_REQUEST-1)) 
							+ 
							newServiceTime) 
						/ 
						(SUM_THIS_REQUEST * 1.0)
					);
		}
		
	}
	
	private void updateProbability(){
		requestProbability = (double) SUM_THIS_REQUEST/ TOTAL_REQUEST_RECEIVED;
	}
	
	public Double getClassLevel(){
		if(requestProbability==0D)
			return Double.MAX_VALUE;
		else{
			requestProbability =  (new Double(SUM_THIS_REQUEST))/(TOTAL_REQUEST_RECEIVED);
			return  (averageServiceTime/requestProbability) * 1000;
		}
	}
}
