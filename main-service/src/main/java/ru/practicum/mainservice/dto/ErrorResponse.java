package ru.practicum.mainservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {

    private String errorType;

    private String message;

    private HttpStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @JsonIgnore
    private List<String> errorDetails = new ArrayList<>();
}
