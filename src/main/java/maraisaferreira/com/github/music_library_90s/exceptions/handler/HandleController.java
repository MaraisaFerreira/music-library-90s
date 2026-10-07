package maraisaferreira.com.github.music_library_90s.exceptions.handler;

import maraisaferreira.com.github.music_library_90s.exceptions.ResourceNotFoundException;
import maraisaferreira.com.github.music_library_90s.exceptions.dto.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@RestControllerAdvice
public class HandleController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> genericExceptionHandler(Exception ex, WebRequest request){
        return ResponseEntity.internalServerError().body(
                new ExceptionResponseDto(
                        Instant.now(),
                        ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        request.getDescription(false).split("=")[1]
                )
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> resourceNotFoundExceptionHandler(
            ResourceNotFoundException ex,  WebRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionResponseDto(
                        Instant.now(),
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        request.getDescription(false).split("=")[1]
                )
        );
    }
}
