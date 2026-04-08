package OM.Bakery.controller;

import OM.Bakery.model.User;
import OM.Bakery.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login-form";
        }
        // Отримуємо дані користувача по email (username в Spring Security)
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        return "profile";
    }
}