package io.spring.guide.tutorials.bookmarks.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.spring.guide.tutorials.bookmarks.exception.UserNotFoundException;
import io.spring.guide.tutorials.bookmarks.model.Bookmark;
import io.spring.guide.tutorials.bookmarks.repository.AccountRepository;
import io.spring.guide.tutorials.bookmarks.repository.BookmarkRepository;
import io.spring.guide.tutorials.bookmarks.restResource.BookmarkResource;

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
	Resources<BookmarkResource> readBookmarks(@PathVariable String userId) {
		this.validateUser(userId);
		
		List<BookmarkResource> bookmarkResourceList = bookmarkRepository
				.findByAccountUsername(userId).stream().map(BookmarkResource::new)
				.collect(Collectors.toList());

		return new Resources<>(bookmarkResourceList);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> add(@PathVariable String userId, @RequestBody Bookmark bookmark) {
		this.validateUser(userId);
		
		return this.accountRepository
				.findByUsername(userId)
				.map(account -> {
					Bookmark result = this.bookmarkRepository
							.save(new Bookmark(account, bookmark.uri, bookmark.description));
					
					Link forOneBookmark = new BookmarkResource(result).getLink("self");
					
					return ResponseEntity.created(URI.create(forOneBookmark.getHref())).build();
				})
				.orElse(ResponseEntity.noContent().build());
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{bookmarkId}")
	public BookmarkResource readBookmark(@PathVariable String userId, @PathVariable Long bookmarkId) {
		this.validateUser(userId);
		
		return new BookmarkResource(this.bookmarkRepository.findOne(bookmarkId));
	}

	private void validateUser(String userId) {
		this.accountRepository.findByUsername(userId).orElseThrow(
				() -> new UserNotFoundException(userId));
	}

}
