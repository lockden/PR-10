import java.util.Scanner;

public class PR {

    static class UserLimitException extends Exception {
        public UserLimitException(String msg) { super(msg); }
    }

    static class InvalidUsernameException extends Exception {
        public InvalidUsernameException(String msg) { super(msg); }
    }

    static class InvalidPasswordException extends Exception {
        public InvalidPasswordException(String msg) { super(msg); }
    }

    static class UserNotFoundException extends Exception {
        public UserNotFoundException(String msg) { super(msg); }
    }

    static class AuthFailedException extends Exception {
        public AuthFailedException(String msg) { super(msg); }
    }

    static class UserAlreadyExistsException extends Exception {
        public UserAlreadyExistsException(String msg) { super(msg); }
    }

    private static final int MAX_USERS = 15;
    private static final String[] usernames = new String[MAX_USERS];
    private static final String[] passwords  = new String[MAX_USERS];
    private static int userCount = 0;

    private static String[] forbidden = {"admin","pass","password","qwerty","ytrewq"};
    private static int forbiddenCount = 5;


    private static boolean hasSpace(String s) {
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == ' ') return true;
        return false;
    }

    private static boolean isSpecial(char c) {
        return (c > ' ' && c <= '~') &&
               !(c >= 'A' && c <= 'Z') &&
               !(c >= 'a' && c <= 'z') &&
               !(c >= '0' && c <= '9');
    }

    private static boolean isAllowed(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
               (c >= '0' && c <= '9') || isSpecial(c);
    }

    private static int countDigits(String s) {
        int n = 0;
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9') n++;
        return n;
    }

    private static int countSpecial(String s) {
        int n = 0;
        for (int i = 0; i < s.length(); i++)
            if (isSpecial(s.charAt(i))) n++;
        return n;
    }

    private static String toLower(String s) {
        char[] buf = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            buf[i] = (c >= 'A' && c <= 'Z') ? (char)(c + 32) : c;
        }
        return new String(buf);
    }

    private static boolean containsWord(String text, String word) {
        String t = toLower(text), w = toLower(word);
        if (w.length() > t.length()) return false;
        for (int i = 0; i <= t.length() - w.length(); i++) {
            boolean match = true;
            for (int j = 0; j < w.length(); j++)
                if (t.charAt(i + j) != w.charAt(j)) { match = false; break; }
            if (match) return true;
        }
        return false;
    }

    private static void validateUsername(String name) throws InvalidUsernameException {
        if (name == null || name.length() < 5)
            throw new InvalidUsernameException("Ім'я повинно мати щонайменше 5 символів.");
        if (hasSpace(name))
            throw new InvalidUsernameException("Ім'я не повинно містити пробіли.");
    }

    private static void validatePassword(String pwd) throws InvalidPasswordException {
        if (pwd == null || pwd.length() < 10)
            throw new InvalidPasswordException("Пароль повинен мати щонайменше 10 символів.");
        if (hasSpace(pwd))
            throw new InvalidPasswordException("Пароль не повинен містити пробіли.");
        for (int i = 0; i < pwd.length(); i++)
            if (!isAllowed(pwd.charAt(i)))
                throw new InvalidPasswordException(
                    "Пароль містить недопустимі символи.");
        if (countSpecial(pwd) < 1)
            throw new InvalidPasswordException("Пароль повинен містити хоча б 1 спецсимвол.");
        if (countDigits(pwd) < 3)
            throw new InvalidPasswordException("Пароль повинен містити хоча б 3 цифри.");
        for (int i = 0; i < forbiddenCount; i++)
            if (containsWord(pwd, forbidden[i]))
                throw new InvalidPasswordException(
                    "Пароль містить заборонене слово: \"" + forbidden[i] + "\".");
    }

    private static void addUser(String name, String pwd)
            throws UserLimitException, InvalidUsernameException,
                   InvalidPasswordException, UserAlreadyExistsException {
        validateUsername(name);
        validatePassword(pwd);
        for (int i = 0; i < MAX_USERS; i++)
            if (name.equals(usernames[i]))
                throw new UserAlreadyExistsException(
                    "Користувач \"" + name + "\" вже існує.");
        if (userCount >= MAX_USERS)
            throw new UserLimitException(
                "Ліміт досягнуто: не можна додати більше " + MAX_USERS + " користувачів.");
        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] == null) {
                usernames[i] = name;
                passwords[i] = pwd;
                userCount++;
                System.out.println("Користувача \"" + name + "\" зареєстровано.");
                return;
            }
        }
    }

    private static void deleteUser(String name) throws UserNotFoundException {
        for (int i = 0; i < MAX_USERS; i++) {
            if (name.equals(usernames[i])) {
                usernames[i] = null;
                passwords[i] = null;
                userCount--;
                System.out.println("Користувача \"" + name + "\" видалено.");
                return;
            }
        }
        throw new UserNotFoundException("Користувача \"" + name + "\" не знайдено.");
    }

    private static void authenticate(String name, String pwd)
            throws UserNotFoundException, AuthFailedException {
        for (int i = 0; i < MAX_USERS; i++) {
            if (name.equals(usernames[i])) {
                if (!passwords[i].equals(pwd))
                    throw new AuthFailedException(
                        "Невірний пароль для користувача \"" + name + "\".");
                System.out.println("Користувача \"" + name + "\" автентифіковано успішно.");
                return;
            }
        }
        throw new UserNotFoundException("Користувача \"" + name + "\" не знайдено.");
    }

    private static void addForbiddenWord(String word) throws InvalidPasswordException {
        if (word == null || word.isEmpty())
            throw new InvalidPasswordException("Заборонене слово не може бути порожнім.");
        String[] newArr = new String[forbiddenCount + 1];
        for (int i = 0; i < forbiddenCount; i++) newArr[i] = forbidden[i];
        newArr[forbiddenCount] = toLower(word);
        forbidden = newArr;
        forbiddenCount++;
        System.out.println("Слово \"" + word + "\" додано до списку заборонених.");
    }

    private static void listUsers() {
        System.out.println("\n--- Список користувачів ---");
        int n = 0;
        for (int i = 0; i < MAX_USERS; i++)
            if (usernames[i] != null) { System.out.println("  • " + usernames[i]); n++; }
        if (n == 0) System.out.println("  (немає зареєстрованих користувачів)");
        System.out.println("Всього: " + n + " / " + MAX_USERS);
    }

    private static String read(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static int readChoice(Scanner sc) {
        System.out.print("Ваш вибір: ");
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private static void printMenu() {
        System.out.println("    Система автентифікації          ");
        System.out.println("                                    ");
        System.out.println("   1. Додати користувача            ");
        System.out.println("   2. Видалити користувача          ");
        System.out.println("   3. Автентифікувати               ");
        System.out.println("   4. Список користувачів           ");
        System.out.println("   5. Додати заборонене слово       ");
        System.out.println("   0. Вихід                         ");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            switch (readChoice(sc)) {

                case 1:
                    try {
                        String name = read(sc, "Ім'я: ");
                        String pwd  = read(sc, "Пароль: ");
                        addUser(name, pwd);
                    } catch (UserLimitException | UserAlreadyExistsException e) {
                    } catch (InvalidUsernameException e) {
                        System.out.println("Невірне ім'я: " + e.getMessage());
                    } catch (InvalidPasswordException e) {
                        System.out.println("Невірний пароль: " + e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        deleteUser(read(sc, "Ім'я для видалення: "));
                    } catch (UserNotFoundException e) {
                    }
                    break;

                case 3:
                    try {
                        String name = read(sc, "Ім'я: ");
                        String pwd  = read(sc, "Пароль: ");
                        authenticate(name, pwd);
                    } catch (UserNotFoundException | AuthFailedException e) {
                    }
                    break;

                case 4:
                    listUsers();
                    break;

                case 5:
                    try {
                        addForbiddenWord(read(sc, "Заборонене слово: "));
                    } catch (InvalidPasswordException e) {
                    }
                    break;

                case 0:
                    System.out.println("До побачення!");
                    running = false;
                    break;

                default:
                    System.out.println("Невідомий варіант. Введіть число з меню.");
            }
        }
        sc.close();
    }
}