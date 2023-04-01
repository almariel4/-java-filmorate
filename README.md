# java-filmorate
Template repository for Filmorate project.
![Диаграмма таблицы проекта](https://github.com/Vkurse/-java-filmorate/blob/main/Untitled.png)
##### _Получение всех пользователей из таблицы:_
SELECT * <br/>
FROM user;

##### _Запрос на получение общих друзей:_
SELECT login <br/>
FROM (SELECT login<br/>
FROM users<br/>
WHERE user_id IN (SELECT friend_id<br/>
FROM friends<br/>
WHERE user_id=(long value)<br/> 
AND status=true))<br/>
WHERE login IN (SELECT login<br/>
FROM user<br/>
WHERE user_id IN (SELECT friend_id<br/>
FROM friends<br/>
WHERE user_id = (long value)<br/>
AND status = true));

##### _Запрос на получение списка всех фильмов:_
SELECT *<br/>
FROM film;
##### _Запрос на получение топ 10 популярных фильмов:_
SELECT *<br/>
FROM film<br/>
WHERE film_id IN (SELECT film_id, count(user_id) AS likes_count<br/>
FROM likes<br/>
GROUP BY film_id<br/>
ORDER BY likes_count DESC<br/>
LIMIT (10));
