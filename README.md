# Java MVC Desktop Task Manager App

## Giới thiệu dự án
1. Dự án "Phát triển Ứng dụng Quản lý Thời gian và Công việc" được thiết kế nhằm giúp người dùng
tổ chức và theo dõi các công việc hằng ngày một cách hiệu quả.
2. Ứng dụng cho phép người dùng:
- Thêm, xóa và quản lý các nhiệm vụ theo từng ngày
- Thiết lập thời gian thực hiện và mức độ quan trọng của công việc
- Sử dụng bộ đếm thời gian Pomodoro kèm theo trình phát nhạc MP3, luân phiên giữa các phiên làm việc và nghỉ ngơi

## Công nghệ và kỹ thuật sử dụng
1. Ngôn ngữ Java – lập trình hướng đối tượng, xây dựng toàn bộ logic và cấu trúc dữ liệu
2. Giao diện người dùng
- JavaFX: xây dựng giao diện đồ họa
- Scene, Stage, Pane, VBox, HBox, ListView, ProgressBar, Dialog
- FXML: tách giao diện và xử lý logic
- JavaFX CSS: tùy chỉnh màu sắc, theme và trải nghiệm người dùng
- ControlsFX: hỗ trợ popup và dialog nâng cao
- Media / AudioClip: phát âm thanh thông báo Pomodoro
3. Xử lý logic
- Gồm các lớp: Calendar, Week, Day, Task, Music
- LocalDate, LocalTime, Duration, DateTimeFormatter
- JavaFX Timeline: Cập nhật thời gian Pomodoro theo từng giây
- ScheduledService: Chạy ngầm kiểm tra các task sắp đến hạn
- ObservableList: Tự động cập nhật dữ liệu từ Model lên View
- Java Collections: Quản lý, sắp xếp và lọc danh sách công việc
4. Lưu trữ dữ liệu
- Java Serializable: Tuần tự hóa các đối tượng Calendar, Week, Day, Task thành file `.dat`
- Lưu trữ theo tuần, mỗi tuần một file riêng
- Sử dụng ArrayList kết hợp Serializable để lưu danh sách công việc

## Mô hình kiến trúc MVC
Ứng dụng được xây dựng theo mô hình MVC nhằm tăng khả năng
mở rộng, bảo trì và quản lý mã nguồn.
1. Model
- Quản lý dữ liệu và logic nghiệp vụ của công việc
2. View
- Giao diện người dùng
- Hiển thị dữ liệu từ Model và cho phép người dùng tương tác
3. Controller
- Điều phối trung gian giữa Model và View
4. Utils
- Tiện ích mở rộng tự viết thêm cho phép người dùng tải hình ảnh lên từ máy tính theo sở thích

## Công cụ quản lý dự án
- Sử dụng Maven 

## Báo cáo
- Báo cáo chi tiết của dự án được đính kèm trong file PDF.

## Lời cảm ơn
Nhóm xin gửi lời cảm ơn chân thành tới TS. Nguyễn Mạnh Sơn – giảng viên môn Lập trình hướng đối tượng, đã hướng dẫn nhóm hoàn thành được dự án này.
##

## Sản phẩm là bài tập lớn cho môn học Lập trình hướng đối tượng của nhóm sinh viên tại Học Viện Công nghệ Bưu chính Viễn Thông năm 2025 ##
