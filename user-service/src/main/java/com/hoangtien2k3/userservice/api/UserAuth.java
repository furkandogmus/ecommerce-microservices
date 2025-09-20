package com.hoangtien2k3.userservice.api;

import com.hoangtien2k3.userservice.model.dto.request.Login;
import com.hoangtien2k3.userservice.model.dto.request.SignUp;
import com.hoangtien2k3.userservice.model.dto.response.TokenValidationResponse;
import com.hoangtien2k3.userservice.model.dto.response.InformationMessage;
import com.hoangtien2k3.userservice.model.dto.response.JwtResponseMessage;
import com.hoangtien2k3.userservice.model.dto.response.ResponseMessage;
import com.hoangtien2k3.userservice.security.validate.AuthorityTokenUtil;
import com.hoangtien2k3.userservice.service.UserService;
import com.hoangtien2k3.userservice.security.validate.TokenValidate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "UserAuth", description = "Authentication operations for users")
public class UserAuth {

    private final UserService userService;

   
    @Operation(summary = "User registration", description = "Register a new user account")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User registered successfully",
            content = @Content(schema = @Schema(implementation = ResponseMessage.class))),
        @ApiResponse(responseCode = "400", description = "Invalid registration data")
    })
    @PostMapping({"/signup", "/register"})
    public Mono<ResponseMessage> register(@Valid @RequestBody SignUp signUp) {
        return userService.register(signUp)
                .map(user -> new ResponseMessage("Create user: " + signUp.getUsername() + " successfully."))
                .onErrorResume(error -> Mono.just(new ResponseMessage("Error occurred while creating the account.")));
    }

    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(schema = @Schema(implementation = JwtResponseMessage.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping({"/signin", "/login"})
    public Mono<ResponseEntity<JwtResponseMessage>> login(@Valid @RequestBody Login signInForm) {
        return userService.login(signInForm)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    JwtResponseMessage errorjwtResponseMessage = new JwtResponseMessage(
                            null,
                            null,
                            new InformationMessage()
                    );
                    return Mono.just(new ResponseEntity<>(errorjwtResponseMessage, HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    @Operation(summary = "User logout", description = "Logout the authenticated user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Logout successful"),
        @ApiResponse(responseCode = "400", description = "Logout failed")
    })
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public Mono<ResponseEntity<String>> logout() {
        log.info("Logout endpoint called");
        return userService.logout()
                .then(Mono.just(new ResponseEntity<>("Logged out successfully.", HttpStatus.OK)))
                .onErrorResume(error -> {
                    log.error("Logout failed", error);
                    return Mono.just(new ResponseEntity<>("Logout failed.", HttpStatus.BAD_REQUEST));
                });
    }


//    @PostMapping("/reset-password")
//    public Mono<ResponseEntity<String>> resetPassword(@RequestParam("token") String token, @RequestBody ResetPasswordRequest resetPasswordRequest) {
//        if (isValidToken(token)) {
//            // Token hợp lệ, đặt mật khẩu mới và cập nhật trong cơ sở dữ liệu
//            updatePassword(userEmail, resetPasswordRequest.getNewPassword());
//            return Mono.just(ResponseEntity.ok("Password reset successful"));
//        } else {
//            // Token không hợp lệ
//            return Mono.just(ResponseEntity.badRequest().body("Invalid token"));
//        }
//    }


//    @PostMapping({"/refresh", "/refresh-token"})
//    public Mono<ResponseEntity<JwtResponseMessage>> refresh(@RequestHeader("Refresh-Token") String refreshToken) {
//        return userService.refreshToken(refreshToken)
//                .map(newAccessToken -> {
//                    JwtResponseMessage jwtResponseMessage = new JwtResponseMessage(newAccessToken, null, null);
//                    return ResponseEntity.ok(jwtResponseMessage);
//                })
//                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
//    }

   
    @Operation(summary = "Validate token", description = "Validate the provided JWT token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Token is valid"),
        @ApiResponse(responseCode = "401", description = "Token is invalid")
    })
    @GetMapping({"/validateToken", "/validate-token"})
    public Boolean validateToken(@RequestHeader(name = "Authorization") String authorizationToken) {
        TokenValidate validate = new TokenValidate();
        if (validate.validateToken(authorizationToken)) {
            return ResponseEntity.ok(new TokenValidationResponse("Valid token")).hasBody();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenValidationResponse("Invalid token")).hasBody();
        }
    }

    @Operation(summary = "Check user authority", description = "Check if user has the required authority/role")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User has required authority"),
        @ApiResponse(responseCode = "401", description = "User does not have required authority")
    })
    @GetMapping({"/hasAuthority", "/authorization"})
    public Boolean getAuthority(@RequestHeader(name = "Authorization") String authorizationToken,
                                String requiredRole) {
        AuthorityTokenUtil authorityTokenUtil = new AuthorityTokenUtil();
        List<String> authorities = authorityTokenUtil.checkPermission(authorizationToken);

        if (authorities.contains(requiredRole)) {
            return ResponseEntity.ok(new TokenValidationResponse("Role access api")).hasBody();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenValidationResponse("Invalid token")).hasBody();
        }
    }

}
