package com.blog.aigetcode.repositories;


import com.blog.aigetcode.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

    @Query("select e.id, e.name from Category e order by e.id asc")
    List<Object[]> getCategories();

    Set<Category> findTop5ByOrderByPostListAscIdAsc();

    List<Category> findAllByIdIn(List<Long> id);

    List<Category> findAll();

}
