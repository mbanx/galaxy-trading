package org.mbanx.challenge.galaxy.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mbanx.challenge.galaxy.dto.ProcessorPatternConfigurationDto;
import org.mbanx.challenge.galaxy.mapper.ProcessorPatternConfigurationMapper;
import org.mbanx.challenge.galaxy.model.ProcessorPatternConfiguration;
import org.mbanx.challenge.galaxy.repo.ProcessorPatternConfigurationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Service
public class ProcessorConfigService {

	@Autowired
	private ProcessorPatternConfigurationRepo configRepo;

	@Autowired
	private ProcessorPatternConfigurationMapper configMapper;

	public ProcessorPatternConfiguration createConfig(ProcessorPatternConfigurationDto dto) {
		ProcessorPatternConfiguration config = new ProcessorPatternConfiguration();
		configMapper.updateDtoToEntity(dto, config);
		config = configRepo.save(config);
		return config;
	}

	public ProcessorPatternConfiguration updateConfig(long id, ProcessorPatternConfigurationDto dto) {
		ProcessorPatternConfiguration config = getConfigById(id);
		if(config != null) {
			configMapper.updateDtoToEntity(dto, config);
			config = configRepo.save(config);
		}
		return config;
	}

	public ProcessorPatternConfiguration deleteConfig(long id) {
		ProcessorPatternConfiguration config = getConfigById(id);
		if(config != null) {
			configRepo.deleteById(id);
		}
		return config;
	}

	public Page<ProcessorPatternConfiguration> getConfig(Set<Long> ids, Set<String> processorNames, Boolean enable, Pageable pageable) {
		return configRepo.findByParameterEquals(ids, processorNames, enable, pageable);
	}

	public ProcessorPatternConfiguration getConfigById(long id) {
		ProcessorPatternConfiguration config = null;
		Set<Long> ids = new HashSet<>();
		ids.add(id);
		List<ProcessorPatternConfiguration> configs = configRepo.findByParameterEquals(ids, null, null, Pageable.unpaged()).getContent();
		if(!configs.isEmpty()) {
			config = configs.get(0);
		}
		return config;
	}
}
