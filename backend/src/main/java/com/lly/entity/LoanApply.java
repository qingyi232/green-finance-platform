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
@TableName("t_loan_apply")
public class LoanApply implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String applyNo;
    private Long productId;
    private String productName;
    private Long bankId;
    private String bankName;
    private Long applicantId;
    private String applicantName;
    private String applicantPhone;
    private String applicantRole;
    private BigDecimal loanAmount;
    private Integer loanTerm;
    private String purpose;
    private String collateral;
    private String applicantInfo;
    private String materials;
    private String step;
    private String status;
    private String reviewMsg;
    private Long reviewerId;
    private String reviewerName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reviewedTime;
    private BigDecimal approvedAmount;
    private BigDecimal approvedRate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
}
