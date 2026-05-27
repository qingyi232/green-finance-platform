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
@TableName("t_demand")
public class Demand implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String demandNo;
    private String title;
    private String category;
    private String subCategory;
    private String cover;
    private String intro;
    private String targetRegion;
    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    private BigDecimal areaDemand;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;
    private Integer acceptAppointment;
    private Integer viewCount;
    private Integer appointmentCount;
    private String auditStatus;
    private String status;
    private Long buyerId;
    private String buyerName;
    private String buyerPhone;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
}
