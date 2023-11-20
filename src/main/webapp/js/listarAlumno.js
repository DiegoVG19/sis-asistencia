$(document).ready(function () {
    
    $('#datatableAlumno').DataTable({
        "ajax": {
            "url": "/ExamenCriptoModulo2/listarAlumno",
            "dataSrc": ""
        },
        "columns": [
            {"data": "CODIGO"},
            {"data": "PATERNO"},
            {"data": "MATERNO"},
            {"data": "DIRECCION"},
            {"data": "PESO"},
            {
                "data": "FECHA",
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
                }
            },
            {
                "render": function (data, type, full, meta) {
                    var codigoAlumno = full.CODIGO;

                    return `<td align="center"> 
                        <button class="btn btn-info btn-sm btnEditar" data-codigoAlumno="${codigoAlumno}">
                            <i class="far fa-edit text-white"></i> 
                        </button> 
                        <button class="btn btn-warning btn-sm btnInformacion" data-codigoAlumno="${codigoAlumno}">
                            <i class="far fa-eye text-white"></i>
                        </button> 
                         <button class="btn btn-danger btn-sm eliminarEmpleado" data-codigoAlumno="${codigoAlumno}">
                         <i class="fa fa-trash text-white"></i>
                        </button>

                    </td>`;
                }
            }
        ]
    });

    $('#datatablesSimple').on('click', '.btnEditar', function () {
        var codigoEmpleado = $(this).data('codigoempleado');
        window.location.href = 'editarEmpleado.html?codigoEmpleado=' + codigoEmpleado;
    });
});


