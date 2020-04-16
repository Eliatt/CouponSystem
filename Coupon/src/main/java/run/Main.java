package run;

import exceptions.ExistException;
import exceptions.LoginException;
import tests.Test;

public class Main {
    public static void main(String[] args) throws LoginException, ExistException {
        Test test = new Test();
        test.testAll();

    }
}














