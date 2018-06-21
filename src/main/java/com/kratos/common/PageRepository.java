package com.kratos.common;

import com.kratos.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface PageRepository<T extends BaseEntity> extends CrudRepository<T, String>, JpaSpecificationExecutor<T> {
}
