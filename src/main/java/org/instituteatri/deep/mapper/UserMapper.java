package org.instituteatri.deep.mappings;

import org.instituteatri.deep.model.user.User;
import org.instituteatri.deep.dtos.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDTO toUserDto(User user);
}
