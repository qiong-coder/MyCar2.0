INSERT INTO Account VALUES(1, 'admin', 'password', 'test', '18810346541', 1, 0, 0, CURRENT_TIMESTAMP);


INSERT INTO Store VALUES (1, '义乌','义乌商贸城一期A14门市','4006470600,0470-6230866', 0, CURRENT_TIMESTAMP());
INSERT INTO Store VALUES (2, '滿洲里','满洲里西郊国际机场','4006470600,0470-6230866', 0, CURRENT_TIMESTAMP());
INSERT INTO Store VALUES (3, '套娃景区', '套娃景区酒店','4006470600,0470-6230866', 0, CURRENT_TIMESTAMP());


INSERT INTO VehicleInfo VALUES (1, 'audi', 'displacement', 'gearbox', 'boxes', 'manned', 'oil', 4, 'description', 'type', 'picture', '{insurance:[10,10,10,10],day_costs:[[100,100,100],[200,100,200],[300,200,200]],discounts:[[100,100,100],[100,90,80],[90,100,100,100]]}', CURRENT_TIMESTAMP(), 0);
INSERT INTO VehicleInfo VALUES (2, 'audi', 'displacement', 'gearbox', 'boxes', 'manned', 'oil', 4, 'description', 'type', 'picture', '{insurance:[10,10,10,10],day_costs:[[300,100,100],[300,100,200],[300,200,100]],discounts:[[100,100,100],[100,100,100],[100,80,80]]}', CURRENT_TIMESTAMP(), 1);

INSERT INTO Vehicle VALUES(1, 1, 'number', 'description', 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1);
INSERT INTO Vehicle VALUES(2, 2, 'number2', 'description', 10, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2);