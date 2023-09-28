package com.bayclip.category.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class Category {
    @Id
    private String id;

    private String name;

}
