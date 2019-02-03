<%-- 
    Document   : page2
    Created on : Sep 26, 2018, 9:37:07 AM
    Author     : Melnikov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Главная</title>
    </head>
    <body>
        <h1>Навигация по сайту</h1>
        <p id="info">${info}</p>
        <br>${adminhash}
        <br>
        <a id="login" href="showLogin">Войти в систему</a><br>
        <a id="logout" href="logout">Выйти из системы</a><br>
        <a id="addReader" href="newReader">добавить читателя</a><br>
        <a id="listBooks" href="showBooks">Список книг</a><br>
        
        <br>
        <p>Для администратора:</p>
        <a id="addBook" href="newBook">добавить книгу</a><br>
        <a id="listReaders" href="showReader">Список читателей</a><br>
        <a id="showTakeBook" href="showTakeBook">Список выданных книг</a>
        <a id="library" href="library">Выдать книгу</a><br>
        <a id="showUserRoles" href="showUserRoles">Назначение ролей пользователям</a>
        <br><br>
        Добавлена книга:<br>
        Название: ${book.nameBook}<br>
        Автор: ${book.author}
        <hr>
        Добавлен читатель:<br>
        Имя: ${reader.name}<br>
        Фамилия: ${reader.surname}
        
    </body>
</html>
