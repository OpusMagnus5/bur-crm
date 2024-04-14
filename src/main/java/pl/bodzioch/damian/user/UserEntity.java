package pl.bodzioch.damian.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.generator.EventType;
import pl.bodzioch.damian.utils.Encoder;
import pl.bodzioch.damian.utils.GeneratedUuidValue;
import pl.bodzioch.damian.valueobject.AuditData;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity(name = "users")
@OptimisticLocking
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UserEntity {

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

    @Embedded
    private AuditData auditData;

    UserEntity(User user) {
        this.id = user.getId();
        this.uuid = user.getUuid();
        this.version = user.getVersion();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = Encoder.encodePassword(user.getPassword());
        this.auditData = user.getAuditData();
        this.roles = user.getRoles().stream()
                .map(UserRole::name)
                .collect(Collectors.joining(";"));
        this.lastLogin = user.getLastLogin();
    }

    User toUser() {
        return User.builder()
                .id(this.id)
                .uuid(this.uuid)
                .version(this.version)
                .email(this.email)
                .password(this.password)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .roles(Arrays.stream(roles.split(";"))
                        .map(UserRole::valueOf)
                        .toList())
                .auditData(auditData)
                .build();
    }
}
