package org.example.belajar_spring.model.task;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchTaskRequest {

    private String title;
    private String startDate;
    private String endDate;
    private String status;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;
}
