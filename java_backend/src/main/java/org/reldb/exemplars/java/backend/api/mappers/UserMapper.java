package org.reldb.exemplars.java.backend.api.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.reldb.exemplars.java.backend.api.model.UserOut;
import org.reldb.exemplars.java.backend.model.user.User;

@Mapper
public interface UserMapper {
    List<UserOut> userToUserOut(List<User> users);
    UserOut userToUserOut(User user);
}
