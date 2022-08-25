package app.controller;

import app.annotation.OptLog;
import app.entity.Comment;
import app.entity.R;
import app.service.ICommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 评论控制区
 *
 * @author 徐鑫
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private ICommentService commentService;

    @PostMapping
    public R release(@RequestBody Comment comment) {
        return commentService.release(comment);
    }

    @DeleteMapping
    public R delete(@RequestBody Comment comment) {
        return commentService.delete(comment);
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
