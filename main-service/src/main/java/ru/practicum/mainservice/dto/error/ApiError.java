package ru.practicum.mainservice.dto.error;

import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonFormat;

@NoArgsConstructor
@Setter
@Getter
public class ApiError {

    private HttpStatus status;

    private String reason;

    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @JsonIgnore
    private List<String> errors = new ArrayList<>();
}
