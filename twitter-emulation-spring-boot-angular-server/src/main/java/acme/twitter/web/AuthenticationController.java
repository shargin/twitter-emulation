package acme.twitter.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequestMapping("/api/auth")
public class AuthenticationController {
    /**
     * Returns user information.
     *
     * @param principal principal
     * @return user information
     */
    @GetMapping("/user")
    @ResponseBody
    public Principal user(Principal principal) {
        return principal;
    }
}