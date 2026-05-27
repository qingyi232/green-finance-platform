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
@TableName("t_finance_product")
public class FinanceProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String productCode;
    private String name;
    private String category;
    private String bankName;
    private String bankLogo;
    private Long bankId;
    private String cover;
    private BigDecimal loanMin;
    private BigDecimal loanMax;
    private BigDecimal rateMin;
    private BigDecimal rateMax;
    private Integer termMin;
    private Integer termMax;
    private String acceptTime;
    private String applyCondition;
    private String applyMaterials;
    private String applyProcess;
    private String targetUsers;
    private String tags;
    private String intro;
    private Integer applyCount;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
}
