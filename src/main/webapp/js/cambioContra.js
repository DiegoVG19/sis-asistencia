$(document).ready(function () {
    let token = localStorage.getItem("token");
    let logi = localStorage.getItem("logiAlumno");

    $("#btnCambio").click(function () {
        let contraAct = $("#txtContraAct").val();
        let contraNue1 = $("#txtContraNue1").val();
        let contraNue2 = $("#txtContraNue2").val();

        if (!contraAct || !contraNue1 || !contraNue2) {
            mostrarMensajeError("#alertClaveVacia", 2500);
            return;
        }

        let parametroVer = {logi: logi, contraAct: sha256(contraAct), token: token};
        let parametroAct = {logi: logi, contraNue1: sha256(contraNue1), token: token};

        $.getJSON("verificarContra", parametroVer, function (data) {
            if (data.resultado === "ok") {
                if (contraAct === contraNue1) {
                    mostrarMensajeError("#alertClaveNueva1", 2500);
                } else {
                    if (contraNue1 !== contraNue2) {
                        mostrarMensajeError("#alertClaveNueva2", 2500);
                    } else {
//                        alert("Va bien");
                        $.getJSON("cambioContra", parametroAct, function (data) {
                            if (data.resultado === "ok") {
                                window.location.href = "index.html";
                            } else {
                                window.alert("Error de Comunicacion");
                            }
                        });
                    }
                }
            } else {
                mostrarMensajeError("#alertClaveActual", 2500);
            }
        });


    });

    $("#btnCancelar").click(function () {
        window.location.href = "principal.html";
    });

    function mostrarMensajeError(id, tiempo) {
        $(id).show();
        setTimeout(function () {
            $(id).hide();
        }, tiempo);
    }

    function sha256(message) {
        const hash = CryptoJS.SHA256(message);
        return hash.toString(CryptoJS.enc.Hex);
    }
});


