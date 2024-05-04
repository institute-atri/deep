package org.instituteatri.deep.mappings.implconfigs;

import org.instituteatri.deep.domain.user.User;
import org.instituteatri.deep.dtos.user.UserDTO;
import org.instituteatri.deep.mappings.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toUserDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail());
    }
}
