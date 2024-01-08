package ez_dev.app.note_everything.data.local;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    /**
     * asynchronous DAO queries on background thread
     */
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "note_database").addCallback(CALLBACK).build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback CALLBACK = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            /**
             *  default system database
             * */
            databaseWriteExecutor.execute(() -> {
                NoteDAO noteDAO = INSTANCE.noteDAO();

                noteDAO.insert(
                        new Note("Programming Languages", "Java: Fundamental for Android development.\n" + "Kotlin: An officially supported language, gaining popularity in Android development.")
                );
                noteDAO.insert(
                        new Note("Android Development Tools","Android Studio: The official IDE for Android development.\n" +
                                "Android SDK: Software Development Kit containing essential tools.")
                );
                noteDAO.insert(
                        new Note("User Interface (UI) Design","XML: Language used for designing layouts in Android.\n" +
                                "Material Design: Google's design system for a cohesive UI experience.")
                );
                noteDAO.insert(
                        new Note("Android Components","Activities: UI components representing screens.\n" +
                                "Fragments: Modular UI components within activities.\n" +
                                "Services: Background processes running without a UI.\n" +
                                "Broadcast Receivers: Respond to system-wide events.\n" +
                                "Content Providers: Manage shared app data.")
                );
                noteDAO.insert(
                        new Note("Networking","HTTP: Understanding basic principles.\n" +
                                "Retrofit: Popular library for API calls.\n" +
                                "Volley: Another networking library for Android.")
                );
                noteDAO.insert(
                        new Note("Threading and Background Processing","AsyncTask: Deprecated but important for historical context.\n" +
                                "Executors: For efficient background processing.\n" +
                                "Handlers: Managing threads and message queues.")
                );
                noteDAO.insert(
                        new Note("Android APIs","Location API: Integrating location-based services.\n" +
                                "Camera API: Capturing photos and videos.\n" +
                                "Google Maps API: Integrating maps functionality.")
                );
                noteDAO.insert(
                        new Note("Authentication and Security","OAuth: For user authentication.\n" +
                                "HTTPS: Ensuring secure communication.\n" +
                                "ProGuard: Code obfuscation tool.")
                );

            });
        }
    };

    /**
     * DAO method
     */
    public abstract NoteDAO noteDAO();


}
