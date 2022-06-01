package org.mbanx.challenge.galaxy.dto;

import org.mbanx.challenge.galaxy.model.ProcessorPatternConfiguration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(alphabetic = true)
public class ProcessorPatternConfigurationDto extends ProcessorPatternConfiguration {
	
}
