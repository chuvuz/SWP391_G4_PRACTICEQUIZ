package com.quiz.g4.converters;

import com.quiz.g4.entity.Role;
import com.quiz.g4.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToRoleConverter implements Converter<String, Role> {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role convert(String source) {
        return roleRepository.findByName(source).orElseThrow(() -> new IllegalArgumentException("Invalid role: " + source));
    }
}
