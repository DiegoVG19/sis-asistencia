$(document).ready(function () {
    $("#btnIngresar").click(function () {
        let logi = $("#txtLogi").val();
        let pass = $("#txtPass").val();
        let parametro = {logi: logi, pass: pass};

        $.getJSON("validar", parametro, function (data) {
            if (data.resultado === "ok") {
                let token = data.token;
                let dato = data.dato;
                // Se establecen las cookies con los valores correspondientes
                document.cookie = "usuario=" + logi;
                document.cookie = "token=" + token;
                document.cookie = "pass=" + pass;
                document.cookie= "dato=" +dato;
                // Se redirige a la página principal sin incluir la contraseña en la URL
                window.location.href = "principal.html";
            } else {
                alert("Error");
            }
        });
    });
});

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
