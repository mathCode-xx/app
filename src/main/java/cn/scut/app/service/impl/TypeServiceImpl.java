package cn.scut.app.service.impl;

import cn.scut.app.entity.Type;
import cn.scut.app.mapper.ITypeMapper;
import cn.scut.app.service.ITypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * type业务实现类
 *
 * @author 徐鑫
 */
@Service
public class TypeServiceImpl implements ITypeService {

    @Resource
    private ITypeMapper typeMapper;

    @Override
    public Type getTypeById(int id) {
        if (id < 0) {
            return null;
        }
        return typeMapper.getById(id);
    }
}
