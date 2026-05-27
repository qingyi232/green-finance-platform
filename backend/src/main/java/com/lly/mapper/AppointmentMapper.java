package com.lly.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lly.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {}
