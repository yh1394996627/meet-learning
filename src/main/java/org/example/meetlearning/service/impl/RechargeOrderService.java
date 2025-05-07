package org.example.meetlearning.service.impl;

import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.RechargeOrder;
import org.example.meetlearning.dao.mapper.RechargeOrderMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RechargeOrderService {

    private final RechargeOrderMapper rechargeOrderMapper;

    public int insert(RechargeOrder record) {
        return rechargeOrderMapper.insert(record);
    }

    public RechargeOrder selectByRecordId(String recordId) {
        return rechargeOrderMapper.selectByRecordId(recordId);
    }

    public int update(RechargeOrder record) {
        return rechargeOrderMapper.update(record);
    }
}
