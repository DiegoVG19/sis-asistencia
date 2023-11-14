<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>Listar Asistencia</title>
    </head>
    <body>
        <h1>Lista de Asistencia</h1>
        <table border="1">
            <tr>
                <th>ID Asistencia</th>
                <th> Empleado</th>
                <th>Asistencia</th>
                <!-- Agrega más encabezados según las columnas de tu tabla -->
            </tr>
            <c:forEach items="${asistenciaList}" var="asistencia">
                <tr>
                    <td>${asistencia.idAsistencia}</td>
                    <td>${asistencia.empleado}</td>
                    <td>${asistencia.asistencia}</td>
                    <!-- Agrega más campos según las columnas de tu tabla -->
                </tr>
            </c:forEach>

        </table>
    </body>
</html>
