public class RegistrationResponseBody {
    private String feedback;

    public String getFeedback() {
        return feedback;
    }

    public Boolean isRegistrationSuccessful() {
        return feedback.contains("registration success");
    }
}
