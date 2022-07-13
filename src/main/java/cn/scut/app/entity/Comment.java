package cn.scut.app.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论的实体类
 * @author 徐鑫
 */
@Data
public class Comment {
    private Long id;
    private String content;
    private LocalDateTime time;
    private Integer likeCount;
    private String userId;
    private Long topicId;
}
