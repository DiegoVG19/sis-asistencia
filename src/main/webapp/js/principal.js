$(document).ready(function () {
    let token = localStorage.getItem("token");
    let logi = localStorage.getItem("logiAlumno");

//    console.log("Usuario de Sesion:"+logi);
//    console.log("Token de Sesion:"+token);
//    alert("Token de Sesion, solo para mostrar para el examen:"+token);
//    Solo para pruebas de inicio de sesion
    let parametros = {token: token};
    $.getJSON("validarPrincipal", parametros, function (data) {
        if (data.resultado === "ok") {
//            window.alert("Usuario con Acceso");
        } else {
            window.alert("Usuario no Permitido");
            window.location.href = "index.html";
        }
    });
    $("#btnIniciar").click(function () {
        window.location.href = "insertarAlumno.html";
    });
});

