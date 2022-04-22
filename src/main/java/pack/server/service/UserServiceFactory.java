package pack.server.service;

public abstract class UserServiceFactory {

    private static final UserService userService = new UserServiceMemoryImpl();

    private UserServiceFactory() {
    }

    public static UserService getUserService() {
        return userService;
    }
}
