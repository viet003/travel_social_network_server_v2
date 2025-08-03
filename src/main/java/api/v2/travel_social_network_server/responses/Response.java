package api.v2.travel_social_network_server.responses;

import lombok.*;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response<T> {
    private boolean success;            // true / false
    private String message;            // thông điệp cho người dùng / dev
    private T data;                    // dữ liệu kết quả (nếu có)
    private Object errors;             // lỗi chi tiết (nullable nếu success)
    private Instant timestamp;         // thời gian phản hồi

    // Factory methods nếu muốn dùng gọn
    public static <T> Response<T> success(T data, String message) {
        return Response.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .errors(null)
                .timestamp(Instant.now())
                .build();
    }

    public static Response<Object> error(String message, Object errors) {
        return Response.builder()
                .success(false)
                .message(message)
                .data(null)
                .errors(errors)
                .timestamp(Instant.now())
                .build();
    }
}
