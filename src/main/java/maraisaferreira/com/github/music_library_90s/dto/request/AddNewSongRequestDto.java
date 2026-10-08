package maraisaferreira.com.github.music_library_90s.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import maraisaferreira.com.github.music_library_90s.contants.GlobalMessages;

public record AddNewSongRequestDto(
        @NotBlank(message = GlobalMessages.NOT_BLANK_FIELD)
        String name,

        @NotNull(message = GlobalMessages.NOT_NULL_FIELD)
        @Min(value = 1990, message = GlobalMessages.LOWEST_YEAR_ALLOWED)
        @Max(value = 1999, message = GlobalMessages.BIGGER_YEAR_ALLOWED)
        Integer releaseYear,

        @NotBlank(message = GlobalMessages.NOT_BLANK_FIELD)
        String artist,

        @NotBlank(message = GlobalMessages.NOT_BLANK_FIELD)
        String album,

        @Min(value = 1, message = GlobalMessages.LOWEST_TRACK_VALUE)
        @NotNull(message = GlobalMessages.NOT_NULL_FIELD)
        Integer track

) {
}
