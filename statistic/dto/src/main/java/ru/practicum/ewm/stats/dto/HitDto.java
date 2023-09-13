package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HitDto {
    Long id;

    @NotBlank(message = "App can't be Blank.")
    @Size(max = 20)
    String app;

    @NotBlank(message = "Uri can't be Blank.")
    @Size(max = 50)
    String uri;

    @NotBlank(message = "Ip can't be Blank.")
    @Size(max = 15)
    String ip;

    @NotNull(message = "Time can't be Null.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;
}