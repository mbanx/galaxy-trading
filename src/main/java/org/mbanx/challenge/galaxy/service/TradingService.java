package org.mbanx.challenge.galaxy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.mbanx.challenge.galaxy.model.QueryOutput;
import org.mbanx.challenge.galaxy.processor.GalaxyCreditCalculationProcessor;
import org.mbanx.challenge.galaxy.processor.GalaxyCreditComparisonProcessor;
import org.mbanx.challenge.galaxy.processor.GalaxyNumberCalculationProcessor;
import org.mbanx.challenge.galaxy.processor.GalaxyNumberComparisonProcessor;
import org.mbanx.challenge.galaxy.processor.GalaxyToRomanProcessor;
import org.mbanx.challenge.galaxy.processor.GalaxyUnitToNumberProcessor;
import org.mbanx.challenge.galaxy.processor.TextProcessor;
import org.mbanx.challenge.galaxy.util.Converter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class TradingService {

	private Map<Character,Integer> romanToNumbeMap;
	private Map<String, String> galaxyToRomanMap;
	private Map<String, Integer> galaxyToNumberMap;
	private Map<String, Double> galaxyUnitToNumberMap;

	public TradingService() {
		this.init();
	}

	public void init() {
		romanToNumbeMap = new HashMap<>();
		galaxyToRomanMap = new HashMap<>();
		galaxyToNumberMap = new HashMap<>();
		galaxyUnitToNumberMap = new HashMap<>();

		//initial value
		romanToNumbeMap.put('I',1);
		romanToNumbeMap.put('V',5);
		romanToNumbeMap.put('X',10);
		romanToNumbeMap.put('L',50);
		romanToNumbeMap.put('C',100);   
		romanToNumbeMap.put('D',500);   
		romanToNumbeMap.put('M',1000); 
		
	}

	public String processTrading(String text) {
		this.init();
		
		Converter converter = new Converter(romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
		
		GalaxyToRomanProcessor p1 = new GalaxyToRomanProcessor(converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
		GalaxyUnitToNumberProcessor p2 = new GalaxyUnitToNumberProcessor(converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
		GalaxyNumberCalculationProcessor p3 = new GalaxyNumberCalculationProcessor(converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
		GalaxyCreditCalculationProcessor p4 = new GalaxyCreditCalculationProcessor(converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
		GalaxyCreditComparisonProcessor p5 = new GalaxyCreditComparisonProcessor(converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
		GalaxyNumberComparisonProcessor p6 = new GalaxyNumberComparisonProcessor(converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
		
		List<TextProcessor> processors = new ArrayList<>();
		processors.add(p1);
		processors.add(p2);
		processors.add(p3);
		processors.add(p4);
		processors.add(p5);
		processors.add(p6);
		
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
