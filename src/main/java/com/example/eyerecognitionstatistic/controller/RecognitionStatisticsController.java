package com.example.eyerecognitionstatistic.controller;


import com.example.eyerecognitionstatistic.model.MemberStats;
import com.example.eyerecognitionstatistic.service.RecognitionStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/statistics")
public class RecognitionStatisticsController {

    @Autowired
    private RecognitionStatisticsService statisticsService;

    /**
     * Lấy danh sách nhân viên được sắp xếp theo số lần nhận dạng thành công
     */
    @GetMapping("/members/ranking")
    public ResponseEntity<List<MemberStats>> getMembersRankedBySuccessfulRecognition(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        List<MemberStats> stats = statisticsService.getMembersRankedBySuccessfulRecognition(startDate, endDate);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    /**
     * Lấy chi tiết thống kê nhận dạng của một nhân viên
     */
    @GetMapping("/members/{memberId}")
    public ResponseEntity<?> getMemberRecognitionDetails(
            @PathVariable Integer memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        try {
            Map<String, Object> details = statisticsService.getMemberRecognitionDetails(memberId, startDate, endDate);
            return new ResponseEntity<>(details, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Lấy tổng quan thống kê nhận dạng trong hệ thống
     */
    @GetMapping("/system")
    public ResponseEntity<Map<String, Object>> getSystemStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        Map<String, Object> stats = statisticsService.getSystemStatistics(startDate, endDate);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    /**
     * Lấy top N nhân viên có số lần nhận dạng thành công cao nhất
     */
    @GetMapping("/members/top/{n}")
    public ResponseEntity<List<MemberStats>> getTopNMembers(
            @PathVariable int n,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        List<MemberStats> allStats = statisticsService.getMembersRankedBySuccessfulRecognition(startDate, endDate);

        // Lấy top N nhân viên (hoặc ít hơn nếu danh sách không đủ)
        List<MemberStats> topN = allStats.stream()
                .filter(stats -> stats.getSuccessfulRecognitions() > 0) // Chỉ lấy nhân viên có ít nhất 1 lần nhận dạng
                .limit(n)
                .toList();

        return new ResponseEntity<>(topN, HttpStatus.OK);
    }

    /**
     * Lấy thống kê nhận dạng theo phòng ban
     */
    @GetMapping("/departments")
    public ResponseEntity<Map<String, Object>> getStatisticsByDepartment(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        List<MemberStats> allStats = statisticsService.getMembersRankedBySuccessfulRecognition(startDate, endDate);

        // Nhóm theo phòng ban
        Map<String, List<MemberStats>> membersByDepartment = allStats.stream()
                .filter(m -> m.getDepartment() != null && !m.getDepartment().isEmpty())
                .collect(java.util.stream.Collectors.groupingBy(MemberStats::getDepartment));

        // Tính tổng số lần nhận dạng cho mỗi phòng ban
        Map<String, Integer> recognitionsByDepartment = new HashMap<>();
        membersByDepartment.forEach((dept, members) -> {
            int count = members.stream()
                    .mapToInt(MemberStats::getSuccessfulRecognitions)
                    .sum();
            recognitionsByDepartment.put(dept, count);
        });

        // Tìm phòng ban có số lần nhận dạng cao nhất
        Optional<Map.Entry<String, Integer>> topDepartment = recognitionsByDepartment.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        // Tạo kết quả
        Map<String, Object> result = new HashMap<>();
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("departmentCounts", recognitionsByDepartment);
        result.put("membersByDepartment", membersByDepartment);

        if (topDepartment.isPresent()) {
            result.put("topDepartment", topDepartment.get().getKey());
            result.put("topDepartmentCount", topDepartment.get().getValue());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
