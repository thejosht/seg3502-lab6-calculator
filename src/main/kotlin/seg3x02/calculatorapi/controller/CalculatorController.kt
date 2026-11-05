package seg3x02.calculatorapi.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/calculator")
@Tag(name = "Calculatrice", description = "Opérations arithmétiques sur deux nombres.")
@ApiResponses(
    ApiResponse(responseCode = "200", description = "Résultat du calcul",
        content = [Content(mediaType = "application/json", schema = Schema(type = "number", format = "double"))]),
    ApiResponse(responseCode = "400", description = "Nombres invalides, division par zéro ou dépassement numérique",
        content = [Content(mediaType = "text/plain", schema = Schema(type = "string"))])
)
class CalculatorController {
    @GetMapping("/add/{nombre1}/{nombre2}")
    @Operation(summary = "Additionner deux nombres")
    fun add(@PathVariable nombre1: Double, @PathVariable nombre2: Double): Double =
        calculate(nombre1, nombre2) { a, b -> a + b }

    @GetMapping("/subtract/{nombre1}/{nombre2}")
    @Operation(summary = "Soustraire le deuxième nombre du premier")
    fun subtract(@PathVariable nombre1: Double, @PathVariable nombre2: Double): Double =
        calculate(nombre1, nombre2) { a, b -> a - b }

    @GetMapping("/multiply/{nombre1}/{nombre2}")
    @Operation(summary = "Multiplier deux nombres")
    fun multiply(@PathVariable nombre1: Double, @PathVariable nombre2: Double): Double =
        calculate(nombre1, nombre2) { a, b -> a * b }

    @GetMapping("/divide/{nombre1}/{nombre2}")
    @Operation(summary = "Diviser le premier nombre par le deuxième")
    fun divide(@PathVariable nombre1: Double, @PathVariable nombre2: Double): Double =
        calculate(nombre1, nombre2) { a, b ->
            require(b != 0.0) { "Cannot divide by zero." }
            a / b
        }

    private fun calculate(a: Double, b: Double, operation: (Double, Double) -> Double): Double {
        require(a.isFinite() && b.isFinite()) { "Both operands must be finite numbers." }
        val result = operation(a, b)
        require(result.isFinite()) { "The result is outside the supported range." }
        return result
    }
}
