package com.onlinecourse.service;

import com.onlinecourse.repository.PaymentRepository;
import com.onlinecourse.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RevenueService {
    private final PaymentRepository paymentRepository;
    private final CourseRepository courseRepository;

    public RevenueService(PaymentRepository paymentRepository, CourseRepository courseRepository) {
        this.paymentRepository = paymentRepository;
        this.courseRepository = courseRepository;
    }

    public RevenueReport buildYearReport(Integer requestedYear) {
        int year = requestedYear == null ? Year.now().getValue() : requestedYear;
        List<PaymentRepository.RevenueProjection> rows = paymentRepository.aggregateRevenueByYear(year);

        Set<String> subjects = new LinkedHashSet<>(courseRepository.findDistinctSubjects());
        for (PaymentRepository.RevenueProjection row : rows) {
            subjects.add(row.getSubject());
        }

        Map<Integer, Map<String, BigDecimal>> matrix = new LinkedHashMap<>();
        Map<Integer, BigDecimal> monthTotals = new LinkedHashMap<>();

        for (int month = 1; month <= 12; month++) {
            Map<String, BigDecimal> monthData = new LinkedHashMap<>();
            for (String subject : subjects) {
                monthData.put(subject, BigDecimal.ZERO);
            }
            matrix.put(month, monthData);
            monthTotals.put(month, BigDecimal.ZERO);
        }

        Map<String, BigDecimal> subjectTotals = new LinkedHashMap<>();
        for (String subject : subjects) {
            subjectTotals.put(subject, BigDecimal.ZERO);
        }

        for (PaymentRepository.RevenueProjection row : rows) {
            matrix.get(row.getMonthNum()).put(row.getSubject(), row.getTotal());
            monthTotals.put(row.getMonthNum(), monthTotals.get(row.getMonthNum()).add(row.getTotal()));
            subjectTotals.put(row.getSubject(), subjectTotals.get(row.getSubject()).add(row.getTotal()));
        }

        BigDecimal maxMonth = monthTotals.values().stream().reduce(BigDecimal.ZERO, BigDecimal::max);
        BigDecimal maxSubject = subjectTotals.values().stream().reduce(BigDecimal.ZERO, BigDecimal::max);
        BigDecimal yearTotal = monthTotals.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        List<BarPoint> monthBars = new ArrayList<>();
        for (Map.Entry<Integer, BigDecimal> item : monthTotals.entrySet()) {
            monthBars.add(new BarPoint(item.getKey().toString(), item.getValue(), toPercent(item.getValue(), maxMonth)));
        }

        List<BarPoint> subjectBars = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> item : subjectTotals.entrySet()) {
            subjectBars.add(new BarPoint(item.getKey(), item.getValue(), toPercent(item.getValue(), maxSubject)));
        }

        return new RevenueReport(year,
                new ArrayList<>(subjects),
                matrix,
                monthTotals,
                subjectTotals,
                yearTotal,
                monthBars,
                subjectBars);
    }

    private int toPercent(BigDecimal value, BigDecimal max) {
        if (max.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }
        return value.multiply(BigDecimal.valueOf(100))
                .divide(max, 0, RoundingMode.HALF_UP)
                .intValue();
    }

    public record BarPoint(String label, BigDecimal value, int percent) {
    }

    public record RevenueReport(int year,
                                List<String> subjects,
                                Map<Integer, Map<String, BigDecimal>> matrix,
                                Map<Integer, BigDecimal> monthTotals,
                                Map<String, BigDecimal> subjectTotals,
                                BigDecimal yearTotal,
                                List<BarPoint> monthBars,
                                List<BarPoint> subjectBars) {
    }
}
