package org.mbanx.challenge.galaxy.request;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(alphabetic = true)
public class ProcessorPatternConfigurationRequest {
	
	private Set<Long> ids; 
	private Set<String> processorNames; 
	private Boolean enable;
	
	private Integer pageNumber;
	private Integer pageSize;
	private List<String> sortFields;
	private List<String> sortOrders;
}
