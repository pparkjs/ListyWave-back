package com.listywave.collection.repository.custom;

import com.listywave.collection.application.domain.Collect;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomCollectionRepository {

    Slice<Collect> getAllCollectionList(Long cursorId, Pageable pageable, Long userId);
}