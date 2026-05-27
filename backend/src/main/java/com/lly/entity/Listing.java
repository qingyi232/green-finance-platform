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
@TableName("t_listing")
public class Listing implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String listingNo;
    private String title;
    private String category;
    private String subCategory;
    private String cover;
    private String images;
    private String intro;
    private String ownership;
    private String manageOrg;
    private String region;
    private BigDecimal areaMu;
    private BigDecimal price;
    private String priceUnit;
    private BigDecimal valuationMin;
    private BigDecimal valuationMax;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;
    private Integer acceptAppointment;
    private Integer viewCount;
    private Integer appointmentCount;
    private String auditStatus;
    private String auditMsg;
    private String status;
    private Long sellerId;
    private String sellerName;
    private String sellerPhone;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
}
