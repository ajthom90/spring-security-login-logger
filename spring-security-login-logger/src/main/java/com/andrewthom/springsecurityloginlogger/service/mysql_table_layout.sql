CREATE TABLE `login_attempts` (
  `id` varchar(33) NOT NULL DEFAULT '',
  `username` varchar(60) NOT NULL DEFAULT '',
  `datetime` datetime NOT NULL,
  `result` VARCHAR(33) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1