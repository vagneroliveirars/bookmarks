package io.spring.guide.tutorials.bookmarks.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Account {

	@Id
    @GeneratedValue
    private Long id;
	
    public String username;
    
    @JsonIgnore
    public String password;
	
    @OneToMany(mappedBy = "account")
    private Set<Bookmark> bookmarks = new HashSet<>();
    
    public Account(String name, String password) {
        this.username = name;
        this.password = password;
    }

    public Account() { 
   
    }
    
    public Long getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }

    public Set<Bookmark> getBookmarks() {
        return bookmarks;
    }

}
    

   