package main.boundary.account;

import main.controller.account.AccountManager;
import main.model.user.UserType;
import main.model.user.User;
import main.utils.exception.PasswordIncorrectException;
import main.utils.exception.UserErrorException;
import main.utils.exception.PageBackException;
import main.utils.ui.ChangePage;
import main.utils.ui.UserTypeGetter;

import java.util.Scanner;

/**
 * Handles the process of changing the password for a user
 */
public class ResetPassword 
{
    /** 
     * Asks the user to retry entering the password or go back to the previous page
     *  
     * @param userType              The type of the user whose password is being changed
     * @param userID                The ID of the user whose password is being changed
     * @throws PageBackException    If the user chooses to go back to the previous page
     */
    public static void askToRetry(UserType userType, String userID) throws PageBackException 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press Enter to try again or [b] to go back.");
        String choice = scanner.nextLine();
        if (choice.equals("b")) {
            throw new PageBackException();
        } else {
            System.out.println("Please try again.");
            changePassword(userType, userID);
        }
    }

    /**
     * Initiates the process of changing the password for a user
     * 
     * @param userType              The type of the user whose password is being changed
     * @param userID                The ID of the user whose password is being changed
     * @throws PageBackException    If the user chooses to go back to the previous page
     */
    public static void changePassword(UserType userType, String userID) throws PageBackException 
    {
        ChangePage.changePage();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter your old password: ");
        String oldPassword = AttributeGetter.getPassword1();

        try {
            AccountManager.login(userType, userID, oldPassword);
        } catch (UserErrorException e) {
            throw new RuntimeException(e);
        } catch (PasswordIncorrectException e) {
            System.out.println("Password incorrect.");
            askToRetry(userType, userID);
        }

        String newPassword;
        String newPasswordAgain;

        do {
            // read the new password
            System.out.print("Please enter a new password: ");
            newPassword = AttributeGetter.getPassword1();
            // read the new password again
            System.out.print("Please enter a new password again: ");
            newPasswordAgain = AttributeGetter.getPassword1();
            // if the new password is not the same as the new password again, ask the user to enter again

            if (!newPassword.equals(newPasswordAgain)) {
                System.out.println("The two passwords are not the same.");
                askToRetry(userType, userID);
            } else if (newPassword.equals(oldPassword)) {
                System.out.println("New password cannot be the same as the old password.");
                askToRetry(userType, userID);
            }
        } while (!newPassword.equals(newPasswordAgain));

        if (newPassword.length() < 8) {
            System.out.println("Password must be more than 8 characters.");
            askToRetry(userType, userID);
        }

        try 
        {
            AccountManager.changePassword(userType, userID, oldPassword, newPassword);

            System.out.println("Password changed successfully.");

            System.out.println("Press [Enter] to go back to the main page.");
            scanner.nextLine();
            throw new PageBackException();
        } catch (PasswordIncorrectException | UserErrorException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkDefaultPassword(UserType userType, String userID, String password)
    {
        if(password.equals("password"))
        {
            return true;
        }
        
        return false;
    }

    public static boolean promptUserForPasswordReset(UserType userType, String userID, String password)
    {
        if(checkDefaultPassword(userType, userID, password))
        {
            System.out.println("Your password is still the default password.");
            System.out.println("Would you like to change it? [y]/[n] : ");
            Scanner scanner = new Scanner(System.in);
            String option = scanner.nextLine().toLowerCase();

            if(option.equals("y"))
            {
                return true;
            }
            else if(option.equals("n"))
            {
                return false;
            }
            else 
            {
                System.out.println("Invalid option. Please try again");
                return promptUserForPasswordReset(userType, userID, password);
            }
        }
        return false;
    }
}

