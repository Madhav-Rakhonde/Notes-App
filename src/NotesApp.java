import java.io.*;
import java.util.*;

public class NotesApp {

    private static final String FILE_NAME = "notes.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Notes App ===");
            System.out.println("1. Add Note");
            System.out.println("2. View Notes");
            System.out.println("3. Delete Note by Line Number");
            System.out.println("4. Search Notes by Keyword");
            System.out.println("5. Edit Note by Line Number");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Please enter a valid number: ");
                scanner.next(); // consume invalid input
            }

            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter your note: ");
                    String note = scanner.nextLine();
                    addNote(note);
                    break;
                case 2:
                    viewNotes();
                    break;
                case 3:
                    viewNotes();
                    System.out.print("Enter line number to delete: ");
                    int delLine = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    deleteNoteByLine(delLine);
                    break;
                case 4:
                    System.out.print("Enter keyword to search: ");
                    String keyword = scanner.nextLine();
                    searchNotes(keyword);
                    break;
                case 5:
                    viewNotes();
                    System.out.print("Enter line number to edit: ");
                    int editLine = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new content for that note: ");
                    String newContent = scanner.nextLine();
                    editNoteByLine(editLine, newContent);
                    break;
                case 6:
                    System.out.println("Exiting Notes App. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please choose between 1 and 6.");
            }

        } while (choice != 6);

        scanner.close();
    }

    private static void addNote(String note) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            fw.write(note + System.lineSeparator());
            System.out.println("Note added successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void viewNotes() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No notes found.");
            return;
        }

        System.out.println("\n=== Your Notes ===");
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int lineNum = 1;
            boolean empty = true;
            while ((line = br.readLine()) != null) {
                empty = false;
                System.out.println(lineNum + ". " + line);
                lineNum++;
            }
            if (empty) {
                System.out.println("(No notes yet)");
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }

    private static void deleteNoteByLine(int lineToDelete) {
        List<String> notes = readAllNotes();
        if (lineToDelete < 1 || lineToDelete > notes.size()) {
            System.out.println("Invalid line number.");
            return;
        }
        notes.remove(lineToDelete - 1);
        writeAllNotes(notes);
        System.out.println("Note deleted successfully.");
    }

    private static void searchNotes(String keyword) {
        List<String> notes = readAllNotes();
        System.out.println("\n=== Search Results ===");
        boolean found = false;
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println((i + 1) + ". " + notes.get(i));
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching notes found.");
        }
    }

    private static void editNoteByLine(int lineToEdit, String newContent) {
        List<String> notes = readAllNotes();
        if (lineToEdit < 1 || lineToEdit > notes.size()) {
            System.out.println("Invalid line number.");
            return;
        }
        notes.set(lineToEdit - 1, newContent);
        writeAllNotes(notes);
        System.out.println("Note updated successfully.");
    }

    // Utility method to read all notes into a list
    private static List<String> readAllNotes() {
        List<String> notes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                notes.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return notes;
    }

    // Utility method to write all notes from a list to the file
    private static void writeAllNotes(List<String> notes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String note : notes) {
                bw.write(note);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}
