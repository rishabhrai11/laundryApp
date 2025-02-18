package com.risha.laundryApp.controllers;



import com.risha.laundryApp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam String newPassword, @RequestParam String confirmPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            if (newPassword.equals(confirmPassword)) {
                userService.updatePassword(username, newPassword);
                return ResponseEntity.ok("Password changed successfully.");
            }
            else{
                throw new Exception("Passwords do not match") ;
            }
        }
        catch(Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            return ResponseEntity.ok(userService.findByUsername(username));
        }
        catch(Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
    }
}

