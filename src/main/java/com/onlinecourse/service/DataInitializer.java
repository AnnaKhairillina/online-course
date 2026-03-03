package com.onlinecourse.service;

import com.onlinecourse.domain.Course;
import com.onlinecourse.domain.Enrollment;
import com.onlinecourse.domain.Payment;
import com.onlinecourse.domain.PaymentStatus;
import com.onlinecourse.domain.User;
import com.onlinecourse.domain.UserRole;
import com.onlinecourse.repository.CourseRepository;
import com.onlinecourse.repository.EnrollmentRepository;
import com.onlinecourse.repository.PaymentRepository;
import com.onlinecourse.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PaymentRepository paymentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           CourseRepository courseRepository,
                           PaymentRepository paymentRepository,
                           EnrollmentRepository enrollmentRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.paymentRepository = paymentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        ensureUser("Администратор", "admin@local", UserRole.ADMIN);
        User teacher = ensureUser("Учитель", "teacher@local", UserRole.TEACHER);
        ensureUser("Менеджер", "manager@local", UserRole.MANAGER);
        User student = ensureUser("Студент", "student@local", UserRole.STUDENT);

        Course math = ensureCourse("Математика базовый курс", "Подготовка по математике", "Математика", new BigDecimal("5000.00"), teacher);
        Course rus = ensureCourse("Русский язык", "Курс по русскому языку", "Русский язык", new BigDecimal("4500.00"), teacher);
        Course bio = ensureCourse("Биология", "Подготовка к экзамену по биологии", "Биология", new BigDecimal("4000.00"), teacher);
        Course chem = ensureCourse("Химия", "Подготовка к экзамену по химии", "Химия", new BigDecimal("4800.00"), teacher);
        Course phys = ensureCourse("Физика", "Подготовка к экзамену по физике", "Физика", new BigDecimal("5200.00"), teacher);
        Course hist = ensureCourse("История", "Курс по истории", "История", new BigDecimal("4200.00"), teacher);
        Course inf = ensureCourse("Информатика", "Курс по информатике", "Информатика", new BigDecimal("5100.00"), teacher);
        Course eng = ensureCourse("Английский язык", "Курс английского языка", "Английский язык", new BigDecimal("4700.00"), teacher);

        seedStudentEnrollments(student, List.of(math, rus, bio));
        seedPayments(student, math, rus, bio, chem, phys, hist, inf, eng);
    }

    private User ensureUser(String name, String email, UserRole role) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPasswordHash(passwordEncoder.encode("password123"));
            user.setRole(role);
            return userRepository.save(user);
        });
    }

    private Course ensureCourse(String title, String description, String subject, BigDecimal price, User teacher) {
        return courseRepository.findByTitle(title).orElseGet(() -> {
            Course c = new Course();
            c.setTitle(title);
            c.setDescription(description);
            c.setSubject(subject);
            c.setPrice(price);
            c.setTeacher(teacher);
            return courseRepository.save(c);
        });
    }

    private void seedStudentEnrollments(User student, List<Course> courses) {
        if (!enrollmentRepository.findStudentCourses(student.getId()).isEmpty()) {
            return;
        }

        int[] progressValues = {72, 48, 83};
        for (int i = 0; i < courses.size(); i++) {
            Enrollment enrollment = new Enrollment();
            enrollment.setUser(student);
            enrollment.setCourse(courses.get(i));
            enrollment.setProgress(progressValues[i]);
            enrollment.setEnrolledAt(LocalDateTime.now().minusMonths(i + 1));
            enrollmentRepository.save(enrollment);
        }
    }

    private void seedPayments(User student,
                              Course math,
                              Course rus,
                              Course bio,
                              Course chem,
                              Course phys,
                              Course hist,
                              Course inf,
                              Course eng) {
        if (paymentRepository.count() >= 22) {
            return;
        }

        int year = LocalDateTime.now().getYear();

        createPayment(student, math, new BigDecimal("5000.00"), PaymentStatus.SUCCESS, LocalDateTime.of(year, 1, 12, 12, 0));
        createPayment(student, rus, new BigDecimal("4500.00"), PaymentStatus.SUCCESS, LocalDateTime.of(year, 1, 22, 14, 0));
        createPayment(student, bio, new BigDecimal("4000.00"), PaymentStatus.SUCCESS, LocalDateTime.of(year, 2, 7, 11, 0));
        createPayment(student, chem, new BigDecimal("4800.00"), PaymentStatus.SUCCESS, LocalDateTime.of(year, 2, 20, 10, 30));
        createPayment(student, math, new BigDecimal("5000.00"), PaymentStatus.SUCCESS, LocalDateTime.of(year, 3, 9, 9, 0));
        createPayment(student, rus, new BigDecimal("4500.00"), PaymentStatus.SUCCESS, LocalDateTime.of(year, 3, 19, 17, 0));
        createPayment(student, chem, new BigDecimal("4800.00"), PaymentStatus.SUCCESS, LocalDateTime.of(year, 4, 2, 15, 0));
        createPayment(student, bio, new BigDecimal("4000.00"), PaymentStatus.SUCCESS, LocalDateTime.of(year, 4, 21, 13, 0));
        createPayment(student, math, new BigDecimal("5000.00"), PaymentStatus.SUCCESS, LocalDateTime.of(year, 5, 11, 12, 0));
        createPayment(student, rus, new BigDecimal("4500.00"), PaymentStatus.SUCCESS, LocalDateTime.of(year, 6, 3, 12, 0));
        createPayment(student, bio, new BigDecimal("4000.00"), PaymentStatus.SUCCESS, LocalDateTime.of(year, 7, 8, 12, 0));
        createPayment(student, chem, new BigDecimal("4800.00"), PaymentStatus.SUCCESS, LocalDateTime.of(year, 8, 16, 12, 0));
        createPayment(student, phys, new BigDecimal("5200.00"), PaymentStatus.SUCCESS, LocalDateTime.of(year, 9, 12, 12, 0));
        createPayment(student, hist, new BigDecimal("4200.00"), PaymentStatus.SUCCESS, LocalDateTime.of(year, 10, 6, 12, 0));
        createPayment(student, inf, new BigDecimal("5100.00"), PaymentStatus.SUCCESS, LocalDateTime.of(year, 11, 13, 12, 0));
        createPayment(student, eng, new BigDecimal("4700.00"), PaymentStatus.SUCCESS, LocalDateTime.of(year, 12, 18, 12, 0));

        createPayment(student, math, new BigDecimal("5000.00"), PaymentStatus.PENDING, LocalDateTime.of(year, 9, 5, 12, 0));
        createPayment(student, rus, new BigDecimal("4500.00"), PaymentStatus.FAILED, LocalDateTime.of(year, 10, 3, 12, 0));
    }

    private void createPayment(User student, Course course, BigDecimal amount, PaymentStatus status, LocalDateTime when) {
        Payment p = new Payment();
        p.setUser(student);
        p.setCourse(course);
        p.setAmount(amount);
        p.setStatus(status);
        p.setPaymentDate(when);
        paymentRepository.save(p);
    }
}
