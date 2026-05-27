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
@TableName("t_lawyer")
public class Lawyer implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orgId;
    private String orgName;
    private String name;
    private String gender;
    private String avatar;
    private String certNo;
    private Integer years;
    private String expertise;
    private String serviceArea;
    private String intro;
    private String phone;
    private String email;
    private Integer caseCount;
    private BigDecimal rating;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
}
