CREATE TABLE IF NOT EXISTS `edu_subject` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `tenant_id` bigint NOT NULL,
    `category_name` varchar(64) NOT NULL,
    `subject_name` varchar(64) NOT NULL,
    `is_core` tinyint NOT NULL DEFAULT 0,
    `status` tinyint NOT NULL DEFAULT 1,
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_subject_tenant_name` (`tenant_id`, `subject_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='EduAI 学科定义';

CREATE TABLE IF NOT EXISTS `edu_student_profile` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `tenant_id` bigint NOT NULL,
    `user_id` bigint NOT NULL,
    `student_no` varchar(64) NOT NULL,
    `grade_name` varchar(64) NOT NULL,
    `class_name` varchar(64) NOT NULL,
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_student_tenant_no` (`tenant_id`, `student_no`),
    UNIQUE KEY `uk_student_tenant_user` (`tenant_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='EduAI 学生档案';

CREATE TABLE IF NOT EXISTS `edu_score_record` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `tenant_id` bigint NOT NULL,
    `student_id` bigint NOT NULL,
    `subject_id` bigint NOT NULL,
    `exam_name` varchar(128) NOT NULL,
    `exam_type` varchar(32) NOT NULL,
    `exam_date` date NOT NULL,
    `score` decimal(7,2) NOT NULL,
    `full_score` decimal(7,2) NOT NULL,
    `source_type` varchar(16) NOT NULL COMMENT 'MANUAL/EXCEL',
    `remark` varchar(500) DEFAULT NULL,
    `create_by` bigint DEFAULT NULL,
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_score_student_subject_date` (`tenant_id`, `student_id`, `subject_id`, `exam_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='EduAI 成绩记录';

CREATE TABLE IF NOT EXISTS `edu_score_comment` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `tenant_id` bigint NOT NULL,
    `student_id` bigint NOT NULL,
    `subject_id` bigint NOT NULL,
    `author_user_id` bigint NOT NULL,
    `author_role` varchar(32) NOT NULL,
    `content` varchar(2000) NOT NULL,
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_comment_student_subject` (`tenant_id`, `student_id`, `subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='EduAI 成绩点评';

CREATE TABLE IF NOT EXISTS `edu_companion_settings` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `tenant_id` bigint NOT NULL,
    `student_id` bigint NOT NULL,
    `push_enabled` tinyint NOT NULL DEFAULT 0,
    `push_rule` varchar(500) DEFAULT NULL,
    `memory_retention_days` int NOT NULL DEFAULT 14,
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_companion_tenant_student` (`tenant_id`, `student_id`),
    CONSTRAINT `ck_memory_retention_days` CHECK (`memory_retention_days` BETWEEN 1 AND 90)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='EduAI 伴学设置';

CREATE TABLE IF NOT EXISTS `edu_reward_rule` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `tenant_id` bigint NOT NULL,
    `student_id` bigint NOT NULL,
    `subject_id` bigint DEFAULT NULL,
    `reward_type` varchar(16) NOT NULL COMMENT 'MATERIAL/SPIRITUAL',
    `reward_level` varchar(32) NOT NULL,
    `condition_text` varchar(500) NOT NULL,
    `reward_text` varchar(500) NOT NULL,
    `fulfillment_status` varchar(16) NOT NULL DEFAULT 'PENDING',
    `create_by` bigint NOT NULL,
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_reward_student_status` (`tenant_id`, `student_id`, `fulfillment_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='EduAI 家长奖励记录';

INSERT INTO `edu_subject` (`tenant_id`, `category_name`, `subject_name`, `is_core`)
VALUES
    (1, '主修学科', '数学', 1),
    (1, '主修学科', '物理', 1),
    (1, '主修学科', '化学', 1),
    (1, '主修学科', '英语', 1),
    (1, '非学科', '计算机应用', 0),
    (1, '非学科', 'Java 开发', 0),
    (1, '非学科', 'Python 开发', 0)
ON DUPLICATE KEY UPDATE `subject_name` = VALUES(`subject_name`);

INSERT INTO `sys_resource`
    (`id`, `tenant_id`, `resource_name`, `parent_id`, `resource_type`, `sort`, `path`, `component`, `menu_status`, `visible`, `perms`, `icon`, `client_code`, `remark`)
VALUES
    (20001, 1, 'AI 伴学工作台', 0, 2, 1, '/eduai/dashboard', '/src/views/eduai/dashboard/index.vue', 1, 1, 'eduai:dashboard:view', 'i-material-symbols:school-outline-rounded', 'pc', 'EduAI 教育业务入口')
ON DUPLICATE KEY UPDATE `resource_name` = VALUES(`resource_name`), `component` = VALUES(`component`);

INSERT IGNORE INTO `sys_role_resource` (`tenant_id`, `role_id`, `resource_id`, `create_time`)
VALUES (1, 1, 20001, CURRENT_TIMESTAMP);

-- 首期未启用的上游菜单在初始化末尾清理，避免前端出现无后端支撑的入口。
CREATE TEMPORARY TABLE `tmp_disabled_resource` (`id` bigint PRIMARY KEY);

INSERT IGNORE INTO `tmp_disabled_resource` (`id`)
SELECT `id`
FROM `sys_resource`
WHERE `path` LIKE '/flow%'
   OR `path` LIKE '/generator%'
   OR `path` LIKE '/report%'
   OR `path` LIKE '/external%'
   OR `path` LIKE '/data%'
   OR `path` LIKE '/leave%'
   OR `path` LIKE '/employee%'
   OR `path` LIKE '/system/job%';

DELETE `rr`
FROM `sys_role_resource` `rr`
JOIN `tmp_disabled_resource` `disabled` ON `disabled`.`id` = `rr`.`resource_id`;

DELETE `resource`
FROM `sys_resource` `resource`
JOIN `tmp_disabled_resource` `disabled` ON `disabled`.`id` = `resource`.`id`;

DROP TEMPORARY TABLE `tmp_disabled_resource`;
