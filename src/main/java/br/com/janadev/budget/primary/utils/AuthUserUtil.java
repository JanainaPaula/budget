package br.com.janadev.budget.primary.utils;

import br.com.janadev.budget.secondary.auth.user.BudgetUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUserUtil {

    private AuthUserUtil() {
    }

    public static long getAuthenticatedUserId(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        BudgetUserDetails user = (BudgetUserDetails) authentication.getPrincipal();
        return user.getId();
    }
}
