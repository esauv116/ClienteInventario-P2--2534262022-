package sv.edu.utec.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import sv.edu.utec.modelo.Producto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ProveedorAPI {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ProveedorAPI() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<Producto> obtenerProductos(int limite) throws IOException, InterruptedException {
        String url = "https://dummyjson.com/products?limit=" + limite + "&select=title,stock";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Error en la API del proveedor. Código HTTP: " + response.statusCode());
        }

        RespuestaProductos respuesta = objectMapper.readValue(response.body(), RespuestaProductos.class);
        List<Producto> lista = new ArrayList<>();

        if (respuesta != null && respuesta.getProducts() != null) {
            for (ProductoApi dto : respuesta.getProducts()) {
                lista.add(dto.aProducto());
            }
        }

        return lista;
    }
}