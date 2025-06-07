package com.weebeeio.demo.global.analytics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class EventDto {
    @NotBlank(message = "Event type is required")
    private String eventType;

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotBlank(message = "Username is required")
    private String username;

    @NotNull(message = "Timestamp is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @NotNull(message = "Properties are required")
    @Size(min = 1)
    private Map<@NotBlank String, @NotNull Object> properties;
}
