package cn.scut.app.controller;

import cn.scut.app.entity.Comment;
import cn.scut.app.entity.R;
import cn.scut.app.service.ICommentService;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 评论控制区
 * @author 徐鑫
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private ICommentService commentService;

    @PostMapping
    public R release(@RequestBody Comment comment, @RequestHeader String token) {
        return commentService.release(comment, token);
    }

    @DeleteMapping
    public R delete(@RequestBody Comment comment, @RequestHeader String token) {
        return commentService.delete(comment, token);
    }

    @GetMapping("/id/{id}")
    public R findById(@PathVariable Long id) {
        return commentService.findById(id);
    }

    @GetMapping("/topic/{topicId}")
    public R findByTopicId(@PathVariable Long topicId) {
        return commentService.findByTopicId(topicId);
    }
}
