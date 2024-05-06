package org.instituteatri.deep.mapper;

import org.instituteatri.deep.model.user.User;
import org.instituteatri.deep.dto.response.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserResponseDTO toUserDto(User user);
}
