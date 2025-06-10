# Hệ Thống Thống Kê Nhận Dạng Mống Mắt

## Mô Tả Dự Án

Dự án **Eye Recognition Statistic** là một dịch vụ web được xây dựng bằng Spring Boot, chuyên cung cấp các tính năng thống kê và phân tích dữ liệu cho hệ thống nhận dạng mống mắt. Ứng dụng này giúp theo dõi, phân tích và báo cáo hiệu suất của các thuật toán nhận dạng mống mắt.

## Tính Năng Chính

- 📊 **Thống kê hiệu suất nhận dạng**: Theo dõi độ chính xác, tốc độ xử lý
- 📈 **Báo cáo chi tiết**: Tạo báo cáo thống kê theo thời gian
- 🔍 **Phân tích dữ liệu**: Phân tích xu hướng và pattern trong dữ liệu nhận dạng
- 🛡️ **Bảo mật dữ liệu**: Đảm bảo an toàn thông tin sinh trắc học
- 🌐 **RESTful API**: Cung cấp API để tích hợp với các hệ thống khác

## Công Nghệ Sử Dụng

- **Java 17**: Ngôn ngữ lập trình chính
- **Spring Boot 3.2.3**: Framework chính cho ứng dụng
- **Spring Web**: Xây dựng RESTful web services
- **Spring Validation**: Validate dữ liệu đầu vào
- **Lombok**: Giảm boilerplate code
- **Maven**: Quản lý dependencies và build project

## Yêu Cầu Hệ Thống

- **Java**: Phiên bản 17 trở lên
- **Maven**: Phiên bản 3.6+ 
- **RAM**: Tối thiểu 512MB
- **Ổ cứng**: Tối thiểu 100MB dung lượng trống

## Hướng Dẫn Cài Đặt

### 1. Clone Repository

```bash
git clone https://github.com/username/eye-recognition-statistic.git
cd eye-recognition-statistic
```

### 2. Build Dự Án

#### Sử dụng Maven Wrapper (Khuyến nghị)
```bash
./mvnw clean install
```

#### Hoặc sử dụng Maven đã cài đặt
```bash
mvn clean install
```

### 3. Chạy Ứng Dụng

#### Chạy bằng Maven
```bash
./mvnw spring-boot:run
```

#### Hoặc chạy file JAR
```bash
java -jar target/recognition-statistics-service-0.0.1-SNAPSHOT.jar
```

### 4. Kiểm Tra Ứng Dụng

Mở trình duyệt và truy cập: `http://localhost:8080`

## Cấu Trúc Dự Án

```
src/
├── main/
│   ├── java/com/example/eyerecognitionstatistic/
│   │   ├── EyeRecognitionStatisticApplication.java  # Main class
│   │   ├── config/                                  # Cấu hình ứng dụng
│   │   ├── controller/                              # REST Controllers
│   │   ├── service/                                 # Business Logic
│   │   └── model/                                   # Data Models
│   └── resources/
│       ├── application.properties                   # Cấu hình ứng dụng
│       └── static/                                  # Static files
└── test/                                           # Unit tests
```

## API Endpoints

### Thống Kê Cơ Bản
- `GET /api/statistics/overview` - Tổng quan thống kê
- `GET /api/statistics/daily` - Thống kê theo ngày
- `GET /api/statistics/monthly` - Thống kê theo tháng

### Báo Cáo
- `GET /api/reports/accuracy` - Báo cáo độ chính xác
- `GET /api/reports/performance` - Báo cáo hiệu suất
- `POST /api/reports/custom` - Tạo báo cáo tùy chỉnh

## Cấu Hình

### Application Properties

Chỉnh sửa file `src/main/resources/application.properties`:

```properties
# Port server
server.port=8080

# Database configuration (nếu có)
# spring.datasource.url=jdbc:mysql://localhost:3306/eye_recognition_db
# spring.datasource.username=username
# spring.datasource.password=password

# Logging
logging.level.com.example.eyerecognitionstatistic=INFO
```

## Development

### Chạy trong môi trường Development
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Chạy Tests
```bash
./mvnw test
```

### Build Production
```bash
./mvnw clean package -Pprod
```

## Đóng Góp

1. Fork dự án
2. Tạo feature branch (`git checkout -b feature/TinhNangMoi`)
3. Commit thay đổi (`git commit -am 'Thêm tính năng mới'`)
4. Push lên branch (`git push origin feature/TinhNangMoi`)
5. Tạo Pull Request

## Troubleshooting

### Lỗi Port đã được sử dụng
```bash
# Tìm process đang sử dụng port 8080
lsof -i :8080
# Kill process
kill -9 <PID>
```

### Lỗi Java version
Đảm bảo đang sử dụng Java 17+:
```bash
java -version
```

### Lỗi Maven
Xóa cache Maven và build lại:
```bash
rm -rf ~/.m2/repository
./mvnw clean install
```

## Bảo Mật

- ⚠️ **Quan trọng**: Không commit các thông tin nhạy cảm (API keys, passwords)
- Sử dụng biến môi trường cho production credentials
- Định kỳ cập nhật dependencies để vá các lỗ hổng bảo mật

## License

Dự án này được cấp phép dưới [MIT License](LICENSE)

## Liên Hệ

- **Email**: [dattq.b21cn222@stu.ptit.edu.vn](mailto:your-email@example.com)
- **GitHub**: [https://github.com/quydatsadboy](https://github.com/username)

## Changelog

### v0.0.1-SNAPSHOT
- Khởi tạo dự án với Spring Boot 3.2.3
- Thiết lập cấu trúc cơ bản cho service thống kê
- Cấu hình Maven và dependencies cơ bản

---

📝 **Ghi chú**: Đây là phiên bản đang phát triển. Các tính năng có thể thay đổi trong tương lai.
