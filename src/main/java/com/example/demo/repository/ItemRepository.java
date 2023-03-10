package com.example.demo.repository;

import com.example.demo.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item) {
        if(item.getId() == null) {
            em.persist(item); //없으면 영속성 컨텍스트에 item 넣음
        } else {
            em.merge(item); // Update 와 유사
        }
    }
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }
    public List<Item> findAll() {
        return em.createQuery("select i from i", Item.class)
                .getResultList();
    }
}
