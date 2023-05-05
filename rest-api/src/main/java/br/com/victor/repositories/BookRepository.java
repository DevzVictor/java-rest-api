package br.com.victor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.victor.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
