package api.v2.travel_social_network_server.controllers;

import api.v2.travel_social_network_server.dtos.auth.LoginDto;
import api.v2.travel_social_network_server.dtos.auth.RegisterDto;
import api.v2.travel_social_network_server.responses.Response;
import api.v2.travel_social_network_server.responses.auth.LoginResponse;
import api.v2.travel_social_network_server.responses.auth.RegisterResponse;
import api.v2.travel_social_network_server.services.auth.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base-url}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response<RegisterResponse>> register(@Valid @RequestBody RegisterDto registerDto) {
        RegisterResponse response = authService.registerService(registerDto);
        return ResponseEntity.ok(Response.success(response, "Register successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@Valid @RequestBody LoginDto loginDto) {
        LoginResponse response = authService.loginService(loginDto);
        return ResponseEntity.ok(Response.success(response, "Login successfully"));
    }
}
