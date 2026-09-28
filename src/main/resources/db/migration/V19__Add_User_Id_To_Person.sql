ALTER TABLE `person`
  ADD COLUMN `user_id` bigint(20) NULL UNIQUE AFTER `id`,
  ADD CONSTRAINT `fk_person_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

-- Vincula usuários seed às primeiras pessoas (se existirem)
UPDATE `person` p
INNER JOIN `users` u ON u.user_name = 'leandro'
SET p.user_id = u.id
WHERE p.id = 1 AND p.user_id IS NULL;

UPDATE `person` p
INNER JOIN `users` u ON u.user_name = 'flavio'
SET p.user_id = u.id
WHERE p.id = 2 AND p.user_id IS NULL;
