package seg3x02.calculatorapi

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition(info = Info(
    title = "API de calcul",
    version = "1.0",
    description = "Addition, soustraction, multiplication et division de deux nombres."
))
class CalculatorApiApplication

fun main(args: Array<String>) {
	runApplication<CalculatorApiApplication>(*args)
}
