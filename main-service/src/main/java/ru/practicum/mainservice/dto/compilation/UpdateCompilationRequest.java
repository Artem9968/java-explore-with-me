package ru.practicum.mainservice.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateCompilationRequest {
    private Boolean pinned;

    @Size(min = 2, max = 50)
    private String title;

    private List<Integer> events = new ArrayList<>();
}
