package org.mbanx.challenge.galaxy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.mbanx.challenge.galaxy.model.ProcessorPatternConfiguration;
import org.mbanx.challenge.galaxy.model.QueryOutput;
import org.mbanx.challenge.galaxy.processor.TextProcessor;
import org.mbanx.challenge.galaxy.processor.factory.TextProcessorFactory;
import org.mbanx.challenge.galaxy.repo.ProcessorPatternConfigurationRepo;
import org.mbanx.challenge.galaxy.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Service
public class TradingService {

	@Autowired
	private ProcessorPatternConfigurationRepo configRepo;
	
	public String processTrading(String text) {

		//create new map for each processing
		Map<Character,Integer> romanToNumbeMap = new HashMap<>();
		Map<String, String> galaxyToRomanMap = new HashMap<>();
		Map<String, Integer> galaxyToNumberMap = new HashMap<>();
		Map<String, Double> galaxyUnitToNumberMap = new HashMap<>();

		//initial value
		romanToNumbeMap.put('I',1);
		romanToNumbeMap.put('V',5);
		romanToNumbeMap.put('X',10);
		romanToNumbeMap.put('L',50);
		romanToNumbeMap.put('C',100);   
		romanToNumbeMap.put('D',500);   
		romanToNumbeMap.put('M',1000); 

		Converter converter = new Converter(romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
		TextProcessorFactory factory = new TextProcessorFactory(converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
		List<TextProcessor> processors = new ArrayList<>();
		
		//Instantiate Processor using factory design pattern
		List<ProcessorPatternConfiguration> configs = configRepo.findByParameterEquals(null, null, true, Pageable.unpaged()).getContent();
		for(ProcessorPatternConfiguration config: configs) {
			TextProcessor p = factory.getTextProcessor(config.getProcessorName(), config.getPattern());
			if(p != null) {
				processors.add(p);
			}
		}
		
		StringBuilder builder = new StringBuilder();
		String[] multiLineText = text.split("\\r?\\n");
		for(String line: multiLineText) {
			String output = this.processLine(line, processors);

			if(StringUtils.isNotBlank(output)) {
				builder.append(output).append(System.lineSeparator());
			}
		}
		return builder.toString();
	}

	public String processLine(String text, List<TextProcessor> processors) {
		String output = "";
		
		// Process and evaluate line for each processor
		for (TextProcessor processor : processors) {
			QueryOutput qo = processor.process(text);
			output = qo.getOutput();
			if(qo.isValid()) {
				break;
			}
		}
		
		return output;
	}
}
