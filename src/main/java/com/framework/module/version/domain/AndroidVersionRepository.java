package com.framework.module.version.domain;

import com.kratos.common.PageRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AndroidVersionRepository extends PageRepository<AndroidVersion> {


    @Query("select m from AndroidVersion m where m.versionCode >?1 order by m.versionCode desc")
    List<AndroidVersion> findNewestVersion(Integer versionCode);
}
