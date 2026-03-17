package com.imaugar.task_manager_api.services;

import org.springframework.stereotype.Service;
import com.imaugar.task_manager_api.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

//Se encarga de cargar los datos del usuario
@Service
public class UserDetailService implements UserDetailsService{
    private final UserRepository userRepository;

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No se ha encontrdo un usuario con ese nombre: " + username));
    }
}
