$(document).ready(function () {
    $("#btnInsertarCargo").click(function () {
        let cargo = $("#Cargo").val();


        if (!cargo ) {
            $("#alertCamposIncompletos").modal('show');
            return;
        }

        let parametro = {
            cargo: cargo
       
        };

        $.getJSON("insertarCargo", parametro, function (data) {
            if (data.resultado === "ok") {
                $("#alertCargoRegistrado").modal('show');
                setTimeout(function () {
                    $("#alertCargoRegistrado").modal('hide');
                    setTimeout(function () {
                        window.location.href = "listarEmpresa.html";
                    }, 1000);
                }, 2000);
            } else {
                alert("Error");
            }
        });
    });
});



