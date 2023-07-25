package isthatkirill.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import isthatkirill.shareit.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


}
