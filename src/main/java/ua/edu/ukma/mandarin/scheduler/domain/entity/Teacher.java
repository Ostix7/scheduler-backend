package ua.edu.ukma.mandarin.scheduler.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.*;
import ua.edu.ukma.mandarin.scheduler.domain.entity.security.Principal;

@Entity
@Table(name = "teacher")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "principal_id")
  private Principal principal;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @NotEmpty private String rank;

  @OneToMany(mappedBy = "teacher")
  private List<Group> groupsToTeach;

  @OneToMany(mappedBy = "author")
  private List<Subject> subjects;
}
