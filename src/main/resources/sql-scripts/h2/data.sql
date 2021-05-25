DROP TABLE IF EXISTS `roles_authorities`;
DROP TABLE IF EXISTS `users_roles`;
DROP TABLE IF EXISTS `authorities`;
DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` char(68) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `failed_login_attempts` int(3) DEFAULT 0,
  PRIMARY KEY (`id`)
);

--
-- NOTE: The passwords are encrypted using BCrypt
--
-- A generation tool: https://www.bcryptcalculator.com/encode
--
-- Default passwords here are: test123
--

INSERT INTO `users`(username,password,enabled)
VALUES
('admin','{bcrypt}$2a$10$MQN2pb8IoYWnq3vJNckW7.QISXwueuvJD12KQOcGwgYFu9A4/QCe.',1),
('user','{bcrypt}$2a$10$MQN2pb8IoYWnq3vJNckW7.QISXwueuvJD12KQOcGwgYFu9A4/QCe.',1),
('user2','{bcrypt}$2a$10$MQN2pb8IoYWnq3vJNckW7.QISXwueuvJD12KQOcGwgYFu9A4/QCe.',1);

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
   PRIMARY KEY (`id`)
);

--
-- Dumping data for table `roles`
--

INSERT INTO `roles`(name)
VALUES
('ROLE_VIEWER'),
('ROLE_ADMIN');

--
-- Table structure for table `users_roles`
--

CREATE TABLE `users_roles` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,

  PRIMARY KEY (`user_id`,`role_id`),

  CONSTRAINT `user_fk_1` FOREIGN KEY (`user_id`)
  REFERENCES `users`(`id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION,

  CONSTRAINT `role_fk_1` FOREIGN KEY (`role_id`)
  REFERENCES `roles`(`id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION
);

SET FOREIGN_KEY_CHECKS = 1;

--
-- Dumping data for table `users_roles`
--

INSERT INTO `users_roles`(user_id,role_id)
VALUES
(1, 2),
(2, 1),
(3, 1);

CREATE TABLE `authorities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permission` varchar(50) DEFAULT NULL,
   PRIMARY KEY (`id`)
);

--
-- Dumping data for table `roles`
--

INSERT INTO `authorities`(permission)
VALUES
('READ'),
('CREATE'),
('UPDATE'),
('DELETE');

CREATE TABLE `roles_authorities` (
  `role_id` int(11) NOT NULL,
   `authority_id` int(11) NOT NULL,

  PRIMARY KEY (`role_id`, authority_id),

  CONSTRAINT `authority_fk_1` FOREIGN KEY (`authority_id`)
  REFERENCES `authorities`(`id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION,

  CONSTRAINT `role_fk_2` FOREIGN KEY (`role_id`)
  REFERENCES `roles`(`id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION
);

SET FOREIGN_KEY_CHECKS = 1;

--
-- Dumping data for table `users_roles`
--

INSERT INTO `roles_authorities`(role_id, authority_id)
VALUES
(1, 1),
(2, 1),
(2, 2),
(2, 3),
(2, 4);