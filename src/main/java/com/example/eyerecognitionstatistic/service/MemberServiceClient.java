package com.example.eyerecognitionstatistic.client;

import com.example.eyerecognitionstatistic.model.Member;
import com.example.eyerecognitionstatistic.model.RecognitionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class MemberServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${employee.service.url}")
    private String employeeServiceUrl;

    /**
     * Lấy tất cả nhân viên từ service quản lý nhân viên
     * @return Danh sách tất cả nhân viên
     */
    public List<Member> getAllMembers() {
        String url = employeeServiceUrl + "/members";

        try {
            ResponseEntity<List<Member>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Member>>() {}
            );

            return response.getBody();
        } catch (Exception e) {
            // Log lỗi và trả về danh sách rỗng nếu không thể kết nối
            System.err.println("Lỗi khi lấy danh sách nhân viên: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Lấy thông tin chi tiết của một nhân viên
     * @param memberId ID của nhân viên
     * @return Thông tin nhân viên hoặc null nếu không tìm thấy
     */
    public Member getMemberById(Integer memberId) {
        String url = employeeServiceUrl + "/members/" + memberId;

        try {
            return restTemplate.getForObject(url, Member.class);
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy thông tin nhân viên: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lấy danh sách sự kiện nhận dạng thành công của một nhân viên trong khoảng thời gian
     * @param memberId ID của nhân viên
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return Danh sách sự kiện nhận dạng
     */
    public List<RecognitionEvent> getSuccessfulEventsByMemberIdAndDateRange(Integer memberId, Date startDate, Date endDate) {
        String url = UriComponentsBuilder.fromHttpUrl(employeeServiceUrl + "/events/member/" + memberId + "/date-range")
                .queryParam("startDate", startDate.toInstant())
                .queryParam("endDate", endDate.toInstant())
                .toUriString();

        try {
            ResponseEntity<List<RecognitionEvent>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<RecognitionEvent>>() {}
            );

            return response.getBody();
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy danh sách sự kiện nhận dạng: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Lấy số lượng nhận dạng thành công của một nhân viên trong khoảng thời gian
     * @param memberId ID của nhân viên
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return Số lượng nhận dạng thành công
     */
    public Long countSuccessfulEventsByMemberIdAndDateRange(Integer memberId, Date startDate, Date endDate) {
        String url = UriComponentsBuilder.fromHttpUrl(employeeServiceUrl + "/events/member/" + memberId + "/count")
                .queryParam("startDate", startDate.toInstant())
                .queryParam("endDate", endDate.toInstant())
                .toUriString();

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("successCount")) {
                return Long.valueOf(response.get("successCount").toString());
            }
            return 0L;
        } catch (Exception e) {
            System.err.println("Lỗi khi đếm số lượng nhận dạng: " + e.getMessage());
            return 0L;
        }
    }

    /**
     * Lấy thống kê nhận dạng theo khoảng thời gian
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return Map chứa thống kê (memberId -> số lần nhận dạng)
     */
    public Map<String, Long> getRecognitionStatsByDateRange(Date startDate, Date endDate) {
        String url = UriComponentsBuilder.fromHttpUrl(employeeServiceUrl + "/events/stats/date-range")
                .queryParam("startDate", startDate.toInstant())
                .queryParam("endDate", endDate.toInstant())
                .toUriString();

        try {
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy thống kê nhận dạng: " + e.getMessage());
            return Map.of();
        }
    }
}
