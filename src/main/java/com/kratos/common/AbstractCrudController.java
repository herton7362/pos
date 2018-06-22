package com.kratos.common;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 提供基本增删改查
 * @author tang he
 * @since 1.0.1
 * @param <T> 实现增删改查的实体
 */
public abstract class AbstractCrudController<T extends BaseEntity> extends AbstractReadController<T> {
    /**
     * 保存
     */
    @ApiOperation(value="保存")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<T> save(@RequestBody T t) throws Exception {
        t = crudService.save(t);
        return new ResponseEntity<>(t, HttpStatus.OK);
    }

    /**
     * 删除
     */
    @ApiOperation(value="删除默认为逻辑删除")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<T> delete(@PathVariable String id) throws Exception {
        String[] ids = id.split(",");
        for (String s : ids) {
            crudService.delete(s);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 调整排序
     */
    @ApiOperation(value="调整排序")
    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    public ResponseEntity<T> sort(@RequestBody List<T> ts) throws Exception {
        crudService.sort(ts);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 启用
     */
    @ApiOperation(value="启用")
    @RequestMapping(value = "/enable/{id}", method = RequestMethod.POST)
    public ResponseEntity<T> enable(@PathVariable String id) throws Exception {
        String[] ids = id.split(",");
        for (String s : ids) {
            crudService.enable(s);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 停用
     */
    @ApiOperation(value="停用")
    @RequestMapping(value = "/disable/{id}", method = RequestMethod.POST)
    public ResponseEntity<T> disable(@PathVariable String id) throws Exception {
        String[] ids = id.split(",");
        for (String s : ids) {
            crudService.disable(s);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
