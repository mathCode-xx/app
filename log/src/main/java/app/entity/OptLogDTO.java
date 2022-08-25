package app.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OptLogDTO {

    public static final String OPT_SUCCESS_MSG = "操作成功";
    public static final String OPT_FAIL_MSG = "操作失败";

    private Long id;

    private String operatorId;

    /**
     * 操作描述
     */
    private String description;

    private String optType;

    private String params;

    private LocalDateTime optTime;
}
