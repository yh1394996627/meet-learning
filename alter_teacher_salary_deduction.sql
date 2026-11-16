ALTER TABLE teacher_salary
  ADD COLUMN deduction_qty DECIMAL(19,4) DEFAULT 0 COMMENT '取消扣款课数量',
  ADD COLUMN group_deduction_qty_qty DECIMAL(19,4) DEFAULT 0 COMMENT '组课取消扣款数量',
  ADD COLUMN deduction_amount DECIMAL(19,4) DEFAULT 0 COMMENT '取消扣款金额';
