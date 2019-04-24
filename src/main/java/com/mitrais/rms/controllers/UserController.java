package com.mitrais.rms.controllers;

import java.util.Optional;

import javax.validation.Valid;

import com.mitrais.rms.domains.User;
import com.mitrais.rms.repositories.RoleRepository;
import com.mitrais.rms.repositories.UserRepository;
import com.mitrais.rms.validators.UserValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class UserController {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @InitBinder("user")
    public void initUserBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(new UserValidator());
    }

    @GetMapping("/users")
    public String index(Model model, Pageable pageable, Authentication auth) {
        Iterable<User> users = this.userRepository.findAll(pageable);
        model.addAttribute("users", users);
        return "users/index";
    }

    @GetMapping("/users/create")
    public String create(ModelMap model) {
        model.put("user", new User());
        model.put("roles", this.roleRepository.findAll());
        return "users/create";
    }

    @PostMapping("/users/create")
    public String save(@Valid User user, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.put("user", user);
            return "user/create";
        } else {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(user);
            return "redirect:/admin/users";
        }
    }

    @GetMapping("/users/{id}/edit")
    public String edit(@PathVariable("id") Long id, ModelMap model) {

        Optional<User> result = userRepository.findById(id);

        if (!result.isPresent()) {
            return "redirect:/admin/users";
        } else {
            model.put("user", result.get());
            return "users/edit";
        }
    }

    @PostMapping("/users/{id}/edit")
    public String update(@Valid User user, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.put("user", user);
            return "users/edit";
        } else {
            userRepository.save(user);
            return "redirect:/admin/users";
        }
    }

    @GetMapping("/users/{id}/delete")
    public String edit(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }
}
