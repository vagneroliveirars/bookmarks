package io.spring.guide.tutorials.bookmarks.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import io.spring.guide.tutorials.bookmarks.model.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	Collection<Bookmark> findByAccountUsername(String username);
	
}
