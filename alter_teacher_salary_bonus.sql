ALTER TABLE teacher_salary ADD COLUMN bonus DECIMAL(19,4) DEFAULT 0 COMMENT '薪资调整(算薪可编辑总额,差额计入此项)';
