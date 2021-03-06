package org.mbanx.challenge.galaxy.service.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mbanx.challenge.galaxy.model.QueryOutput;
import org.mbanx.challenge.galaxy.processor.GalaxyCreditCalculationProcessor;
import org.mbanx.challenge.galaxy.processor.GalaxyCreditComparisonProcessor;
import org.mbanx.challenge.galaxy.processor.GalaxyNumberCalculationProcessor;
import org.mbanx.challenge.galaxy.processor.GalaxyNumberComparisonProcessor;
import org.mbanx.challenge.galaxy.processor.GalaxyToRomanProcessor;
import org.mbanx.challenge.galaxy.processor.GalaxyUnitToNumberProcessor;
import org.mbanx.challenge.galaxy.processor.TextProcessor;
import org.mbanx.challenge.galaxy.service.TradingService;
import org.mbanx.challenge.galaxy.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TradingServiceTest {
	
	@Autowired
	private TradingService service;
	
	@Test
	public void test() {
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
		TextProcessor p1 = new GalaxyToRomanProcessor("([a-zA-Z]+)\\s+(?i)(is)\\s+([I,V,X,L,C,D,M]+)", converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
		TextProcessor p2 = new GalaxyUnitToNumberProcessor("(\\D+)(?i)(is)\\s+([0-9|.]+)\\s+(?i)(Credits)", converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
		TextProcessor p3 = new GalaxyNumberCalculationProcessor("(?i)(how\\s+much\\s+is\\s+)([\\w\\s]+)(\\?)", converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
		TextProcessor p4 = new GalaxyCreditCalculationProcessor("(?i)(how\\s+many\\s+Credits\\s+is\\s+)([\\w\\s]+)(\\?)", converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
		TextProcessor p5 = new GalaxyCreditComparisonProcessor("(?i)does\\s+([\\w\\s]+)(has|have)\\s+(more|less)\\s+credits\\s+than\\s+([\\w\\s]+)\\?", converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
		TextProcessor p6 = new GalaxyNumberComparisonProcessor("(?i)is\\s+([\\w\\s]+)\\s+larger\\s+than\\s+([\\w\\s]+)\\?", converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);

		QueryOutput qo1 = p1.process("glob is I");
		QueryOutput qo2 = p1.process("prok is V");
		QueryOutput qo3 = p1.process("pish is X");
		QueryOutput qo4 = p1.process("tegj is L");
		QueryOutput qo5 = p2.process("glob glob Silver is 34 Credits");
		QueryOutput qo6 = p2.process("glob prok Gold is 57800 Credits");
		QueryOutput qo7 = p2.process("pish pish Iron is 3910 Credits");
		QueryOutput qo8 = p3.process("how much is pish tegj glob glob ?");
		QueryOutput qo9 = p4.process("how many Credits is prok glob Silver ?");
		QueryOutput qo10 = p4.process("how many Credits is glob prok Silver ?");
		QueryOutput qo11 = p4.process("how many Credits is glob prok Gold ?");
		QueryOutput qo12 = p4.process("how many Credits is glob prok Iron ?");
		QueryOutput qo13 = p4.process("how much wood could a woodchuck chuck if a woodchuck could chuck wood ?");

		assertEquals("pish tegj glob glob is 42", qo8.getOutput());
		assertEquals("prok glob Silver is 102 Credits", qo9.getOutput());
		assertEquals("glob prok Silver is 68 Credits", qo10.getOutput());
		assertEquals("glob prok Gold is 57800 Credits", qo11.getOutput());
		assertEquals("glob prok Iron is 782 Credits", qo12.getOutput());
		assertEquals("I have no idea what you are talking about", qo13.getOutput());
	}

	@Test
	public void test2() {
		StringBuilder builder = new StringBuilder();
		builder.append("glob is I").append(System.lineSeparator());
		builder.append("prok is V").append(System.lineSeparator());
		builder.append("pish is X").append(System.lineSeparator());
		builder.append("tegj is L").append(System.lineSeparator());
		builder.append("glob glob Silver is 34 Credits").append(System.lineSeparator());
		builder.append("glob prok Gold is 57800 Credits").append(System.lineSeparator());
		builder.append("pish pish Iron is 3910 Credits").append(System.lineSeparator());
		builder.append("how much is pish tegj glob glob ?").append(System.lineSeparator());
		builder.append("how many Credits is glob prok Silver ?").append(System.lineSeparator());
		builder.append("how many Credits is glob prok Gold ?").append(System.lineSeparator());
		builder.append("how many Credits is glob prok Iron ?").append(System.lineSeparator());
		builder.append("how much wood could a woodchuck chuck if a woodchuck could chuck wood ?").append(System.lineSeparator());

		String output = service.processTrading(builder.toString());

		StringBuilder expectBuilder = new StringBuilder();
		expectBuilder.append("pish tegj glob glob is 42").append(System.lineSeparator());
		expectBuilder.append("glob prok Silver is 68 Credits").append(System.lineSeparator());
		expectBuilder.append("glob prok Gold is 57800 Credits").append(System.lineSeparator());
		expectBuilder.append("glob prok Iron is 782 Credits").append(System.lineSeparator());
		expectBuilder.append("I have no idea what you are talking about").append(System.lineSeparator());
		String expect = expectBuilder.toString();

		assertEquals(expect, output);
	}

	@Test
	public void test3() {
		StringBuilder builder = new StringBuilder();
		builder.append("glob is I").append(System.lineSeparator());
		builder.append("prok is V").append(System.lineSeparator());
		builder.append("pish is X").append(System.lineSeparator());
		builder.append("tegj is L").append(System.lineSeparator());

		builder.append("glob     glob     Silver     is 34 Credits      ").append(System.lineSeparator());
		builder.append("glob prok Gold is     57800 Credits   ").append(System.lineSeparator());
		builder.append("    pish pish Iron is 3910 Credits").append(System.lineSeparator());

		builder.append("how  much is pish     Tegj gLob glob ?    ").append(System.lineSeparator());

		builder.append("   how many Credits is Glob prok Silver ?").append(System.lineSeparator());
		builder.append("how many Credits is gloB  glob  Gold ?").append(System.lineSeparator());
		builder.append("how     manY Credits is glob glob glob glob glob glob gold ?").append(System.lineSeparator());
		builder.append("how many Credits is pish tegj glob Iron ?").append(System.lineSeparator());

		builder.append("Does pish tegj glob glob Iron has more Credits than glob glob Gold ?").append(System.lineSeparator());
		builder.append("Is glob prok larger than pish pish?").append(System.lineSeparator());
		builder.append("Is glob prok smaller than pish pish?").append(System.lineSeparator());
		builder.append("Is pish pish smaller than glob prok?").append(System.lineSeparator());
		builder.append("how much wood could a woodchuck chuck if a woodchuck could chuck wood ?").append(System.lineSeparator());

		String output = service.processTrading(builder.toString());
		log.info(output);
	}
}
