package com.blog.aigetcode.repositories;

import com.blog.aigetcode.entity.Commentary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentaryRepository  extends CrudRepository<Commentary, Long> {
}
