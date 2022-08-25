package app.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 主贴实体类
 * @author 徐鑫
 */
@Data
public class Topic {
    private long id;
    private String content;
    /**
     * 评论数
     */
    private int commentCount;
    /**
     * 点赞数
     */
    private int likeCount;
    /**
     * 收藏数
     */
    private int collectionCount;
    private LocalDateTime time;
    private String userId;
    private int typeId;
    private LocalDateTime updateTime;
}
