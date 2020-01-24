-- The number 64 in here corresponds to StaticConstants.DEFAULT_FIELD_LENGTH

CREATE TABLE users (
  user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_name VARCHAR(16) NOT NULL,
  real_name VARCHAR(64) NOT NULL,
  email VARCHAR(64) NOT NULL,
  timezone VARCHAR(64) NOT NULL,
  password VARCHAR(64) NOT NULL,
  user_status_code_id INT NOT NULL,
  user_type_code_id INT NOT NULL
);

CREATE TABLE user_event_logs (
  log_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  event_date TIMESTAMP NOT NULL DEFAULT 0,
  user_event_code_id INT NOT NULL,
  event_status_code_id INT NOT NULL,
  comment VARCHAR(2048)
);
