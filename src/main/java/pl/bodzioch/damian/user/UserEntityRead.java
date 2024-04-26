package pl.bodzioch.damian.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SourceType;
import org.hibernate.generator.EventType;
import pl.bodzioch.damian.utils.GeneratedUuidValue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

@Entity(name = "users_read")
@Table(name = "users")
@OptimisticLocking
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UserEntityRead {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_seq")
    private Long id;

    @GeneratedUuidValue(types = EventType.INSERT)
    private UUID uuid;

    @Version
    private Integer version;

    @NaturalId
    private String email;

    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String roles;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @CurrentTimestamp(event = EventType.INSERT, source = SourceType.VM)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @CurrentTimestamp(event = EventType.UPDATE, source = SourceType.VM)
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserEntityRead creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    private UserEntityRead modifier;

    User toUser() {
        return new User(
                id,
                uuid,
                version,
                email,
                password,
                firstName,
                lastName,
                Arrays.stream(roles.split(";"))
                        .map(UserRole::valueOf)
                        .sorted(Comparator.comparing(UserRole::getHierarchy))
                        .toList(),
                lastLogin,
                createdAt,
                modifiedAt,
                Optional.ofNullable(creator).map(this::toUser).orElse(null),
                Optional.ofNullable(modifier).map(this::toUser).orElse(null)
                );
    }

    private User toUser(UserEntityRead userEntityRead) {
        return new User(
                id,
                uuid,
                version,
                email,
                password,
                firstName,
                lastName,
                Arrays.stream(roles.split(";"))
                        .map(UserRole::valueOf)
                        .sorted(Comparator.comparing(UserRole::getHierarchy))
                        .toList(),
                lastLogin,
                createdAt,
                modifiedAt,
                null,
                null
                );
    }
}
