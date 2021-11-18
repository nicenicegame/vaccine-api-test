import okhttp3.HttpUrl;

public class UserRegistrationUrl {

    public static class Builder {
        private final HttpUrl.Builder urlBuilder;

        public Builder() {
            urlBuilder = new HttpUrl.Builder()
                    .scheme("https")
                    .host("wcg-apis.herokuapp.com")
                    .addPathSegment("registration");
        }

        public Builder citizenId(String citizenId) {
            urlBuilder.addQueryParameter("citizen_id", citizenId);
            return this;
        }

        public Builder name(String name) {
            urlBuilder.addQueryParameter("name", name);
            return this;
        }

        public Builder surname(String surname) {
            urlBuilder.addQueryParameter("surname", surname);
            return this;
        }

        public Builder birthDate(String birthDate) {
            urlBuilder.addQueryParameter("birth_date", birthDate);
            return this;
        }

        public Builder occupation(String occupation) {
            urlBuilder.addQueryParameter("occupation", occupation);
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            urlBuilder.addQueryParameter("phone_number", phoneNumber);
            return this;
        }

        public Builder isRisk(Boolean isRisk) {
            urlBuilder.addQueryParameter("is_risk", isRisk.toString());
            return this;
        }

        public Builder address(String address) {
            urlBuilder.addQueryParameter("address", address);
            return this;
        }

        public HttpUrl build() {
            return urlBuilder.build();
        }
    }
}
