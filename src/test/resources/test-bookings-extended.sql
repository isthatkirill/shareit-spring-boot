insert into booking (start_date, end_date, item_id, booker_id, status) values
(NOW() + INTERVAL '2' DAY, NOW() + INTERVAL '4' DAY, 1, 4, 'WAITING'), -- id=1
(NOW() + INTERVAL '1' DAY, NOW() + INTERVAL '2' DAY, 2, 4, 'WAITING'), -- id=2
(NOW() - INTERVAL '10' DAY, NOW() - INTERVAL '9' DAY, 1, 2, 'APPROVED'), -- id=3
(NOW() + INTERVAL '10' DAY, NOW() + INTERVAL '12' DAY, 1, 3, 'APPROVED'), -- id=4
(NOW() - INTERVAL '2' DAY, NOW() - INTERVAL '1' DAY, 6, 3, 'APPROVED'), -- id=5
(NOW() + INTERVAL '3' DAY, NOW() + INTERVAL '4' DAY, 4, 6, 'REJECTED'), -- id=6
(NOW() + INTERVAL '4' DAY, NOW() + INTERVAL '6' DAY, 5, 6, 'APPROVED'), -- id=7
(NOW() + INTERVAL '5' DAY, NOW() + INTERVAL '7' DAY, 6, 6, 'CANCELED'), -- id=8
(NOW() - INTERVAL '1' DAY, NOW() + INTERVAL '2' DAY, 2, 5, 'WAITING'), -- id=9
(NOW() - INTERVAL '3' DAY, NOW() + INTERVAL '7' DAY, 3, 5, 'APPROVED'), -- id=10
(NOW() + INTERVAL '9' DAY, NOW() + INTERVAL '23' DAY, 4, 5, 'WAITING'); -- id=11