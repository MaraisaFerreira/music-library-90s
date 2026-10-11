package maraisaferreira.com.github.music_library_90s.exceptions.handler;

import maraisaferreira.com.github.music_library_90s.exceptions.BadRequestException;
import maraisaferreira.com.github.music_library_90s.exceptions.ResourceNotFoundException;
import maraisaferreira.com.github.music_library_90s.exceptions.dto.ExceptionResponseDto;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class HandleController extends ResponseEntityExceptionHandler {

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

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponseDto> badRequestExceptionHandler(
            Exception ex,  WebRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponseDto(
                        Instant.now(),
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        request.getDescription(false).split("=")[1]
                )
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> resourceNotFoundExceptionHandler(
            Exception ex,  WebRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionResponseDto(
                        Instant.now(),
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        request.getDescription(false).split("=")[1]
                )
        );
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()).toList();

        StringBuilder sb = new StringBuilder();
        for (String str : message) {
            sb.append(str).append(" | ");
        }

        return ResponseEntity.badRequest().body(new ExceptionResponseDto(
                Instant.now(),
                sb.substring(0, sb.length() - 3),
                HttpStatus.BAD_REQUEST.value(),
                request.getDescription(false).split("=")[1]
        ));
    }


}
