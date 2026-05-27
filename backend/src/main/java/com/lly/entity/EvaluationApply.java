package com.lly.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_evaluation_apply")
public class EvaluationApply implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String applyNo;
    private Long applicantId;
    private String applicantName;
    private String applicantPhone;
    private Long orgId;
    private String orgName;
    private Long evaluatorId;
    private String evaluatorName;
    private String resourceType;
    private Long resourceId;
    private String resourceName;
    private String purpose;
    private String requirement;
    private String attachments;
    private String step;
    private String status;
    private BigDecimal resultValue;
    private String resultSummary;
    private String resultReportUrl;
    private String feedback;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
}
