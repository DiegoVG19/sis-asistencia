// logout.js

function cerrarSesion() {
    // Elimina todas las cookies
    document.cookie = "usuario=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    document.cookie = "pass=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    document.cookie = "dato=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

    // Redirige a la página de inicio de sesión
    window.location.href = "login.html";
}
