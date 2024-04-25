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
import pl.bodzioch.damian.valueobject.AuditDataEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
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
    private AuditDataEntity auditData;

    UserEntity(User user) {
        AuditData auditData = user.auditData();
        this.id = user.id();
        this.uuid = user.uuid();
        this.version = user.version();
        this.email = user.email();
        this.firstName = user.firstName();
        this.lastName = user.lastName();
        this.password = Encoder.encodePassword(user.password());
        this.auditData = new AuditDataEntity(auditData.createdAt(), auditData.modifiedAt(), auditData.createdBy(), auditData.modifiedBy());
        this.roles = user.roles().stream()
                .map(UserRole::name)
                .collect(Collectors.joining(";"));
        this.lastLogin = user.lastLogin();
    }

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
                new AuditData(auditData.getCreatedAt(), auditData.getModifiedAt(), auditData.getCreatedBy(), auditData.getModifiedBy())
                );
    }
}
