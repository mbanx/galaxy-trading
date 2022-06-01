package org.mbanx.challenge.galaxy.controller;

import org.mbanx.challenge.galaxy.dto.ProcessorPatternConfigurationDto;
import org.mbanx.challenge.galaxy.model.ProcessorPatternConfiguration;
import org.mbanx.challenge.galaxy.request.ProcessorPatternConfigurationRequest;
import org.mbanx.challenge.galaxy.service.ProcessorConfigService;
import org.mbanx.challenge.galaxy.util.RepoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/processor-config")
public class ProcessorConfigController {
	
	@Autowired
	private ProcessorConfigService service;
	
	@PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProcessorPatternConfiguration createConfig(@RequestBody ProcessorPatternConfigurationDto dto) {
		return service.createConfig(dto);
	}

	@PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProcessorPatternConfiguration updateConfig(@PathVariable long id, @RequestBody ProcessorPatternConfigurationDto dto) {
		ProcessorPatternConfiguration config = service.updateConfig(id, dto);
		if(config == null) {
			throw new ResourceNotFoundException("ProcessorPatternConfiguration with ID '"+id+"' doesn't exist");
		}
		return service.updateConfig(id, dto);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProcessorPatternConfiguration deleteConfig(@PathVariable long id) {
		ProcessorPatternConfiguration config = service.deleteConfig(id);
		if(config == null) {
			throw new ResourceNotFoundException("ProcessorPatternConfiguration with ID '"+id+"' doesn't exist");
		}
		return config;
	}

	@PostMapping(value = "/pagination", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<ProcessorPatternConfiguration> getConfig(@RequestBody ProcessorPatternConfigurationRequest request) {
		Pageable pageable = RepoUtil.generatePageable(request.getPageNumber(), request.getPageSize(),
				request.getSortFields(), request.getSortOrders());
		return service.getConfig(request.getIds(), request.getProcessorNames(), request.getEnable(), pageable);
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProcessorPatternConfiguration getConfigById(@PathVariable long id) {
		ProcessorPatternConfiguration config = service.getConfigById(id);
		if(config == null) {
			throw new ResourceNotFoundException("ProcessorPatternConfiguration with ID '"+id+"' doesn't exist");
		}
		return config;
	}
}
