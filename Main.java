import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AuthSystem authSystem = new AuthSystem();

        while (true) {
            System.out.println("\n1 - Додати користувача");
            System.out.println("2 - Видалити користувача");
            System.out.println("3 - Аутентифікація");
            System.out.println("4 - Додати заборонене слово");
            System.out.println("0 - Вихід");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Ім'я: ");
                        String username = scanner.nextLine();

                        System.out.print("Пароль: ");
                        String password = scanner.nextLine();

                        authSystem.registerUser(username, password);
                        System.out.println("Користувача додано.");
                        break;

                    case 2:
                        System.out.print("Ім'я для видалення: ");
                        String delUser = scanner.nextLine();

                        authSystem.deleteUser(delUser);
                        System.out.println("Користувача видалено.");
                        break;

                    case 3:
                        System.out.print("Ім'я: ");
                        String authUser = scanner.nextLine();

                        System.out.print("Пароль: ");
                        String authPass = scanner.nextLine();

                        authSystem.authenticate(authUser, authPass);
                        System.out.println("Користувача успішно аутентифіковано.");
                        break;

                    case 4:
                        System.out.print("Введіть нове заборонене слово: ");
                        String word = scanner.nextLine();
                        authSystem.addForbiddenWord(word);
                        System.out.println("Слово додано.");
                        break;

                    case 0:
                        return;

                    default:
                        System.out.println("Невірний вибір.");
                }

            } catch (UserLimitExceededException |
                     InvalidUsernameException |
                     InvalidPasswordException |
                     UserNotFoundException |
                     AuthenticationException e) {

                System.out.println("ПОМИЛКА: " + e.getMessage());
            }
        }
    }
}