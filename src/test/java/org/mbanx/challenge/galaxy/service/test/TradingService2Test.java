package org.mbanx.challenge.galaxy.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mbanx.challenge.galaxy.model.QueryOutput;
import org.mbanx.challenge.galaxy.service.TradingService2;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TradingService2Test {

	@Ignore
	@Test
	public void test() {
		TradingService2 service = new TradingService2();
		String text = "glob is I";
		QueryOutput qo = service.galaxyToRomanProcessor(text);
		assertTrue(qo.isValid());
	}

	@Ignore
	@Test
	public void test2() {
		TradingService2 service = new TradingService2();
		QueryOutput qo1 = service.galaxyToRomanProcessor("glob is I");
		QueryOutput qo2 = service.galaxyToRomanProcessor("prok is V");
		QueryOutput qo3 = service.galaxyToRomanProcessor("pish is X");
		QueryOutput qo4 = service.galaxyToRomanProcessor("tegj is L");
		QueryOutput qo5 = service.galaxyUnitToNumberProcessor("glob glob Silver is 34 Credits");
		QueryOutput qo6 = service.galaxyUnitToNumberProcessor("glob prok Gold is 57800 Credits");
		QueryOutput qo7 = service.galaxyUnitToNumberProcessor("pish pish Iron is 3910 Credits");
		QueryOutput qo8 = service.galaxyNumberCalculationProcessor("how much is pish tegj glob glob ?");
		QueryOutput qo9 = service.galaxyCreditsCalculationProcessor("how many Credits is prok glob Silver ?");
		QueryOutput qo10 = service.galaxyCreditsCalculationProcessor("how many Credits is glob prok Silver ?");
		QueryOutput qo11 = service.galaxyCreditsCalculationProcessor("how many Credits is glob prok Gold ?");
		QueryOutput qo12 = service.galaxyCreditsCalculationProcessor("how many Credits is glob prok Iron ?");
		QueryOutput qo13 = service.galaxyCreditsCalculationProcessor("how much wood could a woodchuck chuck if a woodchuck could chuck wood ?");

		assertEquals("pish tegj glob glob is 42", qo8.getOutput());
		assertEquals("prok glob Silver is 102", qo9.getOutput());
		assertEquals("glob prok Silver is 68", qo10.getOutput());
		assertEquals("glob prok Gold is 57800", qo11.getOutput());
		assertEquals("glob prok Iron is 782", qo12.getOutput());
		assertEquals("I have no idea what you are talking about", qo13.getOutput());
	}

	@Ignore
	@Test
	public void test3() {
		TradingService2 service = new TradingService2();
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
		expectBuilder.append("glob prok Silver is 68").append(System.lineSeparator());
		expectBuilder.append("glob prok Gold is 57800").append(System.lineSeparator());
		expectBuilder.append("glob prok Iron is 782").append(System.lineSeparator());
		expectBuilder.append("I have no idea what you are talking about").append(System.lineSeparator());
		String expect = expectBuilder.toString();

		assertEquals(expect, output);
	}

	
	@Test
	public void printTest() {
		TradingService2 service = new TradingService2();
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
		builder.append("how much wood could a woodchuck chuck if a woodchuck could chuck wood ?").append(System.lineSeparator());

		String output = service.processTrading(builder.toString());
		System.out.println(output);
	}
}
