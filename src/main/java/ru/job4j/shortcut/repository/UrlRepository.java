package ru.job4j.shortcut.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.shortcut.model.Url;

public interface UrlRepository extends CrudRepository<Url, Integer> { }