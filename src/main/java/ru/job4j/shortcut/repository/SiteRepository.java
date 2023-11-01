package ru.job4j.shortcut.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.shortcut.model.Site;

public interface SiteRepository extends CrudRepository<Site, Integer> {

    Optional<Site> findByLogin(String login);

}