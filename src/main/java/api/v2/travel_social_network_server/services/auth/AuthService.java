package api.v2.travel_social_network_server.services.auth;

import api.v2.travel_social_network_server.components.JwtGenerator;
import api.v2.travel_social_network_server.dtos.auth.LoginDto;
import api.v2.travel_social_network_server.dtos.auth.RegisterDto;
import api.v2.travel_social_network_server.entities.User;
import api.v2.travel_social_network_server.entities.UserProfile;
import api.v2.travel_social_network_server.exceptions.ResourceAlreadyExistedException;
import api.v2.travel_social_network_server.exceptions.ResourceNotFoundException;
import api.v2.travel_social_network_server.responses.auth.LoginResponse;
import api.v2.travel_social_network_server.responses.auth.RegisterResponse;
import api.v2.travel_social_network_server.responsitories.UserRepository;
import api.v2.travel_social_network_server.utilities.GenderEnum;
import api.v2.travel_social_network_server.utilities.ProviderEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;

    @Transactional
    public RegisterResponse registerService(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new ResourceAlreadyExistedException("User already existed with this email");
        }

        String genderInput = registerDto.getGender() != null
                ? registerDto.getGender().toLowerCase().trim()
                : "";

        GenderEnum gender = switch (genderInput) {
            case "male" -> GenderEnum.MALE;
            case "female" -> GenderEnum.FEMALE;
            default -> GenderEnum.OTHER;
        };

        UserProfile userProfile = UserProfile.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .dateOfBirth(registerDto.getDateOfBirth())
                .gender(gender)
                .build();

        User user = User.builder()
                .userName(registerDto.getUserName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .userProfile(userProfile)
                .build();

        userProfile.setUser(user);  // Quan trọng: ánh xạ ngược

        System.out.println(user);

        userRepository.save(user);

        return RegisterResponse.builder()
                .userName(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public LoginResponse loginService(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not existed"));


        String jwtToken = authenticateUser(loginDto, user);

        return LoginResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUsername())
                .avatarImg(user.getAvatarImg())
                .coverImg(user.getCoverImg())
                .email(user.getEmail())
                .token(jwtToken)
                .role(user.getRole().name())
                .userProfile(user.getUserProfile())
                .build();
    }


    private String authenticateUser(LoginDto loginDto, User user) {
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Invalid password");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword(),
                user.getAuthorities()
        );

        System.out.println(authenticationToken);

        authenticationManager.authenticate(authenticationToken);

        String jwtToken = jwtGenerator.generateToken(user, ProviderEnum.LOCAL);
        System.out.println(jwtToken);
        return jwtToken;
    }
}