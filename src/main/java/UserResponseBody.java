import com.google.gson.annotations.SerializedName;

public class UserResponseBody {
    @SerializedName("citizen_id")
    private String citizenId;
    private String name;
    private String surname;
    @SerializedName("birth_date")
    private String birthDate;
    private String occupation;
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("is_risk")
    private String isRisk;
    private String address;
    @SerializedName("vaccine_taken")
    private String vaccineTaken;

    public String getCitizenId() {
        return citizenId;
    }
}
