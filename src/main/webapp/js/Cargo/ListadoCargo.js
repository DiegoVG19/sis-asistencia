$(document).ready(function () {
    $('#dataTableCargo').DataTable({
        "language": {
            "url": "/Asistenciaa3/json/es-ES.json"
        },
        "ajax": {
            "url": "/Asistenciaa3/listadoCargo",
            "dataSrc": ""
        },
        "columns": [
            {"data": "ID","className": "text-center"},  // Aplica la clase text-center
            {"data": "CARGO","className": "text-center"}  // Aplica la clase text-center
        ]
    });

    $('#dataTable').on('click', '.btnEditar', function () {
        var codigoCliente = $(this).data('codigocliente');
        window.location.href = 'editarClientes.html?codigoCliente=' + codigoCliente;
    });
});



