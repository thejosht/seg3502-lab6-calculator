package seg3x02.calculatorapi.controller

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Hidden
@Controller
class HomeController {
    @GetMapping("/")
    fun documentation(): String = "redirect:/swagger-ui.html"
}
