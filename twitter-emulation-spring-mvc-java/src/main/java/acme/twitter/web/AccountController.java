package acme.twitter.web;

import acme.twitter.data.AccountRepository;
import acme.twitter.data.TweetRepository;
import acme.twitter.domain.Account;
import acme.twitter.domain.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Account controller.
 */
@Controller
@RequestMapping("/account")
public class AccountController {
    private AccountRepository accountRepository;
    private TweetRepository tweetRepository;
    private MessageSourceAccessor messageSourceAccessor;

    @Autowired
    public AccountController(AccountRepository accountRepository, TweetRepository tweetRepository,
                             MessageSourceAccessor messageSourceAccessor) {
        this.accountRepository = accountRepository;
        this.tweetRepository = tweetRepository;
        this.messageSourceAccessor = messageSourceAccessor;
    }

    /**
     * Shows login form
     *
     * @param model model
     * @return view name
     */
    @RequestMapping(value = {"/login"}, method = GET)
    public String showLoginForm(Model model) {
        model.addAttribute(new LoginForm());
        return "loginForm";
    }

    /**
     * Processes login
     *
     * @param loginForm login form
     * @param errors    errors
     * @return view name
     */
    @RequestMapping(value = "/login", method = POST)
    public String processLogin(
            @Valid LoginForm loginForm,
            Errors errors) {
        if (errors.hasErrors()) {
            return "loginForm";
        }

        Account account = accountRepository.findByUsername(loginForm.getUsername());

        return "redirect:/account/" + account.getUsername();
    }

    /**
     * Shows registration form
     *
     * @param model model
     * @return view name
     */
    @RequestMapping(value = "/register", method = GET)
    public String showRegistrationForm(Model model) {
        model.addAttribute(new RegistrationForm());
        return "registrationForm";
    }

    /**
     * Processes registration
     *
     * @param registrationForm registration form
     * @param errors           errors
     * @return view name
     */
    @RequestMapping(value = "/register", method = POST)
    public String processRegistration(
            @Valid RegistrationForm registrationForm,
            Errors errors) {
        if (errors.hasErrors()) {
            return "registrationForm";
        }

        if (accountRepository.isAccountExists(registrationForm.getUsername())) {
            errors.reject("account.exists", messageSourceAccessor.getMessage("account.exists"));

            return "registrationForm";
        }

        Account account = new Account(registrationForm.getUsername(), registrationForm.getPassword(), registrationForm.getDescription());
        accountRepository.save(account);

        return "redirect:/account/" + account.getUsername();
    }

    /**
     * Shows main form
     *
     * @param username username
     * @param model    model
     * @return view name
     */
    @RequestMapping(value = "/{username}", method = GET)
    public String showMainForm(@PathVariable String username, Model model) {
        Account account = accountRepository.findByUsername(username);
        List<Tweet> tweets = tweetRepository.findAllByUsername(account);
        model.addAttribute(account);
        model.addAttribute(tweets);

        return "mainForm";
    }
}