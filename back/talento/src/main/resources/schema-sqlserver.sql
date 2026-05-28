IF OBJECT_ID('usuarios', 'U') IS NULL
BEGIN
    CREATE TABLE usuarios (
        id BIGINT IDENTITY(1,1) NOT NULL,
        nombre VARCHAR(150) NOT NULL,
        rol VARCHAR(20) NOT NULL,
        usuario VARCHAR(80) NOT NULL,
        numero_empleado BIGINT NULL,
        unidad_negocio VARCHAR(150) NULL,
        CONSTRAINT pk_usuarios PRIMARY KEY (id),
        CONSTRAINT uq_usuarios_usuario UNIQUE (usuario)
    );
END;

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'idx_usuarios_rol' AND object_id = OBJECT_ID('usuarios'))
    CREATE INDEX idx_usuarios_rol ON usuarios (rol);

IF OBJECT_ID('vacaciones', 'U') IS NULL
BEGIN
    CREATE TABLE vacaciones (
        id BIGINT IDENTITY(1,1) NOT NULL,
        numero_empleado VARCHAR(30) NULL,
        nombre_empleado VARCHAR(150) NULL,
        unidad_negocio VARCHAR(150) NULL,
        fecha_inicio DATETIME2 NULL,
        fecha_fin DATETIME2 NULL,
        total_dias INT NULL,
        comentario VARCHAR(MAX) NULL,
        estado VARCHAR(20) NULL,
        fecha_creacion DATETIME2 NOT NULL CONSTRAINT df_vacaciones_fecha_creacion DEFAULT SYSUTCDATETIME(),
        lider_id BIGINT NULL,
        CONSTRAINT pk_vacaciones PRIMARY KEY (id)
    );
END;

IF COL_LENGTH('vacaciones', 'fecha_creacion') IS NULL
    ALTER TABLE vacaciones ADD fecha_creacion DATETIME2 NOT NULL CONSTRAINT df_vacaciones_fecha_creacion DEFAULT SYSUTCDATETIME();

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'idx_vac_estado' AND object_id = OBJECT_ID('vacaciones'))
    CREATE INDEX idx_vac_estado ON vacaciones (estado);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'idx_vac_empleado' AND object_id = OBJECT_ID('vacaciones'))
    CREATE INDEX idx_vac_empleado ON vacaciones (numero_empleado);

IF OBJECT_ID('fk_vac_lider', 'F') IS NULL
    ALTER TABLE vacaciones ADD CONSTRAINT fk_vac_lider FOREIGN KEY (lider_id) REFERENCES usuarios (id) ON DELETE SET NULL;

IF OBJECT_ID('incapacidades', 'U') IS NULL
BEGIN
    CREATE TABLE incapacidades (
        id BIGINT IDENTITY(1,1) NOT NULL,
        numero_empleado VARCHAR(30) NULL,
        nombre_empleado VARCHAR(150) NULL,
        unidad_negocio VARCHAR(150) NULL,
        tipo_incapacidad VARCHAR(100) NULL,
        entidad_salud VARCHAR(100) NULL,
        categoria VARCHAR(100) NULL,
        fecha_inicio DATETIME2 NULL,
        fecha_fin DATETIME2 NULL,
        total_dias INT NULL,
        diagnostico VARCHAR(MAX) NULL,
        estado VARCHAR(20) NULL,
        fecha_creacion DATETIME2 NOT NULL CONSTRAINT df_incapacidades_fecha_creacion DEFAULT SYSUTCDATETIME(),
        archivo_adjunto VARCHAR(MAX) NULL,
        lider_id BIGINT NULL,
        CONSTRAINT pk_incapacidades PRIMARY KEY (id)
    );
END;

IF COL_LENGTH('incapacidades', 'fecha_creacion') IS NULL
    ALTER TABLE incapacidades ADD fecha_creacion DATETIME2 NOT NULL CONSTRAINT df_incapacidades_fecha_creacion DEFAULT SYSUTCDATETIME();

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'idx_inc_estado' AND object_id = OBJECT_ID('incapacidades'))
    CREATE INDEX idx_inc_estado ON incapacidades (estado);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'idx_inc_empleado' AND object_id = OBJECT_ID('incapacidades'))
    CREATE INDEX idx_inc_empleado ON incapacidades (numero_empleado);

IF OBJECT_ID('fk_inc_lider', 'F') IS NULL
    ALTER TABLE incapacidades ADD CONSTRAINT fk_inc_lider FOREIGN KEY (lider_id) REFERENCES usuarios (id) ON DELETE SET NULL;

IF OBJECT_ID('calamidades', 'U') IS NULL
BEGIN
    CREATE TABLE calamidades (
        id BIGINT IDENTITY(1,1) NOT NULL,
        numero_empleado VARCHAR(30) NULL,
        nombre_empleado VARCHAR(150) NULL,
        unidad_negocio VARCHAR(150) NULL,
        descripcion VARCHAR(MAX) NULL,
        fecha_inicio DATETIME2 NULL,
        fecha_fin DATETIME2 NULL,
        total_dias INT NULL,
        comentario VARCHAR(MAX) NULL,
        estado VARCHAR(20) NULL,
        fecha_creacion DATETIME2 NOT NULL CONSTRAINT df_calamidades_fecha_creacion DEFAULT SYSUTCDATETIME(),
        archivo_adjunto VARCHAR(MAX) NULL,
        lider_id BIGINT NULL,
        CONSTRAINT pk_calamidades PRIMARY KEY (id)
    );
END;

IF COL_LENGTH('calamidades', 'fecha_creacion') IS NULL
    ALTER TABLE calamidades ADD fecha_creacion DATETIME2 NOT NULL CONSTRAINT df_calamidades_fecha_creacion DEFAULT SYSUTCDATETIME();

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'idx_cal_estado' AND object_id = OBJECT_ID('calamidades'))
    CREATE INDEX idx_cal_estado ON calamidades (estado);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'idx_cal_empleado' AND object_id = OBJECT_ID('calamidades'))
    CREATE INDEX idx_cal_empleado ON calamidades (numero_empleado);

IF OBJECT_ID('fk_cal_lider', 'F') IS NULL
    ALTER TABLE calamidades ADD CONSTRAINT fk_cal_lider FOREIGN KEY (lider_id) REFERENCES usuarios (id) ON DELETE SET NULL;

IF OBJECT_ID('dias_cumpleanios', 'U') IS NULL
BEGIN
    CREATE TABLE dias_cumpleanios (
        id BIGINT IDENTITY(1,1) NOT NULL,
        numero_empleado VARCHAR(30) NULL,
        nombre_empleado VARCHAR(150) NULL,
        unidad_negocio VARCHAR(150) NULL,
        fecha_cumpleanio DATETIME2 NULL,
        comentario VARCHAR(MAX) NULL,
        estado VARCHAR(20) NULL,
        fecha_creacion DATETIME2 NOT NULL CONSTRAINT df_dias_cumpleanios_fecha_creacion DEFAULT SYSUTCDATETIME(),
        lider_id BIGINT NULL,
        CONSTRAINT pk_dias_cumpleanios PRIMARY KEY (id)
    );
END;

IF COL_LENGTH('dias_cumpleanios', 'fecha_creacion') IS NULL
    ALTER TABLE dias_cumpleanios ADD fecha_creacion DATETIME2 NOT NULL CONSTRAINT df_dias_cumpleanios_fecha_creacion DEFAULT SYSUTCDATETIME();

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'idx_cum_estado' AND object_id = OBJECT_ID('dias_cumpleanios'))
    CREATE INDEX idx_cum_estado ON dias_cumpleanios (estado);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'idx_cum_empleado' AND object_id = OBJECT_ID('dias_cumpleanios'))
    CREATE INDEX idx_cum_empleado ON dias_cumpleanios (numero_empleado);

IF OBJECT_ID('fk_cum_lider', 'F') IS NULL
    ALTER TABLE dias_cumpleanios ADD CONSTRAINT fk_cum_lider FOREIGN KEY (lider_id) REFERENCES usuarios (id) ON DELETE SET NULL;
