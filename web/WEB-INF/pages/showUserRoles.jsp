<%-- 
    Document   : editUserRoles
    Created on : Nov 7, 2018, 12:35:23 PM
    Author     : Melnikov
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Назначение ролей!</h1>
        <p id="info">${info}</p>
        <form action="changeUserRole" method="POST">
            <table>
                <tr>
                    <th>Пользователь</th>
                    <th>Новая роль</th>
                    <th></th>
                    <th></th>
                </tr>
                <tr>
                    <td>
                        <select name="user">
                            <option value="" disabled selected hidden></option>
                            <c:forEach var="entry" items="${mapMembers}">
                                <option value="${entry.key.user.id}">${entry.key.login}, роль: ${entry.value}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>
                        <select name="role">
                            <option value="" disabled selected hidden></option>
                            <c:forEach var="role" items="${listRoles}">
                                <option value="${role.id}">${role.name}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td><input type="submit" name="setButton" value="Назначить"></td>
                    <td><input type="submit" name="deleteButton" value="Удалить" id="deleteButton"></td>
                </tr>
            </table>
        </form>
    </body>
</html>
