package api.v2.travel_social_network_server.responsitories;

import api.v2.travel_social_network_server.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUserName(String username);

    Optional<User> findByUserId(UUID id);
    //
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

//    @Query("SELECT u FROM User u JOIN u.userProfile up WHERE " +
//            "LOWER(u.userName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(CONCAT(up.firstName, ' ', up.lastName)) LIKE LOWER(CONCAT('%', :keyword, '%'))")
//    Page<User> findByUserNameOrFullNameContainingIgnoreCase(String keyword, Pageable pageable);
}