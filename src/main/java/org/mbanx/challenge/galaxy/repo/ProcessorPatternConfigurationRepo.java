package org.mbanx.challenge.galaxy.repo;

import java.util.Set;

import org.mbanx.challenge.galaxy.model.ProcessorPatternConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProcessorPatternConfigurationRepo extends JpaRepository<ProcessorPatternConfiguration, Long> {

	@Query("SELECT DISTINCT c FROM ProcessorPatternConfiguration c "
			+ "WHERE (COALESCE(:ids) is null or c.id IN (:ids)) "
			+ "AND (COALESCE(:processorNames) is null or c.processorName IN (:processorNames)) "
			+ "AND (:enable is null or c.enable = :enable)")
	public Page<ProcessorPatternConfiguration> findByParameterEquals(
			Set<Long> ids,
			Set<String> processorNames,
			Boolean enable,
			Pageable pageable);

	public ProcessorPatternConfiguration findByProcessorNameAndPattern(String processorName, String pattern);
}
