package com.lly.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String realName;
    private String role;
    private String avatar;
    private String phone;
    private String email;
    private String idCard;
    private String region;
    private String address;
    private String orgType;
    private String businessScope;
    private String qualificationUrl;
    private String intro;
    private Integer status;
    private Date createdTime;
    private Date updatedTime;
}
