package sec.project.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @ExceptionHandler
    @RequestMapping(value = "/error")
    public String renderErrorPage(Model model, Exception error) {
        String errorMsg = "";
        for (StackTraceElement e : error.getStackTrace()) {
            errorMsg += "<br/>";
            errorMsg += e.toString();
        }
        model.addAttribute("error", errorMsg);
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
