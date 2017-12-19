CREATE TABLE IF NOT EXISTS `User` (
    `id` bigint(20) NOT NULL auto_increment,
    `user_name` varchar(200) NOT NULL,
    `password` varchar(200) NOT NULL,
    PRIMARY KEY (`id`)
);