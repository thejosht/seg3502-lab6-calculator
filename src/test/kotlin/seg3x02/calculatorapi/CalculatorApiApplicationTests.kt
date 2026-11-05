package seg3x02.calculatorapi

import com.jayway.jsonpath.JsonPath
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalculatorApiApplicationTests {
    @LocalServerPort
    private var port: Int = 0

    private val client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build()

    private fun get(path: String): HttpResponse<String> = client.send(
        HttpRequest.newBuilder(URI.create("http://127.0.0.1:$port$path"))
            .timeout(Duration.ofSeconds(10)).GET().build(),
        HttpResponse.BodyHandlers.ofString()
    )

    @ParameterizedTest
    @CsvSource(
        "add,10,2,12", "subtract,10,2,8", "multiply,10,2,20", "divide,10,2,5",
        "add,-4,1.5,-2.5", "subtract,-4,1.5,-5.5", "multiply,-4,1.5,-6", "divide,-4,2,-2",
        "divide,7,2,3.5", "divide,0,4,0", "multiply,12,0,0", "add,0,0,0",
        "add,1e3,2,1002", "divide,1,4,0.25", "subtract,2,10,-8", "multiply,-3,-2,6"
    )
    fun `operations return JSON numbers`(operation: String, first: String, second: String, expected: Double) {
        val response = get("/calculator/$operation/$first/$second")
        assertEquals(200, response.statusCode())
        assertTrue(response.headers().firstValue("content-type").orElse("").startsWith("application/json"))
        assertEquals(expected, response.body().toDouble(), 1e-10)
    }

    @ParameterizedTest
    @ValueSource(strings = ["0", "0.0", "-0.0"])
    fun `division by zero returns a clear error`(denominator: String) {
        val response = get("/calculator/divide/10/$denominator")
        assertEquals(400, response.statusCode())
        assertEquals("Cannot divide by zero.", response.body())
    }

    @ParameterizedTest
    @ValueSource(strings = ["add/abc/2", "subtract/1/abc", "multiply/NaN/2", "divide/1/Infinity", "add/-Infinity/2", "multiply/1e309/2"])
    fun `invalid operands are rejected`(path: String) {
        val response = get("/calculator/$path")
        assertEquals(400, response.statusCode())
        assertTrue(response.body().contains("numbers"))
    }

    @ParameterizedTest
    @ValueSource(strings = ["add/1e308/1e308", "subtract/-1e308/1e308", "multiply/1e308/2", "divide/1e308/1e-308"])
    fun `overflow does not produce invalid JSON numbers`(path: String) {
        val response = get("/calculator/$path")
        assertEquals(400, response.statusCode())
        assertEquals("The result is outside the supported range.", response.body())
    }

    @Test
    fun `OpenAPI documents exactly the four required GET routes`() {
        val response = get("/v3/api-docs")
        assertEquals(200, response.statusCode())
        val spec = JsonPath.parse(response.body())
        val paths = spec.read<Map<String, Map<String, Any>>>("$.paths")
        assertEquals(setOf("add", "subtract", "multiply", "divide").map { "/calculator/$it/{nombre1}/{nombre2}" }.toSet(), paths.keys)
        for ((_, path) in paths) {
            assertEquals(setOf("get"), path.keys)
            val operation = path["get"] as Map<*, *>
            val parameters = operation["parameters"] as List<*>
            assertEquals(setOf("nombre1", "nombre2"), parameters.map { (it as Map<*, *>)["name"] }.toSet())
            assertTrue(parameters.all { (it as Map<*, *>)["required"] == true && it["in"] == "path" })
            val responses = operation["responses"] as Map<*, *>
            assertTrue(responses.keys.containsAll(listOf("200", "400")))
        }
    }

    @Test
    fun `OpenAPI is also available as YAML`() {
        val response = get("/v3/api-docs.yaml")
        assertEquals(200, response.statusCode())
        assertTrue(response.body().startsWith("openapi:"))
        assertTrue(response.body().contains("/calculator/divide/{nombre1}/{nombre2}"))
    }

    @Test
    fun `Swagger UI and its configuration are available`() {
        val page = get("/swagger-ui/index.html")
        assertEquals(200, page.statusCode())
        assertTrue(page.body().contains("swagger-ui"))
        assertEquals("/v3/api-docs", JsonPath.read<String>(get("/v3/api-docs/swagger-config").body(), "$.url"))
    }

    @Test
    fun `home directs the browser to the documentation`() {
        val response = get("/")
        assertEquals(302, response.statusCode())
        assertTrue(response.headers().firstValue("location").orElse("").endsWith("/swagger-ui.html"))
    }

    @Test
    fun `unknown routes return not found`() {
        assertEquals(404, get("/calculator/unknown/1/2").statusCode())
    }
}
