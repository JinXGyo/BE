package LDHD.project.domain.user;

import LDHD.project.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "users")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String name;

    @Column
    String loginId;

    @Column
    String loginPassword;

    @Column(unique = true)
    String email;

    @Column
    String address;

    @Column
    int age;

    @Column //(1: 남, 2: 여)
    Integer gender;

    @Column
    LocalDate birthDate;


    @Builder
    public User(String name, String loginId, String loginPassword, String email, String address, int age,
                Integer gender, LocalDate birthDate) {

        this.name = name;
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.email = email;
        this.address = address;
        this.age = age;
        this.gender = gender;
        this.birthDate = birthDate;

    }
}
