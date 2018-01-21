package hello.article.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import hello.article.model.Tag;

public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {
}
