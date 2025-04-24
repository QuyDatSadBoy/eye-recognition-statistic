package com.example.eyerecognitionstatistic.service;

import com.example.eyerecognitionstatistic.model.Member;
import com.example.eyerecognitionstatistic.model.MemberStats;
import com.example.eyerecognitionstatistic.model.RecognitionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecognitionStatisticsService {

    @Autowired
    private MemberServiceClient memberServiceClient;

    /**
     * Lấy danh sách thống kê nhân viên theo số lần nhận dạng thành công trong khoảng thời gian
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return Danh sách thống kê đã sắp xếp theo số lần nhận dạng giảm dần
     */
    public List<MemberStats> getMembersRankedBySuccessfulRecognition(Date startDate, Date endDate) {
        // Giữ nguyên code hiện tại từ đầu đến dòng tạo danh sách result
        List<Member> allMembers = memberServiceClient.getAllMembers();
        Map<String, Integer> recognitionStats = memberServiceClient.getRecognitionStatsByDateRange(startDate, endDate);

        // Chuyển đổi từ Map<String, Integer> sang Map<Integer, Integer>
        Map<Integer, Integer> statsMap = new HashMap<>();
        recognitionStats.forEach((key, value) -> {
            try {
                statsMap.put(Integer.parseInt(key), value);
            } catch (NumberFormatException e) {
                // Bỏ qua nếu không phải số
            }
        });

        // Tạo danh sách kết quả
        List<MemberStats> result = new ArrayList<>();

        for (Member member : allMembers) {
            MemberStats memberStats = new MemberStats();
            memberStats.setMemberId(member.getId());
            memberStats.setUsername(member.getUsername());
            memberStats.setEmail(member.getEmail());
            memberStats.setDepartment(member.getDepartment());

            // Thêm thông tin tên đầy đủ nếu có
            if (member.getFullName() != null) {
                memberStats.setFirstName(member.getFullName().getFirstName());
                memberStats.setLastName(member.getFullName().getLastName());
                memberStats.setFullName(member.getFullName().getFirstName() + " " + member.getFullName().getLastName());
            }

            // Lấy số lần nhận dạng thành công (mặc định là 0 nếu không có)
            Integer successCount = statsMap.getOrDefault(member.getId(), 0);
            memberStats.setSuccessfulRecognitions(successCount);

            // THÊM CODE MỚI: Tính toán averageAccuracy
            if (successCount > 0) {
                // Lấy danh sách sự kiện nhận dạng thành công
                List<RecognitionEvent> events = memberServiceClient.getSuccessfulEventsByMemberIdAndDateRange(member.getId(), startDate, endDate);

                // Tính độ chính xác trung bình
                if (events != null && !events.isEmpty()) {
                    double avgAccuracy = events.stream()
                            .filter(e -> e.getAccuracy() != null)
                            .mapToDouble(e -> e.getAccuracy())
                            .average()
                            .orElse(0.0);

                    memberStats.setAverageAccuracy((float)avgAccuracy);
                } else {
                    memberStats.setAverageAccuracy(0.0f);
                }
            } else {
                memberStats.setAverageAccuracy(0.0f);
            }

            result.add(memberStats);
        }

        // Sắp xếp kết quả theo số lần nhận dạng thành công (giảm dần)
        result.sort(Comparator.comparing(MemberStats::getSuccessfulRecognitions).reversed());

        return result;
    }
    /**
     * Lấy chi tiết thống kê nhận dạng của một nhân viên
     * @param memberId ID của nhân viên
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return Map chứa thông tin chi tiết
     */
    public Map<String, Object> getMemberRecognitionDetails(Integer memberId, Date startDate, Date endDate) {
        // Lấy thông tin nhân viên
        Member member = memberServiceClient.getMemberById(memberId);
        if (member == null) {
            throw new NoSuchElementException("Không tìm thấy nhân viên với ID: " + memberId);
        }

        // Lấy danh sách sự kiện nhận dạng thành công
        List<RecognitionEvent> events = memberServiceClient.getSuccessfulEventsByMemberIdAndDateRange(memberId, startDate, endDate);

        // Lấy số lượng nhận dạng thành công
        Integer successCount = memberServiceClient.countSuccessfulEventsByMemberIdAndDateRange(memberId, startDate, endDate);

        // Tạo kết quả
        Map<String, Object> result = new HashMap<>();
        result.put("memberId", memberId);
        result.put("username", member.getUsername());

        if (member.getFullName() != null) {
            result.put("firstName", member.getFullName().getFirstName());
            result.put("lastName", member.getFullName().getLastName());
            result.put("fullName", member.getFullName().getFirstName() + " " + member.getFullName().getLastName());
        }

        result.put("department", member.getDepartment());
        result.put("email", member.getEmail());
        result.put("successCount", successCount);

        // Tính độ chính xác trung bình
        if (!events.isEmpty()) {
            double avgAccuracy = events.stream()
                    .filter(e -> e.getAccuracy() != null)
                    .mapToDouble(e -> e.getAccuracy())
                    .average()
                    .orElse(0.0);

            result.put("averageAccuracy", avgAccuracy);

            // Phân bổ nhận dạng theo ngày
            Map<String, Integer> recognitionByDate = events.stream()
                    .filter(e -> e.getTimeVerify() != null)
                    .collect(Collectors.groupingBy(
                            e -> new java.text.SimpleDateFormat("yyyy-MM-dd").format(e.getTimeVerify()),
                            Collectors.summingInt(e -> 1)
                    ));

            result.put("recognitionByDate", recognitionByDate);
        } else {
            result.put("averageAccuracy", 0.0);
            result.put("recognitionByDate", Collections.emptyMap());
        }

        return result;
    }

    /**
     * Lấy tổng quan về thống kê nhận dạng trong hệ thống
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return Map chứa thông tin tổng quan
     */
    public Map<String, Object> getSystemStatistics(Date startDate, Date endDate) {
        List<MemberStats> memberStats = getMembersRankedBySuccessfulRecognition(startDate, endDate);

        // Tính tổng số lần nhận dạng thành công
        int totalSuccessfulRecognitions = memberStats.stream()
                .mapToInt(MemberStats::getSuccessfulRecognitions)
                .sum();

        // Tìm nhân viên có nhiều lần nhận dạng nhất
        Optional<MemberStats> topMember = memberStats.stream()
                .filter(m -> m.getSuccessfulRecognitions() > 0)
                .max(Comparator.comparing(MemberStats::getSuccessfulRecognitions));

        // Phân nhóm theo phòng ban
        Map<String, Integer> recognitionsByDepartment = memberStats.stream()
                .filter(m -> m.getDepartment() != null && !m.getDepartment().isEmpty())
                .collect(Collectors.groupingBy(
                        MemberStats::getDepartment,
                        Collectors.summingInt(MemberStats::getSuccessfulRecognitions)
                ));

        // Thống kê số nhân viên đã được nhận dạng ít nhất 1 lần
        long membersWithRecognition = memberStats.stream()
                .filter(m -> m.getSuccessfulRecognitions() > 0)
                .count();

        // Tạo kết quả
        Map<String, Object> result = new HashMap<>();
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("totalSuccessfulRecognitions", totalSuccessfulRecognitions);
        result.put("totalMembers", memberStats.size());
        result.put("membersWithRecognition", membersWithRecognition);
        result.put("recognitionsByDepartment", recognitionsByDepartment);

        if (topMember.isPresent()) {
            result.put("topPerformer", topMember.get());
        }

        return result;
    }
}

