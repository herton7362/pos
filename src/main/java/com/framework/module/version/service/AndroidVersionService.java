package com.framework.module.version.service;

import com.framework.module.version.domain.AndroidVersion;
import com.kratos.common.CrudService;

public interface AndroidVersionService extends CrudService<AndroidVersion> {

    AndroidVersion getNewestVersion(Integer versionCode);

}
