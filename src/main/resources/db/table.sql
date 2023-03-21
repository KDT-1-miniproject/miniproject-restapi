CREATE TABLE user_tb(
    id int auto_increment primary key,
    email varchar not null UNIQUE,
    password varchar not null,
    salt varchar not null,
    p_info_id int default 0,
    c_info_id int default 0,
    created_at timestamp not null
);

CREATE TABLE person_info_tb( -- skill에 있는 p_info_id
    id int auto_increment primary key,
    name varchar(20) not null,
    phone varchar(11),
    address varchar, 
    birthday TIMESTAMP,
    created_at timestamp not null
);

CREATE TABLE skill_tb(
    id int auto_increment primary key,
    p_info_id int,
    post_id int, -- c_info -> post_id
    resume_id int,
    skills varchar not null,
    created_at timestamp not null
); 

CREATE TABLE company_info_tb(
    id int auto_increment primary key,
    logo longtext,
    name varchar(20) not null,
    number varchar not null,
    boss_name varchar,
    address varchar not null,
    manager_name varchar(20) not null,
    manager_phone varchar(11),
    size int ,
    cyear int ,
    created_at timestamp not null
);

CREATE TABLE job_post_tb(
    id int auto_increment primary key,
    title varchar(20) not null,
    c_info_id int not null,
    career int not null,
    pay varchar not null,
    condition varchar not null,
    start_hour varchar not null,
    end_hour varchar not null,
    deadline timestamp not null,
    c_intro varchar(200) not null,
    job_intro varchar(200) not null,    -- skill에     post_id int, 
    created_at timestamp not null
);

CREATE TABLE resume_tb(
    id int auto_increment primary key,
    p_info_id int not null,
    profile longtext not null,
    title varchar(20) not null,
    publish boolean not null, 
    portfolio varchar not null,
    self_intro varchar(200) not null,
    created_at timestamp not null
);

CREATE TABLE person_proposal_tb(
    id int auto_increment primary key,  
    p_info_id int not null,
    post_id int not null,
    resume_id int not null,
    status int not null default 0, -- 0 : pending 
    created_at timestamp not null    
);

CREATE TABLE proposal_pass_tb(
    id int auto_increment primary key, 
    p_info_id int not null,
    p_proposal_id int not null, 
    comment varchar not null,
    created_at timestamp not null    
);

CREATE TABLE skill_filter_tb(
    id int auto_increment primary key,  
    skill varchar not null,
    post_id int default 0,
    resume_id int default 0
);

CREATE TABLE person_scrap_tb(
    id int auto_increment primary key,  
    p_info_id int not null,
    post_id int not null,
    created_at timestamp not null  
);

CREATE TABLE company_scrap_tb(
    id int auto_increment primary key,  
    c_info_id int not null,
    resume_id int not null,
    created_at timestamp not null  
);

CREATE TABLE person_customer_service_tb(
    id int auto_increment primary key,  
    question varchar not null,
    answer varchar not null,
    created_at timestamp not null  
);

CREATE TABLE company_customer_service_tb(
    id int auto_increment primary key,  
    question varchar not null,
    answer varchar not null,
    created_at timestamp not null  
);
