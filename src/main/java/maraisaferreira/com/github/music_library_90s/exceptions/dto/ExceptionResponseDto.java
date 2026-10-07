package maraisaferreira.com.github.music_library_90s.exceptions.dto;

import java.time.Instant;

public record ExceptionResponseDto(
        Instant time,
        String message,
        Integer status,
        String path
) {
}
