package src.presentation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import src.business.Book;
import src.business.Document;
import src.business.Library;
import src.business.Magazine;
import src.utils.Filter;

public class ConsoleUI {

    Library lib;
    Scanner in = new Scanner(System.in);
    int input = -1;

    public ConsoleUI() {
        this.lib = new Library();
        lib.seedLibrary();
    }

    // ------------ Menu loop --------------
    public void start() {
        int input;
        do {
            input = showMenu();
            menuHandler(input);
        } while (input != 6);
    }

    public int showMenu() {

        in.useDelimiter(System.lineSeparator());

        do {
            System.out.println("\n\t\t+----------------------------------------+");
            System.out.println("\t\t|            MENU PRINCIPALE             |");
            System.out.println("\t\t+----------------------------------------+");
            System.out.println("\t\t|                                        |");
            System.out.println("\t\t|     1- Add a document                  |");
            System.out.println("\t\t|     2- Borrow a document               |");
            System.out.println("\t\t|     3- Return a document               |");
            System.out.println("\t\t|     4- Show all documents              |");
            System.out.println("\t\t|     5- Find a document                 |");
            System.out.println("\t\t|     6- Exit                            |");
            System.out.println("\t\t|                                        |");
            System.out.println("\t\t+----------------------------------------+");
            System.out.print("Pick your choice : ");

            try {
                input = in.nextInt();
                if (input < 1 || input > 6) {
                    System.out.print("Please pick a choice between 1 and 6...");
                    in.next();
                }
            } catch (Exception e) {
                System.out.print("Please pick a valid number...");
                in.next();
                in.next();
            }

        } while (input < 1 || input > 6);
        return input;
    }

    public void menuHandler(int input) {
        switch (input) {
            case 1:
                addDocumentUI();
                break;
            case 2:
                borrowDocumentUI();
                break;
            case 3:
                returnDocumentUI();
                break;
            case 4:
                listDocumentsUI(Filter.ALL);
                System.out.print("Press Enter key to continue...");
                in.next();
                break;
            case 5:
                findDocumentUI();
                break;
            default:
                return;
        }
    }

    private void addDocumentUI() {
        Document doc;
        LocalDate publicationDate = null;
        in.useDelimiter(System.lineSeparator());

        do {
            System.out.println("What would you like to add ?\n 1- Book \t\t 2- Magazine");
            try {
                input = in.nextInt();
                if (input < 1 || input > 2) {
                    System.out.println("Please pick a choice between 1 and 2...");
                    in.next();
                }
            } catch (Exception e) {
            }

        } while (input < 1 || input > 2);

        System.out.print("Enter book title: ");
        String title = in.next();

        System.out.print("Enter author name: ");
        String author = in.next();

        System.out.print("Enter number of pages: ");
        int pageNumbers = in.nextInt();

        // ---------- Date validation ----------------
        while (publicationDate == null) {
            try {
                System.out.print("Enter publication date (dd-MM-yyyy): ");
                String dateString = in.next();

                // Parse and check if the date is valid (pattern)
                publicationDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (DateTimeParseException e) {
                System.out.print("Invalid date format. Please enter the date in the format dd-MM-yyyy...");
                in.next();
            }
        }

        if (input == 1) {
            System.out.print("Enter book number: ");
            int number = in.nextInt();
            doc = new Book(title, author, publicationDate, pageNumbers, number);
        } else {
            System.out.print("Enter ISBN number: ");
            int number = in.nextInt();
            doc = new Magazine(title, author, publicationDate, pageNumbers, number);
        }

        lib.addDocument(doc);

        System.out.print("Press Enter key to continue...");
        in.next();
    }

    private void borrowDocumentUI() {
        listDocumentsUI(Filter.AVAILABLE);

    }

    private void returnDocumentUI() {
        listDocumentsUI(Filter.BORROWED);
    }

    public void listDocumentsUI(Filter filter) {
        List<Document> filteredDocuments = new ArrayList<>();

        switch (filter) {
            case ALL:
                filteredDocuments.addAll(lib.getDocuments());
                break;
            case AVAILABLE:
                for (Document doc : lib.getDocuments()) {
                    if (!doc.getIsBorrowed()) { // Check if the document is not borrowed
                        filteredDocuments.add(doc);
                    }
                }
                break;
            case BORROWED:
                for (Document doc : lib.getDocuments()) {
                    if (doc.getIsBorrowed()) { // Check if the document is borrowed
                        filteredDocuments.add(doc);
                    }
                }
                break;
            default:
                break;

        }

        System.out.println("+--------------------------------------------------------------------------------------+");
        System.out.println("| Id |        Title        |         Author        |        Published        |  Pages  |");
        System.out.println("+--------------------------------------------------------------------------------------+");
        for (Document document : filteredDocuments) {
            System.out.printf("| %-3d| %-20s| %-22s| %-24s| %-8d|%n", document.getId(), document.getTitle(),
                    document.getAuthor(),
                    document.getPublicationDate(), document.getPageNumbers());
            System.out.println(
                    "+--------------------------------------------------------------------------------------+");
        }
    }

    private void findDocumentUI() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addDocumentUI'");
    }

    public int showDetailsUI() {

        do {
            System.out.println("\t\t+----------------------------------------+");
            System.out.println("\t\t|     1- Borrow the document             |");
            System.out.println("\t\t|     2- Return the document             |");
            System.out.println("\t\t|     3- Back                            |");
            System.out.println("\t\t+----------------------------------------+");
            System.out.print("\t\t Pick your choice : ");

            try {
                input = in.nextInt();
                if (input < 1 || input > 3) {
                    System.out.print("Please pick a choice between 1 and 3...");
                    in.nextLine();
                }
            } catch (Exception e) {
                System.out.print("Please pick a valid number...");
                in.nextLine();
            }

        } while (input < 1 || input > 3);

        in.close();

        return input;
    }

}
