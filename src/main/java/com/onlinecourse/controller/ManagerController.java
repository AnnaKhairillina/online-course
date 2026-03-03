package com.onlinecourse.controller;

import com.onlinecourse.service.RevenueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Year;
import java.util.Map;

@Controller
public class ManagerController {
    private final RevenueService revenueService;

    private static final Map<Integer, String> MONTHS = Map.ofEntries(
            Map.entry(1, "Январь"),
            Map.entry(2, "Февраль"),
            Map.entry(3, "Март"),
            Map.entry(4, "Апрель"),
            Map.entry(5, "Май"),
            Map.entry(6, "Июнь"),
            Map.entry(7, "Июль"),
            Map.entry(8, "Август"),
            Map.entry(9, "Сентябрь"),
            Map.entry(10, "Октябрь"),
            Map.entry(11, "Ноябрь"),
            Map.entry(12, "Декабрь")
    );

    public ManagerController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping("/manager/revenue")
    public String revenue(@RequestParam(required = false) Integer year, Model model) {
        int currentYear = Year.now().getValue();
        RevenueService.RevenueReport report = revenueService.buildYearReport(year == null ? currentYear : year);
        model.addAttribute("report", report);
        model.addAttribute("months", MONTHS);
        return "manager-revenue";
    }
}
