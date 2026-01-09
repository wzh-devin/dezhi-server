
DROP TABLE IF EXISTS dezhi.dz_session;
CREATE TABLE dezhi.dz_session(
                                 id INT8 NOT NULL,
                                 title VARCHAR(64) NOT NULL,
                                 create_time TIMESTAMP NOT NULL DEFAULT NOW(),
                                 update_time TIMESTAMP NOT NULL DEFAULT NOW(),
                                 PRIMARY KEY (id)
);
COMMENT ON COLUMN dezhi.dz_session.id IS '主键id';
COMMENT ON COLUMN dezhi.dz_session.title IS '会话标题';
COMMENT ON COLUMN dezhi.dz_session.create_time IS '创建时间';
COMMENT ON COLUMN dezhi.dz_session.update_time IS '更新时间';
COMMENT ON TABLE dezhi.dz_session IS '用户会话表';
DROP TABLE IF EXISTS dezhi.dz_message;
CREATE TABLE dezhi.dz_message(
                                 id INT8 NOT NULL,
                                 session_id INT8 NOT NULL,
                                 role VARCHAR(10) NOT NULL,
                                 content TEXT,
                                 create_time TIMESTAMP NOT NULL DEFAULT NOW(),
                                 update_time TIMESTAMP NOT NULL DEFAULT NOW(),
                                 PRIMARY KEY (id)
);
COMMENT ON COLUMN dezhi.dz_message.id IS '主键id';
COMMENT ON COLUMN dezhi.dz_message.session_id IS '会话id';
COMMENT ON COLUMN dezhi.dz_message.role IS '消息角色（USER, SYSTEM, ASSISTANT, TOOL）';
COMMENT ON COLUMN dezhi.dz_message.content IS '消息内容';
COMMENT ON COLUMN dezhi.dz_message.create_time IS '创建时间';
COMMENT ON COLUMN dezhi.dz_message.update_time IS '更新时间';
COMMENT ON TABLE dezhi.dz_message IS '对话消息表';
