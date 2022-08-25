package app.controller;

import app.annotation.OptLog;
import app.entity.R;
import app.entity.Topic;
import app.service.ITopicService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 论坛的主贴控制层
 *
 * @author 徐鑫
 */
@RestController
@RequestMapping("/topic")
public class TopicController {

    @Resource
    private ITopicService topicService;

    /**
     * 发布
     */
    @PostMapping
    public R release(@RequestBody Topic topic) {
        return topicService.release(topic);
    }

    /**
     * 删除
     */
    @DeleteMapping
    public R delete(@RequestBody Topic topic) {
        return topicService.delete(topic);
    }

    /**
     * 更新
     */
    @PutMapping
    public R update(@RequestBody Topic topic) {
        return topicService.update(topic);
    }

    /**
     * 根据id查找，应用场景，显示详情
     */
    @GetMapping("/id/{id}")
    public R findById(@PathVariable Long id) {
        return topicService.findById(id);
    }

    /**
     * 根据类型查找，场景：分类
     */
    @GetMapping("/type/{type}")
    public R findByType(@PathVariable Long type) {
        return topicService.findByType(type);
    }

    /**
     * 分页查找，场景：查找全部
     */
    @GetMapping("/page")
    public R findByPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        return topicService.findByPage(pageNum, pageSize);
    }

    /**
     * 根据用户id查找，场景：查看个人讨论
     */
    @GetMapping("/user_id/{id}")
    public R findByUserId(@PathVariable String id) {
        return topicService.findByUser(id);
    }

}
