-- FUNCIONALIDAD EXTRA 2: TRIGGERS (AUTOMATIZACIÓN)
-- Requisito: Actualizar un campo en una tabla (Usuario) cuando pasa algo en otra (Valora).

DELIMITER $$

CREATE OR REPLACE TRIGGER auto_subir_nivel
AFTER INSERT ON valora
FOR EACH ROW
BEGIN
    -- 1. Declaramos una variable para guardar el número de votos
    DECLARE total_votos INT;

    -- 2. Contamos cuántas valoraciones tiene el usuario que acaba de votar
    SELECT COUNT(*) INTO total_votos 
    FROM valora 
    WHERE id_usuario = NEW.id_usuario;

    -- 3. Lógica de ascenso (Gamificación)
    -- Si tiene 10 o más, sube a Nivel 3 (Experto)
    IF total_votos >= 10 THEN
        UPDATE usuario SET nivel_prestigio = 3 WHERE id = NEW.id_usuario;
    
    -- Si tiene 5 o más, sube a Nivel 2 (Intermedio)
    ELSEIF total_votos >= 5 THEN
        UPDATE usuario SET nivel_prestigio = 2 WHERE id = NEW.id_usuario;
    END IF;

END $$

DELIMITER ;