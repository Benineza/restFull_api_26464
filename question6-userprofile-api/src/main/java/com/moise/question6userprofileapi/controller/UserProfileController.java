package com.moise.question6userprofileapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moise.question6userprofileapi.model.ApiResponse;
import com.moise.question6userprofileapi.model.UserProfile;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private List<UserProfile> userList = new ArrayList<>();

    public UserProfileController() {

        userList.add(new UserProfile(1L, "john_doe", "john@example.com", "John Doe", 25, "USA", "Developer", true));
        userList.add(new UserProfile(2L, "jane_smith", "jane@example.com", "Jane Smith", 30, "UK", "Designer", true));
        userList.add(new UserProfile(3L, "ali_khan", "ali@example.com", "Ali Khan", 22, "Pakistan", "Student", false));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserProfile>>> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse<>(true, "All users retrieved", userList));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> getUserById(@PathVariable Long userId) {
        return userList.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .map(user -> ResponseEntity.ok(new ApiResponse<>(true, "User found", user)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "User not found", null)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserProfile>>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge) {

        List<UserProfile> results = userList.stream()
                .filter(u -> (username == null || u.getUsername().toLowerCase().contains(username.toLowerCase())) &&
                             (country == null || u.getCountry().equalsIgnoreCase(country)) &&
                             (minAge == null || u.getAge() >= minAge) &&
                             (maxAge == null || u.getAge() <= maxAge))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Search results", results));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserProfile>> createUser(@RequestBody UserProfile newUser) {
        newUser.setUserId((long) (userList.size() + 1));
        userList.add(newUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User profile created successfully", newUser));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> updateUser(@PathVariable Long userId,
                                                               @RequestBody UserProfile updatedUser) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUserId().equals(userId)) {
                updatedUser.setUserId(userId);
                userList.set(i, updatedUser);
                return ResponseEntity.ok(new ApiResponse<>(true, "User profile updated", updatedUser));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User not found", null));
    }

    @PatchMapping("/{userId}/activate")
    public ResponseEntity<ApiResponse<UserProfile>> activateUser(@PathVariable Long userId) {
        for (UserProfile u : userList) {
            if (u.getUserId().equals(userId)) {
                u.setActive(true);
                return ResponseEntity.ok(new ApiResponse<>(true, "User activated", u));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User not found", null));
    }

    @PatchMapping("/{userId}/deactivate")
    public ResponseEntity<ApiResponse<UserProfile>> deactivateUser(@PathVariable Long userId) {
        for (UserProfile u : userList) {
            if (u.getUserId().equals(userId)) {
                u.setActive(false);
                return ResponseEntity.ok(new ApiResponse<>(true, "User deactivated", u));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User not found", null));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        boolean removed = userList.removeIf(u -> u.getUserId().equals(userId));
        if (removed) {
            return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User not found", null));
    }
}
