
ALTER TABLE dezhi.dz_model_manager ADD COLUMN status VARCHAR(10);
COMMENT ON COLUMN dezhi.dz_model_manager.status IS '模型状态（STOP, ACTIVATED）';
ALTER TABLE dezhi.dz_model_manager ALTER COLUMN status SET DEFAULT 'STOP';