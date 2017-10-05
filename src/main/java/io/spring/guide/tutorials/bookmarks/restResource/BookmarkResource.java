package io.spring.guide.tutorials.bookmarks.restResource;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import io.spring.guide.tutorials.bookmarks.controller.BookmarkRestController;
import io.spring.guide.tutorials.bookmarks.model.Bookmark;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

public class BookmarkResource extends ResourceSupport {
	
	private final Bookmark bookmark;

	public BookmarkResource(Bookmark bookmark) {
		String username = bookmark.getAccount().getUsername();
		this.bookmark = bookmark;
		this.add(new Link(bookmark.getUri(), "bookmark-uri"));
		this.add(linkTo(BookmarkRestController.class, username).withRel("bookmarks"));
		this.add(linkTo(methodOn(BookmarkRestController.class, username)
				.readBookmark(username, bookmark.getId())).withSelfRel());
	}
	
	public Bookmark getBookmark() {
		return bookmark;
	}

}
