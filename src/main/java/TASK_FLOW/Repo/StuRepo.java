package TASK_FLOW.Repo;

import TASK_FLOW.Entity.Task;
import TASK_FLOW.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StuRepo extends JpaRepository<Task,Integer> {
    List<Task> findByUser(User user);
}
