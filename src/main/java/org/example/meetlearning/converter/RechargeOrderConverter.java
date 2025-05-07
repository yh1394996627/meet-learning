package org.example.meetlearning.converter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.example.meetlearning.dao.entity.PayConfig;
import org.example.meetlearning.dao.entity.RechargeOrder;
import org.example.meetlearning.dao.entity.Student;
import org.example.meetlearning.enums.PayStatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RechargeOrderConverter {

    RechargeOrderConverter INSTANCE = Mappers.getMapper(RechargeOrderConverter.class);

    default RechargeOrder toCreate(Student student, PayConfig payConfig, String ipAddress) {
        RechargeOrder order = new RechargeOrder();
        order.setOrderId(UUID.randomUUID().toString().replace("-", ""));
        order.setCreateTime(new Date());
        order.setStudentId(student.getRecordId());
        order.setManagerId(student.getAffiliateId());
        order.setAmount(payConfig.getAmount());
        order.setQuantity(payConfig.getQuantity());
        order.setStatus(PayStatusEnum.CREATED.getStatus());
        order.setIpAddress(ipAddress);
        return order;
    }

}
