CREATE TABLE `password_reset` (
  `pr_id` bigint(20) NOT NULL,
  `pr_link` varchar(255) DEFAULT NULL,
  `pr_num` datetime DEFAULT NULL,
  `pr_us_id` bigint(20) DEFAULT NULL
);

CREATE TABLE `play` (
  `pl_id` bigint(20) NOT NULL,
  `pl_call` varchar(255) DEFAULT NULL,
  `pl_desc` varchar(255) DEFAULT NULL,
  `pl_name` varchar(255) DEFAULT NULL,
  `pl_type` int(11) DEFAULT NULL,
  `pl_published` bit(1) DEFAULT NULL
);

CREATE TABLE `player` (
  `pla_id` bigint(20) NOT NULL,
  `pla_num` int(11) DEFAULT NULL,
  `pla_pos` varchar(255) DEFAULT NULL,
  `pla_pos2` varchar(255) DEFAULT NULL,
  `pla_us_id` bigint(20) DEFAULT NULL,
  `pla_first_name` varchar(255) DEFAULT NULL,
  `pla_last_name` varchar(255) DEFAULT NULL,
  `pla_role` varchar(255) DEFAULT NULL,
  `pla_comment` varchar(255) DEFAULT NULL,
  `pla_birthdate` datetime DEFAULT NULL,
  `pla_height` int(11) DEFAULT NULL,
  `pla_email` varchar(255) DEFAULT NULL,
  `pla_phone` varchar(255) DEFAULT NULL
);

CREATE TABLE `step` (
  `st_id` bigint(20) NOT NULL,
  `st_desc` varchar(255) DEFAULT NULL,
  `st_order` int(11) DEFAULT NULL,
  `st_pl_id` bigint(20) DEFAULT NULL
);

CREATE TABLE `step_entity` (
  `se_id` bigint(20) NOT NULL,
  `se_en_id` bigint(20) NOT NULL,
  `se_label` varchar(255) DEFAULT NULL,
  `se_type` int(11) DEFAULT NULL,
  `se_x` int(11) DEFAULT NULL,
  `se_y` int(11) DEFAULT NULL,
  `se_st_id` bigint(20) NOT NULL
);

CREATE TABLE `user` (
  `us_id` bigint(20) NOT NULL,
  `us_name` varchar(255) DEFAULT NULL,
  `us_password` varchar(255) DEFAULT NULL,
  `us_email` varchar(255) DEFAULT NULL
);

CREATE TABLE `user_role` (
  `ro_id` bigint(20) NOT NULL,
  `ro_name` varchar(255) DEFAULT NULL,
  `ro_us_id` bigint(20) DEFAULT NULL
);

ALTER TABLE `password_reset`
  ADD PRIMARY KEY (`pr_id`),
  ADD KEY `fk_pr_us` (`pr_us_id`);

ALTER TABLE `play`
  ADD PRIMARY KEY (`pl_id`);

ALTER TABLE `player`
  ADD PRIMARY KEY (`pla_id`),
  ADD KEY `fk_pla_us` (`pla_us_id`);

ALTER TABLE `step`
  ADD PRIMARY KEY (`st_id`),
  ADD KEY `fk_st_pl` (`st_pl_id`);

ALTER TABLE `step_entity`
  ADD PRIMARY KEY (`se_id`),
  ADD KEY `fk_se_st` (`se_st_id`);

ALTER TABLE `user`
  ADD PRIMARY KEY (`us_id`);

ALTER TABLE `user_role`
  ADD PRIMARY KEY (`ro_id`),
  ADD KEY `fk_ro_us` (`ro_us_id`);

ALTER TABLE `password_reset`
  MODIFY `pr_id` bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `play`
  MODIFY `pl_id` bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `player`
  MODIFY `pla_id` bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `step`
  MODIFY `st_id` bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `step_entity`
  MODIFY `se_id` bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `user`
  MODIFY `us_id` bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `user_role`
  MODIFY `ro_id` bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `password_reset`
  ADD CONSTRAINT `fk_pr_us` FOREIGN KEY (`pr_us_id`) REFERENCES `user` (`us_id`);

ALTER TABLE `player`
  ADD CONSTRAINT `FK1ev8isfks99ibfuyimjyw1co4` FOREIGN KEY (`pla_us_id`) REFERENCES `user` (`us_id`),
  ADD CONSTRAINT `fk_pla_us` FOREIGN KEY (`pla_us_id`) REFERENCES `user` (`us_id`);

ALTER TABLE `step`
  ADD CONSTRAINT `FKaopuao3jgr0evyvslgagrdcpj` FOREIGN KEY (`st_pl_id`) REFERENCES `play` (`pl_id`),
  ADD CONSTRAINT `fk_st_pl` FOREIGN KEY (`st_pl_id`) REFERENCES `play` (`pl_id`);

ALTER TABLE `step_entity`
  ADD CONSTRAINT `FKhanqc6obn6purcxj3jv6di7jq` FOREIGN KEY (`se_st_id`) REFERENCES `step` (`st_id`),
  ADD CONSTRAINT `fk_se_st` FOREIGN KEY (`se_st_id`) REFERENCES `step` (`st_id`);

ALTER TABLE `user_role`
  ADD CONSTRAINT `FKsrkni6fh89u8ffj21dpb0kju8` FOREIGN KEY (`ro_us_id`) REFERENCES `user` (`us_id`),
  ADD CONSTRAINT `fk_ro_us` FOREIGN KEY (`ro_us_id`) REFERENCES `user` (`us_id`);

COMMIT;
