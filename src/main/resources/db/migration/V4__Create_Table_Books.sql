CREATE TABLE IF NOT EXISTS `books` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `author` varchar(180) NOT NULL,
  `launch_date` date NOT NULL,
  `price` double NOT NULL,
  `title` varchar(250) NOT NULL,
  PRIMARY KEY (`id`)
);
