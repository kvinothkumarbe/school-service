
package com.cts.ms.school.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cts.ms.school.util.XMLutils;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * @author 430040
 *
 */
@Service
@EnableDiscoveryClient
public class StudentServiceDelegate {

	@Autowired
    RestTemplate restTemplate;
	
	 @Autowired
	 private LoadBalancerClient lba;
	 
	 @Autowired
	 private XMLutils xmlUtils;
	
	     
	    @HystrixCommand(fallbackMethod = "callStudentServiceAndGetData_Fallback")
	    public String callStudentServiceAndGetData(String schoolname) {
	 
	        System.out.println("Getting School details for " + schoolname);
	        String response=null;
	         response = restTemplate
	                .exchange("http://localhost:8082/getStudentDetailsForSchool/{schoolname}"
	                , HttpMethod.GET
	                , null
	                , new ParameterizedTypeReference<String>() {
	            }, schoolname).getBody();
	
	 
	        System.out.println("Response Received as " + response + " -  " + new Date());
	 
	        return "NORMAL FLOW !!! - School Name -  " + schoolname +  xmlUtils.gnerateXML(response) + " <br/>Date :  " + new Date();
	    }
	    
	    public String callStudentServiceAndGetDataWithLoadBalace(String schoolname) {
	 
	        System.out.println("Getting School details for " + schoolname);
	 
	        ServiceInstance servInstance= lba.choose("student-service");
	        System.out.println(servInstance.getUri());
	        
	        String baseUrl=servInstance.getUri().toString();
	        baseUrl= baseUrl + "getStudentDetailsForSchool/{schoolname}";
	 
	        String response = restTemplate
	                .exchange(baseUrl
	                , HttpMethod.GET
	                , null
	                , new ParameterizedTypeReference<String>() {
	            }, schoolname).getBody();
	 
	        System.out.println("Response Received as " + response + " -  " + new Date());
	        String hostPort[]=servInstance.getUri().toString().split(":");
	        return "NORMAL FLOW WITH LOAD BALANCING !!! - <br/>Host Name :"+ hostPort[1]+  "<br/>Port :"+ hostPort[2]+" <br/>School Name -  " + schoolname +  xmlUtils.gnerateXML(response) + " <br/>Date :  " + new Date();
	    }
	    
	    
	   
	     
	    @SuppressWarnings("unused")
	    private String callStudentServiceAndGetData_Fallback(String schoolname) {
	 
	        System.out.println("Student Service is down!!! fallback route enabled...");
	 
	        return "CIRCUIT BREAKER ENABLED!!! <br/>No Response From Student Service at this moment. " +
	                    " Service will be back shortly - " + new Date();
	    }
	    
	    @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }
	 
	}
