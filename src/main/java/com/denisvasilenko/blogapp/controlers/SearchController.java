package com.denisvasilenko.blogapp.controlers;

import com.denisvasilenko.blogapp.DTO.Search.SearchQueryDto;
import com.denisvasilenko.blogapp.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {
    private final SearchService searchService;
    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchQueryDto searchQueryDto) {
        return searchService.search(searchQueryDto);
    }
}
