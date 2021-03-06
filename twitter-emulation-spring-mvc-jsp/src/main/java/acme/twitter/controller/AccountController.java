package acme.twitter.controller;

import acme.twitter.dao.exception.AccountExistsException;
import acme.twitter.dao.exception.AccountNotExistsException;
import acme.twitter.domain.Account;
import acme.twitter.domain.AccountStatistics;
import acme.twitter.domain.Tweet;
import acme.twitter.service.AccountService;
import acme.twitter.service.FollowerService;
import acme.twitter.service.TweetService;
import acme.twitter.web.AccountForm;
import acme.twitter.web.SearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Account controller.
 */
@Controller
@RequestMapping("/account")
public class AccountController {
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private AccountService accountService;
    private TweetService tweetService;
    private FollowerService followerService;
    private MessageSourceAccessor messageSourceAccessor;

    @Autowired
    public AccountController(AccountService accountService, TweetService tweetService, FollowerService followerService,
                             MessageSourceAccessor messageSourceAccessor) {
        this.accountService = accountService;
        this.tweetService = tweetService;
        this.followerService = followerService;
        this.messageSourceAccessor = messageSourceAccessor;
    }

    /**
     * Shows registration form
     *
     * @param model model
     * @return view name
     */
    @RequestMapping(value = "/register", method = GET)
    public String showRegistrationForm(Model model) {
        model.addAttribute(new AccountForm());

        return "registrationForm";
    }

    /**
     * Processes registration
     *
     * @param accountForm account form
     * @param errors      errors
     * @return view name
     */
    @RequestMapping(value = "/register", method = POST)
    public String processRegistration(
            @Valid AccountForm accountForm,
            Errors errors) {
        if (errors.hasErrors()) {
            return "registrationForm";
        }

        try {
            accountService.add(accountForm.getUsername(), accountForm.getPassword(), accountForm.getDescription());

            return "redirect:/login";
        } catch (AccountExistsException e) {
            errors.reject("account.exists", messageSourceAccessor.getMessage("account.exists"));

            return "registrationForm";
        }
    }

    /**
     * Shows profile form
     *
     * @param model     model
     * @param principal principal
     * @return view name
     * @throws AccountNotExistsException if account does not exist
     */
    @RequestMapping(value = "/profile", method = GET)
    public String showProfileForm(Model model, Principal principal) throws AccountNotExistsException {
        Account account = accountService.findByUsername(principal.getName());
        model.addAttribute(new AccountForm(account.getUsername(), account.getDescription()));

        return "profileForm";
    }

    /**
     * Processes profile to cancel
     *
     * @return view name
     */
    @RequestMapping(value = "/profile", method = POST, params = "cancel")
    public String cancelProfile() {
        return "redirect:/account/show";
    }

    /**
     * Processes profile to save
     *
     * @param accountForm account form
     * @param errors      errors
     * @return view name
     */
    @RequestMapping(value = "/profile", method = POST, params = "save")
    public String processProfile(
            @Valid AccountForm accountForm,
            Errors errors) {
        if (errors.hasErrors()) {
            return "profileForm";
        }

        accountService.update(accountForm.getUsername(), accountForm.getPassword(), accountForm.getDescription());

        return "redirect:/account/show";
    }

    /**
     * Deletes account
     *
     * @return view name
     */
    @RequestMapping(value = "/delete", method = GET)
    public String showDeleteAccountForm() {
        return "deleteAccountForm";
    }

    /**
     * Processes deletion to cancel
     *
     * @return view name
     */
    @RequestMapping(value = "/delete", method = POST, params = "cancel")
    public String cancelDelete() {
        return "redirect:/account/profile";
    }

    /**
     * Processes deletion to delete
     *
     * @param principal principal
     * @return view name
     */
    @RequestMapping(value = "/delete", method = POST, params = "delete")
    public String processDelete(Principal principal) {
        accountService.delete(principal.getName());

        return "redirect:/logout";
    }

    /**
     * Shows account form for current authenticated account
     *
     * @param principal principal
     * @return view name
     */
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String showAccountForm(Principal principal) {
        return "redirect:/account/show/" + principal.getName();
    }

    /**
     * Shows account form
     *
     * @param username  username
     * @param model     model
     * @param principal principal
     * @return view name
     * @throws AccountNotExistsException if account does not exist
     */
    @RequestMapping(value = "/show/{username}", method = RequestMethod.GET)
    public String showAccountForm(
            @PathVariable String username,
            Model model,
            Principal principal) throws AccountNotExistsException {
        if (principal != null) {
            log.debug("username: {}, principal.name: {}", username, principal.getName());
        } else {
            log.debug("username: {}", username);
        }

        // Forward for unauthorized or other account
        if ((principal == null) || !username.equals(principal.getName())) {
            return "forward:/account/tweets/" + username;
        }

        Account account = accountService.findByUsername(username);
        AccountStatistics accountStatistics = getAccountStatistics(principal.getName(), username);
        List<Tweet> tweets = tweetService.findTimelineByAccount(account);

        model.addAttribute(account);
        model.addAttribute(accountStatistics);
        model.addAttribute(tweets);
        model.addAttribute(new SearchForm());
        model.addAttribute("copyrightDate", new Date());

        return "accountForm";
    }

    /**
     * Processes search.
     *
     * @param searchForm search form
     * @param errors     errors
     * @param model      model
     * @param principal  principal
     * @return view name
     * @throws AccountNotExistsException if account does not exist
     */
    @RequestMapping(value = "/search", method = POST)
    public String processSearch(
            @Valid SearchForm searchForm,
            Errors errors,
            Model model,
            Principal principal) throws AccountNotExistsException {
        if (errors.hasErrors()) {
            return "redirect:/account/show";
        }

        Account account = accountService.findByUsername(principal.getName());
        AccountStatistics accountStatistics = getAccountStatistics(principal.getName(), principal.getName());
        List<Account> accounts = accountService.findByUsernamePart(searchForm.getUsernamePart());

        model.addAttribute(account);
        model.addAttribute(accountStatistics);
        model.addAttribute("title", "Search Result");
        model.addAttribute("searchAccountList", accounts);
        model.addAttribute("copyrightDate", new Date());

        return "searchForm";
    }

    /**
     * Processes tweets.
     *
     * @param username  username
     * @param model     model
     * @param principal principal
     * @return view name
     * @throws AccountNotExistsException if account does not exist
     */
    @RequestMapping(value = "/tweets/{username}", method = GET)
    public String processTweets(
            @PathVariable String username,
            Model model,
            Principal principal) throws AccountNotExistsException {
        Account account = accountService.findByUsername(username);
        AccountStatistics accountStatistics = getAccountStatistics(
                (principal != null) ? principal.getName() : username,
                username);
        List<Tweet> tweets = tweetService.findByAccount(account);

        model.addAttribute(account);
        model.addAttribute(accountStatistics);
        model.addAttribute("title", "Tweets");
        model.addAttribute(tweets);
        model.addAttribute(new SearchForm());
        model.addAttribute("copyrightDate", new Date());

        return "accountForm";
    }

    /**
     * Processes following.
     *
     * @param username  username
     * @param model     model
     * @param principal principal
     * @return view name
     * @throws AccountNotExistsException if account does not exist
     */
    @RequestMapping(value = "/following/{username}", method = GET)
    public String processFollowing(
            @PathVariable String username,
            Model model,
            Principal principal) throws AccountNotExistsException {
        Account account = accountService.findByUsername(username);
        AccountStatistics accountStatistics = getAccountStatistics(
                (principal != null) ? principal.getName() : username,
                username);
        List<Account> accounts = followerService.findFollowingByUsername(username);

        model.addAttribute(account);
        model.addAttribute(accountStatistics);
        model.addAttribute("title", "Following");
        model.addAttribute("searchAccountList", accounts);
        model.addAttribute(new SearchForm());
        model.addAttribute("copyrightDate", new Date());

        return "searchForm";
    }

    /**
     * Processes followers.
     *
     * @param username  username
     * @param model     model
     * @param principal principal
     * @return view name
     * @throws AccountNotExistsException if account does not exist
     */
    @RequestMapping(value = "/followers/{username}", method = GET)
    public String processFollowers(
            @PathVariable String username,
            Model model,
            Principal principal) throws AccountNotExistsException {
        Account account = accountService.findByUsername(username);
        AccountStatistics accountStatistics = getAccountStatistics(
                (principal != null) ? principal.getName() : username,
                username);
        List<Account> accounts = followerService.findFollowersByUsername(username);

        model.addAttribute(account);
        model.addAttribute(accountStatistics);
        model.addAttribute("title", "Followers");
        model.addAttribute("searchAccountList", accounts);
        model.addAttribute(new SearchForm());
        model.addAttribute("copyrightDate", new Date());

        return "searchForm";
    }

    private AccountStatistics getAccountStatistics(String whoUsername, String whomUsername) {
        int tweetsCount = tweetService.countByUsername(whomUsername);
        int followingCount = followerService.countFollowingByUsername(whomUsername);
        int followersCount = followerService.countFollowersByUsername(whomUsername);
        boolean isFollow = followerService.isExist(whoUsername, whomUsername);

        return new AccountStatistics(tweetsCount, followingCount, followersCount, isFollow);
    }

    /**
     * Follows account
     *
     * @param username  username
     * @param principal principal
     * @return view name
     * @throws AccountNotExistsException if account does not exist
     */
    @RequestMapping(value = "/follow/{username}", method = POST)
    public String processFollow(
            @PathVariable String username,
            Principal principal) throws AccountNotExistsException {
        Account whoAccount = accountService.findByUsername(principal.getName());
        Account whomAccount = accountService.findByUsername(username);

        followerService.add(whoAccount.getUsername(), whomAccount.getUsername());

        return "redirect:/account/show/" + username;
    }

    /**
     * Unfollows account
     *
     * @param username  username
     * @param principal principal
     * @return view name
     * @throws AccountNotExistsException if account does not exist
     */
    @RequestMapping(value = "/unfollow/{username}", method = POST)
    public String processUnfollow(
            @PathVariable String username,
            Principal principal) throws AccountNotExistsException {
        Account whoAccount = accountService.findByUsername(principal.getName());
        Account whomAccount = accountService.findByUsername(username);

        followerService.delete(whoAccount.getUsername(), whomAccount.getUsername());

        return "redirect:/account/show/" + username;
    }

    @ExceptionHandler(AccountNotExistsException.class)
    public String handleAccountNotExistException() {
        return "redirect:/account/show";
    }
}
