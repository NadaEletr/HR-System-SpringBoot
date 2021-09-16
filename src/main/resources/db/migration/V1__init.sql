create table teams
(
    team_id   int AUTO_INCREMENT not null
        primary key,
    team_name varchar(255) not null
);

create table department
(
    department_id   int auto_increment
        primary key,
    department_name varchar(255) not null
);
create table employee
(
    national_id         varchar(255) unique,
    id                  int AUTO_INCREMENT primary key,
    first_name          VARCHAR(255) not null,
    leaves              int          not null,
    last_name           VARCHAR(255) not null,
    employee_degree     VARCHAR(255) null,
    years_of_experience int          not null,
    gender              VARCHAR(255) null,
    graduation_date     date null,
    gross_salary        double       not null,
    net_salary          double null,
    team_id             int null,
    acceptable_leaves   int null,
    department_id       int null,
    birthdate           date null,
    manager_id          int null,

    constraint employee_department_department_id_fk
        foreign key (department_id) references department (department_id),
    constraint employee_employee_employee_id_fk
        foreign key (manager_id) references employee (id),
    constraint employee_teams_team_id_fk
        foreign key (team_id) references teams (team_id)


);
create table salary_history
(
    id              int auto_increment
        primary key,
    date            date null,
    raises          double null,
    bonus           double null,
    exceeded_leaves double null,
    taxes           double null,
    insurance       double null,
    net_salary      double null,
    employee_id     int null,
    constraint salary_history_employee_employeeId_fk
        foreign key (employee_id) references employee (id)
);

create table absence
(
    id          int auto_increment
        primary key,

    date        date null,
    employee_id int null,
    CONSTRAINT date_employee_id UNIQUE (employee_id, date),
    constraint vacations_employee_employeeId_fk
        foreign key (employee_id) references employee (id)

);
create table user_account
(
    user_name   VARCHAR(255)
        primary key,
    password    VARCHAR(255) null,
    employee_id int null,
    roles       VARCHAR(255) null,
    constraint UserAccount_employee_employeeId_fk
        foreign key (employee_id) references employee (id)

);
create table earnings
(
    id          int auto_increment
        primary key,
    bonus       double null,
    employee_id int null,
    raise       double null,
    date        DATE null,
--     CONSTRAINT date_employee UNIQUE(employee_id, date),
    constraint earnings_employee_employeeId_fk
        foreign key (employee_id) references employee (id)

);






