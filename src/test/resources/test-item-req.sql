insert into item_requests (description, requester_id, created) values
('user 3 request', 3, NOW()),
('user 3 request second', 3, NOW() - INTERVAL '1' DAY),
('user 4 request', 4, NOW() - INTERVAL '2' DAY),
('user 2 request', 2, NOW() - INTERVAL '3' DAY);

