//package mx.unam.aragon.elZorro.controller.error;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//public class CustomErrorController implements ErrorController {
//
//    @RequestMapping("/error")
//    public String handleError(HttpServletRequest request) {
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        boolean isAuthenticated = auth != null && auth.isAuthenticated();
//
//        String referer = request.getHeader("Referer");
//
//        if (status != null) {
//            int statusCode = Integer.parseInt(status.toString());
//
//            if (statusCode == HttpStatus.NOT_FOUND.value()) {
//                if (isAuthenticated && referer != null && !referer.isEmpty()) {
//                    return "redirect:" + referer;
//                } else {
//                    if (auth.getAuthorities().stream()
//                            .anyMatch(granted -> granted.getAuthority().equals("ROLE_ADMIN"))) {
//                        return "redirect:administracion";
//                    } else if (auth.getAuthorities().stream()
//                            .anyMatch(granted -> granted.getAuthority().equals("ROLE_CAJA"))) {
//                        return "redirect:caja";
//                    } else {
//                        return "redirect:login";
//                    }
//                }
//            }
//
//            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//                return "error/500";
//            }
//        }
//
//        return "error/error";
//    }
//}