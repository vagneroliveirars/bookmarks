package io.spring.guide.tutorials.bookmarks.controller;

import io.spring.guide.tutorials.bookmarks.exception.UserNotFoundException;
import io.spring.guide.tutorials.bookmarks.model.Bookmark;
import io.spring.guide.tutorials.bookmarks.repository.AccountRepository;
import io.spring.guide.tutorials.bookmarks.repository.BookmarkRepository;
import io.spring.guide.tutorials.bookmarks.restResource.BookmarkResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookmarks")
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
	Resources<BookmarkResource> readBookmarks(Principal principal) {
		this.validateUser(principal);
		
		List<BookmarkResource> bookmarkResourceList = bookmarkRepository
				.findByAccountUsername(principal.getName()).stream().map(BookmarkResource::new)
				.collect(Collectors.toList());

		return new Resources<>(bookmarkResourceList);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> add(Principal principal,@Valid @RequestBody Bookmark bookmark) {
		this.validateUser(principal);
		
		return this.accountRepository
				.findByUsername(principal.getName())
				.map(account -> {
					Bookmark result = this.bookmarkRepository
							.save(new Bookmark(account, bookmark.uri, bookmark.description));
					
					Link forOneBookmark = new BookmarkResource(result).getLink("self");
					
					return ResponseEntity.created(URI.create(forOneBookmark.getHref())).build();
				})
				.orElse(ResponseEntity.noContent().build());
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{bookmarkId}")
	public BookmarkResource readBookmark(Principal principal, @PathVariable Long bookmarkId) {
		this.validateUser(principal);
		
		return new BookmarkResource(this.bookmarkRepository.findOne(bookmarkId));
	}

	private void validateUser(Principal principal) {
		this.accountRepository.findByUsername(principal.getName()).orElseThrow(
				() -> new UserNotFoundException(principal.getName()));
	}

}
