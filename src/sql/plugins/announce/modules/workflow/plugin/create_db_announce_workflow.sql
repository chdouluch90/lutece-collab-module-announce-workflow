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
