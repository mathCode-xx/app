package app.service;

import app.entity.Type;

/**
 * 与type有关的业务
 * @author 徐鑫
 */
public interface ITypeService {
    /**
     * 根据id查询type实体
     * @param id 需要查询的id
     * @return 查询到的type实体
     */
    Type getTypeById(int id);
}
