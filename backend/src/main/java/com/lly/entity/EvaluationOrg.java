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
@TableName("t_evaluation_org")
public class EvaluationOrg implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orgName;
    private String businessLicense;
    private String businessScope;
    private String officeAddress;
    private String phone;
    private String email;
    private String website;
    private String logo;
    private String qualificationUrl;
    private String intro;
    private String expertise;
    private Integer evaluatorCount;
    private Integer caseCount;
    private BigDecimal rating;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
}
