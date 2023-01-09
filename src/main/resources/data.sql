drop table if exists tbl_client CASCADE
drop table if exists tbl_transaction CASCADE
drop table if exists tbl_transaction_status CASCADE
drop table if exists tbl_wallet CASCADE
drop sequence if exists hibernate_sequence

create sequence hibernate_sequence start with 1 increment by 1
create table tbl_client (id uuid not null, name varchar(255), primary key (id))
create table tbl_transaction (id numeric(19,2) not null, amount numeric(19,2) not null, cause varchar(255), client_id uuid, date timestamp, status integer not null, target_client_id uuid not null, target_wallet_id uuid not null, type integer not null, wallet_id uuid, primary key (id))
create table tbl_transaction_status (id integer not null, name varchar(255) not null, primary key (id))
create table tbl_wallet (id uuid not null, balance numeric(19,2), status integer not null, client_id uuid, primary key (id))
create table tbl_wallet_status (id integer not null, name varchar(255) not null, primary key (id))

alter table tbl_transaction add constraint FKdehjx9jqxpx57mhbugmpk6uw6 foreign key (client_id) references tbl_client
alter table tbl_transaction add constraint FKdehjx9jqxpx57mhbugmpk6uw7 foreign key (status) references tbl_transaction_status
alter table tbl_transaction add constraint FKtkya0tswenhf9vmhsvht6m9fm foreign key (wallet_id) references tbl_wallet

alter table tbl_wallet add constraint FKfltcthjw2eksf81h55v72hjio foreign key (client_id) references tbl_client
alter table tbl_wallet add constraint FKfltcthjw2eksf81h55v72hjip foreign key (status) references tbl_wallet_status

insert into tbl_wallet_status (id, name) values (0, 'CREATED'), (1, 'DEACTIVATED');
insert into tbl_transaction_status (id, name) values (0, 'PROCESSING'), (1, 'SUCCESS'), (2, 'FAIL');