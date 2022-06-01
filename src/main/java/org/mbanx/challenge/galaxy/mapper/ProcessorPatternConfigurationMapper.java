package org.mbanx.challenge.galaxy.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mbanx.challenge.galaxy.dto.ProcessorPatternConfigurationDto;
import org.mbanx.challenge.galaxy.model.ProcessorPatternConfiguration;

@Mapper(componentModel = "spring")
public interface ProcessorPatternConfigurationMapper {

	@Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public void updateDtoToEntity(ProcessorPatternConfigurationDto dto, @MappingTarget ProcessorPatternConfiguration entity);
	
}
