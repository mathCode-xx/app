package app.mapper;

import app.entity.OptLogDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 日志与数据库的交互
 * @author 徐鑫
 */
@Mapper
public interface ILogMapper {

    @Select("select * from log;")
    List<OptLogDTO> findAll();

    @Select("select * from log where id = #{id}")
    OptLogDTO findById(Long id);

    @Insert("insert into log() value(#{id}, #{operatorId}, #{optType}, #{description}, #{params}, #{optTime})")
    int insert(OptLogDTO optLogDTO);

    @Delete("delete from log where id = #{id}")
    int delete(Long id);
}
