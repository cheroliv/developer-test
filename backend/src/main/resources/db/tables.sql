-- noinspection SqlNoDataSourceInspectionForFile
CREATE TABLE IF NOT EXISTS `routes` (
    `id`          UUID default random_uuid() PRIMARY KEY,
    `origin`      VARCHAR,
    `destination` VARCHAR,
    `travel_time` INT
);
CREATE UNIQUE INDEX IF NOT EXISTS `unique_idx_origin_destination`
ON `routes` (`origin`, `destination`);
