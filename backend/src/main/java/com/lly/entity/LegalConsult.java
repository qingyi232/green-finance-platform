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
@TableName("t_legal_consult")
public class LegalConsult implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String consultNo;
    private String consultType;
    private String category;
    private String urgency;
    private Long applicantId;
    private String applicantName;
    private String applicantPhone;
    private String applicantRegion;
    private Long targetOrgId;
    private String targetOrgName;
    private Long targetLawyerId;
    private String targetLawyerName;
    private String description;
    private String attachments;
    private String prevHandling;
    private String status;
    private String feedback;
    private String serviceRecord;
    private BigDecimal serviceFee;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
}
