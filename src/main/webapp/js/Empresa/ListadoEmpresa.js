$(document).ready(function () {
    $('#dataTableEmpresa').DataTable({
        "language": {
            "url": "/Asistenciaa3/json/es-ES.json"
        },
        "ajax": {
            "url": "/Asistenciaa3/listadoEmpresa",
            "dataSrc": ""
        },
        "columns": [
            {"data": "ID", "className": "text-center"},
            {"data": "RUC", "className": "text-center"},
            {"data": "EMPRESA", "className": "text-center"}
        ]
    });

    $('#dataTable').on('click', '.btnEditar', function () {
        var codigoCliente = $(this).data('codigocliente');
        window.location.href = 'editarClientes.html?codigoCliente=' + codigoCliente;
    });
});


