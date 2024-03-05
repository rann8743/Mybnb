import java.io.Console;
import java.io.StringReader;
import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

public class MyBnB {

    private static Console console;
    private static Connection connection;
    private static Boolean loggedIn = false;
    private static String currentUsername;
    private static String firstName;
    private static float recommendedPrice;
    private static LinkedHashSet<String> recommendedAmennities;
    private static Integer DEFAULT_SEARCH_DISTANCE = 5; // ie: 5km
    private static final String DATE_FORMAT = "yyyy-mm-dd";
    private static final String INVALID_INPUT = "Invalid input. Please enter an valid input.";
    private static final String BOOLEAN_PROMPT = "[Please enter Y for Yes or N for No]: ";
    private static final String DATE_PROMPT = "[Please enter a date in this format: " + DATE_FORMAT + "]: ";
    private static final String WELCOME = "Welcome to MyBnB";
    private static final String LOG_IN_PROMPT = "Please log in or create an account.\n" +
                                                 "[Enter A to log in, B to create an account, or Z to quit the app]: ";
    private static final String LOGGED_OUT = "You have logged out of your account.";
    private static final String INPUT_PROMPT = "Please enter:\n" +
                                                "A - to search for listings\n" +
                                                "B - to book a listing\n" +
                                                "C - to cancel your booking\n" +
                                                "D - to become a host\n" +
                                                "E - to create a listing\n" +
                                                "F - to delete a listing\n" +
                                                "G - to add listing's availabilities\n" +
                                                "H - to delete listing's availabilities\n" +
                                                "I - to update a listing's price\n" +
                                                "J - to update a listing's amenities\n" +
                                                "K - to cancel a renter's bookings\n" +
                                                "L - to leave a rating/review for a renter\n" +
                                                "M - to leave a rating/review for a listing\n" +
                                                "N - to print bookings by cities/postal codes report\n" +
                                                "O - to print number of listings by country/cities/codes report\n" +
                                                "P - to print listings per hosts ranking report\n" +
                                                "Q - to print flagged hosts report\n" +
                                                "R - to print bookings per user ranking report\n" +
                                                "S - to print the host and renter with the largest cancellation within a year\n" +
                                                "T - to print listings' popular noun\n" +
                                                "W - to log out\n" +
                                                "X - to deactivate your host account\n" +
                                                "Y - to deactivate your user account\n" +
                                                "Z - to quit the app\n";
    private static final String ASK_USER_NAME = "Please enter your username: ";
    private static final String ASK_PASSWORD = "Please enter your password: ";
    private static final String DUPLICATED_USER_NAME = "already exists, please enter a new username.";
    private static final String ASK_FIRST_NAME = "Please enter your first name: ";
    private static final String ASK_LAST_NAME = "Please enter your last name: ";
    private static final String ASK_DOB = "Please enter your date of birth";
    private static final String ASK_HOST = "Do you want to become a host?";
    private static final String CREATE_USER_SUCCESSFUL = "Your account has been created. Your username is";
    private static final String LOG_IN_FAILED = "Incorrect username or password.";
    private static final String LOG_IN_SUCCESSFUL = "You have successfully logged into your account.";
    private static final String CREATE_HOST_SUCCESSFUL = "You have successfully become a host.";
    private static final String ALREADY_HOST = "You are already a host.";
    private static final String MUST_BE_HOST = "You must become a host first.";
    private  static final String HOUSE = "house";
    private static final String APARTMENT = "apartment";
    private  static final String GUESTHOUSE = "guesthouse";
    private  static final String HOTEL = "hotel";
    private static final String PROPERTY_TYPE_PROMPT = "Please enter the property type.\n" +
                                                        "[Enter A for " + HOUSE + ", B for " + APARTMENT + ", C for " +
                                                        GUESTHOUSE + ", or D for " + HOTEL + "]: ";
    private static final String COUNTRY_PROMPT = "Please enter the country: ";
    private static final String PROVINCE_PROMPT = "Pleae enter the province: ";
    private static final String CITY_PROMPT = "Please enter the city: ";
    private static final String ADDRESS_PROMPT = "Pleaes enter the address: ";
    private static final String SUITE_NUMBER_PROMPT = "Pleae enter the suite number if applicable: ";
    private static final String POSTAL_CODE_PROMPT = "Please enter the postal code: ";
    private static final String LONGITUDE_PROMPT = "Pleae enter the longitude.\n" +
                                                    "[Note that longitude ranges from -180 to 180]: ";
    private static final String LATTITUDE_PROMPT = "Please enter the latitude.\n" +
                                                    "[Note that latitude ranges from -90 to 90]: ";
    private static final String LISTING_CREATED = "You have successfully created a listing at";
    private static final String START_DATE_PROMPT = "Please enter the start date.";
    private static final String END_DATE_PROMPT = "Please enter the end date.";
    private static final String PRICE_PROMPT = "Please enter the per day price: ";
    private static final String CONTINUE_UPDATE_AVAILABILITIES = "Do you wish to continue updating availabilities for " +
                                                                  "this listing?";
    private static final String AVAILABILITY_SUCCESSFUL = "Availability successfully updated.";
    private static final String END_DATE_ERROR = "End date must be after start date. Please re-enter.";
    private static final String LISTING_NOT_FOUND = "Cannot find listing.";
    private static final String LISTING_NOT_OWNED = "You do not have a listing with id ";
    private static final String ASK_DELETE_LISTING = "Deleting your listing will also cancel all unfulfilled bookings" +
                                                        " for your listing. Do you wish to continue?";
    private static final String LISTING_DELETED = "Listing has been deleted.";
    private static final String ASK_DEACTIVATE_HOST = "Deactivate your host account will also remove all of your " +
                                                       "listings. All unfulfilled bookings for your listings would be" +
                                                       " cancelled. Do you wish to continue?";
    private static final String DEACTIVATED_HOST = "Your host account has been deactivated.";
    private static final String ASK_DEACTIVATE_USER = "Are you sure you want to deactivate your account? " +
                                                       "All unfulfilled bookings would be cancelled.";
    private static final String DEACTIVATED_USER = "Your account has been deactivated.";
    private static final String SEARCH_LISTING_PROMPT = "[Please enter A to search by address or B to search around " +
                                                         "coordinates or C to search around a postal code]: ";
    private static final String LISTING_ID_PROMPT = "Do you have a listing id?\n" +
                                                     "[Please enter A if you do or B to search for listings]: ";
    private static final String GET_LISTING_ID = "Please enter the listing id: ";
    private static final String LISTING_NOT_AVAILABLE = "Listing is not available during the given time range.";
    private static final String CANNOT_RENT_OWN = "You cannot rent your own listing.";
    private static final String BOOKING_SUCCESSFUL = "You have successfully completed the booking.";
    private static final String BOOKING_NOT_FOUND = "Cannot find booking. The booking might have already been " +
                                                     "cancelled or fulfilled.";
    private static final String BOOKING_CANCELLED = "Booking has been cancelled.";
    private static final String DATES_BOOKED = "Some dates within the provided date range has already been booked. " +
                                                "You need to cancel the bookings first before you can modify their " +
                                                "availabilities.";
    private static final String AVAILABILITIES_EXISTS = "Some dates within the provided date range has already been " +
                                                         "registered with a different price. Do you wish to update " +
                                                         "their price?";
    private static final String ASK_AMENITY_NAME = "Please select the amenity name.\n"+
                                                    "A - wifi[essentials]\n" +
                                                    "B - kitchen[essentials]\n" +
                                                    "C - washer[essentials]\n" +
                                                    "D - dryer[essentials]\n" +
                                                    "E - refrigerator[essentials]\n" +
                                                    "F - microwave[essentials]\n" +
                                                    "G - coffee maker[essentials]\n" +
                                                    "H - air conditioning[essentials]\n" +
                                                    "I - stove[essentials]\n" +
                                                    "J - heating[essentials]\n" +
                                                    "K - tv[essentials]\n" +
                                                    "L - pool[essentials]\n" +
                                                    "M - hot tub[essentials]\n" +
                                                    "N - bbq[essentials]\n" +
                                                    "O - parking[essentials]\n" +
                                                    "P - gym[essentials]\n" +
                                                    "Q - waterfront[location]\n" +
                                                    "R - beachfront[location]\n" +
                                                    "S - lake view[location]\n" +
                                                    "T - smoke alarm[safety]\n" +
                                                    "U - carbon monoxide alarm[safety]\n" +
                                                    "V - security cameras on property[safety]\n" +
                                                    "W - pets allowed[service]\n" +
                                                    "X - self check-in[service]\n";
    private static final String CONTINUE_UPDATE_AMENITY = "Do you wish to add more amenities?";
    private static final String DUPLICATE_AMENITY = "This amenity already exists for this listing, please select a new one.";
    private static final String ENTER_PAYMENT_INFO = "Please provide your payment information if you want to continue booking. ";
    private static final String CREDIT_CARD_NUM = "Please enter your credit card number: ";
    private static final String CREDIT_CARD_DATE = "Please enter your credit card expiration date: ";
    private static final String CREDIT_CARD_CODE = "Please enter your credit card code: ";
    private static final String UPDATE_PAYMENT = "Update payment successfully! ";
    private static final String RATE_RENTER = "Please leave your rating to the renter. [please provide an integer from 1 to 5]: ";
    private static final String RATE_HOST = "Please leave your rating to the host. [please provide an integer from 1 to 5]: ";
    private static final String REVIEW_RENTER = "Please leave your review to the renter: ";
    private static final String REVIEW_HOST = "Please leave your review to the host: ";
    private static final String INVALID_INFORMATION = "Can not find a recent stay. It may have been cancelled. ";
    private static final String REVIEW_UPDATED = "Your review has been updated successfully! ";
    private static final String ALREADY_RATED = "You have already rated. ";
    private static final String ASK_SIN = "Please enter your sin number: ";
    private static final String ASK_OCCUPATION = "Please enter your occupation: ";
    private static final String DISTANCE_RANGE = "Please enter your desired distance range in km, " +
                                                  "or leave blank for default (5 km): ";
    private static final String FILTER_DATE = "Would you like to filter by date range?";
    private static final String FILTER_PRICE = "Would you like to filter by price range?";
    private static final String FILTER_AMENITIES = "Would you like to filter by amenities?";
    private static final String MIN_PRICE_PROMPT = "Please enter the min price: ";
    private static final String MAX_PRICE_PROMPT = "Please enter the max price: ";
    private static final String RANK_PRICE_ASC = "Do you wish to rank by price ascending? Default is by distance.";
    private static final String RANK_PRICE_DESC = "Do you wish to rank by price descending? Default is by distance.";

    /* Common Queries. */
    private static final String FIND_USER_QUERY = "SELECT * FROM user WHERE user_id = ?";
    private static final String FIND_LISTING_BY_ID_QUERY = "SELECT * FROM listing WHERE removed = false AND " +
                                                            "listing_id = ?";
    private static final String INSERT_AVAILABILITY = "INSERT INTO availability(listing_id,price,date)" +
                                                       " VALUES(?,?,?)";
    private static final String INSERT_OR_UPDATE_AVAILABILITY = "INSERT INTO availability(listing_id, price, date) " +
                                                                 "VALUES(?,?,?) ON DUPLICATE KEY UPDATE price = ?";
    private static final String INSERT_PAYMENT = "UPDATE user SET credit_card_number = ?, credit_card_expiration_date = ?, credit_card_code = ? WHERE user_id = ?";

    /* Main. */
    public static void main(String[] args) {
        try {
            /* The text-based interface for the app. */
            console = System.console();

            /* Initialize database for app. */
            connection = MyJDBC.initializeDB();

            /* Start application. */
            console.printf("\n%s\n", WELCOME + "!");
            String userInput;
            while (true) {
                console.printf("\n%s\n\n", "++++++++++++++++");
                if (!loggedIn) {
                    /* Log in first. */
                    userInput = console.readLine(LOG_IN_PROMPT).toLowerCase().strip();
                    switch (userInput) {
                        case "a":
                            // log in
                            login();
                            break;
                        case "b":
                            // sign up
                            createUser();
                            break;
                        case "z":
                            // quit app
                            connection.close();
                            return;
                        default:
                            // invalid input
                            console.printf("\n%s\n", INVALID_INPUT);
                            break;
                    }
                    if (loggedIn)
                        console.printf("\n%s\n", WELCOME + " " + firstName + "!");
                } else {
                    userInput = console.readLine(INPUT_PROMPT).toLowerCase().strip();
                    switch (userInput) {
                        case "a":
                            // search for listings
                            searchListings();
                            break;
                        case "b":
                            // book a listing
                            bookListing();
                            break;
                        case "c":
                            // cancel your booking
                            cancelBooking();
                            break;
                        case "d":
                            // become a host
                            becomeHost();
                            break;
                        case "e":
                            /* call host tool kit to update the recommended values */
                            hostToolkit();
                            // create a listing
                            createListing();
                            break;
                        case "f":
                            //delete a listing
                            if (getBoolean(ASK_DELETE_LISTING)) {
                                if(deleteListing(getListingId()))
                                    console.printf("\n%s\n", LISTING_DELETED);
                            }
                            break;
                        case "g":
                            /* call host tool kit to update the recommended values */
                            hostToolkit();
                            // add listing's availability
                            updateAvailabilities(getListingId());
                            break;
                        case "h":
                            // delete listing's availabilities
                            deleteAvailabilities();
                            break;
                        case "i":
                            /* call host tool kit to update the recommended values */
                            hostToolkit();
                            // update listing's price
                            updateAvailabilities(getListingId());
                            break;
                        case "j":
                            /* call host tool kit to update the recommended values */
                            hostToolkit();
                            // update listing's amenities
                            updateAmenities(getListingId());
                            break;
                        case "k":
                            // cancel renter's booking
                            cancelRenterBooking();
                            break;
                        case "l":
                            // ratings and comments for renter
                            rateRenter();
                            break;
                        case "m":
                            // ratings and comments for listing
                            rateListing();
                            break;
                        case "n":
                            // print bookings per city report
                            bookingsPerCitiesReport();
                            break;
                        case "o":
                            // print number of listing per country report
                            listingsNumberPerCitiesReport();
                            break;
                        case "p":
                            // print listings per hosts report
                            listingsPerHostsReport();
                            break;
                        case "q":
                            // flag hosts
                            flagHost();
                            break;
                        case "r":
                            // print rank user's bookings report
                            rankUserBookingsReport();
                            break;
                        case "s":
                            // largest cancellations
                            largestCancellation();
                            break;
                        case "t":
                            // popular noun phrases
                            popularNoun();
                            break;
                        case "w":
                            // log out
                            loggedIn = false;
                            currentUsername = null;
                            firstName = null;
                            console.printf("%s\n", LOGGED_OUT);
                            break;
                        case "x":
                            // deactivate host account
                            if (getBoolean(ASK_DEACTIVATE_HOST))
                                deactivateHost();
                            break;
                        case "y":
                            // deactivate user account
                            if (getBoolean(ASK_DEACTIVATE_USER))
                                deactivateUser();
                            break;
                        case "z":
                            // quit app
                            connection.close();
                            return;
                        default:
                            // invalid input
                            console.printf("\n%s\n", INVALID_INPUT);
                            break;
                    }
                }
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /* Log in. */
    private static void login () throws SQLException {
        String username = console.readLine(ASK_USER_NAME).toLowerCase().strip();
        String password = console.readLine(ASK_PASSWORD).toLowerCase().strip();
        String query = "SELECT * FROM user WHERE user_id = ? AND password = ?";
        PreparedStatement findUserPS = connection.prepareStatement(query);
        findUserPS.setString(1, username);
        findUserPS.setString(2, password);
        ResultSet result = findUserPS.executeQuery();
        if (!result.next()) {
            console.printf("%s\n", LOG_IN_FAILED);
            return;
        } else {
            loggedIn = true;
            currentUsername = username;
            firstName = result.getString("first_name");
            console.printf("%s\n", LOG_IN_SUCCESSFUL);
        }
    }

    /* Create a new user. */
    private static void createUser() throws SQLException {
        String username;
        PreparedStatement findUserPS = connection.prepareStatement(FIND_USER_QUERY);
        while (true) {
            username = console.readLine(ASK_USER_NAME).toLowerCase().strip();
            findUserPS.setString(1, username);
            ResultSet result = findUserPS.executeQuery();
            if (result.next())
                console.printf("%s %s\n", username, DUPLICATED_USER_NAME);
            else
                break;
        }
        String password = console.readLine(ASK_PASSWORD).toLowerCase().strip();
        String firstname = console.readLine(ASK_FIRST_NAME).toLowerCase().strip();
        String lastname = console.readLine(ASK_LAST_NAME).toLowerCase().strip();
        Date dob = getDate(ASK_DOB);
        Boolean becomeHost = getBoolean(ASK_HOST);
        String creditcardNum = console.readLine(CREDIT_CARD_NUM).toLowerCase().strip();
        if (creditcardNum.isBlank())
            creditcardNum = null;
        String creditcardDate = console.readLine(CREDIT_CARD_DATE).toLowerCase().strip();
        if(creditcardDate.isBlank())
            creditcardDate = null;
        String creditcardCode = console.readLine(CREDIT_CARD_CODE).toLowerCase().strip();
        if(creditcardCode.isBlank())
            creditcardCode = null;
        String sin = console.readLine(ASK_SIN).toLowerCase().strip();
        if(sin.isBlank())
            sin = null;
        String occ = console.readLine(ASK_OCCUPATION).toLowerCase().strip();
        if(occ.isBlank())
            occ = null;
        String country = console.readLine(COUNTRY_PROMPT).toLowerCase();
        if(country.isBlank())
            country = null;
        String province = console.readLine(PROVINCE_PROMPT).toLowerCase();
        if(province.isBlank())
            province = null;
        String city = console.readLine(CITY_PROMPT).toLowerCase();
        if(city.isBlank())
            city = null;
        String address = console.readLine(ADDRESS_PROMPT).toLowerCase();
        if(address.isBlank())
            address = null;
        String suiteNumber = console.readLine(SUITE_NUMBER_PROMPT).toLowerCase();
        if (suiteNumber.isBlank())
            suiteNumber = null;
        String postalCode = console.readLine(POSTAL_CODE_PROMPT).toLowerCase();
        if (postalCode.isBlank())
            postalCode = null;

        String query = "INSERT INTO user(user_id,password,first_name,last_name,birthday,is_host,credit_card_number," +
                        "credit_card_expiration_date,credit_card_code,sin,occupation,country,province,city,address," +
                        "suite_number,postal_code) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement createUserPS = connection.prepareStatement(query);
        createUserPS.setString(1, username);
        createUserPS.setString(2, password);
        createUserPS.setString(3, firstname);
        createUserPS.setString(4, lastname);
        createUserPS.setDate(5, dob);
        createUserPS.setBoolean(6, becomeHost);
        createUserPS.setString(7, creditcardNum);
        createUserPS.setString(8, creditcardDate);
        createUserPS.setString(9, creditcardCode);
        createUserPS.setString(10, sin);
        createUserPS.setString(11, occ);
        createUserPS.setString(12, country);
        createUserPS.setString(13, province);
        createUserPS.setString(14, city);
        createUserPS.setString(15, address);
        createUserPS.setString(16, suiteNumber);
        createUserPS.setString(17, postalCode);
        createUserPS.executeUpdate();
        console.printf("\n%s %s.\n", CREATE_USER_SUCCESSFUL, username);
        loggedIn = true;
        currentUsername = username;
        firstName = firstname;
    }

    /* Become host. */
    private static void becomeHost() throws SQLException {
        /* Check if user is already a host. */
        PreparedStatement findUserPS = connection.prepareStatement(FIND_USER_QUERY);
        findUserPS.setString(1, currentUsername);
        ResultSet result = findUserPS.executeQuery();
        result.next();
        if (result.getBoolean("is_host")) {
            console.printf("\n%s\n", ALREADY_HOST);
            return;
        }
        String query = "UPDATE user SET is_host = true WHERE user_id = ?";
        PreparedStatement setIsHost = connection.prepareStatement(query);
        setIsHost.setString(1, currentUsername);
        setIsHost.executeUpdate();
        console.printf("\n%s\n", CREATE_HOST_SUCCESSFUL);
    }

    /* Search for listings. */
    private static void searchListings() throws SQLException {
        String userInput;
        while (true) {
            userInput = console.readLine(SEARCH_LISTING_PROMPT).strip();
            if (userInput.equalsIgnoreCase("a") ||
                    userInput.equalsIgnoreCase("b") ||
                    userInput.equalsIgnoreCase("c"))
                break;
            console.printf("\n%s\n\n", INVALID_INPUT);
        }

        PreparedStatement statement;
        String query;
        ResultSet result = null;

        /* Search a specific listing by address */
        if (userInput.equalsIgnoreCase("a")) {
            String country = console.readLine(COUNTRY_PROMPT).toLowerCase().strip();
            String province = console.readLine(PROVINCE_PROMPT).toLowerCase().strip();
            String city = console.readLine(CITY_PROMPT).toLowerCase().strip();
            String address = console.readLine(ADDRESS_PROMPT).toLowerCase().strip();
            String suiteNumber = console.readLine(SUITE_NUMBER_PROMPT).toLowerCase().strip();
            if (suiteNumber.isBlank())
                suiteNumber = null;
            query = "SELECT * FROM listing WHERE removed = false AND country = ? AND province = ? AND city = ? AND " +
                    "address = ? AND suite_number ";
            if (suiteNumber == null)
                query = query + "IS NULL";
            else
                query = query + "= ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, country);
            statement.setString(2, province);
            statement.setString(3, city);
            statement.setString(4, address);
            if (suiteNumber != null)
                statement.setString(5, suiteNumber);
            result = statement.executeQuery();
            if (!result.next()) {
                console.printf("\n%s\n", LISTING_NOT_FOUND);
                return;
            }
            int listingId = result.getInt("listing_id");
            String postalCode = result.getString("postal_code");
            console.printf("\n======================\n");
            if (suiteNumber != null)
                console.printf("Found listing at: %s-%s, %s, %s, %s, %s with id: %d", suiteNumber, address, city,
                        province, country, postalCode, listingId);
            else
                console.printf("Found listing at: %s, %s, %s, %s, %s with id: %d", address, city, province, country,
                        postalCode, listingId);
            console.printf("\n======================\n\n");
            return;
        }

        /* Get coordinates and distance */
        double longitude = 200;
        double latitude = 200;
        float distanceRange = -1;
        if (userInput.equalsIgnoreCase("b")) {
            longitude = getLongitude();
            latitude = getLatitude();
            distanceRange = DEFAULT_SEARCH_DISTANCE * 10; // for the purpose of calculation, km * 10
            String i;
            while (true) {
                i = console.readLine(DISTANCE_RANGE).strip();
                if (i.isBlank())
                    break;
                try {
                    distanceRange = Float.valueOf(i) * 10; // for the purpose of calculation, km * 10
                    break;
                } catch (Exception e) {
                    console.printf("%s\n", INVALID_INPUT);
                }
            }
        }

        /* Get postal code */
        String postalCode = "-------";
        if (userInput.equalsIgnoreCase("c"))
            postalCode = console.readLine(POSTAL_CODE_PROMPT).toLowerCase().strip();

        /* Get filters */
        Date startDate = null;
        Date endDate = null;
        float minPrice = -1;
        float maxPrice = -1;
        Boolean filterByPrice = false;
        Boolean rankByPriceAsc = false;
        Boolean rankByPriceDesc = false;
        Boolean filterByDate = getBoolean(FILTER_DATE);
        if (filterByDate) {
            startDate = getDate(START_DATE_PROMPT);
            endDate = getDate(END_DATE_PROMPT);
            filterByPrice = getBoolean(FILTER_PRICE);
            if (filterByPrice) {
                while(true) {
                    try {
                        if (minPrice == -1)
                            minPrice = Float.valueOf(console.readLine(MIN_PRICE_PROMPT).strip());
                        maxPrice = Float.valueOf(console.readLine(MAX_PRICE_PROMPT).strip());
                        break;
                    } catch (Exception e) {
                        console.printf("%s\n", INVALID_INPUT);
                    }
                }
            }
            rankByPriceAsc = getBoolean(RANK_PRICE_ASC);
            if (!rankByPriceAsc)
                rankByPriceDesc = getBoolean(RANK_PRICE_DESC);
        }
        LinkedHashSet<String> amennities = null;
        Boolean filterByAmenities = getBoolean(FILTER_AMENITIES);
        if (filterByAmenities) {
            amennities = new LinkedHashSet<String>();
            do {
                amennities.add(getAmenity());
            } while (getBoolean(CONTINUE_UPDATE_AMENITY));
        }

        /* Search around coordinates. */
        if (userInput.equalsIgnoreCase("b")) {
            if (!filterByAmenities && !filterByDate && !filterByPrice) {
                query = "SELECT * FROM (" +
                        "SELECT listing_id,country,province,city,address,suite_number,postal_code," +
                        "ABS((longitude + latitude)-(? + ?)) AS distance FROM listing) AS A " +
                        "WHERE distance <= ? ORDER BY distance asc;";
                statement = connection.prepareStatement(query);
                statement.setDouble(1, longitude);
                statement.setDouble(2, latitude);
                statement.setFloat(3, distanceRange);
                result = statement.executeQuery();
            } else if (filterByDate && !filterByAmenities && !filterByPrice) {
                query = "SELECT * FROM (" +
                        "SELECT * FROM (" +
                        "SELECT listing_id,country,province,city,address,suite_number,postal_code," +
                        "ABS((longitude + latitude)-(? + ?)) AS distance FROM listing) AS A1 " +
                        "WHERE distance <= ?) AS A " +
                        "JOIN (" +
                        "SELECT listing_id, total_price FROM (" +
                        "SELECT listing_id, COUNT(*) as total_days, SUM(price) as total_price FROM availability " +
                        "WHERE date >= ? AND date < ? AND booked = false GROUP BY listing_id) as B1 " +
                        "WHERE total_days = ?) AS B " +
                        "ON A.listing_id=B.listing_id";
                if (rankByPriceAsc)
                    query = query + " ORDER BY total_price asc";
                else if (rankByPriceDesc)
                    query = query + " ORDER BY total_price desc";
                else
                    query = query + " ORDER BY distance asc";
                statement = connection.prepareStatement(query);
                statement.setDouble(1, longitude);
                statement.setDouble(2, latitude);
                statement.setFloat(3, distanceRange);
                statement.setDate(4, startDate);
                statement.setDate(5, endDate);
                statement.setLong(6, DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()));
                result = statement.executeQuery();
            } else if (filterByDate && filterByPrice && !filterByAmenities) {
                query = "SELECT * FROM (" +
                        "SELECT * FROM (" +
                        "SELECT listing_id,country,province,city,address,suite_number,postal_code," +
                        "ABS((longitude + latitude)-(? + ?)) AS distance FROM listing) AS A1 " +
                        "WHERE distance <= ?) AS A " +
                        "JOIN (" +
                        "SELECT listing_id, total_price FROM (" +
                        "SELECT listing_id, COUNT(*) as total_days, SUM(price) as total_price FROM availability " +
                        "WHERE date >= ? AND date < ? AND booked = false GROUP BY listing_id) as B1 " +
                        "WHERE total_days = ? AND total_price >= ? AND total_price <= ?) AS B " +
                        "ON A.listing_id=B.listing_id";
                if (rankByPriceAsc)
                    query = query + " ORDER BY total_price asc";
                else if (rankByPriceDesc)
                    query = query + " ORDER BY total_price desc";
                else
                    query = query + " ORDER BY distance asc";
                statement = connection.prepareStatement(query);
                statement.setDouble(1, longitude);
                statement.setDouble(2, latitude);
                statement.setFloat(3, distanceRange);
                statement.setDate(4, startDate);
                statement.setDate(5, endDate);
                statement.setLong(6, DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()));
                statement.setFloat(7, minPrice);
                statement.setFloat(8, maxPrice);
                result = statement.executeQuery();
            } else if (filterByAmenities && !filterByDate && !filterByPrice) {
                StringBuilder queryBuilder = new StringBuilder(
                                "SELECT * FROM (" +
                                "SELECT * FROM (" +
                                "SELECT listing_id,country,province,city,address,suite_number,postal_code," +
                                "ABS((longitude + latitude)-(? + ?)) AS distance FROM listing) AS A1 " +
                                "WHERE distance <= ?) AS A " +
                                "JOIN (" +
                                "SELECT * FROM (" +
                                "SELECT listing_id, COUNT(*) as total_amenity FROM listing_amenity " +
                                "WHERE listing_id IN (SELECT listing_id FROM listing) AND amenity IN (");
                int i;
                for(i = 1; i < amennities.size(); i++){
                    queryBuilder.append("?,");
                }
                queryBuilder.append(
                                "?) GROUP BY listing_id) as B1 " +
                                "WHERE total_amenity = ?) AS B " +
                                "ON A.listing_id=B.listing_id ORDER BY distance asc");
                statement = connection.prepareStatement(queryBuilder.toString());
                statement.setDouble(1, longitude);
                statement.setDouble(2, latitude);
                statement.setFloat(3, distanceRange);
                Iterator<String> aIter = amennities.iterator();
                int k;
                for(k = 1; k <= amennities.size(); k++)
                    statement.setString(k + 3, aIter.next());
                statement.setInt(k + 3, amennities.size());
                result = statement.executeQuery();
            } else if (filterByAmenities && filterByDate && !filterByPrice) {
                StringBuilder queryBuilder = new StringBuilder(
                                "SELECT * FROM (" +
                                "SELECT listing_id, total_price FROM (" +
                                "SELECT listing_id, COUNT(*) as total_days, SUM(price) as total_price FROM availability " +
                                "WHERE date >= ? AND date < ? AND booked = false GROUP BY listing_id) AS A1 " +
                                "WHERE total_days = ?) AS A " +
                                "JOIN (" +
                                "SELECT * FROM (" +
                                "SELECT listing_id,country,province,city,address,suite_number,postal_code," +
                                "ABS((longitude + latitude)-(? + ?)) AS distance FROM listing) AS B1 " +
                                "WHERE distance <= ?) AS B " +
                                "ON A.listing_id=B.listing_id " +
                                "JOIN (" +
                                "SELECT * FROM (" +
                                "SELECT listing_id, COUNT(*) as total_amenity FROM listing_amenity " +
                                "WHERE listing_id IN (SELECT listing_id FROM listing) AND amenity IN (");
                int i;
                for(i = 1; i < amennities.size(); i++)
                    queryBuilder.append("?,");
                queryBuilder.append(
                        "?) GROUP BY listing_id) as C1 " +
                                "WHERE total_amenity = ?) AS C " +
                                "ON A.listing_id=C.listing_id");
                if (rankByPriceAsc)
                    queryBuilder.append(" ORDER BY total_price asc");
                else if (rankByPriceDesc)
                    queryBuilder.append(" ORDER BY total_price desc");
                else
                    queryBuilder.append(" ORDER BY distance asc");
                statement = connection.prepareStatement(queryBuilder.toString());
                statement.setDate(1, startDate);
                statement.setDate(2, endDate);
                statement.setLong(3, DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()));
                statement.setDouble(4, longitude);
                statement.setDouble(5, latitude);
                statement.setFloat(6, distanceRange);
                Iterator<String> aIter = amennities.iterator();
                int k;
                for(k = 1; k <= amennities.size(); k++)
                    statement.setString(k + 6, aIter.next());
                statement.setInt(k + 6, amennities.size());
                result = statement.executeQuery();
            } else if (filterByAmenities && filterByDate && filterByPrice) {
                StringBuilder queryBuilder = new StringBuilder(
                                "SELECT * FROM (" +
                                "SELECT listing_id, total_price FROM (" +
                                "SELECT listing_id, COUNT(*) as total_days, SUM(price) as total_price FROM availability " +
                                "WHERE date >= ? AND date < ? AND booked = false GROUP BY listing_id) AS A1 " +
                                "WHERE total_days = ? AND total_price >= ? AND total_price <= ?) AS A " +
                                "JOIN (" +
                                "SELECT * FROM (" +
                                "SELECT listing_id,country,province,city,address,suite_number,postal_code," +
                                "ABS((longitude + latitude)-(? + ?)) AS distance FROM listing) AS B1 " +
                                "WHERE distance <= ?) AS B " +
                                "ON A.listing_id=B.listing_id " +
                                "JOIN (" +
                                "SELECT * FROM (" +
                                "SELECT listing_id, COUNT(*) as total_amenity FROM listing_amenity " +
                                "WHERE listing_id IN (SELECT listing_id FROM listing) AND amenity IN (");
                int i;
                for(i = 1; i < amennities.size(); i++)
                    queryBuilder.append("?,");
                queryBuilder.append(
                                "?) GROUP BY listing_id) as C1 " +
                                "WHERE total_amenity = ?) AS C " +
                                "ON A.listing_id=C.listing_id");
                if (rankByPriceAsc)
                    queryBuilder.append(" ORDER BY total_price asc");
                else if (rankByPriceDesc)
                    queryBuilder.append(" ORDER BY total_price desc");
                else
                    queryBuilder.append(" ORDER BY distance asc");
                statement = connection.prepareStatement(queryBuilder.toString());
                statement.setDate(1, startDate);
                statement.setDate(2, endDate);
                statement.setLong(3, DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()));
                statement.setFloat(4, minPrice);
                statement.setFloat(5, maxPrice);
                statement.setDouble(6, longitude);
                statement.setDouble(7, latitude);
                statement.setFloat(8, distanceRange);
                Iterator<String> aIter = amennities.iterator();
                int k;
                for(k = 1; k <= amennities.size(); k++)
                    statement.setString(k + 8, aIter.next());
                statement.setInt(k + 8, amennities.size());
                result = statement.executeQuery();
            }
        }

        /* Search around postal code. */
        if (userInput.equalsIgnoreCase("c")) {
            /* Calculate using the ascii value of the first letter of postal code. If difference within 2,
                we consider it as adjacent postal code.*/
            if (!filterByAmenities && !filterByDate && !filterByAmenities) {
                query = "SELECT * FROM (" +
                        "SELECT listing_id,country,province,city,address,suite_number,postal_code," +
                        "ABS(ASCII(postal_code) - ASCII(?)) AS distance FROM listing) AS A " +
                        "WHERE distance <= 2 OR postal_code = ? ORDER BY distance asc;";
                statement = connection.prepareStatement(query);
                statement.setString(1, postalCode);
                statement.setString(2, postalCode);
                result = statement.executeQuery();
            } else if (filterByDate && !filterByAmenities && !filterByPrice) {
                query = "SELECT * FROM (" +
                        "SELECT * FROM (" +
                        "SELECT listing_id,country,province,city,address,suite_number,postal_code," +
                        "ABS(ASCII(postal_code) - ASCII(?)) AS distance FROM listing) AS A1 " +
                        "WHERE distance <= 2 OR postal_code = ?) AS A " +
                        "JOIN (" +
                        "SELECT listing_id, total_price FROM (" +
                        "SELECT listing_id, COUNT(*) as total_days, SUM(price) as total_price FROM availability " +
                        "WHERE date >= ? AND date < ? AND booked = false GROUP BY listing_id) as B1 " +
                        "WHERE total_days = ?) AS B " +
                        "ON A.listing_id=B.listing_id";
                if (rankByPriceAsc)
                    query = query + " ORDER BY total_price asc";
                else if (rankByPriceDesc)
                    query = query + " ORDER BY total_price desc";
                else
                    query = query + " ORDER BY distance asc";
                statement = connection.prepareStatement(query);
                statement.setString(1, postalCode);
                statement.setString(2, postalCode);
                statement.setDate(3, startDate);
                statement.setDate(4, endDate);
                statement.setLong(5, DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()));
                result = statement.executeQuery();
            } else if (filterByDate && filterByPrice && !filterByAmenities) {
                query = "SELECT * FROM (" +
                        "SELECT * FROM (" +
                        "SELECT listing_id,country,province,city,address,suite_number,postal_code," +
                        "ABS(ASCII(postal_code) - ASCII(?)) AS distance FROM listing) AS A1 " +
                        "WHERE distance <= 2 OR postal_code = ?) AS A " +
                        "JOIN (" +
                        "SELECT listing_id, total_price FROM (" +
                        "SELECT listing_id, COUNT(*) as total_days, SUM(price) as total_price FROM availability " +
                        "WHERE date >= ? AND date < ? AND booked = false GROUP BY listing_id) as B1 " +
                        "WHERE total_days = ? AND total_price >= ? AND total_price <= ?) AS B " +
                        "ON A.listing_id=B.listing_id";
                if (rankByPriceAsc)
                    query = query + " ORDER BY total_price asc";
                else if (rankByPriceDesc)
                    query = query + " ORDER BY total_price desc";
                else
                    query = query + " ORDER BY distance asc";
                statement = connection.prepareStatement(query);
                statement.setString(1, postalCode);
                statement.setString(2, postalCode);
                statement.setDate(3, startDate);
                statement.setDate(4, endDate);
                statement.setLong(5, DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()));
                statement.setFloat(6, minPrice);
                statement.setFloat(7, maxPrice);
                result = statement.executeQuery();
            } else if (filterByAmenities && !filterByDate && !filterByPrice) {
                StringBuilder queryBuilder = new StringBuilder(
                                "SELECT * FROM (" +
                                "SELECT * FROM (" +
                                "SELECT listing_id,country,province,city,address,suite_number,postal_code," +
                                "ABS(ASCII(postal_code) - ASCII(?)) AS distance FROM listing) AS A1 " +
                                "WHERE distance <= 2 OR postal_code = ?) AS A " +
                                "JOIN (" +
                                "SELECT * FROM (" +
                                "SELECT listing_id, COUNT(*) as total_amenity FROM listing_amenity " +
                                "WHERE listing_id IN (SELECT listing_id FROM listing) AND amenity IN (");
                int i;
                for(i = 1; i < amennities.size(); i++)
                    queryBuilder.append("?,");
                queryBuilder.append(
                                "?) GROUP BY listing_id) as B1 " +
                                "WHERE total_amenity = ?) AS B " +
                                "ON A.listing_id=B.listing_id ORDER BY distance asc");
                statement = connection.prepareStatement(queryBuilder.toString());
                statement.setString(1, postalCode);
                statement.setString(2, postalCode);
                Iterator<String> aIter = amennities.iterator();
                int k;
                for(k = 1; k <= amennities.size(); k++)
                    statement.setString(k + 2, aIter.next());
                statement.setInt(k + 2, amennities.size());
                result = statement.executeQuery();
            } else if (filterByAmenities && filterByDate && !filterByPrice) {
                StringBuilder queryBuilder = new StringBuilder(
                                "SELECT * FROM (" +
                                "SELECT listing_id, total_price FROM (" +
                                "SELECT listing_id, COUNT(*) as total_days, SUM(price) as total_price FROM availability " +
                                "WHERE date >= ? AND date < ? AND booked = false GROUP BY listing_id) AS A1 " +
                                "WHERE total_days = ?) AS A " +
                                "JOIN (" +
                                "SELECT * FROM (" +
                                "SELECT listing_id,country,province,city,address,suite_number,postal_code," +
                                "ABS(ASCII(postal_code) - ASCII(?)) AS distance FROM listing) AS B1 " +
                                "WHERE distance <= 2 OR postal_code = ?) AS B " +
                                "ON A.listing_id=B.listing_id " +
                                "JOIN (" +
                                "SELECT * FROM (" +
                                "SELECT listing_id, COUNT(*) as total_amenity FROM listing_amenity " +
                                "WHERE listing_id IN (SELECT listing_id FROM listing) AND amenity IN (");
                int i;
                for(i = 1; i < amennities.size(); i++)
                    queryBuilder.append("?,");
                queryBuilder.append(
                                "?) GROUP BY listing_id) as C1 " +
                                "WHERE total_amenity = ?) AS C " +
                                "ON A.listing_id=C.listing_id");
                if (rankByPriceAsc)
                    queryBuilder.append(" ORDER BY total_price asc");
                else if (rankByPriceDesc)
                    queryBuilder.append(" ORDER BY total_price desc");
                else
                    queryBuilder.append(" ORDER BY distance asc");
                statement = connection.prepareStatement(queryBuilder.toString());
                statement.setDate(1, startDate);
                statement.setDate(2, endDate);
                statement.setLong(3, DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()));
                statement.setString(4, postalCode);
                statement.setString(5, postalCode);
                Iterator<String> aIter = amennities.iterator();
                int k;
                for(k = 1; k <= amennities.size(); k++)
                    statement.setString(k + 5, aIter.next());
                statement.setInt(k + 5, amennities.size());
                result = statement.executeQuery();
            } else if (filterByAmenities && filterByDate && filterByPrice) {
                StringBuilder queryBuilder = new StringBuilder(
                                "SELECT * FROM (" +
                                "SELECT listing_id, total_price FROM (" +
                                "SELECT listing_id, COUNT(*) as total_days, SUM(price) as total_price FROM availability " +
                                "WHERE date >= ? AND date < ? AND booked = false GROUP BY listing_id) AS A1 " +
                                "WHERE total_days = ? AND total_price >= ? AND total_price <= ?) AS A " +
                                "JOIN (" +
                                "SELECT * FROM (" +
                                "SELECT listing_id,country,province,city,address,suite_number,postal_code," +
                                "ABS(ASCII(postal_code) - ASCII(?)) AS distance FROM listing) AS B1 " +
                                "WHERE distance <= 2 OR postal_code = ?) AS B " +
                                "ON A.listing_id=B.listing_id " +
                                "JOIN (" +
                                "SELECT * FROM (" +
                                "SELECT listing_id, COUNT(*) as total_amenity FROM listing_amenity " +
                                "WHERE listing_id IN (SELECT listing_id FROM listing) AND amenity IN (");
                int i;
                for(i = 1; i < amennities.size(); i++)
                    queryBuilder.append("?,");
                queryBuilder.append(
                        "?) GROUP BY listing_id) as C1 " +
                        "WHERE total_amenity = ?) AS C " +
                        "ON A.listing_id=C.listing_id");
                if (rankByPriceAsc)
                    queryBuilder.append(" ORDER BY total_price asc");
                else if (rankByPriceDesc)
                    queryBuilder.append(" ORDER BY total_price desc");
                else
                    queryBuilder.append(" ORDER BY distance asc");
                statement = connection.prepareStatement(queryBuilder.toString());
                statement.setDate(1, startDate);
                statement.setDate(2, endDate);
                statement.setLong(3, DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()));
                statement.setFloat(4, minPrice);
                statement.setFloat(5, maxPrice);
                statement.setString(6, postalCode);
                statement.setString(7, postalCode);
                Iterator<String> aIter = amennities.iterator();
                int k;
                for(k = 1; k <= amennities.size(); k++)
                    statement.setString(k + 7, aIter.next());
                statement.setInt(k + 7, amennities.size());
                result = statement.executeQuery();
            }
        }

        /* Print results. */
        if (!result.next()) {
            console.printf("\n%s\n", LISTING_NOT_FOUND);
            return;
        }
        console.printf("\nFound listings: \n");
        do {
            int listingId = result.getInt("listing_id");
            String suiteNumber = result.getString("suite_number");
            String address = result.getString("address");
            String city = result.getString("city");
            String province = result.getString("province");
            String country = result.getString("country");
            postalCode = result.getString("postal_code");
            Double distance = result.getDouble("distance");
            if (userInput.equalsIgnoreCase("b"))
                distance = distance / 10; // for the purpose of calculation, km / 10
            float totalPrice = -1;
            try {
                totalPrice = result.getFloat("total_price");
            } catch (Exception e) {
            }
            console.printf("======================\n");
            if (totalPrice != -1) {
                if (suiteNumber != null)
                    console.printf("%s-%s, %s, %s, %s, %s with id: %d [Distance: around %.2fkm, Total price: $%.2f].\n",
                            suiteNumber, address, city, province, country, postalCode, listingId, distance, totalPrice);
                else
                    console.printf("%s, %s, %s, %s, %s with id: %d [Distance: around %.2fkm, Total price: $%.2f].\n",
                            address, city, province, country, postalCode, listingId, distance, totalPrice);
            } else {
                if (suiteNumber != null)
                    console.printf("%s-%s, %s, %s, %s, %s with id: %d [Distance: around %.2fkm].\n", suiteNumber,
                            address, city, province, country, postalCode, listingId, distance);
                else
                    console.printf("%s, %s, %s, %s, %s with id: %d [Distance: around %.2fkm].\n", address, city,
                            province, country, postalCode, listingId, distance);
            }
        } while(result.next());
        console.printf("======================\n");
        return;
    }

    /* Get a listing from user. */
    private static int getListingId() throws SQLException {
        String userInput;
        while(true) {
            userInput = console.readLine(LISTING_ID_PROMPT).toLowerCase().strip();
            if (userInput.equalsIgnoreCase("a") ) {
                try {
                    return Integer.valueOf(console.readLine(GET_LISTING_ID).strip());
                } catch (Exception e) {
                    console.printf("\n%s\n\n", INVALID_INPUT);
                }
            } else if (userInput.equalsIgnoreCase("b"))
                searchListings();
            else
                console.printf("\n%s\n\n", INVALID_INPUT);
        }
    }

    /* Book a listing. */
    private static void bookListing() throws SQLException {
        int listingId = getListingId();

        /* Validate listing. */
        PreparedStatement statement = connection.prepareStatement(FIND_LISTING_BY_ID_QUERY);
        statement.setInt(1, listingId);
        ResultSet result = statement.executeQuery();
        if (!result.next()) {
            console.printf("\n%s\n", LISTING_NOT_FOUND);
            return;
        }
        if (result.getString("host_id").equalsIgnoreCase(currentUsername)) {
            console.printf("\n%s\n", CANNOT_RENT_OWN);
            return;
        }

        /* Validate dates and availabilities*/
        Date startDate = getDate(START_DATE_PROMPT);
        Date endDate;
        while(true) {
            endDate = getDate(END_DATE_PROMPT);
            if (endDate.equals(startDate) ||
                    endDate.toLocalDate().isBefore(startDate.toLocalDate()))
                console.printf("%s\n", END_DATE_ERROR);
            else
                break;
        }
        String query = "SELECT total_price FROM " +
                        "(SELECT COUNT(*) as total_days, SUM(price) as total_price FROM availability " +
                        "WHERE listing_id = ? AND date >= ? AND date < ? AND booked = false " +
                        "GROUP BY listing_id) as new_table " +
                        "WHERE total_days = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, listingId);
        statement.setDate(2, startDate);
        statement.setDate(3, endDate);
        statement.setLong(4, DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()));
        result = statement.executeQuery();
        if (!result.next()) {
            console.printf("\n%s\n", LISTING_NOT_AVAILABLE);
            return;
        }
        float price = result.getFloat("total_price");
        if(!getBoolean("\nThe total price is " + price + ". Do you wish to complete the booking?"))
            return;

        /* Check user's payment information. */
        query = "SELECT * FROM user WHERE user_id = ? AND credit_card_number IS NOT NULL AND " +
                "credit_card_expiration_date IS NOT NULL AND credit_card_code IS NOT NULL";
        statement = connection.prepareStatement(query);
        statement.setString(1, currentUsername);
        result = statement.executeQuery();
        //result cannot work => no payment information => must provide payment information
        if(!result.next()){
            console.printf("%s\n", ENTER_PAYMENT_INFO);
            //provide info
            String creditcardNum = getCreditNum();
            String creditcardDate = getCreditDate();
            String creditcardCode = getCreditCode();
       
            PreparedStatement payment = connection.prepareStatement(INSERT_PAYMENT);
            payment.setString(1, creditcardNum);
            payment.setString(2, creditcardDate);
            payment.setString(3, creditcardCode);
            payment.setString(4, currentUsername);
            payment.executeUpdate();
            console.printf("%s\n", UPDATE_PAYMENT);
        }

        /* Update history table. */
        query = "INSERT INTO history (listing_id,user_id,start_date,end_date,price) VALUES (?,?,?,?,?)";
        statement = connection.prepareStatement(query);
        statement.setInt(1, listingId);
        statement.setString(2, currentUsername);
        statement.setDate(3, startDate);
        statement.setDate(4, endDate);
        statement.setFloat(5, price);
        statement.executeUpdate();

        /* Update Availability table. */
        query = "UPDATE availability SET booked = true WHERE listing_id = ? AND date >= ? AND date < ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, listingId);
        statement.setDate(2, startDate);
        statement.setDate(3, endDate);
        statement.executeUpdate();

        console.printf("\n%s\n", BOOKING_SUCCESSFUL);
    }

    /* Cancel a booking. */
    private static void cancelBooking() throws SQLException {
        int listingId = getListingId();
        Date startDate = getDate(START_DATE_PROMPT);
        Date endDate;
        while(true) {
            endDate = getDate(END_DATE_PROMPT);
            if (endDate.equals(startDate) ||
                    endDate.toLocalDate().isBefore(startDate.toLocalDate()))
                console.printf("%s\n", END_DATE_ERROR);
            else
                break;
        }
        String query = "SELECT * FROM history WHERE listing_id = ? AND user_id = ? AND start_date = ? AND end_date = ?"
                        + "AND cancelled = false AND fulfilled = false";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, listingId);
        statement.setString(2, currentUsername);
        statement.setDate(3, startDate);
        statement.setDate(4, endDate);
        ResultSet result = statement.executeQuery();
        if (!result.next()) {
            console.printf("\n%s\n", BOOKING_NOT_FOUND);
            return;
        }

        /* Update db. */
        removeBooking(listingId, currentUsername, startDate, endDate);

        console.printf("\n%s\n", BOOKING_CANCELLED);
    }

    /* Cancel renter's booking. */
    private static void cancelRenterBooking() throws SQLException {
        /* Validate host. */
        PreparedStatement statement = connection.prepareStatement(FIND_USER_QUERY);
        statement.setString(1, currentUsername);
        ResultSet result = statement.executeQuery();
        result.next();
        if (!result.getBoolean("is_host")) {
            console.printf("%s\n", MUST_BE_HOST);
            return;
        }
        /* Validate listing. */
        int listingId = getListingId();
        statement = connection.prepareStatement(FIND_LISTING_BY_ID_QUERY + " AND host_id = ?");
        statement.setInt(1, listingId);
        statement.setString(2, currentUsername);
        result = statement.executeQuery();
        if (!result.next()) {
            console.printf("\n%s\n", LISTING_NOT_FOUND);
            return;
        }
        Date startDate = getDate(START_DATE_PROMPT);
        Date endDate;
        while(true) {
            endDate = getDate(END_DATE_PROMPT);
            if (endDate.equals(startDate) ||
                    endDate.toLocalDate().isBefore(startDate.toLocalDate()))
                console.printf("%s\n", END_DATE_ERROR);
            else
                break;
        }
        String query = "SELECT * FROM history WHERE listing_id = ? AND start_date = ? AND end_date = ? AND " +
                        "cancelled = false AND fulfilled = false";
        statement = connection.prepareStatement(query);
        statement.setInt(1, listingId);
        statement.setDate(2, startDate);
        statement.setDate(3, endDate);
        result = statement.executeQuery();
        if (!result.next()) {
            console.printf("\n%s\n", BOOKING_NOT_FOUND);
            return;
        }

        /* Update db. */
        removeBooking(listingId, result.getString("user_id"), startDate, endDate);

        console.printf("\n%s\n", BOOKING_CANCELLED);
    }

    /* Create a listing. */
    private static void createListing() throws SQLException {
        /* Check if user is a host. */
        PreparedStatement findUserPS = connection.prepareStatement(FIND_USER_QUERY);
        findUserPS.setString(1, currentUsername);
        ResultSet result = findUserPS.executeQuery();
        result.next();
        if (!result.getBoolean("is_host")) {
            console.printf("%s\n", MUST_BE_HOST);
            if (getBoolean(ASK_HOST))
                becomeHost();
            else
                return;
        }

        /* Create listing. */
        String propertyType = getPropertyType();
        String country = console.readLine(COUNTRY_PROMPT).toLowerCase();
        String province = console.readLine(PROVINCE_PROMPT).toLowerCase();
        String city = console.readLine(CITY_PROMPT).toLowerCase();
        String address = console.readLine(ADDRESS_PROMPT).toLowerCase();
        String suiteNumber = console.readLine(SUITE_NUMBER_PROMPT).toLowerCase();
        if (suiteNumber.isBlank())
            suiteNumber = null;
        String postalCode = console.readLine(POSTAL_CODE_PROMPT).toLowerCase();
        Double longitude = getLongitude();
        Double latitude = getLatitude();
        String query = "INSERT INTO listing(host_id,property_type,country,province,city,address,suite_number," +
                       "postal_code,longitude,latitude) VALUES(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement createListing = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        createListing.setString(1, currentUsername);
        createListing.setString(2, propertyType);
        createListing.setString(3, country);
        createListing.setString(4, province);
        createListing.setString(5, city);
        createListing.setString(6, address);
        createListing.setString(7, suiteNumber);
        createListing.setString(8, postalCode);
        createListing.setDouble(9, longitude);
        createListing.setDouble(10, latitude);
        createListing.executeUpdate();
        result = createListing.getGeneratedKeys();
        result.next();
        Integer listingId = result.getInt(1);
        if (suiteNumber != null)
            console.printf("\n%s %s-%s, %s, %s, %s, %s.\n\n", LISTING_CREATED, suiteNumber, address, city, province,
                           country, postalCode);
        else
            console.printf("\n%s %s, %s, %s, %s, %s.\n\n", LISTING_CREATED, address, city, province, country,
                           postalCode);

        /* Create amenities. */
        if(getBoolean(CONTINUE_UPDATE_AMENITY))
            updateAmenities(listingId);

        /* Create availabilities. */
        if (getBoolean(CONTINUE_UPDATE_AVAILABILITIES))
            updateAvailabilities(listingId);
    }

    /* Host toolkit. */
    private static void hostToolkit() throws SQLException {
        String query = "SELECT listing_id, SUM(price) AS total_earning " +
                        "FROM history " +
                        "WHERE start_date >= DATE_SUB(NOW(), INTERVAL 1 YEAR) AND cancelled = false " +
                        "GROUP BY listing_id " +
                        "ORDER BY total_earning desc;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        Integer listingId = -1;
        if (result.next()) {
            /* The listing that created the most revenue the past recent one year */
            listingId = result.getInt("listing_id");
        }

        /* Get amenities. */
        recommendedAmennities = new LinkedHashSet<String>();
        query = "SELECT * FROM listing_amenity WHERE listing_id = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, listingId);
        result = statement.executeQuery();
        while(result.next()) {
            recommendedAmennities.add(result.getString("amenity"));
        }

        /* Get average price. */
        query = "SELECT listing_id, AVG(price) AS average_price FROM availability " +
                "WHERE listing_id = ? AND date >= DATE_SUB(NOW(), INTERVAL 1 YEAR)";
        statement = connection.prepareStatement(query);
        statement.setInt(1, listingId);
        result = statement.executeQuery();
        result.next();
        recommendedPrice = result.getFloat("average_price");
    }

    private static void updateAmenities(Integer listingId) throws SQLException{
        /* Validate host. */
        PreparedStatement findUserPS = connection.prepareStatement(FIND_USER_QUERY);
        findUserPS.setString(1, currentUsername);
        ResultSet result = findUserPS.executeQuery();
        result.next();
        if (!result.getBoolean("is_host")) {
            console.printf("%s\n", MUST_BE_HOST);
            return;
        }
        /* Validate listing. */
        PreparedStatement statement = connection.prepareStatement(FIND_LISTING_BY_ID_QUERY);
        statement.setInt(1, listingId);
        result = statement.executeQuery();
        if(!result.next()){
            console.printf("\n%s\n", LISTING_NOT_FOUND);
            return;
        }
        if(!result.getString("host_id").equalsIgnoreCase(currentUsername)){
            console.printf("\n%s\n", LISTING_NOT_OWNED + listingId);
            return;
        }
        /* update amenities */
        String amenity;
        String query;
        do{
            console.printf("\nWe recommend the following amenities: %s\n\n", recommendedAmennities.toString());
            amenity = getAmenity();
            //check duplication
            query = "SELECT * FROM listing_amenity WHERE listing_id = ? AND amenity = ?";
            PreparedStatement check = connection.prepareStatement(query);
            check.setInt(1, listingId);
            check.setString(2, amenity);
            result = check.executeQuery();
            if (result.next()){
                console.printf("%s\n", DUPLICATE_AMENITY);
            } else {
                //insert amenity
                query = "INSERT INTO listing_amenity(listing_id, amenity) VALUES(?, ?)";
                PreparedStatement insertAmenity = connection.prepareStatement(query);
                insertAmenity.setInt(1, listingId);
                insertAmenity.setString(2, amenity);
                insertAmenity.executeUpdate();
            }
        } while(getBoolean(CONTINUE_UPDATE_AMENITY));
    }

    /* Create availabilities. */
    private static void updateAvailabilities(Integer listingId) throws SQLException {
        /* Validate host. */
        PreparedStatement findUserPS = connection.prepareStatement(FIND_USER_QUERY);
        findUserPS.setString(1, currentUsername);
        ResultSet result = findUserPS.executeQuery();
        result.next();
        if (!result.getBoolean("is_host")) {
            console.printf("%s\n", MUST_BE_HOST);
            return;
        }

        /* Validate listing. */
        PreparedStatement statement = connection.prepareStatement(FIND_LISTING_BY_ID_QUERY);
        statement.setInt(1, listingId);
        result = statement.executeQuery();
        if (!result.next()) {
            console.printf("\n%s\n", LISTING_NOT_FOUND);
            return;
        }
        if (!result.getString("host_id").equalsIgnoreCase(currentUsername)) {
            console.printf("\n%s\n", LISTING_NOT_OWNED + listingId);
            return;
        }

        /* Update availabilities. */
        Date startDate;
        Date endDate;
        Float price;
        PreparedStatement createAvailability = connection.prepareStatement(INSERT_OR_UPDATE_AVAILABILITY);
        String query;
        Boolean updateAvailability;
        do {
            updateAvailability = true;
            startDate = getDate(START_DATE_PROMPT);
            while(true) {
                endDate = getDate(END_DATE_PROMPT);
                if (endDate.equals(startDate) ||
                    endDate.toLocalDate().isBefore(startDate.toLocalDate()))
                    console.printf("%s\n", END_DATE_ERROR);
                else
                    break;
            }

            /* Check if any date within the date range is booked already. */
            query = "SELECT * FROM availability WHERE listing_id = ? AND date >= ? AND date < ? AND booked = true";
            statement = connection.prepareStatement(query);
            statement.setInt(1, listingId);
            statement.setDate(2, startDate);
            statement.setDate(3, endDate);
            result = statement.executeQuery();
            if (result.next()) {
                console.printf("\n%s\n", DATES_BOOKED);
                return;
            }

            while(true) {
                try {
                    price = Float.valueOf(console.readLine(
                            "Please enter the per day price [We recommend $%.2f per day]: ", recommendedPrice));
                    break;
                } catch (Exception e) {
                    console.printf("\n%s\n", INVALID_INPUT);
                }
            }

            /* Check if any date already exist with another price. */
            query = "SELECT * FROM availability WHERE listing_id = ? AND date >= ? AND date < ? AND price != ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, listingId);
            statement.setDate(2, startDate);
            statement.setDate(3, endDate);
            statement.setFloat(4, price);
            result = statement.executeQuery();
            if (result.next() && !getBoolean(AVAILABILITIES_EXISTS))
                updateAvailability = false;

            /* Update availabilities*/
            if (updateAvailability) {
                Date temp = Date.valueOf(startDate.toLocalDate());
                createAvailability.setInt(1, listingId);
                createAvailability.setFloat(2, price);
                createAvailability.setFloat(4, price);
                while(temp.toLocalDate().isBefore(endDate.toLocalDate())) {
                    createAvailability.setDate(3, temp);
                    createAvailability.executeUpdate();
                    temp = Date.valueOf(temp.toLocalDate().plusDays(1));
                }
                console.printf("\n%s\n\n", AVAILABILITY_SUCCESSFUL);
            }
        } while (getBoolean(CONTINUE_UPDATE_AVAILABILITIES));
    }

    /* Delete availabilities. */
    private static void deleteAvailabilities() throws SQLException {
        /* Validate host. */
        PreparedStatement findUserPS = connection.prepareStatement(FIND_USER_QUERY);
        findUserPS.setString(1, currentUsername);
        ResultSet result = findUserPS.executeQuery();
        result.next();
        if (!result.getBoolean("is_host")) {
            console.printf("%s\n", MUST_BE_HOST);
            return;
        }

        Integer listingId = getListingId();

        /* Validate listing. */
        PreparedStatement statement = connection.prepareStatement(FIND_LISTING_BY_ID_QUERY);
        statement.setInt(1, listingId);
        result = statement.executeQuery();
        if (!result.next()) {
            console.printf("\n%s\n", LISTING_NOT_FOUND);
            return;
        }
        if (!result.getString("host_id").equalsIgnoreCase(currentUsername)) {
            console.printf("\n%s\n", LISTING_NOT_OWNED + listingId);
            return;
        }

        Date startDate;
        Date endDate;
        String query = "DELETE FROM availability WHERE listing_id = ? AND date >= ? AND date < ?";
        PreparedStatement deleteAvailability = connection.prepareStatement(query);
        deleteAvailability.setInt(1, listingId);
        do {
            startDate = getDate(START_DATE_PROMPT);
            while(true) {
                endDate = getDate(END_DATE_PROMPT);
                if (endDate.equals(startDate) ||
                        endDate.toLocalDate().isBefore(startDate.toLocalDate()))
                    console.printf("%s\n", END_DATE_ERROR);
                else
                    break;
            }

            /* Check if any date within the date range is booked already. */
            query = "SELECT * FROM availability WHERE listing_id = ? AND date >= ? AND date < ? AND booked = true";
            statement = connection.prepareStatement(query);
            statement.setInt(1, listingId);
            statement.setDate(2, startDate);
            statement.setDate(3, endDate);
            result = statement.executeQuery();
            if (result.next()) {
                console.printf("\n%s\n", DATES_BOOKED);
                return;
            }

            deleteAvailability.setDate(2, startDate);
            deleteAvailability.setDate(3, endDate);
            deleteAvailability.executeUpdate();
            console.printf("\n%s\n\n", AVAILABILITY_SUCCESSFUL);
        } while (getBoolean("Do you wish to continue deleting availabilities?"));
    }

    /* Delete listing. */
    private static Boolean deleteListing(Integer listingId) throws SQLException {
        /* Validate host. */
        PreparedStatement findUserPS = connection.prepareStatement(FIND_USER_QUERY);
        findUserPS.setString(1, currentUsername);
        ResultSet result = findUserPS.executeQuery();
        result.next();
        if (!result.getBoolean("is_host")) {
            console.printf("%s\n", MUST_BE_HOST);
            return false;
        }

        /* Validate listing. */
        PreparedStatement statement = connection.prepareStatement(FIND_LISTING_BY_ID_QUERY);
        statement.setInt(1, listingId);
        result = statement.executeQuery();
        if (!result.next()) {
            console.printf("\n%s\n", LISTING_NOT_FOUND);
            return false;
        }
        if (!result.getString("host_id").equalsIgnoreCase(currentUsername)) {
            console.printf("\n%s\n", LISTING_NOT_OWNED + listingId);
            return false;
        }

        /* Cancel all bookings. */
        String query = "UPDATE history SET cancelled = true WHERE fulfilled = false AND cancelled = false AND " +
                        "listing_id = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, listingId);
        statement.executeUpdate();

        /* Remove all availabilities. */
        query = "DELETE FROM availability WHERE listing_id = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, listingId);
        statement.executeUpdate();

        /* Remove listing amenity table */
        query = "DELETE FROM listing_amenity WHERE listing_id = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, listingId);
        statement.executeUpdate();

        /* Remove listing. */
        query = "UPDATE listing SET removed = true WHERE listing_id = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, listingId);
        statement.executeUpdate();

        return true;
    }

    /* rate renter => host rate renter*/
    private static void rateRenter() throws SQLException{
        // validate listing
        int listingId = getListingId();
        PreparedStatement statement = connection.prepareStatement(FIND_LISTING_BY_ID_QUERY);
        statement.setInt(1, listingId);
        ResultSet result = statement.executeQuery();
        if (!result.next()) {
            console.printf("\n%s\n", LISTING_NOT_FOUND);
            return;
        }
        if (!result.getString("host_id").equalsIgnoreCase(currentUsername)) {
            console.printf("\n%s\n", LISTING_NOT_OWNED + listingId);
            return;
        }

        // validate history information
        Date start = getDate(START_DATE_PROMPT);
        Date end = getDate(END_DATE_PROMPT);
        String query = "SELECT * FROM history WHERE listing_id = ? AND start_date = ? AND end_date = ? AND " +
                        "cancelled = false";
        statement = connection.prepareStatement(query);
        statement.setInt(1, listingId);
        statement.setDate(2, start);
        statement.setDate(3, end);
        result = statement.executeQuery();
        if(!result.next()){
            console.printf("%s\n", INVALID_INFORMATION);
            return;
        }
        //check if rated before
        int rating = result.getInt("renter_rating");
        if(rating > 0)
        {
            console.printf("%s\n", ALREADY_RATED);
            return;
        }
        /* update history table */
        query = "UPDATE history SET fulfilled = true, renter_rating = ?, renter_review = ? " + 
                "WHERE listing_id = ? AND start_date = ? AND end_date = ? AND cancelled = false";
        rating = getRenterRating();
        String review = console.readLine(REVIEW_RENTER);
        statement = connection.prepareStatement(query);
        statement.setInt(1, rating);
        statement.setCharacterStream(2, new StringReader(review));
        statement.setInt(3, listingId);
        statement.setDate(4, start);
        statement.setDate(5, end);
        statement.executeUpdate();

        console.printf("%s\n", REVIEW_UPDATED);
    }

    /* rate listing => renter rate host*/
    private static void rateListing() throws SQLException{
        int listingId = getListingId();
        PreparedStatement statement = connection.prepareStatement(FIND_LISTING_BY_ID_QUERY);
        statement.setInt(1, listingId);
        ResultSet result = statement.executeQuery();
        if (!result.next()) {
            console.printf("\n%s\n", LISTING_NOT_FOUND);
            return;
        }
        Date start = getDate(START_DATE_PROMPT);
        Date end = getDate(END_DATE_PROMPT);
        String query = "SELECT * FROM history WHERE listing_id = ? AND start_date = ? AND end_date = ? AND " +
                        "cancelled = false AND user_id = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, listingId);
        statement.setDate(2, start);
        statement.setDate(3, end);
        statement.setString(4, currentUsername);
        result = statement.executeQuery();
        if(!result.next()){
            console.printf("%s\n", INVALID_INFORMATION);
            return;
        }
        //check if rated before
        int rating = result.getInt("host_rating");
        if(rating > 0)
        {
            console.printf("%s\n", ALREADY_RATED);
            return;
        }
        /* update history table */
        query = "UPDATE history SET fulfilled = true, host_rating = ?, host_review = ? " + 
                "WHERE listing_id = ? AND start_date = ? AND end_date = ? AND cancelled = false AND user_id = ?";
        rating = getHostRating();
        String review = console.readLine(REVIEW_HOST);
        statement = connection.prepareStatement(query);
        statement.setInt(1, rating);
        statement.setCharacterStream(2, new StringReader(review));
        statement.setInt(3, listingId);
        statement.setDate(4, start);
        statement.setDate(5, end);
        statement.setString(6, currentUsername);
        statement.executeUpdate();

        console.printf("%s\n", REVIEW_UPDATED);
    }

    /* Deactivate host account. */
    private static void deactivateHost() throws SQLException {
        /* Check if user is a host. */
        PreparedStatement statement = connection.prepareStatement(FIND_USER_QUERY);
        statement.setString(1, currentUsername);
        ResultSet result = statement.executeQuery();
        result.next();
        if (!result.getBoolean("is_host")) {
            console.printf("%s\n", MUST_BE_HOST);
            return;
        }

        /* Delete all listings. */
        String query = "SELECT * FROM listing WHERE removed = false AND host_id = ?";
        statement = connection.prepareStatement(query);
        statement.setString(1, currentUsername);
        ResultSet listings = statement.executeQuery();
        Integer listingId;
        while(listings.next()) {
            listingId = listings.getInt("listing_id");
            deleteListing(listingId);
        }

        /* Update user account. */
        query = "UPDATE user SET is_host = false, used_to_be_host = true WHERE user_id = ?";
        statement = connection.prepareStatement(query);
        statement.setString(1, currentUsername);
        statement.executeUpdate();

        console.printf("\n%s\n", DEACTIVATED_HOST);
    }

    /* Deavtivate user account. */
    private static void deactivateUser() throws SQLException {
        /* Deactivate host account first. */
        PreparedStatement statement = connection.prepareStatement(FIND_USER_QUERY);
        statement.setString(1, currentUsername);
        ResultSet result = statement.executeQuery();
        result.next();
        if (result.getBoolean("is_host") && !getBoolean(ASK_DEACTIVATE_HOST))
            return;
        else if (result.getBoolean("is_host"))
            deactivateHost();

        /* Cancel all unfulfilled bookings. */
        String query = "SELECT * FROM history WHERE fulfilled = false AND cancelled = false AND user_id = ?";
        statement = connection.prepareStatement(query);
        statement.setString(1, currentUsername);
        ResultSet histories = statement.executeQuery();
        statement = connection.prepareStatement(FIND_LISTING_BY_ID_QUERY);
        Integer listingId;
        while(histories.next()) {
            listingId = histories.getInt("listing_id");
            statement.setInt(1, listingId);
            result = statement.executeQuery();
            if (result.next())
                removeBooking(listingId, histories.getString("user_id"),
                                histories.getDate("start_date"), histories.getDate("end_date"));
        }

        /* Delete user record. */
        query = "DELETE FROM user WHERE user_id = ?";
        statement = connection.prepareStatement(query);
        statement.setString(1, currentUsername);
        statement.executeUpdate();

        loggedIn = false;
        currentUsername = null;
        firstName = null;
        console.printf("\n%s\n", DEACTIVATED_USER);
    }

    /* Bookings per cities report. */
    private static void bookingsPerCitiesReport() throws SQLException {
        Date startDate = getDate(START_DATE_PROMPT);
        Date endDate = getDate(END_DATE_PROMPT);
        String query = "SELECT listing.country, listing.city, COUNT(*) AS city_booking " +
                        "FROM history " +
                        "JOIN listing " +
                        "ON history.listing_id=listing.listing_id " +
                        "WHERE history.start_date >= ? AND history.end_date < ? AND history.cancelled = false " +
                        "GROUP BY listing.country, listing.city " +
                        "ORDER BY city_booking desc";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, startDate);
        statement.setDate(2, endDate);
        ResultSet result = statement.executeQuery();
        console.printf("\nTotal number of bookings by city in the provided time range: \n");
        while(result.next()) {
            console.printf("%s, %s: %d bookings\n", result.getString("city"),
                    result.getString("country"), result.getInt("city_booking"));
        }

        query = "SELECT listing.country, listing.city, listing.postal_code, COUNT(*) AS city_booking " +
                "FROM history " +
                "JOIN listing " +
                "ON history.listing_id=listing.listing_id " +
                "WHERE history.start_date >= ? AND history.end_date < ? AND history.cancelled = false " +
                "GROUP BY listing.country, listing.city, listing.postal_code " +
                "ORDER BY country, city, city_booking desc";
        statement = connection.prepareStatement(query);
        statement.setDate(1, startDate);
        statement.setDate(2, endDate);
        result = statement.executeQuery();
        console.printf("\nTotal number of bookings by postal codes within a city in the provided time range: \n");
        while(result.next()) {
            console.printf("%s, %s [%s]: %d bookings\n", result.getString("city"),
                    result.getString("country"),
                    result.getString("postal_code"),
                    result.getInt("city_booking"));
        }
    }

    /* report of total number of listing per country per city per postal code */
    private static void listingsNumberPerCitiesReport() throws SQLException{
        String query = "SELECT listing.country, COUNT(*) AS listing_number " +
                        "FROM listing WHERE removed = false GROUP BY listing.country ORDER BY listing_number desc";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        console.printf("\nTotal number of listing by country: \n");
        while(result.next()){
            console.printf("%s: %d listings\n", result.getString("country"),
                    result.getInt("listing_number"));
        }

        query = "SELECT listing.country, listing.city, COUNT(*) AS listing_number " +
                        "FROM listing WHERE removed = false GROUP BY listing.country,listing.city " +
                        "ORDER BY country, listing_number desc";
        statement = connection.prepareStatement(query);
        result = statement.executeQuery();
        console.printf("\nTotal number of listing by city within country: \n");
        while(result.next()){
            console.printf("%s, %s: %d listings\n", result.getString("city"),
                    result.getString("country"),result.getInt("listing_number"));
        }

        query = "SELECT listing.country, listing.city, listing.postal_code, COUNT(*) AS listing_number " +
                        "FROM listing WHERE removed = false GROUP BY listing.country,listing.city," +
                        "listing.postal_code ORDER BY country, city, listing_number desc";
        statement = connection.prepareStatement(query);
        result = statement.executeQuery();
        console.printf("\nTotal number of listing by postal code within cities: \n");
        while(result.next()){
            console.printf("%s, %s [%s]: %d listings\n", result.getString("city"),
                    result.getString("country"), result.getString("postal_code"), 
                    result.getInt("listing_number"));
        }
    }

    /* Rank hosts report. */
    private static void listingsPerHostsReport() throws SQLException {
        String query = "SELECT country, host_id, COUNT(*) AS total_listing " +
                        "FROM listing " +
                        "WHERE removed = false " +
                        "GROUP BY country, host_id " +
                        "ORDER BY country, total_listing desc";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        console.printf("\nRanking hosts by the total number of listings they have overall per country:");
        String country = "";
        int i = 1;
        while(result.next())  {
            if (!country.equalsIgnoreCase(result.getString("country"))) {
                country = result.getString("country");
                console.printf("\n%s:\n", country);
                i = 1;
            }
            console.printf("%d. %s: %d listings\n", i, result.getString("host_id"),
                    result.getInt("total_listing"));
            i++;
        }

        query = "SELECT country, city, host_id, COUNT(*) AS total_listing " +
                "FROM listing " +
                "WHERE removed = false " +
                "GROUP BY country, city, host_id " +
                "ORDER BY country, city, total_listing desc";
        statement = connection.prepareStatement(query);
        result = statement.executeQuery();
        console.printf("\nRanking hosts by the total number of listings they have overall per city:");
        String city = "";
        country = "";
        i = 1;
        while(result.next())  {
            if (!city.equalsIgnoreCase(result.getString("city")) ||
                    !country.equalsIgnoreCase(result.getString("country"))) {
                city = result.getString("city");
                country = result.getString("country");
                console.printf("\n%s, %s:\n", city, country);
                i = 1;
            }
            console.printf("%d. %s: %d listings\n", i, result.getString("host_id"),
                    result.getInt("total_listing"));
            i++;
        }
    }
    /* flag host */
    private static void flagHost() throws SQLException{
        String query = "SELECT * FROM (SELECT country, city, host_id, COUNT(*) AS host_listings FROM listing" + 
                    " WHERE removed = false GROUP BY country, city, host_id) AS A " +
                    "WHERE A.host_listings > (SELECT total_listings FROM( " +
                    "SELECT country, city, COUNT(*) AS total_listings FROM listing WHERE removed = false " +
                    "GROUP BY country, city) AS B" +
                    " WHERE A.city = B.city AND A.country = B.country) / 10 ORDER BY country, city, host_listings desc";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        console.printf("\nFlagged hosts:\n");
        String country = "";
        String city = "";
        while(result.next()){
            if (!city.equalsIgnoreCase(result.getString("city")) ||
                    !country.equalsIgnoreCase(result.getString("country"))) {
                city = result.getString("city");
                country = result.getString("country");
                console.printf("\n%s, %s:\n", city, country);
            }
            console.printf("%s: %d listings\n", result.getString("host_id"),
                    result.getInt("host_listings"));
        }
        
    }

    /* Rank user's booking */
    private static void rankUserBookingsReport() throws SQLException {
        Date startDate = getDate(START_DATE_PROMPT);
        Date endDate = getDate(END_DATE_PROMPT);
        String query = "SELECT user_id, COUNT(*) as total_bookings " +
                        "FROM history " +
                        "WHERE start_date >= ? AND end_date < ? AND cancelled = false " +
                        "GROUP BY user_id " +
                        "ORDER BY total_bookings desc";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, startDate);
        statement.setDate(2, endDate);
        ResultSet result = statement.executeQuery();
        console.printf("\nRanking renters by number of bookings during the given time period: \n");
        int i = 1;
        while (result.next()) {
            console.printf("%d. %s: %d bookings\n", i, result.getString("user_id"),
                    result.getInt("total_bookings"));
            i++;
        }

        query = "SELECT * FROM (" +
                "SELECT listing.country, listing.city, history.user_id, COUNT(*) as total_bookings " +
                "FROM history " +
                "JOIN listing " +
                "ON listing.listing_id = history.listing_id " +
                "WHERE start_date >= ? AND end_date < ? AND cancelled = false " +
                "GROUP BY listing.country, listing.city, history.user_id) AS A " +
                "JOIN (" +
                "SELECT user_id FROM (" +
                "SELECT user_id, COUNT(*) as num_booking FROM history " +
                "WHERE YEAR(start_date) = YEAR(?) AND cancelled = false " +
                "GROUP BY user_id) AS B1 " +
                "WHERE num_booking >= 2) AS B " +
                "ON A.user_id=B.user_id " +
                "ORDER BY A.country, A.city, A.total_bookings desc";
        statement = connection.prepareStatement(query);
        statement.setDate(1, startDate);
        statement.setDate(2, endDate);
        statement.setDate(3, startDate);
        result = statement.executeQuery();
        console.printf("\nRanking renters by number of bookings within cities during the given time period:");
        String city = "";
        String country = "";
        i = 1;
        while (result.next()) {
            if (!city.equalsIgnoreCase(result.getString("city")) ||
                    !country.equalsIgnoreCase(result.getString("country"))) {
                city = result.getString("city");
                country = result.getString("country");
                console.printf("\n%s, %s:\n", city, country);
                i = 1;
            }
            console.printf("%d. %s: %d bookings\n", i, result.getString("user_id"),
                    result.getInt("total_bookings"));
            i++;
        }
    }

    /* Largest cancellation. */
    private static void largestCancellation() throws SQLException {
        String query = "SELECT listing.host_id, COUNT(*) AS total_cancellation " +
                        "FROM listing " +
                        "JOIN history " +
                        "ON listing.listing_id=history.listing_id " +
                        "WHERE start_date >= DATE_SUB(NOW(), INTERVAL 1 YEAR) AND cancelled = true " +
                        "GROUP BY host_id ORDER BY total_cancellation desc";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            console.printf("\nThe host with the largest cancellation within a year is %s with %d total cancellations\n",
                    result.getString("host_id"), result.getInt("total_cancellation"));
        }

        query = "SELECT user_id, COUNT(*) AS total_cancellation " +
                "FROM history " +
                "WHERE start_date >= DATE_SUB(NOW(), INTERVAL 1 YEAR) AND cancelled = true " +
                "GROUP BY user_id " +
                "ORDER BY total_cancellation desc";
        statement = connection.prepareStatement(query);
        result = statement.executeQuery();
        if (result.next()) {
            console.printf("\nThe user with the largest cancellation within a year is %s with %d total cancellations\n",
                    result.getString("user_id"), result.getInt("total_cancellation"));
        }
    }

    /* Print popular noun phrases */
    private static void popularNoun() throws SQLException, IOException {
        String query = "SELECT listing_id, host_review FROM history " +
                        "WHERE host_review IS NOT NULL " +
                        "ORDER BY listing_id";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery(query);
        int listingId = -1;
        HashMap<String, Integer> wordHash = new HashMap<String, Integer>();
        while (result.next()) {
            if (listingId != result.getInt("listing_id")) {
                if (listingId != -1)
                    console.printf("\nfor listing with id %d: %s\n", listingId, wordHash.toString());
                listingId = result.getInt("listing_id");
                wordHash = new HashMap<String, Integer>();
            }
            Reader reader = result.getCharacterStream("host_review");
            String review = new BufferedReader(reader).readLine();
            String [] reviewWords = review.split(" ");
            for (int i = 0; i < reviewWords.length; i++) {
                int num = 1;
                if (wordHash.containsKey(reviewWords[i].strip()))
                    num = wordHash.get(reviewWords[i].strip()) + 1;
                wordHash.put(reviewWords[i].strip(), num++);
            }
        }
        if (listingId != -1)
            console.printf("\nfor listing with id %d: %s\n", listingId, wordHash.toString());
    }

    /* Helper functions. */

    /* Cancel a booking. Update history and availability table. */
    private static void removeBooking (Integer listingId, String userId, Date startDate, Date endDate)
                                        throws SQLException {
        /* Update history table. */
        String query = "UPDATE history SET cancelled = true WHERE listing_id = ? AND user_id = ? AND " +
                        "start_date = ? AND end_date = ? AND fulfilled = false AND cancelled = false";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, listingId);
        statement.setString(2, userId);
        statement.setDate(3, startDate);
        statement.setDate(4, endDate);
        statement.executeUpdate();

        /* Update availability table. */
        Date temp = Date.valueOf(startDate.toLocalDate());
        query = "UPDATE availability SET booked = false WHERE listing_id = ? AND date >= ? AND date < ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, listingId);
        statement.setDate(2, startDate);
        statement.setDate(3, endDate);
        statement.executeUpdate();
    }

    /* Get amenity */
    private static String getAmenity(){
        String amenity_name;
        while (true) {
            amenity_name = console.readLine(ASK_AMENITY_NAME).toLowerCase().strip();
            switch (amenity_name) {
                case "a":
                    return "wifi";
                case "b":
                    return "kitchen";
                case "c":
                    return "washer";
                case "d":
                    return "dryer";
                case "e":
                    return "refrigerator";
                case "f":
                    return "microwave";
                case "g":
                    return "coffee maker";
                case "h":
                    return "air conditioning";
                case "i":
                    return "stove";
                case "j":
                    return "heating";
                case "k":
                    return "tv";
                case "l":
                    return "pool";
                case "m":
                    return "hot tub";
                case "n":
                    return "bbq";
                case "o":
                    return "parking";
                case "p":
                    return "gym";
                case "q":
                    return "waterfront";
                case "r":
                    return "beachfront";
                case "s":
                    return "lake view";
                case "t":
                    return "smoke alarm";
                case "u":
                    return "carbon monoxide alarm";
                case "v":
                    return "security cameras on property";
                case "w":
                    return "pets allowed";
                case "x":
                    return "self check-in";
                default:
                    console.printf("\n%s\n\n", INVALID_INPUT);
                    break;
            }
        }
    }

    /* Get a boolean from user. */
    private static Boolean getBoolean (String prompt) {
        String userInput;
        while(true) {
            console.printf("%s\n", prompt);
            userInput = console.readLine(BOOLEAN_PROMPT).strip();
            if (userInput.equalsIgnoreCase("y")) {
                return Boolean.TRUE;
            } else if (userInput.equalsIgnoreCase("n")) {
                return Boolean.FALSE;
            } else {
                console.printf("\n%s\n\n", INVALID_INPUT);
            }
        }
    }

    /* Get a date from user. */
    private static Date getDate (String prompt) {
        Date userInput;
        while (true) {
            console.printf("%s\n", prompt);
            try {
                userInput = Date.valueOf(console.readLine(DATE_PROMPT).strip());
                return userInput;
            } catch (IllegalArgumentException e) {
                console.printf("\n%s\n\n", INVALID_INPUT);
            }
        }
    }

    /* Get property type from user. */
    private static String getPropertyType () {
        String userInput;
        while (true) {
            userInput = console.readLine(PROPERTY_TYPE_PROMPT).toLowerCase().strip();
            switch (userInput) {
                case "a":
                    return HOUSE;
                case "b":
                    return APARTMENT;
                case "c":
                    return GUESTHOUSE;
                case "d":
                    return HOTEL;
                default:
                    console.printf("\n%s\n\n", INVALID_INPUT);
                    break;
            }
        }
    }

    /* Get longitude from user. */
    private static final Double getLongitude() {
        Double userInput;
        while(true) {
            try {
                userInput = Double.valueOf(console.readLine(LONGITUDE_PROMPT).strip());
                if (userInput >= -180 && userInput < 180)
                    return userInput;
                console.printf("\n%s\n\n", INVALID_INPUT);
            } catch (Exception e) {
                console.printf("\n%s\n\n", INVALID_INPUT);
            }
        }
    }

    /* Get latitude from user. */
    private static final Double getLatitude() {
        Double userInput;
        while(true) {
            try {
                userInput = Double.valueOf(console.readLine(LATTITUDE_PROMPT).strip());
                if (userInput >= -90 && userInput < 90)
                    return userInput;
                console.printf("\n%s\n\n", INVALID_INPUT);
            } catch (Exception e) {
                console.printf("\n%s\n\n", INVALID_INPUT);
            }
        }
    }

    private static final String getCreditNum(){
        String num;
        while(true){
            try{
                num = console.readLine(CREDIT_CARD_NUM).strip();
                if(num.length() == 16){
                    return num;
                }
                console.printf("\n%s\n\n", INVALID_INPUT);
            }catch (Exception e) {
                console.printf("\n%s\n\n", INVALID_INPUT);
            }
        }
    }
    private static final String getCreditDate(){
        String date;
        while(true){
            try{
                date = console.readLine(CREDIT_CARD_DATE).strip();
                if(date.length() == 4){
                    return date;
                }
                console.printf("\n%s\n\n", INVALID_INPUT);
            }catch (Exception e) {
                console.printf("\n%s\n\n", INVALID_INPUT);
            }
        }
    }
    private static final String getCreditCode(){
        String code;
        while(true){
            try{
                code = console.readLine(CREDIT_CARD_CODE).strip();
                if(code.length() == 3){
                    return code;
                }
                console.printf("\n%s\n\n", INVALID_INPUT);
            }catch (Exception e) {
                console.printf("\n%s\n\n", INVALID_INPUT);
            }
        }
    }

    // get renter rating
    private static final int getRenterRating(){
        int rating;
        while(true){
            try{
                rating = Integer.valueOf(console.readLine(RATE_RENTER).strip());
                if(rating >= 1 && rating <= 5){
                    return rating;
                }
                console.printf("\n%s\n\n", INVALID_INPUT);
            }catch (Exception e) {
                console.printf("\n%s\n\n", INVALID_INPUT);
            }
        }
    }

    //get user rating
    private static final int getHostRating(){
        int rating;
        while(true){
            try{
                rating = Integer.valueOf(console.readLine(RATE_HOST).strip());
                if(rating >= 1 && rating <= 5){
                    return rating;
                }
                console.printf("\n%s\n\n", INVALID_INPUT);
            }catch (Exception e) {
                console.printf("\n%s\n\n", INVALID_INPUT);
            }
        }
    }
}