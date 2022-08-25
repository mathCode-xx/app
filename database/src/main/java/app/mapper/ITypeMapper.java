package app.mapper;

import app.entity.Type;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 与数据库type表的映射类
 * @author 徐鑫
 */
@Mapper
public interface ITypeMapper {

    /**
     * 根据id获取类型
     * @param id 需要查询的id
     * @return 查询到的结果
     */
    @Select("select * from type where id = #{id};")
    Type getById(int id);
}
