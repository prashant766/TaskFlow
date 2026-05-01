package TASK_FLOW.Controller;

import TASK_FLOW.Entity.Task;
import TASK_FLOW.Entity.User;
import TASK_FLOW.Repo.StuRepo;
import TASK_FLOW.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class MyController {

    @Autowired private StuRepo repo;
    @Autowired private UserRepo userRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    // Helper: get currently logged-in User entity
    private User getCurrentUser(Authentication auth) {
        return userRepo.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping
    public String add(@RequestBody Task t, Authentication auth) {
        t.setUser(getCurrentUser(auth)); // ← THIS was missing
        repo.save(t);
        return "ADDED SUCCESSFULLY";
    }

    @GetMapping
    public List<Task> getAll(Authentication auth) {
        return repo.findByUser(getCurrentUser(auth)); // ← was returning everyone's tasks
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable int id, Authentication auth) {
        return repo.findById(id)
                .filter(t -> t.getUser().getUsername().equals(auth.getName()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id, Authentication auth) {
        return repo.findById(id)
                .filter(t -> t.getUser().getUsername().equals(auth.getName()))
                .map(t -> { repo.deleteById(id); return ResponseEntity.ok("DELETED"); })
                .orElse(ResponseEntity.status(403).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable int id, @RequestBody Task t, Authentication auth) {
        return repo.findById(id)
                .filter(existing -> existing.getUser().getUsername().equals(auth.getName()))
                .map(existing -> {
                    existing.setTaskName(t.getTaskName());
                    existing.setStatus(t.getStatus());
                    existing.setPriority(t.getPriority());
                    existing.setDescription(t.getDescription());
                    existing.setDueDate(t.getDueDate());
                    return ResponseEntity.ok(repo.save(existing));
                })
                .orElse(ResponseEntity.status(403).build());
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return ResponseEntity.ok("USER REGISTERED");
    }
}