package hello.article.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import hello.article.model.Article;

public interface ArticleRepository extends PagingAndSortingRepository<Article, Long> {
}
