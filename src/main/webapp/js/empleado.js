// Función para obtener el valor de una cookie por su nombre
function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

// Comprobar si el usuario ha iniciado sesión
let token = getCookie("token");
if (token === "") {
    // Si no hay token, redirigir a la página de inicio de sesión
    window.location.href = "login.html";
} else {
    // El usuario tiene un token, realizar otras operaciones aquí si es necesario
    $(document).ready(function () {
        let usuario = getCookie("usuario");
        $.getJSON("obtenerUsuario", {usuario: usuario}, function (data) {
            if (data.error) {
                console.error("Error: " + data.error);
            } else {
                $("#nombreUsuario").text(data.nombreCompleto);
            }
        });

        // Obtener y listar datos en la tabla
        function obtenerYListarEmpleados() {
            $.ajax({
                url: 'obtenerEmpleados', // Cambiado a 'obtenerEmpleados' según el servlet proporcionado
                type: 'GET',
                dataType: 'json',
                success: function (data) {
                    // Limpiar la tabla antes de agregar nuevos datos
                    $('#example tbody').empty();

                    // Recorrer los resultados y agregar cada fila a la tabla
                    for (var i = 0; i < data.length; i++) {
                        var empleado = data[i];
                        var deleteIcon = '<a href="#" class="btn btn-danger delete-icon" data-id="' + usuario.idUsuario + '"><i class="fa-solid fa-trash"></i></a>';

                        var rowHtml = '<tr>' +
                                '<td>' + empleado[0] + '</td>' +
                                '<td>' + empleado[1] + '</td>' +
                                '<td>' + empleado[2] + '</td>' +
                                '<td>' + empleado[3] + '</td>' +
                                '<td>' + empleado[5] + '</td>' + // Usamos el índice 5 para el nombre del cargo
                                '<td><a href="#" class="btn btn-danger delete-icon" data-id="' + empleado[0] + '"><i class="fa-solid fa-trash"></i></a></td>' +
                                '</tr>';
                        $('#example tbody').append(rowHtml);
                    }

                    $('.delete-icon').on('click', function (e) {
                        e.preventDefault();
                        var id = $(this).data('id');
                        var confirmDelete = confirm('¿Realmente desea eliminar este registro?');
                        if (confirmDelete) {
                            // Enviar el ID al servlet de eliminación
                            $.ajax({
                                url: 'eliminarEmpleado?id=' + id, // Ajusta la URL según el servlet de eliminación de empleados
                                type: 'GET',
                                success: function (response) {
                                    console.log(response);
                                    // Actualizar la tabla después de la eliminación
                                    location.reload();
                                },
                                error: function (error) {
                                    console.error(error);
                                }
                            });
                        }
                    });
                },
                error: function (error) {
                    console.error(error);
                }
            });
        }

        // Llamar a la función para obtener y listar datos al cargar la página
        obtenerYListarEmpleados();
    });
}
