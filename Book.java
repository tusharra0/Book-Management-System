
public class Book {

    private String title;
    private String author;
    private String publicationDate;
    private String publisher;
    private String genre;
    private int isbnNumber;

    public Book() {
        this.title = "";
        this.author = "";
        this.publicationDate = "";
        this.publisher = "";
        this.genre = "";
        this.isbnNumber = 0;
    }

    public Book(String title, String author, String publisher, String publicationDate, String genre, int isbnNumber) {

        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.genre = genre;
        this.isbnNumber = isbnNumber;
    }

    public void setBookTitle(String title) {

        this.title = title;
    }

    public String getBookTitle() {

        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {

        return author;
    }

     public void setPublisher(String publisher) {

        this.publisher = publisher;
    }

    public String getPublisher() {

        return publisher;
    }

    public void setPublicationDate(String publicationDate) {

        this.publicationDate = publicationDate;
    }

    public String getPublicationDate() {

        return publicationDate;
    }

    public void setGenre(String genre) {

        this.genre = genre;
    }

    public String getGenre() {

        return genre;
    }

    public void setIsbnNumber(int isbnNumber) {

        this.isbnNumber = isbnNumber;
    }

    public int getIsbnNumber() {

        return isbnNumber;
    }

    public String toPrint() {
        return title + "\t" + author + "\t" + publisher + "\t" + publicationDate + "\t" + genre + "\t" + isbnNumber;
    }
    
    public String toString() {
        return "Title: " + title + "\tAuthor: " + author + "\tPublisher: " + publisher + "\tPublication Date: " + publicationDate + "\tGenre: "
            + genre + "\tISBN Number: " + isbnNumber;
    }


}
