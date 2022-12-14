insert into device_type(id,name) values(1,'Mac');
insert into device_type(id,name) values(2,'Windows');

insert into device(id, system_name, device_type_id) values (1000, 'My mac', 1);
insert into device(id, system_name, device_type_id) values (1001, 'My Windows Server', 2);

insert into service(id, name, price) values (1000, 'Device of any type', 4);
insert into service(id, name, price,device_type_id) values (1001, 'Antivirus for Windows', 5, 2);
insert into service(id, name, price,device_type_id) values (1002, 'Antivirus for Mac', 7, 1);
insert into service(id, name, price) values (1003, 'Backup', 3);
insert into service(id, name, price) values (1004, 'Screen share', 1);


insert into inventory(id) values (1000);
insert into inventory_item(id, inventory_id, device_id) values(1000, 1000, 1000);
insert into inventory_item_service(id,inventory_item_id, service_id) values (5000, 1000, 1002);
insert into inventory_item_service(id,inventory_item_id, service_id) values (5001, 1000, 1003);
insert into inventory_item_service(id,inventory_item_id, service_id) values (5002, 1000, 1004);


insert into inventory(id) values (1010);

-- First Windows
insert into inventory_item(id, inventory_id, device_id) values(1010, 1010, 1001);
insert into inventory_item_service(id,inventory_item_id, service_id) values (6050, 1010, 1000);
insert into inventory_item_service(id,inventory_item_id, service_id) values (5050, 1010, 1001);
insert into inventory_item_service(id,inventory_item_id, service_id) values (5051, 1010, 1004);

-- Second windows
insert into inventory_item(id, inventory_id, device_id) values(1011, 1010, 1001);
insert into inventory_item_service(id,inventory_item_id, service_id) values (6051, 1011, 1000);
insert into inventory_item_service(id,inventory_item_id, service_id) values (5052, 1011, 1001);
insert into inventory_item_service(id,inventory_item_id, service_id) values (5053, 1011, 1004);
insert into inventory_item_service(id,inventory_item_id, service_id) values (5054, 1011, 1003);

-- First Mac
insert into inventory_item(id, inventory_id, device_id) values(1012, 1010, 1000);
insert into inventory_item_service(id,inventory_item_id, service_id) values (6052, 1012, 1000);
insert into inventory_item_service(id, inventory_item_id, service_id) values (5055,1012, 1002);
insert into inventory_item_service(id, inventory_item_id, service_id) values (5056,1012, 1003);
insert into inventory_item_service(id, inventory_item_id, service_id) values (5057,1012, 1004);

-- Second Mac
insert into inventory_item(id, inventory_id, device_id) values(1013, 1010, 1000);
insert into inventory_item_service(id,inventory_item_id, service_id) values (6053, 1013, 1000);
insert into inventory_item_service(id,inventory_item_id, service_id) values (5058, 1013, 1002);
insert into inventory_item_service(id,inventory_item_id, service_id) values (5059, 1013, 1003);
insert into inventory_item_service(id,inventory_item_id, service_id) values (5060, 1013, 1004);

-- Third Mac
insert into inventory_item(id, inventory_id, device_id) values(1014, 1010, 1000);
insert into inventory_item_service(id,inventory_item_id, service_id) values (6054, 1014, 1000);
insert into inventory_item_service(id, inventory_item_id, service_id) values (5061, 1014, 1002);

