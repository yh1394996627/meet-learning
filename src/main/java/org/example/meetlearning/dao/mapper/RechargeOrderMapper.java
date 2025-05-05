package org.example.meetlearning.dao.mapper;

import java.util.List;

import org.example.meetlearning.dao.entity.RechargeOrder;

public interface RechargeOrderMapper {

    int insert(RechargeOrder record);

    List<RechargeOrder> selectByStudentId(String studentId);

    int update(RechargeOrder record);

}