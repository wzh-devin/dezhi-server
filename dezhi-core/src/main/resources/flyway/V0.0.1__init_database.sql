-- 加载Vector插件
CREATE EXTENSION IF NOT EXISTS vector;

-- 创建相关表

DROP TABLE IF EXISTS dezhi.dz_user;
CREATE TABLE dezhi.dz_user(
                              id INT8 NOT NULL,
                              username VARCHAR(100) NOT NULL,
                              password VARCHAR(255) NOT NULL,
                              email VARCHAR(255) NOT NULL,
                              create_time TIMESTAMP NOT NULL DEFAULT NOW(),
                              update_time TIMESTAMP NOT NULL DEFAULT NOW(),
                              PRIMARY KEY (id)
);
COMMENT ON COLUMN dezhi.dz_user.id IS '主键id';
COMMENT ON COLUMN dezhi.dz_user.username IS '用户名';
COMMENT ON COLUMN dezhi.dz_user.password IS '密码';
COMMENT ON COLUMN dezhi.dz_user.email IS '邮箱';
COMMENT ON COLUMN dezhi.dz_user.create_time IS '创建时间';
COMMENT ON COLUMN dezhi.dz_user.update_time IS '更新时间';
COMMENT ON TABLE dezhi.dz_user IS '用户表';


DROP TABLE IF EXISTS dezhi.dz_article;
CREATE TABLE dezhi.dz_article(
                                 id INT8 NOT NULL,
                                 category_id INT8 NOT NULL,
                                 title VARCHAR(200) NOT NULL,
                                 summary VARCHAR(500),
                                 content TEXT,
                                 content_md TEXT,
                                 uri VARCHAR(255) NOT NULL,
                                 status VARCHAR(10) NOT NULL DEFAULT 'DRAFT',
                                 is_top INT2 NOT NULL DEFAULT 0,
                                 is_hot INT2 NOT NULL DEFAULT 0,
                                 create_time TIMESTAMP NOT NULL DEFAULT NOW(),
                                 update_time TIMESTAMP NOT NULL DEFAULT NOW(),
                                 summary_embedding VECTOR(1536),
                                 PRIMARY KEY (id)
);
COMMENT ON COLUMN dezhi.dz_article.id IS '主键id';
COMMENT ON COLUMN dezhi.dz_article.category_id IS '分类id';
COMMENT ON COLUMN dezhi.dz_article.title IS '文章标题';
COMMENT ON COLUMN dezhi.dz_article.summary IS '文章简介';
COMMENT ON COLUMN dezhi.dz_article.content IS '文章内容';
COMMENT ON COLUMN dezhi.dz_article.content_md IS '文章内容markdown';
COMMENT ON COLUMN dezhi.dz_article.uri IS '文章的uri地址';
COMMENT ON COLUMN dezhi.dz_article.status IS '文章状态（DRAFT,PUBLISHED）';
COMMENT ON COLUMN dezhi.dz_article.is_top IS '是否置顶（0: 正常; 1: 置顶）';
COMMENT ON COLUMN dezhi.dz_article.is_hot IS '是否热门（0: 正常; 1: 热门）';
COMMENT ON COLUMN dezhi.dz_article.create_time IS '创建时间';
COMMENT ON COLUMN dezhi.dz_article.update_time IS '更新时间';
COMMENT ON COLUMN dezhi.dz_article.summary_embedding IS '文章简介向量';
COMMENT ON TABLE dezhi.dz_article IS '文章表';


DROP TABLE IF EXISTS dezhi.dz_category;
CREATE TABLE dezhi.dz_category(
                                  id INT8 NOT NULL,
                                  name VARCHAR(100) NOT NULL,
                                  create_time TIMESTAMP NOT NULL DEFAULT NOW(),
                                  update_time TIMESTAMP NOT NULL DEFAULT NOW(),
                                  PRIMARY KEY (id)
);
COMMENT ON COLUMN dezhi.dz_category.id IS '主键id';
COMMENT ON COLUMN dezhi.dz_category.name IS '分类名称';
COMMENT ON COLUMN dezhi.dz_category.create_time IS '创建时间';
COMMENT ON COLUMN dezhi.dz_category.update_time IS '更新时间';
COMMENT ON TABLE dezhi.dz_category IS '分类';


DROP TABLE IF EXISTS dezhi.dz_tag;
CREATE TABLE dezhi.dz_tag(
                             id INT8 NOT NULL,
                             name VARCHAR(100) NOT NULL,
                             color VARCHAR(255) NOT NULL,
                             create_time TIMESTAMP NOT NULL DEFAULT NOW(),
                             update_time TIMESTAMP NOT NULL DEFAULT NOW(),
                             PRIMARY KEY (id)
);
COMMENT ON COLUMN dezhi.dz_tag.id IS '主键id';
COMMENT ON COLUMN dezhi.dz_tag.name IS '标签名称';
COMMENT ON COLUMN dezhi.dz_tag.color IS '标签颜色';
COMMENT ON COLUMN dezhi.dz_tag.create_time IS '创建时间';
COMMENT ON COLUMN dezhi.dz_tag.update_time IS '更新时间';
COMMENT ON TABLE dezhi.dz_tag IS '标签';


DROP TABLE IF EXISTS dezhi.dz_article_tag;
CREATE TABLE dezhi.dz_article_tag(
                                     id INT8 NOT NULL,
                                     article_id INT8 NOT NULL,
                                     tag_id INT8 NOT NULL,
                                     create_time TIMESTAMP NOT NULL DEFAULT NOW(),
                                     update_time TIMESTAMP NOT NULL DEFAULT NOW(),
                                     PRIMARY KEY (id)
);
COMMENT ON COLUMN dezhi.dz_article_tag.id IS '主键id';
COMMENT ON COLUMN dezhi.dz_article_tag.article_id IS '文章id';
COMMENT ON COLUMN dezhi.dz_article_tag.tag_id IS '标签id';
COMMENT ON COLUMN dezhi.dz_article_tag.create_time IS '创建时间';
COMMENT ON COLUMN dezhi.dz_article_tag.update_time IS '更新时间';
COMMENT ON TABLE dezhi.dz_article_tag IS '文章-标签关联表';


DROP TABLE IF EXISTS dezhi.dz_file;
CREATE TABLE dezhi.dz_file(
                              id INT8 NOT NULL,
                              original_name VARCHAR(255) NOT NULL,
                              final_name VARCHAR(255) NOT NULL,
                              bucket_name VARCHAR(255) NOT NULL,
                              hash VARCHAR(128) NOT NULL,
                              size INT8 NOT NULL,
                              type VARCHAR(10) NOT NULL,
                              mime_type VARCHAR(100) NOT NULL,
                              storage_type VARCHAR(10) NOT NULL,
                              uri VARCHAR(255),
                              is_deleted INT2 NOT NULL DEFAULT 0,
                              status VARCHAR(10) NOT NULL,
                              create_time TIMESTAMP NOT NULL DEFAULT NOW(),
                              update_time TIMESTAMP NOT NULL DEFAULT NOW(),
                              PRIMARY KEY (id)
);
COMMENT ON COLUMN dezhi.dz_file.id IS '主键id';
COMMENT ON COLUMN dezhi.dz_file.original_name IS '文件原始名称';
COMMENT ON COLUMN dezhi.dz_file.final_name IS '存储的文件名称';
COMMENT ON COLUMN dezhi.dz_file.bucket_name IS '存储桶名称';
COMMENT ON COLUMN dezhi.dz_file.hash IS '文件哈希值';
COMMENT ON COLUMN dezhi.dz_file.size IS '文件大小';
COMMENT ON COLUMN dezhi.dz_file.type IS '文件类型（IMAGE, ZIP, PDF）';
COMMENT ON COLUMN dezhi.dz_file.mime_type IS '文件MIME类型';
COMMENT ON COLUMN dezhi.dz_file.storage_type IS '文件存储类型';
COMMENT ON COLUMN dezhi.dz_file.uri IS '文件uri地址';
COMMENT ON COLUMN dezhi.dz_file.is_deleted IS '是否被删除（0: 正常; 1: 已删除）';
COMMENT ON COLUMN dezhi.dz_file.status IS '文件状态（UPLOADING, FINISHED, FAILED）';
COMMENT ON COLUMN dezhi.dz_file.create_time IS '创建时间';
COMMENT ON COLUMN dezhi.dz_file.update_time IS '更新时间';
COMMENT ON TABLE dezhi.dz_file IS '文件素材表';


DROP TABLE IF EXISTS dezhi.dz_article_file;
CREATE TABLE dezhi.dz_article_file(
                                      id INT8 NOT NULL,
                                      article_id INT8 NOT NULL,
                                      file_id INT8 NOT NULL,
                                      create_time TIMESTAMP NOT NULL DEFAULT NOW(),
                                      update_time TIMESTAMP NOT NULL DEFAULT NOW(),
                                      PRIMARY KEY (id)
);
COMMENT ON COLUMN dezhi.dz_article_file.id IS '主键id';
COMMENT ON COLUMN dezhi.dz_article_file.article_id IS '文章id';
COMMENT ON COLUMN dezhi.dz_article_file.file_id IS '文件id';
COMMENT ON COLUMN dezhi.dz_article_file.create_time IS '创建时间';
COMMENT ON COLUMN dezhi.dz_article_file.update_time IS '更新时间';
COMMENT ON TABLE dezhi.dz_article_file IS '文章-文件关联表';


DROP TABLE IF EXISTS dezhi.dz_model_manager;
CREATE TABLE dezhi.dz_model_manager(
                                       id INT8 NOT NULL,
                                       provider VARCHAR(50) NOT NULL,
                                       name VARCHAR(255) NOT NULL,
                                       base_url VARCHAR(255),
                                       api_key VARCHAR(255) NOT NULL,
                                       create_time TIMESTAMP NOT NULL DEFAULT NOW(),
                                       update_time TIMESTAMP NOT NULL DEFAULT NOW(),
                                       PRIMARY KEY (id)
);
COMMENT ON COLUMN dezhi.dz_model_manager.id IS '主键id';
COMMENT ON COLUMN dezhi.dz_model_manager.provider IS '模型提供商';
COMMENT ON COLUMN dezhi.dz_model_manager.name IS '模型名称';
COMMENT ON COLUMN dezhi.dz_model_manager.base_url IS '模型的base_url';
COMMENT ON COLUMN dezhi.dz_model_manager.api_key IS '模型的api_key';
COMMENT ON COLUMN dezhi.dz_model_manager.create_time IS '创建时间';
COMMENT ON COLUMN dezhi.dz_model_manager.update_time IS '更新时间';
COMMENT ON TABLE dezhi.dz_model_manager IS 'AI模型管理';



