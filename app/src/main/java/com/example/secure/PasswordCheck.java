package com.example.secure;

public class PasswordCheck {

    public int check(String psw1, String psw2)
    {
        final String letters = ".*[A-Za-z].*";
        final String numbers = ".*[0-9].*";

        if(psw1.equals(psw2))
        {
            if(psw1.length() >= 8)
            {
                if(psw1.matches(letters))
                {
                    if(psw1.matches(numbers))
                    {
                        return 0;
                    }
                    return 4;
                }
                return 3;
            }
            return 2;
        }
        return 1;
    }
}
