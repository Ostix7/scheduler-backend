package ua.edu.ukma.mandarin.scheduler.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.mandarin.scheduler.domain.dto.jwt.JwtToken;
import ua.edu.ukma.mandarin.scheduler.domain.dto.principal.ChangePasswordDTO;
import ua.edu.ukma.mandarin.scheduler.domain.dto.principal.LoginPrincipalDTO;
import ua.edu.ukma.mandarin.scheduler.domain.dto.principal.RegisterPrincipalDTO;
import ua.edu.ukma.mandarin.scheduler.exception.EntityValidationException;
import ua.edu.ukma.mandarin.scheduler.service.AuthenticationService;
import ua.edu.ukma.mandarin.scheduler.web.response.ErrorResponse;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/login")
  public ResponseEntity<?> loginPrincipal(@RequestBody LoginPrincipalDTO loginPrincipalDTO) {
    try {
      JwtToken token = authenticationService.loginPrincipal(loginPrincipalDTO);
      return ResponseEntity.ok(token);
    } catch (AuthenticationException ex) {
      return ResponseEntity.badRequest()
          .body(
              ErrorResponse.builder()
                  .status(HttpStatus.BAD_REQUEST.value())
                  .message(ex.getMessage())
                  .build());
    } catch (Exception ex) {
      return ResponseEntity.internalServerError()
          .body(
              ErrorResponse.builder()
                  .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                  .message(ex.getMessage())
                  .build());
    }
  }

  @PostMapping("/registration")
  public ResponseEntity<?> registerPrincipal(
      @RequestBody RegisterPrincipalDTO registerPrincipalDTO) {
    try {
      authenticationService.registerPrincipal(registerPrincipalDTO);
      return ResponseEntity.status(HttpStatus.CREATED).build();
    } catch (EntityValidationException ex) {
      return ResponseEntity.badRequest()
          .body(
              ErrorResponse.builder()
                  .status(HttpStatus.BAD_REQUEST.value())
                  .message(ex.getMessage())
                  .build());
    } catch (Exception ex) {
      return ResponseEntity.internalServerError()
          .body(
              ErrorResponse.builder()
                  .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                  .message(ex.getMessage())
                  .build());
    }
  }

  @PostMapping("/password/change")
  public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
    try {
      authenticationService.changePassword(changePasswordDTO);
      return ResponseEntity.noContent().build();
    } catch (EntityValidationException ex) {
      return ResponseEntity.badRequest()
          .body(
              ErrorResponse.builder()
                  .status(HttpStatus.BAD_REQUEST.value())
                  .message(ex.getMessage())
                  .build());
    } catch (Exception ex) {
      return ResponseEntity.internalServerError()
          .body(
              ErrorResponse.builder()
                  .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                  .message(ex.getMessage())
                  .build());
    }
  }
}
