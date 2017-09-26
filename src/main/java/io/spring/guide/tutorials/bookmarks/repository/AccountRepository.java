package io.spring.guide.tutorials.bookmarks.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.spring.guide.tutorials.bookmarks.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByUsername(String username);
	
}
