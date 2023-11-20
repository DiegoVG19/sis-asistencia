$(document).ready(function () {
    $('#dataTableEmpleado').DataTable({
        "language": {
            "url": "/Asistenciaa3/json/es-ES.json"
        },
        "ajax": {
            "url": "/Asistenciaa3/listadoEmpleado",
            "dataSrc": ""
        },
        "columns": [
            {"data": "EMPRESA", "className": "text-center"},
            {"data": "CARGO", "className": "text-center"},
            {"data": "NOMBRE", "className": "text-center"},
            {"data": "APELLIDO PATERNO", "className": "text-center"},
            {"data": "APELLIDO MATERNO", "className": "text-center"},
            {
                "data": "FECHA NACIMIENTO",
                "render": function (data, type, full, meta) {
                    function formatearFecha(fechaCompleta) {
                        const fecha = new Date(fechaCompleta);
                        const dia = fecha.getDate();
                        const mes = fecha.getMonth() + 1;
                        const año = fecha.getFullYear();
                        return dia + '/' + mes + '/' + año;
                    }
                    const fechaFormateada = formatearFecha(data);
                    return fechaFormateada;
                },
                "className": "text-center"
            },
            {"data": "DNI", "className": "text-center"},
            {"data": "DIRECCION", "className": "text-center"},
            {"data": "CELULAR", "className": "text-center"},
        ]
    });

    $('#dataTable').on('click', '.btnEditar', function () {
        var codigoCliente = $(this).data('codigocliente');
        window.location.href = 'editarClientes.html?codigoCliente=' + codigoCliente;
    });
});

