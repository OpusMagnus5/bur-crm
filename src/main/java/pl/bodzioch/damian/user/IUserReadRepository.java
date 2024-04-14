package pl.bodzioch.damian.user;

import java.util.Optional;

interface IUserReadRepository {

    Optional<User> getByEmail(String email);
}
