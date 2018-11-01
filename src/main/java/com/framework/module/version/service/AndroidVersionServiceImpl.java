package com.framework.module.version.service;

import com.framework.module.version.domain.AndroidVersion;
import com.framework.module.version.domain.AndroidVersionRepository;
import com.kratos.common.AbstractCrudService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@Transactional
public class AndroidVersionServiceImpl extends AbstractCrudService<AndroidVersion> implements AndroidVersionService {

    private final AndroidVersionRepository androidVersionRepository;

    public AndroidVersionServiceImpl(AndroidVersionRepository androidVersionRepository) {
        this.androidVersionRepository = androidVersionRepository;
    }

    @Override
    public AndroidVersion getNewestVersion(Integer versionCode) {
        List<AndroidVersion> result = androidVersionRepository.findNewestVersion(versionCode);
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        return result.get(0);
    }
}
