//package api.v2.travel_social_network_server.services;
//
//import api.v2.travel_social_network_server.dtos.user.UpdateUserDto;
//import api.v2.travel_social_network_server.dtos.user.UpdateUserImgDto;
//import api.v2.travel_social_network_server.entities.User;
//import api.v2.travel_social_network_server.responses.PageableResponse;
//import api.v2.travel_social_network_server.responses.user.UpdateUserResponse;
//import api.v2.travel_social_network_server.responses.user.UserResponse;
//import java.io.IOException;
//import java.util.UUID;
//
//public interface IUserService {
//    PageableResponse<UserResponse> searchUsersByKeyword(String keyword, int page, int pageSize);
//    UserResponse getUserProfile(UUID userId) throws IOException;
//    UserResponse updateUserImg(UpdateUserImgDto updateUserImgDto, User user) throws IOException;
//    UpdateUserResponse updateUserProfile(UpdateUserDto updateUserDto, User user) throws IOException;
//}