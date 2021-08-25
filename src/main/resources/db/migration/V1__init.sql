create table teams
(
    team_id   int   AUTO_INCREMENT   not null
        primary key,
    team_name varchar(255) null
);

create table department
(
    department_id   int auto_increment
        primary key,
    department_name varchar(255) null
);

create table employee
(
    employee_id     int AUTO_INCREMENT  primary key,
    gender          VARCHAR(255)        null,
    graduation_date date         null,
    employee_name   varchar(255) null,
    gross_salary    double       null,
    net_salary      double       null,
    team_id         int          null,
    department_id   int          null,
    birthdate       date         null,
    manager_id      int          null,

    constraint employee_department_department_id_fk
        foreign key (department_id) references department (department_id),
    constraint employee_employee_employee_id_fk
        foreign key (manager_id) references employee (employee_id),
    constraint employee_teams_team_id_fk
        foreign key (team_id) references teams (team_id)
);


