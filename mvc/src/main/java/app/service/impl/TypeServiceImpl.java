package app.service.impl;

import app.entity.Type;
import app.mapper.ITypeMapper;
import app.service.ITypeService;
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
