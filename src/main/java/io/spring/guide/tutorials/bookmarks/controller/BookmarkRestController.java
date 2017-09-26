package io.spring.guide.tutorials.bookmarks.controller;

import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.spring.guide.tutorials.bookmarks.exception.UserNotFoundException;
import io.spring.guide.tutorials.bookmarks.model.Bookmark;
import io.spring.guide.tutorials.bookmarks.repository.AccountRepository;
import io.spring.guide.tutorials.bookmarks.repository.BookmarkRepository;

@RestController
@RequestMapping("/{userId}/bookmarks")
public class BookmarkRestController {
	
	private final BookmarkRepository bookmarkRepository;

	private final AccountRepository accountRepository;

	@Autowired
	BookmarkRestController(BookmarkRepository bookmarkRepository,
						   AccountRepository accountRepository) {
		this.bookmarkRepository = bookmarkRepository;
		this.accountRepository = accountRepository;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	Collection<Bookmark> readBookmarks(@PathVariable String userId) {
		this.validateUser(userId);
		return this.bookmarkRepository.findByAccountUsername(userId);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> add(@PathVariable String userId, @RequestBody Bookmark bookmark) {
		this.validateUser(userId);
		
		return this.accountRepository
				.findByUsername(userId)
				.map(account -> {
					Bookmark result = this.bookmarkRepository.save(new Bookmark(account, 
							bookmark.uri, bookmark.description));
					
					URI location = ServletUriComponentsBuilder
							.fromCurrentRequest().path("/{id}")
							.buildAndExpand(result.getId()).toUri();
					
					return ResponseEntity.created(location).build();
				}).orElse(ResponseEntity.noContent().build());
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{bookmarkId}")
	public Bookmark readBookmark(@PathVariable String userId, @PathVariable Long bookmarkId) {
		this.validateUser(userId);
		return this.bookmarkRepository.findOne(bookmarkId);
	}

	private void validateUser(String userId) {
		this.accountRepository.findByUsername(userId).orElseThrow(
				() -> new UserNotFoundException(userId));
	}

}
