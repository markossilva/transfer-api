alter table tbl_transaction add constraint FKdehjx9jqxpx57mhbugmpk6uw7 foreign key (status) references tbl_transaction_status
alter table tbl_wallet add constraint FKfltcthjw2eksf81h55v72hjip foreign key (status) references tbl_wallet_status

insert into tbl_wallet_status (id, name) values (0, 'CREATED'), (1, 'DEACTIVATED');
insert into tbl_transaction_status (id, name) values (0, 'PROCESSING'), (1, 'SUCCESS'), (2, 'FAIL');