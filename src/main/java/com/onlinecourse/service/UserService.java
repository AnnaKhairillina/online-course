package com.onlinecourse.service;

import com.onlinecourse.domain.User;
import com.onlinecourse.domain.UserRole;
import com.onlinecourse.dto.AdminUpdateUserForm;
import com.onlinecourse.repository.EnrollmentRepository;
import com.onlinecourse.repository.PaymentRepository;
import com.onlinecourse.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentRepository paymentRepository;

    public UserService(UserRepository userRepository,
                       EnrollmentRepository enrollmentRepository,
                       PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    @Transactional
    public void updateUser(Long userId, AdminUpdateUserForm form) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setName(form.getName().trim());
        user.setRole(form.getRole());
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Пользователь уже удален или не найден."));
        try {
            if (user.getRole() == UserRole.STUDENT) {
                paymentRepository.deleteByUserId(userId);
                enrollmentRepository.deleteByUserId(userId);
            }
            userRepository.deleteById(userId);
            userRepository.flush();
        } catch (DataIntegrityViolationException ignored) {
            throw new IllegalStateException("Нельзя удалить пользователя: есть связанные курсы, оплаты или записи.");
        }
    }
}
