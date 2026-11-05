package seg3x02.calculatorapi.controller

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@Hidden
@RestControllerAdvice
class ControllerExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun invalidNumber(): ResponseEntity<String> =
        ResponseEntity.badRequest().body("Both operands must be valid numbers.")

    @ExceptionHandler(IllegalArgumentException::class)
    fun invalidCalculation(exception: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity.badRequest().body(exception.message ?: "Unable to process request.")
}
