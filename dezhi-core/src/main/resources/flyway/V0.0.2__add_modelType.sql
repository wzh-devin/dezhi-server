
ALTER TABLE dezhi.dz_model_manager ADD COLUMN type VARCHAR(10) NOT NULL;
COMMENT ON COLUMN dezhi.dz_model_manager.type IS '模型类型（CHAT, EMBEDDING）';
ALTER TABLE dezhi.dz_model_manager ALTER COLUMN type SET DEFAULT 'CHAT';

INSERT INTO dezhi.dz_model_manager(id, provider, name, base_url, api_key, type) VALUES
(1, 'OPENAI', 'gpt-4o', 'https://api.openai.com/v1', 'sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'CHAT'),
(2, 'OPENAI', 'text-embedding-3-small', 'https://api.openai.com/v1', 'sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'EMBEDDING');