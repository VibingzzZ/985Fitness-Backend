-- 为临时密码登录新增字段
-- openid 改为可空（密码注册用户无微信openid）
-- 新增 password 字段存储 BCrypt 密码摘要

ALTER TABLE t_985fitness_user
    MODIFY COLUMN openid VARCHAR(64) NULL COMMENT '微信openid（密码注册用户可为空）',
    ADD COLUMN password VARCHAR(255) NULL COMMENT 'BCrypt密码摘要（微信用户为空）';