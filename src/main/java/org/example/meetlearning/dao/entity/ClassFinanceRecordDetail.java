package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 课程课时币扣减明细
 * 记录某课程从哪些 user_finance_record 扣减了多少课时币，用于删除课程时精确回退
 */
@Data
public class ClassFinanceRecordDetail {

    private Long id;

    private Boolean deleted;

    private String creator;

    private String createName;

    private Date createTime;

    private String updator;

    private String updateName;

    private Date updateTime;

    /** 业务主键 UUID */
    private String recordId;

    /** 课程 record_id（关联 student_class.record_id） */
    private String classId;

    /** 关联 user_finance_record.id（自增主键） */
    private Long financeRecordId;

    /** 学生 record_id（冗余便于查询） */
    private String userId;

    /** 本次从该 record 扣减的数量（正数） */
    private BigDecimal qty;

}
