insert into booking (start_date, end_date, item_id, booker_id, status) values
(NOW() - INTERVAL '4' DAY, NOW() - INTERVAL '3' DAY, 1, 2, 'APPROVED'),
(NOW() + INTERVAL '1' DAY, NOW() + INTERVAL '2' DAY, 1, 3, 'APPROVED');