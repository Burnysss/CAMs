package main.boundary;

import main.boundary.welcome.Welcome;
import main.controller.account.AccountManager;
import main.controller.camp.CampManager;

public class UIBootup 
{

    private static boolean onBootUp()
    {
        return CampManager.repositoryIsEmpty();
    }
    public static void start() 
    {
        AccountManager.loadUsers();
        if(onBootUp())
        {
            CampManager.loadCamps();
        }
        Welcome.welcome();
    }

    
}