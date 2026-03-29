package com.backend.fiestaStaff.security;

import com.backend.fiestaStaff.model.User;
import com.backend.fiestaStaff.model.Worker;
import com.backend.fiestaStaff.repository.UserRepository;
import com.backend.fiestaStaff.repository.WorkerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {

    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;

    public CurrentUser(UserRepository userRepository, WorkerRepository workerRepository) {
        this.userRepository = userRepository;
        this.workerRepository = workerRepository;
    }

    public User get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Long getWorkerId(jakarta.servlet.http.HttpServletRequest request) {
        Object workerId = request.getAttribute("workerId");
        if (workerId != null) {
            return (Long) workerId;
        }
        User user = get();
        return workerRepository.findByUserId(user.getId())
                .filter(w -> w.getStatus() == Worker.Status.ACTIVE)
                .map(Worker::getId)
                .orElse(null);
    }
}
