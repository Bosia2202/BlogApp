package com.denisvasilenko.blogapp.services;

import com.denisvasilenko.blogapp.DTO.Search.ArticleDtoSearchPreview;
import com.denisvasilenko.blogapp.DTO.Search.SearchQueryDto;
import com.denisvasilenko.blogapp.DTO.Search.UserDtoSearchPreview;
import com.denisvasilenko.blogapp.models.Article;
import com.denisvasilenko.blogapp.models.User;
import com.denisvasilenko.blogapp.repositories.ArticleRepository;
import com.denisvasilenko.blogapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class SearchService {

private final ArticleRepository articleRepository;
private final UserRepository userRepository;
private final short ARTICLE_FILTER_ID = 0;
private final short USER_FILTER_ID = 1;
    public ResponseEntity<?> search (SearchQueryDto searchQueryDto) {
        short filterId = searchQueryDto.searchId();
        switch (filterId) {
            case ARTICLE_FILTER_ID: {
                List<ArticleDtoSearchPreview> articleDtoPreviewList = convertFoundArticleListToArticleDtoForSearch(searchQueryDto.searchQuery(),
                        articleRepository::findAllByNameArticleContainingIgnoreCaseOrderByDateOfCreation);
                return new ResponseEntity<>(articleDtoPreviewList,HttpStatus.OK);
            }
            case USER_FILTER_ID: {
                List<UserDtoSearchPreview> foundedUsers = convertFoundUserListToUserDtoForSearch(searchQueryDto.searchQuery(),userRepository::findAllUsersByUsernameContainingIgnoreCase);
                return new ResponseEntity<>(foundedUsers,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private List<ArticleDtoSearchPreview> convertFoundArticleListToArticleDtoForSearch(String searchParam, Function<String, List<Article>> finder) {
        return finder.apply(searchParam).stream().map(article ->
        {
            return new ArticleDtoSearchPreview(
                    article.getId().toString(),
                    article.getNameArticle(),
                    article.getUserOwner().getUsername(),
                    article.getDateOfCreation().toString());
        }).toList();
    }

    private List<UserDtoSearchPreview> convertFoundUserListToUserDtoForSearch(String searchParam, Function<String, List<User>> finder) {
        return finder.apply(searchParam).stream().map(user -> {
          return new UserDtoSearchPreview(user.getUsername());
        }).toList();
    }
}
