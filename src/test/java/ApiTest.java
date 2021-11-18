import com.google.gson.Gson;
import okhttp3.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test cases for World Class Government API
 *
 * @Author Tatpol Samakpong
 */
public class ApiTest {
    /**
     * Base url of World Class Government API
     */
    private final String baseUrl = "https://wcg-apis.herokuapp.com";

    /**
     * JSON Media Type
     */
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * Empty JSON request body
     */
    private final RequestBody emptyRequestBody = RequestBody.create("", JSON);

    /**
     * Http Client
     */
    private OkHttpClient client;

    /**
     * Initialize http client instance
     */
    @Before
    public void setUp() {
        client = new OkHttpClient();
    }

    public Boolean isUserExisted(String citizenId) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/registration/" + citizenId)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                UserResponseBody responseBody = new Gson()
                        .fromJson(response.body().string(), UserResponseBody.class);
                return responseBody.getCitizenId().equals(citizenId);
            }
            return false;
        }
    }

    public void deleteUserByCitizenId(String citizenId) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/registration/" + citizenId )
                .delete()
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Delete user request failed");
        }
    }

    /**
     * Test get reservations to check status code and content type
     *
     * @throws IOException if an error occurred during request execution
     */
    @Test
    public void testGetReservations() throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/reservations")
                .build();

        try (Response response = client.newCall(request).execute()) {
            Headers headers = response.headers();
            assertEquals(response.code(), 200);
            assertEquals(headers.get("Content-Type"), "application/json");
        }
    }

    /**
     * Test registration with all require fields
     * (This test will pass when user is not registered yet)
     *
     * @throws IOException if an error occurred during request execution
     */
    @Test
    public void testRegistrationWithQueryParams() throws IOException {
        String citizenId = "1102003283576";

        if (isUserExisted(citizenId)) {
            deleteUserByCitizenId(citizenId);
        }

        HttpUrl url = new UserRegistrationUrl.Builder()
                .citizenId(citizenId)
                .name("Tatpol")
                .surname("Samakpong")
                .birthDate("2001/06/05")
                .occupation("Student")
                .phoneNumber("0970638897")
                .isRisk(false)
                .address("122/167")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(emptyRequestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            RegistrationResponseBody responseBody = new Gson()
                    .fromJson(response.body().string(), RegistrationResponseBody.class);
            assertEquals(response.code(), 201);
            assertEquals(responseBody.getFeedback(), "registration success!");
        }
    }

    @Test
    public void testRegistrationWithFormBody() throws IOException {
        String citizenId = "1102003283576";

        if (isUserExisted(citizenId)) {
            deleteUserByCitizenId(citizenId);
        }

        RequestBody body = new FormBody.Builder()
                .add("name", "Tatpol")
                .add("surname", "Samakpong")
                .add("citizen_id", citizenId)
                .add("birth_date", "2001/06/05")
                .add("occupation", "Student")
                .add("phone_number", "0970638897")
                .add("is_risk", "false")
                .add("address", "122/167")
                .build();

        Request request = new Request.Builder()
                .url(baseUrl + "/registration")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            RegistrationResponseBody responseBody = new Gson()
                    .fromJson(response.body().string(), RegistrationResponseBody.class);
            assertEquals(response.code(), 201);
            assertEquals(responseBody.getFeedback(), "registration success!");
        }
    }

    /**
     * Test registration with the user that already registered
     *
     * @throws IOException if an error occurred during request execution
     */
    @Test
    public void testRegistrationWithExistingUser() throws IOException {
        HttpUrl url = new UserRegistrationUrl.Builder()
                .citizenId("1102003283576")
                .name("Tatpol")
                .surname("Samakpong")
                .birthDate("2001/06/05")
                .occupation("Student")
                .phoneNumber("0970638897")
                .isRisk(false)
                .address("122/167")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(emptyRequestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            RegistrationResponseBody responseBody = new Gson()
                    .fromJson(response.body().string(), RegistrationResponseBody.class);
            assertEquals(response.code(), 200);
            assertEquals(
                    responseBody.getFeedback(),
                    "registration failed: this person already registered"
            );
        }
    }

    /**
     * Test registration with missing required citizen id field
     *
     * @throws IOException if an error occurred during request execution
     */
    @Test
    public void testRegistrationWithNoCitizenId() throws IOException {
        HttpUrl url = new UserRegistrationUrl.Builder()
                // missing citizen id field
                .name("Tatpol")
                .surname("Samakpong")
                .birthDate("2001/06/05")
                .occupation("Student")
                .address("122/167")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(emptyRequestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(response.code(), 400);
        }
    }

    /**
     * Test registration with missing required name field
     *
     * @throws IOException if an error occurred during request execution
     */
    @Test
    public void testRegistrationWithNoName() throws IOException {
        HttpUrl url = new UserRegistrationUrl.Builder()
                .citizenId("1102003283576")
                // missing name field
                .surname("Samakpong")
                .birthDate("2001/06/05")
                .occupation("Student")
                .address("122/167")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(emptyRequestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(response.code(), 400);
        }
    }

    /**
     * Test registration with missing required surname id field
     *
     * @throws IOException if an error occurred during request execution
     */
    @Test
    public void testRegistrationWithNoSurname() throws IOException {
        HttpUrl url = new UserRegistrationUrl.Builder()
                .citizenId("1102003283576")
                .name("Tatpol")
                // missing surname field
                .birthDate("2001/06/05")
                .occupation("Student")
                .address("122/167")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(emptyRequestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(response.code(), 400);
        }
    }

    /**
     * Test registration with missing required birth-date field
     *
     * @throws IOException if an error occurred during request execution
     */
    @Test
    public void testRegistrationWithNoBirthDate() throws IOException {
        HttpUrl url = new UserRegistrationUrl.Builder()
                .citizenId("1102003283576")
                .name("Tatpol")
                .surname("Samakpong")
                // missing birth date field
                .occupation("Student")
                .address("122/167")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(emptyRequestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(response.code(), 400);
        }
    }

    /**
     * Test registration with missing required occupation field
     *
     * @throws IOException if an error occurred during request execution
     */
    @Test
    public void testRegistrationWithNoOccupation() throws IOException {
        HttpUrl url = new UserRegistrationUrl.Builder()
                .citizenId("1102003283576")
                .name("Tatpol")
                .surname("Samakpong")
                .birthDate("2001/06/05")
                // missing occupation field
                .address("122/167")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(emptyRequestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(response.code(), 400);
        }
    }

    /**
     * Test registration with missing required address field
     *
     * @throws IOException if an error occurred during request execution
     */
    @Test
    public void testRegistrationWithNoAddress() throws IOException {
        HttpUrl url = new UserRegistrationUrl.Builder()
                .citizenId("1102003283576")
                .name("Tatpol")
                .surname("Samakpong")
                .birthDate("2001/06/05")
                .occupation("Student")
                // missing address field
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(emptyRequestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(response.code(), 400);
        }
    }

    /**
     * Test registration with numeric name
     *
     * @throws IOException if an error occurred during request execution
     */
    @Test
    public void testRegistrationWithNumericName() throws IOException {
        HttpUrl url = new UserRegistrationUrl.Builder()
                .citizenId("1102003283576")
                .name("213432994")
                .surname("Samakpong")
                .birthDate("2001/06/05")
                .occupation("Student")
                .address("Thailand")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(emptyRequestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            // registration should be failed and status code shouldn't be 200
            assertNotEquals(response.code(), 200);
        }
    }

    /**
     * Test registration with invalid citizen id
     *
     * @throws IOException if an error occurred during request execution
     */
    @Test
    public void testRegistrationWithInvalidCitizenId() throws IOException {
        HttpUrl url = new UserRegistrationUrl.Builder()
                .citizenId("okafvsapk34pmb0zed")
                .name("213432994")
                .surname("Samakpong")
                .birthDate("2001/06/05")
                .occupation("Student")
                .address("Thailand")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(emptyRequestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            // registration should be failed and status code shouldn't be 200
            assertNotEquals(response.code(), 200);
        }
    }

    /**
     * Test registration with incorrect format of birth-date
     *
     * @throws IOException if an error occurred during request execution
     */
    @Test
    public void testRegistrationMisFormatBirthDate() throws IOException {
        HttpUrl url = new UserRegistrationUrl.Builder()
                .citizenId("1102003283576")
                .name("Hello")
                .surname("Guy")
                .birthDate("1996/30/03")
                .occupation("Student")
                .address("Thailand")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(emptyRequestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            // registration should be failed and status code shouldn't be 200
            assertNotEquals(response.code(), 200);
        }
    }
}
