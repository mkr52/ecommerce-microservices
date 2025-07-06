package com.mkr.ecom.user.services;

import com.mkr.ecom.user.dto.AddressDTO;
import com.mkr.ecom.user.dto.UserRequest;
import com.mkr.ecom.user.dto.UserResponse;
import com.mkr.ecom.user.models.Address;
import com.mkr.ecom.user.models.User;
import com.mkr.ecom.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository; // it is final coz we are using constructor injection
//    private List<User> users = new ArrayList<>();
//    private Long nextUserId = 1L;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> fetchAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    public void addUser(UserRequest request) {
//        user.setId(nextUserId++);
        User user = new User();
        updateUserFromRequest(user,request);
        userRepository.save(user);
//        users.add(user);
//        return users;
    }

    public Optional<UserResponse> fetchUser(String id) {
        return userRepository.findById(id).map(this::mapToUserResponse);
    }

    public boolean editUser(String id, UserRequest updatedUserRequest) {
//        return users.stream()
//                .filter(u -> u.getId().equals(id))
//                        .findFirst()
//                .map(existingUser -> {
//                    existingUser.setFirstName(user.getFirstName());
//                    existingUser.setLastName(user.getLastName());
//                    return true;
//                }).orElse(false);

        return userRepository.findById(id)
                .map(existingUser -> {
                    updateUserFromRequest(existingUser, updatedUserRequest);
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName() != null ? userRequest.getFirstName() : user.getFirstName());
        user.setLastName(userRequest.getLastName() != null ? userRequest.getLastName(): user.getLastName());
        user.setEmail(userRequest.getEmail() != null ? userRequest.getEmail() : user.getEmail());
        user.setPhone(userRequest.getPhone() != null ? userRequest.getPhone() : user.getPhone());
        if (userRequest.getAddress() != null) {
            Address address = new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setState(userRequest.getAddress().getState());
            address.setZipcode(userRequest.getAddress().getZipcode());
            address.setCity(userRequest.getAddress().getCity());
            address.setCountry(userRequest.getAddress().getCountry());
            user.setAddress(address);
        }
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId().toString())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .address( user.getAddress() == null ? null :
                        AddressDTO.builder()
                                .street(user.getAddress().getStreet())
                                .city(user.getAddress().getCity())
                                .state(user.getAddress().getState())
                                .country(user.getAddress().getCountry())
                                .zipcode(user.getAddress().getZipcode())
                                .build()
                )
                .build();
    }
}
