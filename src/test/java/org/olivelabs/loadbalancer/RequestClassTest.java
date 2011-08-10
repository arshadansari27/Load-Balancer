package org.olivelabs.loadbalancer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RequestClassTest {

	RequestClass[] requestClass;
	
	@Before
	public void setUp(){
		
	}
	
	
	@Test
	public void testIncreaseRequestCount() {
		requestClass = new RequestClass[10];
		for(int i = 0; i < 10; i++){
			requestClass[i] = new RequestClass("/path"+i);
		}
		for(RequestClass reqClass : requestClass){
			reqClass.increaseRequestCount();
			System.out.println(reqClass.SUM_THIS_REQUEST);
			System.out.println(reqClass.TOTAL_REQUEST_RECEIVED);
			System.out.println(reqClass.requestProbability);
		}
	}

	@Test
	public void testUpdateAverageServiceTime() {
		RequestClass.TOTAL_REQUEST_RECEIVED = 0L;
		requestClass = new RequestClass[10];
		for(int i = 0; i < 10; i++){
			requestClass[i] = new RequestClass("/path"+i);
		}
		for(RequestClass reqClass : requestClass){
			reqClass.increaseRequestCount();
			reqClass.updateAverageServiceTime((long)(100 * Math.random()));
			System.out.println(reqClass.SUM_THIS_REQUEST);
			System.out.println(reqClass.TOTAL_REQUEST_RECEIVED);
			System.out.println(reqClass.averageServiceTime);
		}
		
	}

	@Test
	public void testGetClassLevel() {
		RequestClass.TOTAL_REQUEST_RECEIVED = 0L;
		requestClass = new RequestClass[10];
		for(int i = 0; i < 10; i++){
			requestClass[i] = new RequestClass("/path"+i);
		}
		for(RequestClass reqClass : requestClass){
			reqClass.increaseRequestCount();
			reqClass.updateAverageServiceTime((long)(100 * Math.random()));
		}
		for(RequestClass reqClass : requestClass){
			System.out.println(reqClass.getClassLevel());
		}
	}

}
