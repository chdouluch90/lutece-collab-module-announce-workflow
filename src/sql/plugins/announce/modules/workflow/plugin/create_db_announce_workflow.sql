DROP TABLE IF EXISTS workflow_task_change_announce_status_cf;
CREATE TABLE workflow_task_change_announce_status_cf(
	id_task int DEFAULT '0' NOT NULL,
	announce_published SMALLINT NOT NULL,
	PRIMARY KEY (id_task)
);

DROP TABLE IF EXISTS wf_prerequisite_announce_publication;
CREATE TABLE wf_prerequisite_announce_publication(
	id_prerequisite int DEFAULT '0' NOT NULL,
	nb_days int NOT NULL,
	PRIMARY KEY (id_prerequisite)
);


DROP TABLE IF EXISTS workflow_task_notify_announce_cf;
CREATE TABLE workflow_task_notify_announce_cf(
  id_task INT NOT NULL,
  sender_name VARCHAR(255) DEFAULT NULL,
  sender_email VARCHAR(255) DEFAULT NULL,
  subject VARCHAR(255) DEFAULT NULL,
  message long VARCHAR DEFAULT NULL,
  recipients_cc VARCHAR(255) DEFAULT '' NOT NULL,
  recipients_bcc VARCHAR(255) DEFAULT '' NOT NULL,
  PRIMARY KEY  (id_task)
);