CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE app_user (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email VARCHAR(320) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL,
  display_name VARCHAR(120) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE health_connection (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  provider VARCHAR(30) NOT NULL,
  status VARCHAR(20) NOT NULL,
  permissions JSONB NOT NULL DEFAULT '[]'::jsonb,
  last_sync_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  UNIQUE(user_id, provider)
);

CREATE TABLE health_record (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  provider VARCHAR(30) NOT NULL,
  record_type VARCHAR(80) NOT NULL,
  value NUMERIC(20,6) NOT NULL,
  unit VARCHAR(40) NOT NULL,
  start_time TIMESTAMPTZ NOT NULL,
  end_time TIMESTAMPTZ,
  source_record_id VARCHAR(255),
  metadata JSONB NOT NULL DEFAULT '{}'::jsonb,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX uq_health_record_source
ON health_record(user_id, provider, source_record_id)
WHERE source_record_id IS NOT NULL;

CREATE INDEX ix_health_record_user_type_time
ON health_record(user_id, record_type, start_time DESC);

CREATE TABLE medical_report (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  report_name VARCHAR(255) NOT NULL,
  report_type VARCHAR(80),
  original_file_name VARCHAR(255) NOT NULL,
  storage_key VARCHAR(500) NOT NULL UNIQUE,
  mime_type VARCHAR(100) NOT NULL,
  file_size BIGINT NOT NULL,
  report_date DATE,
  source VARCHAR(30) NOT NULL DEFAULT 'MANUAL',
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE import_job (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  source VARCHAR(30) NOT NULL,
  status VARCHAR(20) NOT NULL,
  total_records INTEGER NOT NULL DEFAULT 0,
  imported_records INTEGER NOT NULL DEFAULT 0,
  skipped_records INTEGER NOT NULL DEFAULT 0,
  failed_records INTEGER NOT NULL DEFAULT 0,
  error_message TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  completed_at TIMESTAMPTZ
);

CREATE TABLE consent_event (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  provider VARCHAR(30) NOT NULL,
  purpose VARCHAR(255) NOT NULL,
  status VARCHAR(20) NOT NULL,
  consent_reference VARCHAR(255),
  granted_at TIMESTAMPTZ,
  revoked_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE audit_event (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID REFERENCES app_user(id) ON DELETE SET NULL,
  action VARCHAR(80) NOT NULL,
  resource_type VARCHAR(80) NOT NULL,
  resource_id VARCHAR(100),
  occurred_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
