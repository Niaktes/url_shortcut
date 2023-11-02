package ru.job4j.shortcut.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.shortcut.model.Url;

public interface UrlRepository extends CrudRepository<Url, Integer> {

    Optional<Url> findByCode(String code);

    @Transactional
    @Modifying
    @Query(value = "UPDATE urls SET call_number = call_number + 1 WHERE id = ?", nativeQuery = true)
    void incrementCount(int id);

}