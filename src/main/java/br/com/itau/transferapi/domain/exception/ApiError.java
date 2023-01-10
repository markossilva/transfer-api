package br.com.itau.transferapi.domain.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiError {
  private int code;
  private HttpStatus status;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private LocalDateTime timestamp;
  private String message;

  private ApiError() {
    timestamp = LocalDateTime.now();
  }

  ApiError(HttpStatus status, Throwable ex) {
    this();
    this.status = status;
    this.code = status.value();
    this.message = ex.getLocalizedMessage();
  }
}
