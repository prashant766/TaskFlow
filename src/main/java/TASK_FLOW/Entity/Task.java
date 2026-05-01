package TASK_FLOW.Entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="TASK")
public class Task {

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    @SequenceGenerator(name = "task_seq", sequenceName = "task_seq", allocationSize = 1)
    private Integer id;

    private String taskName;
    private String description;
    private String priority;   // HIGH, MEDIUM, LOW
    private String status;     // PENDING, COMPLETED
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;


}
