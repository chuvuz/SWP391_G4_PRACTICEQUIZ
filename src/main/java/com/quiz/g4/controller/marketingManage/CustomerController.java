package com.quiz.g4.controller.marketingManage;

import com.quiz.g4.entity.User;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/marketing")
public class CustomerController {
    @Autowired
    private UserService userService;
    @GetMapping("/customers")
    public String viewCustomerUsers(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    Model model) {
        Page<User> customers = userService.findByRole("ROLE_CUSTOMER", PageRequest.of(page, size));
        model.addAttribute("customers", customers);
        return "marketing/view-customers";
    }

    @PostMapping("/customers/update-status")
    public String updateUserStatus(@RequestParam("userId") Integer userId) {
        User user = userService.findUserByUserId(userId);
        userService.changeUserStatus(user);
        return "redirect:/marketing/customers";
    }

    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        long totalCustomers = userService.countUsersByRole("ROLE_CUSTOMER");
        long activeCustomers = userService.countUsersByRoleAndStatus("ROLE_CUSTOMER", true);
        long inactiveCustomers = userService.countUsersByRoleAndStatus("ROLE_CUSTOMER", false);

        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("activeCustomers", activeCustomers);
        model.addAttribute("inactiveCustomers", inactiveCustomers);

        return "marketing/dashboard";
    }
}
