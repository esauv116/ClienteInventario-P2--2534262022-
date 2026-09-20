package sv.edu.utec.servicio;

import sv.edu.utec.api.ProveedorAPI;
import sv.edu.utec.datos.ProductoDAO;
import sv.edu.utec.modelo.Producto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SincronizacionService {

    private final ProveedorAPI proveedorApi;
    private final ProductoDAO dao;

    // Recibe dependencias por constructor
    public SincronizacionService(ProveedorAPI proveedorApi, ProductoDAO dao) {
        this.proveedorApi = proveedorApi;
        this.dao = dao;
    }

    public String sincronizar(int limite) throws IOException, InterruptedException, SQLException {
        List<Producto> productosApi = proveedorApi.obtenerProductos(limite);
        int insertados = 0;
        int actualizados = 0;

        for (Producto p : productosApi) {
            if (dao.existe(p.getId())) {
                dao.actualizar(p);
                actualizados++;
            } else {
                dao.insertar(p);
                insertados++;
            }
        }

        return "insertados: " + insertados + " | actualizados: " + actualizados;
    }
}