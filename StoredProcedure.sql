CREATE DEFINER=`root`@`localhost` PROCEDURE `ROWPERROW`()
BEGIN
DECLARE n INT DEFAULT 0;
DECLARE i INT DEFAULT 0;
SELECT COUNT(*) FROM client INTO n;
SET i=0;
IF NOT EXISTS( SELECT NULL
            FROM INFORMATION_SCHEMA.COLUMNS
           WHERE table_name = 'client'
             AND table_schema = 'evalart_reto'
             AND column_name = 'total_balance')  THEN

  ALTER TABLE `client` ADD `total_balance` DECIMAL(10,2) NOT NULL default '0.00';
END IF;
WHILE i<n DO 
  UPDATE client set total_balance = (SELECT SUM(balance) FROM evalart_reto.account WHERE client_id = i GROUP BY client_id) where id = i;
  SET i = i + 1;
END WHILE;
End