package com.qthegamep.pattern.project2.mapper;

import com.qthegamep.pattern.project2.dto.ErrorResponseDTO;
import com.qthegamep.pattern.project2.entity.Error;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ErrorMapper {

    ErrorMapper INSTANCE = Mappers.getMapper(ErrorMapper.class);

    @Mappings({
            @Mapping(source = "errorCode", target = "errorCode"),
            @Mapping(source = "errorMessage", target = "errorMessage")
    })
    ErrorResponseDTO errorToErrorResponseDTO(Error error);

    @Mappings({
            @Mapping(source = "errorCode", target = "errorCode"),
            @Mapping(source = "errorMessage", target = "errorMessage"),
            @Mapping(target = "objectId", ignore = true),
            @Mapping(target = "requestId", ignore = true)
    })
    Error errorResponseDTOToError(ErrorResponseDTO errorResponseDTO);
}
