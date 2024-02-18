import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import java.io.*;

public class BookManagementT implements ActionListener {

    private JComboBox<String> ddBox;
    private JButton isbnSearchButton;
    private JButton isbnRemoveButton;
    private JButton isbnEditButton;
    private JButton saveEditedBookButton;
    private JButton addBookButton;
    private JButton returnToMenu;
    private JTextField isbnSearchText;

    private JTextField isbnText;
    private JTextField titleText;
    private JTextField authorText;
    private JTextField publisherText;
    private JTextField publicationDateText;
    private JTextField genreText;

    private ArrayList<Book> booksList;
    private ArrayList<String> bookTitleList;
    private ArrayList<Integer> isbnNumberList;

    private JFrame frame;
    private JPanel mainContent;
    private JPanel isbnSearchPanel;
    // private JPanel addBooksPanel;

    public BookManagementT() {
        booksList = new ArrayList<>();
        bookTitleList = new ArrayList<>();
        isbnNumberList = new ArrayList<>();
        frame = new JFrame("Book Management System");
        initializeGUI();
    }

    public static void main(String[] args) {
        BookManagementT bookManagement = new BookManagementT();
        bookManagement.outputGUI();
    }

    public void initializeGUI() {
        JLabel ddLabel = new JLabel("What would you like to do? ");
        String[] options = { "Select an option", "View All Books", "Search Book by ISBN", "Add a new Book",
                "Remove a Book",
                "Edit Book Details", "Export Books sorted by Title", "Export Books sorted by ISBN",
                "Save and exit" };
        ddBox = new JComboBox<>(options);
        ddBox.addActionListener(this);
        mainContent = new JPanel();
        mainContent.add(ddLabel);
        mainContent.add(ddBox);

        returnToMenu = new JButton("Return to Menu");
        returnToMenu.addActionListener(this);
    }

    public void outputGUI() {
        try {
            loadBooksFromFile();
        } catch (IOException e) {
            System.out.println("Error loading books from file: " + e.getMessage());
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(670, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(mainContent);
        frame.setVisible(true);
    }

    public void loadBooksFromFile() throws IOException {
        Scanner reader = new Scanner(new File("Library.txt"));

        while (reader.hasNext()) {
            String title = reader.next();
            String author = reader.next();
            String publisher = reader.next();
            String publicationDate = reader.next();
            String genre = reader.next();
            int isbnNumber = reader.nextInt();

            Book temp = new Book(title, author, publisher, publicationDate, genre, isbnNumber);
            booksList.add(temp);
            bookTitleList.add(title);
            isbnNumberList.add(isbnNumber);
        }

        reader.close();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ddBox) {
            String selectedOption = (String) ddBox.getSelectedItem();

            if (selectedOption.equals("View All Books"))
                displayAllBooks();
            else if (selectedOption.equals("Search Book by ISBN"))
                displayBookSearchPanel("search");
            else if (selectedOption.equals("Add a new Book"))
                displayAddBookPanel(false);
            else if (selectedOption.equals("Edit Book Details"))
                displayBookSearchPanel("edit");
            else if (selectedOption.equals("Remove a Book"))
                displayBookSearchPanel("remove");
            else if (selectedOption.equals("Export Books sorted by Title")) {
                try {
                    exportBooksByTitle();
                } catch (IOException f) {
                    System.out.println("Error loading books from file: " + f.getMessage());
                }
            } else if (selectedOption.equals("Export Books sorted by ISBN")) {
                try {
                    exportBooksByIsbnNumber();
                } catch (IOException f) {
                    System.out.println("Error loading books from file: " + f.getMessage());
                }
            } else if (selectedOption.equals("Save and exit")) {
                saveAndExit();
            }

        } else if (e.getSource() == isbnSearchButton) {
            searchBookByISBN();
        } else if (e.getSource() == isbnRemoveButton) {
            removeBook();
        } else if (e.getSource() == addBookButton) {
            addBook();
        } else if (e.getSource() == isbnEditButton) {
            displayAddBookPanel(true);
            loadBookDetails();
        } else if (e.getSource() == saveEditedBookButton) {
            saveEditedBookDetails();
        } else if (e.getSource() == returnToMenu) {
            displayMainPanel();
        }
    }

    public void saveAndExit() {
        JLabel message = new JLabel();

        try {
            FileWriter writer = new FileWriter("Library.txt");
            for (int i = 0; i < booksList.size(); i++) {
                Book book = booksList.get(i);
                writer.write(book.toPrint() + "\n");
            }
            // Close the FileWriter
            writer.close();
            message.setText("Data saved successfully!");
        } catch (IOException f) {
            System.out.println("Error saving books into file: " + f.getMessage());
            message.setText("Error saving the data!");
        }
        JOptionPane.showMessageDialog(null, message);
        System.exit(0);
    }

    public void addBook() {
        Book newBook = new Book(titleText.getText(), authorText.getText(), publisherText.getText(),
                publicationDateText.getText(), genreText.getText(), Integer.parseInt(isbnText.getText()));
        booksList.add(newBook);
        bookTitleList.add(titleText.getText());
        isbnNumberList.add(Integer.parseInt(isbnText.getText()));
        JLabel resultLabel = new JLabel();
        resultLabel.setText("Book saved successfully!");
        JOptionPane.showMessageDialog(null, resultLabel);
    }

    public void saveEditedBookDetails() {

        int bookIndex = getISBNIndex(Integer.parseInt(isbnSearchText.getText()));
        Book book = booksList.get(bookIndex);
        book.setBookTitle(titleText.getText());
        bookTitleList.set(bookIndex, titleText.getText());
        book.setAuthor(authorText.getText());
        book.setPublisher(publisherText.getText());
        book.setPublicationDate(publicationDateText.getText());
        book.setGenre(genreText.getText());
        // booksList.add(bookIndex, book);
        JLabel resultLabel = new JLabel();
        resultLabel.setText("Book saved successfully!");
        JOptionPane.showMessageDialog(null, resultLabel);
    }

    public void loadBookDetails() {
        int bookIndex = getISBNIndex(Integer.parseInt(isbnSearchText.getText()));
        Book book = booksList.get(bookIndex);
        isbnText.setText(book.getIsbnNumber() + "");
        authorText.setText(book.getAuthor());
        publisherText.setText(book.getPublisher());
        publicationDateText.setText(book.getPublicationDate());
        genreText.setText(book.getGenre());
        titleText.setText(book.getBookTitle());

    }

    public void displayAllBooks() {
        frame.getContentPane().removeAll();
        JPanel resultsPanel = new JPanel();

        for (int i = 0; i < booksList.size(); i++) {
            Book book = booksList.get(i);
            JLabel label = new JLabel(book.toString());
            resultsPanel.add(label);
        }
        resultsPanel.add(returnToMenu);
        frame.getContentPane().add(resultsPanel);
        frame.revalidate();
        frame.repaint();
    }

    public void displayMainPanel() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(mainContent);
        frame.revalidate();
        frame.repaint();
    }

    public void displayAddBookPanel(boolean isEdit) {
        JPanel addBooksPanel = new JPanel();
        JLabel isbnLabel = new JLabel("ISBN number");
        isbnText = new JTextField(20);
        if (isEdit == true)
            isbnText.setEditable(false);

        JLabel titleLabel = new JLabel("Title");
        titleText = new JTextField(20);
        JLabel authorLabel = new JLabel("Author");
        authorText = new JTextField(20);
        JLabel publisherLabel = new JLabel("Publisher");
        publisherText = new JTextField(20);
        JLabel publicationDateLabel = new JLabel("Publication Date");
        publicationDateText = new JTextField(20);
        JLabel genreLabel = new JLabel("Genre");
        genreText = new JTextField(20);
        addBooksPanel.add(isbnLabel);
        addBooksPanel.add(isbnText);
        addBooksPanel.add(titleLabel);
        addBooksPanel.add(titleText);
        addBooksPanel.add(authorLabel);
        addBooksPanel.add(authorText);
        addBooksPanel.add(publisherLabel);
        addBooksPanel.add(publisherText);
        addBooksPanel.add(publicationDateLabel);
        addBooksPanel.add(publicationDateText);
        addBooksPanel.add(genreLabel);
        addBooksPanel.add(genreText);

        if (isEdit == true) {
            saveEditedBookButton = new JButton("Save");
            saveEditedBookButton.addActionListener(this);
            addBooksPanel.add(saveEditedBookButton);
        } else {
            addBookButton = new JButton("Save");
            addBookButton.addActionListener(this);
            addBooksPanel.add(addBookButton);
        }

        addBooksPanel.add(returnToMenu);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(addBooksPanel);
        frame.revalidate();
        frame.repaint();
    }

    public void displayBookSearchPanel(String type) {
        JLabel txtLabel = new JLabel("Enter ISBN number and click Search ");
        isbnSearchText = new JTextField(20);

        isbnSearchPanel = new JPanel();
        isbnSearchPanel.add(txtLabel);
        isbnSearchPanel.add(isbnSearchText);

        if (type.equals("remove")) {
            isbnRemoveButton = new JButton("Remove");
            isbnRemoveButton.addActionListener(this);
            isbnSearchPanel.add(isbnRemoveButton);
        } else if (type.equals("search")) {
            isbnSearchButton = new JButton("Search");
            isbnSearchButton.addActionListener(this);
            isbnSearchPanel.add(isbnSearchButton);
        } else if (type.equals("edit")) {
            isbnEditButton = new JButton("Search");
            isbnEditButton.addActionListener(this);
            isbnSearchPanel.add(isbnEditButton);

        }

        isbnSearchPanel.add(returnToMenu);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(isbnSearchPanel);
        frame.revalidate();
        frame.repaint();
    }

    public int getISBNIndex(int isbnNumber) {
        int index = -1;
        for (int i = 0; i < isbnNumberList.size(); i++) {
            if (isbnNumberList.get(i) == isbnNumber) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void searchBookByISBN() {

        int isbnNumber = Integer.parseInt(isbnSearchText.getText());
        int index = getISBNIndex(isbnNumber);
        frame.getContentPane().removeAll();
        JLabel resultLabel = new JLabel();

        if (index >= 0) {
            Book book = booksList.get(index);
            resultLabel.setText(book.toString());

        } else {
            resultLabel.setText("Book not found");
        }
        frame.getContentPane().add(returnToMenu);
        frame.getContentPane().add(resultLabel);
        frame.revalidate();
        frame.repaint();
    }

    public void removeBook() {
        String isbnText = isbnSearchText.getText();
        int isbnNumber = Integer.parseInt(isbnText);
        JLabel resultLabel = new JLabel();

        int indexToRemove = getISBNIndex(isbnNumber);

        if (indexToRemove >= 0) {
            Book book = booksList.get(indexToRemove);
            bookTitleList.remove(indexToRemove);
            booksList.remove(indexToRemove);
            isbnNumberList.remove(indexToRemove);
            resultLabel.setText("Book removed successfully: " + book.toString());
        } else {
            resultLabel.setText("Book not found.");
        }

        JOptionPane.showMessageDialog(null, resultLabel);
    }

    public void exportBooksByTitle() throws IOException {
        sortBooksByTitle();

        // Create a FileWriter to write the sorted accounts to a file
        FileWriter writer = new FileWriter("BooksSortedByTitle.txt");

        // Iterate through the sorted list and write each account's information to the
        // file
        for (int i = 0; i < booksList.size(); i++) {
            Book book = booksList.get(i);
            writer.write(book.toString() + "\n");
        }

        // Close the FileWriter
        writer.close();

        JLabel message = new JLabel();
        message.setText("Sorted Books exported to BooksSortedByTitle.txt");
        JOptionPane.showMessageDialog(null, message);
        displayMainPanel();

    }

    public void sortBooksByTitle() {

        String temp = "";
        int temp2 = 0;
        int lowestValueIdx = 0;
        Book tempBook = booksList.get(0);

        for (int i = 0; i < bookTitleList.size() - 1; i++) {
            lowestValueIdx = getLowestValueIndexByTitle(bookTitleList, i + 1);

            if (bookTitleList.get(i).compareToIgnoreCase(bookTitleList.get(lowestValueIdx)) > 0) {
                temp = bookTitleList.get(i);
                temp2 = isbnNumberList.get(i);
                tempBook = booksList.get(i);

                bookTitleList.set(i, bookTitleList.get(lowestValueIdx));
                bookTitleList.set(lowestValueIdx, temp);

                isbnNumberList.set(i, isbnNumberList.get(lowestValueIdx));
                isbnNumberList.set(lowestValueIdx, temp2);

                booksList.set(i, booksList.get(lowestValueIdx));
                booksList.set(lowestValueIdx, tempBook);

            }
        }

    }

    public int getLowestValueIndexByTitle(ArrayList<String> bookTitleList, int startingIndex) {

        int lowestValueIdx = startingIndex;
        String lowestValue = bookTitleList.get(startingIndex);
        int arrayListLength = bookTitleList.size();

        for (int i = startingIndex; i < arrayListLength; i++) {
            if (lowestValue.compareToIgnoreCase(bookTitleList.get(i)) > 0) {
                lowestValue = bookTitleList.get(i);
                lowestValueIdx = i;
            }
        }

        return lowestValueIdx;
    }

    public void exportBooksByIsbnNumber() throws IOException {
        sortBooksByIsbnNumber();

        FileWriter writer = new FileWriter("BooksSortedByISBN.txt");

        for (int i = 0; i < booksList.size(); i++) {
            Book book = booksList.get(i);
            writer.write(book.toString() + "\n");
        }

        // Close the FileWriter
        writer.close();

        // Display a confirmation message
        JLabel message = new JLabel();
        message.setText("Sorted Books exported to BooksSortedByISBN.txt");
        JOptionPane.showMessageDialog(null, message);
        displayMainPanel();

    }

    public void sortBooksByIsbnNumber() {

        int temp = 0;
        String temp2 = "";

        int lowestValueIdx = 0;
        Book tempBook = booksList.get(0);

        for (int i = 0; i < isbnNumberList.size() - 1; i++) {

            lowestValueIdx = getLowestValueIndexByIsbnNumber(isbnNumberList, i + 1);

            if (isbnNumberList.get(i) > isbnNumberList.get(lowestValueIdx)) {
                temp = isbnNumberList.get(i);
                temp2 = bookTitleList.get(i);
                tempBook = booksList.get(i);

                isbnNumberList.set(i, isbnNumberList.get(lowestValueIdx));
                isbnNumberList.set(lowestValueIdx, temp);
                bookTitleList.set(i, bookTitleList.get(lowestValueIdx));
                bookTitleList.set(lowestValueIdx, temp2);
                booksList.set(i, booksList.get(lowestValueIdx));
                booksList.set(lowestValueIdx, tempBook);

            }
        }

    }

    public int getLowestValueIndexByIsbnNumber(ArrayList<Integer> isbnNumberList, int startingIndex) {

        int lowestValueIdx = startingIndex;
        int lowestValue = isbnNumberList.get(startingIndex);
        int arrayListLength = isbnNumberList.size();

        for (int i = startingIndex; i < arrayListLength; i++) {
            if (lowestValue > isbnNumberList.get(i)) {
                lowestValue = isbnNumberList.get(i);
                lowestValueIdx = i;
            }
        }

        return lowestValueIdx;
    }

}