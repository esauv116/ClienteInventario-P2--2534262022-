package sv.edu.utec.servicio;

import com.fasterxml.jackson.databind.ObjectMapper;
import sv.edu.utec.api.ProductoApi;
import sv.edu.utec.api.RespuestaProductos;
import sv.edu.utec.modelo.Producto;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ClienteProveedorApi {
    private static final String URL_API = "https://dummyjson.com/products";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ClienteProveedorApi() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<Producto> obtenerProductosProveedor() {
        List<Producto> listaProductos = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_API))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                RespuestaProductos respuesta = objectMapper.readValue(response.body(), RespuestaProductos.class);
                if (respuesta != null && respuesta.getProducts() != null) {
                    for (ProductoApi dto : respuesta.getProducts()) {
                        listaProductos.add(dto.aProducto());
                    }
                }
            } else {
                System.out.println("Error al obtener productos. Código HTTP: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error al consumir la API: " + e.getMessage());
        }
        return listaProductos;
    }
}