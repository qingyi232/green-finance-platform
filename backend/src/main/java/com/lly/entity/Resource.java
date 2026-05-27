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
@TableName("t_resource")
public class Resource implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String resourceNo;
    private String resourceName;
    private String resourceType;
    private String forestType;
    private BigDecimal areaMu;
    private BigDecimal carbonStorage;
    private String region;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String boundary;
    private String ownerName;
    private String ownerIdCard;
    private String certNo;
    private String contractUrl;
    private String cover;
    private String images;
    private String intro;
    private String status;
    private String auditMsg;
    private String auditBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTime;
    private Long userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
}
