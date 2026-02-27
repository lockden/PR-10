public class AuthSystem {

    private User[] users = new User[15];
    private int userCount = 0;

    private String[] forbiddenWords = {"admin", "pass", "password", "qwerty", "ytrewq"};
    private int forbiddenCount = 5;

    public void registerUser(String username, String password)
            throws UserLimitExceededException, InvalidUsernameException, InvalidPasswordException {

        if (userCount == 15) {
            throw new UserLimitExceededException("Досягнуто максимум користувачів (15).");
        }

        validateUsername(username);
        validatePassword(password);

        int freeIndex = -1;
        for (int i = 0; i < users.length; i++) {
            if (users[i] == null) {
                freeIndex = i;
                break;
            }
        }

        users[freeIndex] = new User(username, password);
        userCount++;
    }

    public void deleteUser(String username) throws UserNotFoundException {
        for (int i = 0; i < users.length; i++) {
            if (users[i] != null && users[i].getUsername().equals(username)) {
                users[i] = null;
                userCount--;
                return;
            }
        }
        throw new UserNotFoundException("Користувача не знайдено.");
    }

    public void authenticate(String username, String password)
            throws AuthenticationException {

        for (int i = 0; i < users.length; i++) {
            if (users[i] != null &&
                users[i].getUsername().equals(username) &&
                users[i].getPassword().equals(password)) {
                return;
            }
        }
        throw new AuthenticationException("Невірне ім'я користувача або пароль.");
    }

    private void validateUsername(String username)
            throws InvalidUsernameException {

        if (username.length() < 5) {
            throw new InvalidUsernameException("Ім'я повинно містити мінімум 5 символів.");
        }

        for (int i = 0; i < username.length(); i++) {
            if (username.charAt(i) == ' ') {
                throw new InvalidUsernameException("Ім'я не повинно містити пробілів.");
            }
        }
    }

    private void validatePassword(String password)
            throws InvalidPasswordException {

        if (password.length() < 10) {
            throw new InvalidPasswordException("Пароль має бути не менше 10 символів.");
        }

        int digitCount = 0;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {

            char c = password.charAt(i);

            if (c == ' ') {
                throw new InvalidPasswordException("Пароль не повинен містити пробілів.");
            }

            if (c >= '0' && c <= '9') {
                digitCount++;
            }
            else if (!((c >= 'a' && c <= 'z') ||
                       (c >= 'A' && c <= 'Z'))) {
                hasSpecial = true;
            }
        }

        if (digitCount < 3) {
            throw new InvalidPasswordException("Пароль має містити мінімум 3 цифри.");
        }

        if (!hasSpecial) {
            throw new InvalidPasswordException("Пароль має містити мінімум 1 спеціальний символ.");
        }

        for (int i = 0; i < forbiddenCount; i++) {
            if (password.toLowerCase().contains(forbiddenWords[i])) {
                throw new InvalidPasswordException("Пароль містить заборонене слово: " + forbiddenWords[i]);
            }
        }
    }

    public void addForbiddenWord(String word) {
        String[] newArray = new String[forbiddenCount + 1];

        for (int i = 0; i < forbiddenCount; i++) {
            newArray[i] = forbiddenWords[i];
        }

        newArray[forbiddenCount] = word;
        forbiddenWords = newArray;
        forbiddenCount++;
    }
}