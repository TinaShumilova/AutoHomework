package hw3;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ApiTest extends AbstractTest {
    private String apikey = getApiKey();
    private String locationsURL = getBaseUrl() + "/locations/v1";
    private String forecastsURL = getBaseUrl() + "/forecasts/v1";
    private String currentconditionsURL = getBaseUrl() + "/currentconditions/v1";
    private String indicesURL = getBaseUrl() + "/indices/v1/daily";
    private String countryCode = "LV";
    private int group = 50;
    private String regionCode = "EUR";
    private String city = "Riga";
    private int locationKey = 225780;
    private int ID = 10;


    @Test
    void test1FindNovadsInAdminAreaList() {
        JsonPath response = getResponseWithOnePathParam(apikey, "countryCode", countryCode,
                locationsURL + "/adminareas/{countryCode}");

        assertThat(response.get("[0].LocalizedName"), equalTo("Aglonas novads"));
    }

    @Test
    void test3RegionsContainEurope() {
        JsonPath response = getResponseWithoutPathParam(apikey, locationsURL + "/regions");

        assertThat(response.get("[5].LocalizedName"), equalTo("Europe"));
    }

    @Test
    void test3ContriesContainLatvia() {
        JsonPath response = getResponseWithOnePathParam(apikey, "regionCode", regionCode,
                locationsURL + "/countries/{regionCode}");

        assertThat(response.get("[30].LocalizedName"), equalTo("Latvia"));
    }

    @Test
    void test4TopCitiesContainDhaka() {
        JsonPath response = getResponseWithOnePathParam(apikey, "group", String.valueOf(group),
                locationsURL + "/topcities/{group}");

        assertThat(response.get("[0].EnglishName"), equalTo("Dhaka"));
    }

    @Test
    void test5AutocompleteSearch() {
        JsonPath response = getResponseWithOnePathParam(apikey, "q", city,
                locationsURL + "/cities/autocomplete?q={q}");

        assertThat(response.get("[0].LocalizedName"), equalTo("Riga"));
    }

    @Test
    void test6RigaNeighbor() {
        JsonPath response = getResponseWithOnePathParam(apikey, "locationKey", String.valueOf(locationKey),
                locationsURL + "/cities/neighbors/{locationKey}");

        assertThat(response.get("[0].LocalizedName"), equalTo("Bolderaja"));
    }

    @Test
    void test7SearchByLocationKey() {
        JsonPath response = getResponseWithOnePathParam(apikey, "locationKey", String.valueOf(locationKey),
                locationsURL + "/{locationKey}");

        assertThat(response.get("LocalizedName"), equalTo(city));
    }

    @Test
    void test8DailyForecasts() {
        given()
                .queryParam("apikey", getApiKey())
                .pathParam("locationKey", locationKey)
                .when()
                .get(forecastsURL + "/daily/1day/{locationKey}")
                .then()
                .statusCode(200);
    }

    @Test
    void test9CurrentConditions() {
        JsonPath response = getResponseWithOnePathParam(apikey, "locationKey", String.valueOf(locationKey),
                currentconditionsURL + "/{locationKey}");

        assertThat(
                response.get("[0].Link"),
                equalTo("http://www.accuweather.com/en/lv/riga/225780/current-weather/225780?lang=en-us"));
    }

    @Test
    void test10CurrentConditionsForTopCities() {
        JsonPath response =
                getResponseWithOnePathParam(apikey, "group", String.valueOf(group),
                        currentconditionsURL + "/topcities/{group}");

        assertThat(response.get("[0].LocalizedName"), equalTo("Dhaka"));
    }

    @Test
    void test11ListOfDailyIndices() {
        JsonPath response = given()
                .queryParam("apikey", getApiKey())
                .when()
                .get(indicesURL)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();

        assertThat(response.get("[0].Name"), equalTo("Air Conditioning Index"));
    }

    @Test
    void test12CitySearchCountryCode() {
        JsonPath response = getResponseWithTwoPathParam(
                apikey,
                "countryCode", countryCode,
                "q", city,
                locationsURL + "/cities/{countryCode}/search?q={q}");

        assertThat(response.get("[0].LocalizedName"), equalTo("Riga"));
    }

    @Test
    void test13FiveDaysForecasts() {
        given()
                .queryParam("apikey", getApiKey())
                .pathParam("locationKey", locationKey)
                .when()
                .get(forecastsURL + "/daily/5day/{locationKey}")
                .then()
                .statusCode(200);
    }

    @Test
    void test14ListIndexGroups() {
        JsonPath response = getResponseWithoutPathParam(apikey, indicesURL + "/groups");

        assertThat(response.get("[9].Name"), equalTo("Health API"));
    }

    @Test
    void test15ValuesGroupIndices() {
        JsonPath response = getResponseWithTwoPathParam(
                apikey,
                "locationKey", String.valueOf(locationKey),
                "ID", String.valueOf(ID),
                indicesURL + "/1day/{locationKey}/groups/{ID}");

        assertThat(response.get("[0].Name"), equalTo("Healthy Heart Fitness Forecast"));
    }

    @Test
    void test16ValuesSpecificIndices() {
        JsonPath response =
                getResponseWithOnePathParam(apikey, "ID", String.valueOf(ID), indicesURL + "/groups/{ID}");

        assertThat(response.get("[3].Name"), equalTo("Dust & Dander Forecast"));
    }

    @Test
    void test17OneDayValuesSpecificIndices() {
        JsonPath response = getResponseWithTwoPathParam(
                apikey,
                "locationKey", String.valueOf(locationKey),
                "ID", String.valueOf(ID),
                indicesURL + "/1day/{locationKey}/{ID}"  );

        assertThat(response.get("[0].Name"), equalTo("Beach & Pool Forecast"));
    }

    @Test
    void test18MetadataSpecificIndices() {
        JsonPath response = getResponseWithoutPathParam(apikey, indicesURL);

        assertThat(response.get("[10].Description"), equalTo("The AccuWeather.com Beach and Pool Forecast " +
                "considers numerous weather and water factors to help predict the conditions you will encounter at the" +
                " beach or pool."));
    }

    @Test
    void test19OneDayForAllIndices() {
        String URL = indicesURL + "/1day/{locationKey}";
        JsonPath response = getResponseWithOnePathParam(apikey, "locationKey", String.valueOf(locationKey), URL);

        assertThat(response.get("[1].Name"), equalTo("Indoor Activity Forecast"));
    }

    @Test
    void test20AlertsLocationKey() {
        given()
                .queryParam("apikey", getApiKey())
                .pathParam("locationKey", locationKey)
                .when()
                .get(getBaseUrl() + "/alerts/v1/{locationKey}")
                .then()
                .statusCode(401);
    }

//******************************************************************
    public JsonPath getResponseWithoutPathParam(String apikey, String URL) {
        JsonPath response = given()
                .queryParam("apikey", apikey)
                .when()
                .get(URL)
                .body()
                .jsonPath();
        return response;
    }

    public JsonPath getResponseWithOnePathParam(String apikey, String paramTitle, String pathParam, String URL) {
        JsonPath response = given()
                .queryParam("apikey", apikey)
                .pathParam(paramTitle, pathParam)
                .when()
                .get(URL)
                .body()
                .jsonPath();
        return response;
    }
    public JsonPath getResponseWithTwoPathParam(
            String apikey, String paramTitle1, String pathParam1, String paramTitle2, String pathParam2,String URL) {
        JsonPath response = given()
                .queryParam("apikey", apikey)
                .pathParam(paramTitle1, pathParam1)
                .pathParam(paramTitle2, pathParam2)
                .when()
                .get(URL)
                .body()
                .jsonPath();
        return response;
    }

}