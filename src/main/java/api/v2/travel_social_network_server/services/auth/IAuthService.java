package api.v2.travel_social_network_server.services.auth;

import api.v2.travel_social_network_server.dtos.auth.LoginDto;
import api.v2.travel_social_network_server.dtos.auth.RegisterDto;
import api.v2.travel_social_network_server.responses.auth.LoginResponse;
import api.v2.travel_social_network_server.responses.auth.RegisterResponse;

public interface IAuthService {
    RegisterResponse registerService(RegisterDto registerDto);
    LoginResponse loginService(LoginDto loginDto);
}