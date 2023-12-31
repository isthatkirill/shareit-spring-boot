package isthatkirill.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import isthatkirill.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequesterIdNotOrderByCreatedDesc(Long userId, Pageable pageable);

    List<ItemRequest> findAllByRequesterIdOrderByCreatedDesc(Long userId);
}
