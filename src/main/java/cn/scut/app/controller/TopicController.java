package cn.scut.app.controller;

import cn.scut.app.entity.R;
import cn.scut.app.entity.Topic;
import cn.scut.app.service.ITopicService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 论坛的主贴控制层
 * @author 徐鑫
 */
@RestController
@RequestMapping("/topic")
public class TopicController {

    @Resource
    private ITopicService topicService;

    @PostMapping
    public R release(@RequestBody Topic topic, @RequestHeader String token){
        return topicService.release(topic, token);
    }

    @DeleteMapping
    public R delete(@RequestBody Topic topic, @RequestHeader String token) {
        return topicService.delete(topic, token);
    }

    @PutMapping
    public R update(@RequestBody Topic topic, @RequestHeader String token) {
        return topicService.update(topic, token);
    }

    @GetMapping("/id/{id}")
    public R findById(@PathVariable Long id) {
        return topicService.findById(id);
    }

    @GetMapping("/type/{type}")
    public R findByType(@PathVariable Long type) {
        return topicService.findByType(type);
    }

    @GetMapping("/page")
    public R findByPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        return topicService.findByPage(pageNum, pageSize);
    }

}
