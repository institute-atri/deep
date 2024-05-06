package org.instituteatri.deep.mapper.implconfigs;

import org.instituteatri.deep.model.user.User;
import org.instituteatri.deep.dto.response.UserResponseDTO;
import org.instituteatri.deep.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponseDTO toUserDto(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail());
    }
}
