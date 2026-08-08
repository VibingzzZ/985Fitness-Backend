-- 手机号密文、检索哈希和用户注销申请字段
ALTER TABLE t_985fitness_user
    MODIFY COLUMN phone VARCHAR(128) NULL COMMENT 'AES-256-GCM加密手机号',
    ADD COLUMN phone_hash CHAR(64) NULL COMMENT '手机号HMAC-SHA256检索哈希' AFTER phone,
    ADD COLUMN cancellation_reason VARCHAR(200) NULL COMMENT '注销原因',
    ADD COLUMN cancellation_requested_at DATETIME NULL COMMENT '注销申请时间',
    ADD COLUMN scheduled_deletion_at DATETIME NULL COMMENT '预计数据清理时间',
    ADD UNIQUE KEY uk_user_phone_hash (phone_hash);
