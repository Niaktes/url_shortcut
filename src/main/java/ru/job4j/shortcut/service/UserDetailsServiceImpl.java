package ru.job4j.shortcut.service;

import java.util.Collections;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.repository.SiteRepository;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SiteRepository siteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Site> siteOptional = siteRepository.findByLogin(username);
        if (siteOptional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return new User(siteOptional.get().getLogin(), siteOptional.get().getPassword(),
                Collections.emptyList());
    }

}