package app.service;

import app.entity.R;
import app.entity.Topic;

/**
 * 主贴业务
 * @author 徐鑫
 */
public interface ITopicService {
    /**
     * 发布主贴
     * @param topic 需要发布的主贴
     * @return 返回操作信息
     */
    R release(Topic topic);

    /**
     * 根据id删除topic
     * @param topic 需要删除的topic
     * @return 返回操作信息
     */
    R delete(Topic topic);

    /**
     * 修改 topic
     * @param topic 修改后的 topic
     * @return 操作信息
     */
    R update(Topic topic);

    /**
     * 根据id查topic
     * @param id 需要查询的topic id
     * @return 查询到的结果
     */
    R findById(Long id);

    /**
     * 根据type查询topics
     * @param type 需要查询的type
     * @return 操作结果
     */
    R findByType(Long type);

    /**
     * 分页查询
     * @param page 页码
     * @param pageSize 页的大小
     * @return 当前页数据
     */
    R findByPage(Integer page, Integer pageSize);

    /**
     * 根据用户id查找
     */
    R findByUser(String userId);
}
