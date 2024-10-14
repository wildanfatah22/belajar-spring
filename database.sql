CREATE TABLE users (
    username VARCHAR(100) NOT NULL PRIMARY KEY ,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    token VARCHAR(100),
    token_expired_at BIGINT,
    UNIQUE (token)
);

drop table users;

SELECT * from users;

CREATE TABLE task (
                      id VARCHAR(100) NOT NULL PRIMARY KEY,
                      username VARCHAR(100) NOT NULL,
                      title VARCHAR(255) NOT NULL,
                      description TEXT,
                      status BOOLEAN DEFAULT FALSE,
                      created_at DATE DEFAULT CURRENT_DATE,
                      updated_at DATE DEFAULT CURRENT_DATE,
                      start_date DATE,
                      end_date DATE,
                      FOREIGN KEY (username) REFERENCES users(username)
);


DROP table task;