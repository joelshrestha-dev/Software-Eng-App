// Core Google API client classes
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;

// Calendar-specific classes
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;

// Utilities
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class Main {
    private static final String APPLICATION_NAME = "My Calendar App";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "client_secret.json"; // Put your file here

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        // 1. Setup the HTTP transport
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // 2. Load credentials from file
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH))
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/calendar.readonly"));

        // 3. Build the Calendar service
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // 4. List the calendars
        CalendarList calendarList = service.calendarList().list().execute();
        List<CalendarListEntry> items = calendarList.getItems();

        System.out.println("Your calendars:");
        for (CalendarListEntry calendar : items) {
            System.out.println("- " + calendar.getSummary());
        }
    }
}
