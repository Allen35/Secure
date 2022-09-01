package com.example.secure;

public class PasswordCheck {

    public int check(String psw1, String psw2)
    {
        final String letters = ".*[a-z].*";
        final String LETTERS = ".*[A-Z].*";
        final String numbers = ".*[0-9].*";

        if(psw1.equals(psw2))
        {
            if(psw1.length() >= 8)
            {
                if(psw1.length() < 65)
                {
                    if(psw1.matches(letters))
                    {
                        if(psw1.matches(LETTERS))
                        {
                            if(psw1.matches(numbers))
                            {
                                return 0;
                            }
                            return 6;
                        }
                        return 5;
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
