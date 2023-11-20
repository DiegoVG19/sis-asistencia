// Nombre del archivo: miScript.js

$(document).ready(function () {
    // Función para obtener el valor de una cookie
    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2)
            return parts.pop().split(';').shift();
    }

    // Obtener los valores de las cookies
    const usuario = getCookie("usuario");
    const token = getCookie("token");
    const dato = getCookie("dato");
    const clave = getCookie("clave");

    // Llena los campos con los valores obtenidos
    $("#txtUsuario").val(usuario);
    $("#txtToken").val(token);
    $("#txtDato").val(dato);
    $("#txtClave").val(clave);

    // ...

    $("#btnActualizar").click(function () {
        const actual = $("#txtActual").val();
        const nueva = $("#txtNueva").val();
        const confirmar = $("#txtConfirmar").val();

        // Validar si la contraseña actual coincide con "clave"
        if (clave !== actual) {
            $("#resultado").html('Contraseña actual no coincide');
            return; // Detener la ejecución si no coincide
        }

        // Validar si las contraseñas nuevas coinciden
        if (nueva !== confirmar) {
            $("#resultado").html('Las contraseñas nuevas no coinciden');
            return; // Detener la ejecución si no coinciden
        }

        // Si las validaciones pasan, procede a enviar los valores al servlet
        const parametros = {
            usuario: usuario,
            nueva: nueva,
            clave: clave,
            actual: actual,
            confirmar: confirmar,
            token: token,
            dato: dato
        };
        console.log(parametros);
        $.ajax({
            url: 'actualizar-contrasena', // Ruta correcta de tu servlet
            method: 'POST',
            data: parametros,
            success: function (data) {
                if (data.resultado === 'ok') {
                    alert('Contraseña actualizada exitosamente');
                    $(location).attr('href', 'index.html');
                } else {
                    alert('Error');
                    $("#resultado").html('Contraseña actualizada exitosamente');
                    $(location).attr('href', 'index.html');
                }
            },
            error: function () {
                alert('Error al enviar la solicitud al servidor');
            }
        });
    });
});
