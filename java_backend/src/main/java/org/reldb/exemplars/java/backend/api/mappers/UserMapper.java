package org.reldb.exemplars.java.backend.api.mappers;

import org.reldb.exemplars.java.backend.api.model.UserOut;
import org.reldb.exemplars.java.backend.model.main.User;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    List<UserOut> userToUserOut(List<User> users);
    UserOut userToUserOut(User user);
}
