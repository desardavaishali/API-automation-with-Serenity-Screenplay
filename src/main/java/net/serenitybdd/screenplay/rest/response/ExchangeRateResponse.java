package net.serenitybdd.screenplay.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

    public class ExchangeRateResponse {
        private String result;
        private String provider;
        private String documentation;
        private String termsOfUse;
        private long timeLastUpdateUnix;
        private String timeLastUpdateUtc;
        private long timeNextUpdateUnix;
        private String timeNextUpdateUtc;
        private long timeEolUnix;
        private String baseCode;
        private Map<String, Double> rates;

        // Getters and setters for the fields
        // Make sure to add annotations like @JsonProperty if the JSON field names differ from the Java field names

        @JsonProperty("result")
        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        @JsonProperty("provider")
        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        @JsonProperty("documentation")
        public String getDocumentation() {
            return documentation;
        }

        public void setDocumentation(String documentation) {
            this.documentation = documentation;
        }

        @JsonProperty("terms_of_use")
        public String getTermsOfUse() {
            return termsOfUse;
        }

        public void setTermsOfUse(String termsOfUse) {
            this.termsOfUse = termsOfUse;
        }

        @JsonProperty("time_last_update_unix")
        public long getTimeLastUpdateUnix() {
            return timeLastUpdateUnix;
        }

        public void setTimeLastUpdateUnix(long timeLastUpdateUnix) {
            this.timeLastUpdateUnix = timeLastUpdateUnix;
        }

        @JsonProperty("time_last_update_utc")
        public String getTimeLastUpdateUtc() {
            return timeLastUpdateUtc;
        }

        public void setTimeLastUpdateUtc(String timeLastUpdateUtc) {
            this.timeLastUpdateUtc = timeLastUpdateUtc;
        }

        @JsonProperty("time_next_update_unix")
        public long getTimeNextUpdateUnix() {
            return timeNextUpdateUnix;
        }

        public void setTimeNextUpdateUnix(long timeNextUpdateUnix) {
            this.timeNextUpdateUnix = timeNextUpdateUnix;
        }

        @JsonProperty("time_next_update_utc")
        public String getTimeNextUpdateUtc() {
            return timeNextUpdateUtc;
        }

        public void setTimeNextUpdateUtc(String timeNextUpdateUtc) {
            this.timeNextUpdateUtc = timeNextUpdateUtc;
        }

        @JsonProperty("time_eol_unix")
        public long getTimeEolUnix() {
            return timeEolUnix;
        }

        public void setTimeEolUnix(long timeEolUnix) {
            this.timeEolUnix = timeEolUnix;
        }

        @JsonProperty("base_code")
        public String getBaseCode() {
            return baseCode;
        }

        public void setBaseCode(String baseCode) {
            this.baseCode = baseCode;
        }

        @JsonProperty("rates")
        public Map<String, Double> getRates() {
            return rates;
        }

        public void setRates(Map<String, Double> rates) {
            this.rates = rates;
        }
    }


