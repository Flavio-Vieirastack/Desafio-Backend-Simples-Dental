INSERT INTO users (name, email, password, role)
VALUES ('Admin SimplesDental', 'contato@simplesdental.com', '$2a$12$xfTfcSa7dVdYQi/CTbY9MOJmlu5PalxydsAetrSfmKEGJ1FTgKDGm', 'ADMIN')
ON CONFLICT (email) DO NOTHING;