package com.library;

import com.library.builder.BookBuilder;
import com.library.builder.PatronBuilder;
import com.library.factory.BookFactory;
import com.library.model.Book;
import com.library.model.Branch;
import com.library.model.BorrowRecord;
import com.library.model.Patron;
import com.library.model.Reservation;
import com.library.notification.EmailNotificationListener;
import com.library.notification.InAppNotificationListener;
import com.library.notification.NotificationService;
import com.library.recommendation.AuthorBasedRecommendationStrategy;
import com.library.recommendation.GenreBasedRecommendationStrategy;
import com.library.recommendation.HybridRecommendationStrategy;
import com.library.repository.BookRepository;
import com.library.repository.BranchRepository;
import com.library.repository.InMemoryBookRepository;
import com.library.repository.InMemoryBranchRepository;
import com.library.repository.InMemoryPatronRepository;
import com.library.repository.PatronRepository;
import com.library.search.AuthorSearchStrategy;
import com.library.search.CompositeSearchStrategy;
import com.library.search.IsbnSearchStrategy;
import com.library.search.TitleSearchStrategy;
import com.library.service.BookService;
import com.library.service.BranchService;
import com.library.service.LendingService;
import com.library.service.PatronService;
import com.library.service.RecommendationService;
import com.library.service.ReservationService;
import com.library.util.LoggerUtil;

import java.util.List;
import java.util.logging.Logger;

/**
 * Main application runner demonstrating the complete functionality of the Library Management System.
 */
public class Main {

    private static final Logger LOGGER = LoggerUtil.getLogger(Main.class);

    public static void main(String[] args) {
        printBanner("LIBRARY MANAGEMENT SYSTEM DEMONSTRATION");

        // 1. Initialize Repositories & Notification Service
        BookRepository bookRepository = new InMemoryBookRepository();
        PatronRepository patronRepository = new InMemoryPatronRepository();
        BranchRepository branchRepository = new InMemoryBranchRepository();

        NotificationService notificationService = new NotificationService();
        notificationService.registerObserver(new EmailNotificationListener());
        notificationService.registerObserver(new InAppNotificationListener());

        // 2. Initialize Services
        BookService bookService = new BookService(bookRepository);
        PatronService patronService = new PatronService(patronRepository);
        BranchService branchService = new BranchService(branchRepository, bookRepository);
        ReservationService reservationService = new ReservationService(bookRepository, patronRepository, notificationService);
        LendingService lendingService = new LendingService(bookRepository, patronRepository, reservationService);
        RecommendationService recommendationService = new RecommendationService(bookRepository, patronRepository);

        // -------------------------------------------------------------
        // SCENARIO 1: Branch Management
        // -------------------------------------------------------------
        printSectionHeader("SCENARIO 1: Branch Management");
        Branch mainBranch = new Branch("B001", "Central Library", "123 University Ave");
        Branch northBranch = new Branch("B002", "Northside Branch", "456 College Blvd");
        branchService.addBranch(mainBranch);
        branchService.addBranch(northBranch);

        // -------------------------------------------------------------
        // SCENARIO 2: Book Creation & Inventory Management (Factory & Builder Patterns)
        // -------------------------------------------------------------
        printSectionHeader("SCENARIO 2: Book Creation & Inventory Management");
        Book book1 = BookFactory.createFictionBook("978-0134685991", "Effective Java", "Joshua Bloch", 2018, "B001");
        Book book2 = BookFactory.createStandardBook("978-0596009205", "Head First Design Patterns", "Eric Freeman", 2004, "Software", "B001");
        Book book3 = BookFactory.createStandardBook("978-0132350884", "Clean Code", "Robert C. Martin", 2008, "Software", "B001");
        Book book4 = new BookBuilder()
                .setIsbn("978-0201633610")
                .setTitle("Design Patterns: Elements of Reusable Object-Oriented Software")
                .setAuthor("Erich Gamma")
                .setPublicationYear(1994)
                .setGenre("Software")
                .setBranchId("B002")
                .build();
        Book book5 = BookFactory.createFictionBook("978-0061120084", "To Kill a Mockingbird", "Harper Lee", 1960, "B002");

        bookService.addBook(book1);
        bookService.addBook(book2);
        bookService.addBook(book3);
        bookService.addBook(book4);
        bookService.addBook(book5);

        System.out.println("\n--- Current Book Inventory ---");
        bookService.getAllBooks().forEach(b -> System.out.println("  * " + b));

        // -------------------------------------------------------------
        // SCENARIO 3: Book Search (Strategy Pattern)
        // -------------------------------------------------------------
        printSectionHeader("SCENARIO 3: Flexible Search Functionality (Strategy Pattern)");
        System.out.println("\n[Search 1] Finding books with title containing 'Design':");
        List<Book> designBooks = bookService.searchBooks(new TitleSearchStrategy("Design"));
        designBooks.forEach(b -> System.out.println("  -> " + b.getTitle() + " by " + b.getAuthor()));

        System.out.println("\n[Search 2] Finding books by author 'Joshua Bloch':");
        List<Book> blochBooks = bookService.searchBooks(new AuthorSearchStrategy("Joshua Bloch"));
        blochBooks.forEach(b -> System.out.println("  -> " + b.getTitle() + " (ISBN: " + b.getIsbn() + ")"));

        System.out.println("\n[Search 3] Composite Search (Author 'Robert' AND Title 'Clean'):");
        CompositeSearchStrategy compositeStrategy = new CompositeSearchStrategy(
                CompositeSearchStrategy.LogicOperator.AND,
                new AuthorSearchStrategy("Robert"),
                new TitleSearchStrategy("Clean")
        );
        bookService.searchBooks(compositeStrategy).forEach(b -> System.out.println("  -> " + b.getTitle()));

        // -------------------------------------------------------------
        // SCENARIO 4: Patron Registration (Builder Pattern)
        // -------------------------------------------------------------
        printSectionHeader("SCENARIO 4: Patron Registration");
        Patron patron1 = new PatronBuilder()
                .setPatronId("P101")
                .setName("Alice Smith")
                .setEmail("alice@example.com")
                .setPhone("555-0192")
                .build();

        Patron patron2 = new PatronBuilder()
                .setPatronId("P102")
                .setName("Bob Jones")
                .setEmail("bob@example.com")
                .setPhone("555-0193")
                .build();

        patronService.addPatron(patron1);
        patronService.addPatron(patron2);

        // -------------------------------------------------------------
        // SCENARIO 5: Lending Process (Checkout & Return)
        // -------------------------------------------------------------
        printSectionHeader("SCENARIO 5: Lending Process (Checkout & Return)");
        System.out.println("\n--> Alice borrows 'Clean Code'...");
        BorrowRecord record1 = lendingService.checkoutBook("978-0132350884", "P101");
        System.out.println("    Checkout record created: " + record1);

        System.out.println("\n--> Bob attempts to checkout 'Clean Code' (Should fail as it's already checked out):");
        try {
            lendingService.checkoutBook("978-0132350884", "P102");
        } catch (IllegalStateException e) {
            System.out.println("    [EXPECTED ERROR]: " + e.getMessage());
        }

        // -------------------------------------------------------------
        // SCENARIO 6: Reservation System & Observer Pattern
        // -------------------------------------------------------------
        printSectionHeader("SCENARIO 6: Reservation System & Observer Notifications");
        System.out.println("\n--> Bob reserves 'Clean Code' (currently checked out by Alice)...");
        Reservation reservation = reservationService.reserveBook("978-0132350884", "P102");
        System.out.println("    Reservation logged: " + reservation);

        System.out.println("\n--> Alice returns 'Clean Code'. Expecting Observer Notification to trigger for Bob:");
        lendingService.returnBook("978-0132350884", "P101");

        System.out.println("\n--> Bob now checks out his reserved copy of 'Clean Code':");
        BorrowRecord record2 = lendingService.checkoutBook("978-0132350884", "P102");
        System.out.println("    Checkout record created: " + record2);

        // -------------------------------------------------------------
        // SCENARIO 7: Multi-Branch Transfers
        // -------------------------------------------------------------
        printSectionHeader("SCENARIO 7: Multi-Branch Book Transfers");
        System.out.println("\n--> Transferring 'Effective Java' from Central Library (B001) to Northside Branch (B002)...");
        branchService.transferBook("978-0134685991", "B002");
        Book transferredBook = bookService.getBookByIsbn("978-0134685991").get();
        System.out.println("    Book status post-transfer: " + transferredBook);

        // -------------------------------------------------------------
        // SCENARIO 8: Recommendation System
        // -------------------------------------------------------------
        printSectionHeader("SCENARIO 8: Book Recommendation System");
        System.out.println("\n--> Alice borrows another Software book 'Head First Design Patterns'...");
        lendingService.checkoutBook("978-0596009205", "P101");

        System.out.println("\n--> Generating Recommendations for Alice (P101):");
        System.out.println("    1. [Genre-Based Recommendation]:");
        List<Book> genreRecs = recommendationService.getRecommendationsWithStrategy("P101",
                new GenreBasedRecommendationStrategy(bookService.getAllBooks()), 2);
        genreRecs.forEach(b -> System.out.println("       * Recommended: " + b.getTitle() + " [" + b.getGenre() + "]"));

        System.out.println("    2. [Hybrid Preference Recommendation]:");
        List<Book> hybridRecs = recommendationService.getRecommendations("P101", 3);
        hybridRecs.forEach(b -> System.out.println("       * Recommended: " + b.getTitle() + " by " + b.getAuthor() + " [" + b.getGenre() + "]"));

        // -------------------------------------------------------------
        // SUMMARY REPORT
        // -------------------------------------------------------------
        printSectionHeader("SYSTEM INVENTORY SUMMARY");
        System.out.println("Total Available Books: " + bookService.getAvailableBookCount());
        System.out.println("Total Borrowed Books:  " + bookService.getBorrowedBookCount());
        System.out.println("Total Registered Patrons: " + patronService.getAllPatrons().size());
        System.out.println("\nDemonstration completed successfully!");
    }

    private static void printBanner(String title) {
        System.out.println("================================================================================");
        System.out.println("  " + title);
        System.out.println("================================================================================");
    }

    private static void printSectionHeader(String title) {
        System.out.println("\n================================================================================");
        System.out.println("  " + title);
        System.out.println("================================================================================");
    }
}
