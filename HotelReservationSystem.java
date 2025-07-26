import java.io.*;
import java.util.*;

class Room {
    private int roomNumber;
    private String category;
    private boolean isBooked;

    public Room(int roomNumber, String category) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.isBooked = false;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getCategory() {
        return category;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        this.isBooked = booked;
    }
}

class Booking {
    private String customerName;
    private int roomNumber;
    private String category;

    public Booking(String customerName, int roomNumber, String category) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.category = category;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Customer: " + customerName + ", Room: " + roomNumber + ", Category: " + category;
    }
}

class Hotel {
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Booking> bookings = new ArrayList<>();

    private final String FILE_NAME = "bookings.txt";

    public Hotel() {
        // Initialize some rooms
        for (int i = 1; i <= 5; i++) {
            rooms.add(new Room(i, "Standard"));
        }
        for (int i = 6; i <= 8; i++) {
            rooms.add(new Room(i, "Deluxe"));
        }
        for (int i = 9; i <= 10; i++) {
            rooms.add(new Room(i, "Suite"));
        }

        loadBookings();
    }

    public void showAvailableRooms() {
        System.out.println("\nAvailable Rooms:");
        for (Room r : rooms) {
            if (!r.isBooked()) {
                System.out.println("Room " + r.getRoomNumber() + " (" + r.getCategory() + ")");
            }
        }
    }

    public void makeBooking(String customerName, String category) {
        for (Room r : rooms) {
            if (r.getCategory().equalsIgnoreCase(category) && !r.isBooked()) {
                r.setBooked(true);
                Booking b = new Booking(customerName, r.getRoomNumber(), category);
                bookings.add(b);
                System.out.println("Booking successful! Room " + r.getRoomNumber() + " booked for " + customerName);
                simulatePayment();
                saveBookings();
                return;
            }
        }
        System.out.println("No available rooms in this category.");
    }

    public void cancelBooking(String customerName) {
        Iterator<Booking> it = bookings.iterator();
        boolean found = false;

        while (it.hasNext()) {
            Booking b = it.next();
            if (b.getCustomerName().equalsIgnoreCase(customerName)) {
                for (Room r : rooms) {
                    if (r.getRoomNumber() == b.getRoomNumber()) {
                        r.setBooked(false);
                        break;
                    }
                }
                it.remove();
                found = true;
                System.out.println("Booking cancelled for " + customerName);
                saveBookings();
                break;
            }
        }

        if (!found) {
            System.out.println("No booking found for " + customerName);
        }
    }

    public void viewBookings() {
        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings yet.");
        } else {
            System.out.println("\nCurrent Bookings:");
            for (Booking b : bookings) {
                System.out.println(b);
            }
        }
    }

    private void simulatePayment() {
        System.out.println("Processing payment...");
        System.out.println("Payment successful!");
    }

    private void saveBookings() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(bookings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadBookings() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            bookings = (ArrayList<Booking>) ois.readObject();
            // Mark booked rooms as booked
            for (Booking b : bookings) {
                for (Room r : rooms) {
                    if (r.getRoomNumber() == b.getRoomNumber()) {
                        r.setBooked(true);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

public class HotelReservationSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Hotel hotel = new Hotel();

        while (true) {
            System.out.println("\n=== Hotel Reservation System ===");
            System.out.println("1. View available rooms");
            System.out.println("2. Make a booking");
            System.out.println("3. Cancel a booking");
            System.out.println("4. View all bookings");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    hotel.showAvailableRooms();
                    break;
                case 2:
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter room category (Standard/Deluxe/Suite): ");
                    String category = sc.nextLine();
                    hotel.makeBooking(name, category);
                    break;
                case 3:
                    System.out.print("Enter your name to cancel booking: ");
                    String cancelName = sc.nextLine();
                    hotel.cancelBooking(cancelName);
                    break;
                case 4:
                    hotel.viewBookings();
                    break;
                case 5:
                    System.out.println("Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
