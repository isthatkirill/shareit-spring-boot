package isthatkirill.shareit.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import isthatkirill.shareit.item.model.Item;
import isthatkirill.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "item_requests")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "description", nullable = false)
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    User requester;

    @Builder.Default
    @Column(name = "created", nullable = false)
    LocalDateTime created = LocalDateTime.now();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "requestId")
    List<Item> items = new ArrayList<>();

}
