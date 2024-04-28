package pl.bodzioch.damian.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.SourceType;
import org.hibernate.generator.EventType;
import pl.bodzioch.damian.utils.Encoder;
import pl.bodzioch.damian.utils.GeneratedUuidValue;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/*@Entity(name = "users_write")
@Table(name = "users")
@OptimisticLocking*/
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UserEntityWrite {

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

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "modified_by")
    private Long modifiedBy;

    UserEntityWrite(User user) {
        this.id = user.usr_id();
        this.uuid = user.usr_uuid();
        this.version = user.usr_version();
        this.email = user.usr_email();
        this.firstName = user.usr_first_name();
        this.lastName = user.usr_last_name();
        this.password = Encoder.encodePassword(user.usr_password());
        this.createdAt = user.usr_created_at();
        this.modifiedAt = user.usr_modified_at();
        this.createdBy = Optional.ofNullable(user.creator()).map(User::usr_id).orElse(null);
        this.modifiedBy = Optional.ofNullable(user.modifier()).map(User::usr_id).orElse(null);
        this.roles = user.usr_roles().stream()
                .map(UserRole::name)
                .collect(Collectors.joining(";"));
        this.lastLogin = user.usr_last_login();
    }

    /*User toUser() {
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
                new User(createdBy, null, null, null, null, null, null, null, null, null, null, null, null),
                new User(createdBy, null, null, null, null, null, null, null, null, null, null, null, null)
        );
    }*/
}
