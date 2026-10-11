package maraisaferreira.com.github.music_library_90s.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponseDto<T>(
        List<T> content,
        Integer page,
        Integer size,
        Integer totalPages,
        Long totalElements,
        Boolean hasNext
) {
    public PageResponseDto(Page<T> dtoPage) {
        this(
                dtoPage.getContent(),
                dtoPage.getPageable().getPageNumber(),
                dtoPage.getSize(),
                dtoPage.getTotalPages(),
                dtoPage.getTotalElements(),
                dtoPage.hasNext()
        );
    }
}
