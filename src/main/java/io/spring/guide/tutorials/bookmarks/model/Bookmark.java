package io.spring.guide.tutorials.bookmarks.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Bookmark {

    @Id
    @GeneratedValue
    private Long id;
    
    @JsonIgnore
    @ManyToOne
    private Account account;

    @NotEmpty(message = "Uri is required")
    public String uri;

    @NotEmpty(message = "Description is required")
    public String description;

    public Bookmark() {
   
    }

    public Bookmark(Account account, String uri, String description) {
        this.uri = uri;
        this.description = description;
        this.account = account;
    }
    
    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public String getUri() {
        return uri;
    }

    public String getDescription() {
        return description;
    }

}
