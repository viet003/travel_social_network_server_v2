package api.v2.travel_social_network_server.services.user;

//import api.v2.travel_social_network_server.dtos.user.UpdateUserDto;
//import api.v2.travel_social_network_server.dtos.user.UpdateUserImgDto;
import api.v2.travel_social_network_server.entities.User;
//import api.v2.travel_social_network_server.entities.UserProfile;
import api.v2.travel_social_network_server.exceptions.ResourceNotFoundException;
//import api.v2.travel_social_network_server.reponses.PageableResponse;
//import api.v2.travel_social_network_server.reponses.post.PostResponse;
//import api.v2.travel_social_network_server.reponses.user.UpdateUserResponse;
//import api.v2.travel_social_network_server.reponses.user.UserResponse;
import api.v2.travel_social_network_server.responsitories.UserRepository;

//import api.v2.travel_social_network_server.utilities.GenderEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final static String AVATAR_FOLDER = "avatars";
    private final static String COVER_FOLDER = "covers";

    private final UserRepository userRepository;
//    private final CloudinaryService cloudinaryService;

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

//    @Transactional(readOnly = true)
//    public UserResponse getUserProfile(UUID userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//
//        return UserResponse.builder()
//                .userName(user.getUsername())
//                .userProfile(user.getUserProfile())
//                .avatarImg(user.getAvatarImg())
//                .coverImg(user.getCoverImg())
//                .build();
//    }
//
//
//    @Transactional
//    public UserResponse updateUserImg(UpdateUserImgDto updateUserImgDto, User user) throws IOException {
//        User exUser = userRepository.findById(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
//        String imgUrl = "";
//        User userUpdate = null;
//
//        if (updateUserImgDto.getAvatarImg() != null) {
//            imgUrl = cloudinaryService.uploadFile(updateUserImgDto.getAvatarImg().getBytes(), AVATAR_FOLDER, "image");
//            exUser.setAvatarImg(imgUrl);
//            log.info("Avatar url: {}", imgUrl);
//            userUpdate = userRepository.save(exUser);
//        }
//
//        if (updateUserImgDto.getCoverImg() != null) {
//            imgUrl = cloudinaryService.uploadFile(updateUserImgDto.getCoverImg().getBytes(), COVER_FOLDER, "image");
//            exUser.setCoverImg(imgUrl);
//            log.info("Cover url: {}", imgUrl);
//            userUpdate = userRepository.save(exUser);
//        }
//
//        return UserResponse.builder()
//                .userName(userUpdate.getUsername())
//                .userProfile(userUpdate.getUserProfile())
//                .avatarImg(userUpdate.getAvatarImg())
//                .coverImg(userUpdate.getCoverImg())
//                .build();
//    }
//
//    @Transactional
//    public UpdateUserResponse updateUserProfile(UpdateUserDto updateUserDto, User user) throws IOException {
//        User exUser = userRepository.findById(user.getUserId())
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        exUser.setUserName(updateUserDto.getUserName());
//
//        System.out.println(updateUserDto);
//
//        UserProfile profile = exUser.getUserProfile();
//        profile.setFirstName(updateUserDto.getFirstName());
//        profile.setLastName(updateUserDto.getLastName());
//        profile.setDateOfBirth(updateUserDto.getDateOfBirth());
//        profile.setLocation(updateUserDto.getLocation());
//        profile.setAbout(updateUserDto.getAbout());
//
//        String genderInput = updateUserDto.getGender().toLowerCase().trim();
//        switch (genderInput) {
//            case "male" -> profile.setGender(GenderEnum.MALE);
//            case "female" -> profile.setGender(GenderEnum.FEMALE);
//            default -> profile.setGender(GenderEnum.OTHER);
//        }
//
//        exUser.setUserProfile(profile);
//        User userUpdate = userRepository.save(exUser);
//
//        return UserResponse.builder()
//                .userName(userUpdate.getUsername())
//                .userProfile(userUpdate.getUserProfile())
//                .build();
//    }
//
//    @Transactional(readOnly = true)
//    public PageableResponse<UserResponse> searchUsersByKeyword(String keyword, int page, int pageSize) {
//        Pageable pageable = PageRequest.of(page, pageSize);
//        Page<User> userPage = userRepository.findByUserNameOrFullNameContainingIgnoreCase(keyword, pageable);
//
//        List<UserResponse> userResponses = userPage.getContent().stream()
//                .map(this::mapToUserResponse)
//                .toList();
//
//        return PageableResponse.<UserResponse>builder()
//                .content(userResponses)
//                .totalPages(userPage.getTotalPages())
//                .totalElements(userPage.getTotalElements())
//                .build();
//    }
//
//    private UserResponse mapToUserResponse(User user) {
//        return UserResponse.builder()
//                .userId(user.getUserId())
//                .userName(user.getUsername())
//                .avatarImg(user.getAvatarImg())
//                .coverImg(user.getCoverImg())
//                .userProfile(user.getUserProfile())
//                .build();
//    }
}
