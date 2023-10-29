package hw5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccuweatherTest extends AbstractTest {
    private String locationsURL = getBaseUrl() + "/locations/v1";
    private String forecastsURL = getBaseUrl() + "/forecasts/v1";
    private String currentconditionsURL = getBaseUrl() + "/currentconditions/v1";
    private String indicesURL = getBaseUrl() + "/indices/v1/daily";
    private String countryCode = "LV";
    private String regionCode = "EUR";
    private int group = 50;
    private String city = "Riga";
    private int locationKey = 225780;
    private int ID = 10;

    private static final Logger logger
            = LoggerFactory.getLogger(AccuweatherTest.class);

    @Test
    void test1FindNovadsInAdminAreaList() throws IOException, URISyntaxException {
        logger.info("Test test1FindNovadsInAdminAreaList starts");
        ObjectMapper mapper = new ObjectMapper();
        AdministrativeArea adminarea = new AdministrativeArea();
        adminarea.setLocalizedName("Aglonas Novads");

        logger.debug("Create mock for /locations/v1/adminareas/LV");
        stubFor(get(urlPathEqualTo("/locations/v1/adminareas/LV"))
                .withQueryParam("countryCode", WireMock.equalTo(countryCode))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(adminarea))));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithOnePathParam("countryCode", countryCode,
                locationsURL + "/adminareas/"+countryCode);

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Aglonas Novads",
                mapper.readValue(response.getEntity().getContent(), AdministrativeArea.class).getLocalizedName());

        logger.info("Test test1FindNovadsInAdminAreaList passed");
    }

    @Test
    void test2RegionsContainEurope() throws IOException {

        logger.info("Test test2RegionsContainEurope starts");
        ObjectMapper mapper = new ObjectMapper();
        Location region = new Location();
        region.setLocalizedName("Europe");

        logger.debug("Create mock for /locations/v1/regions");
        stubFor(get(urlPathEqualTo("/locations/v1/regions"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(region))));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithoutPathParam(locationsURL + "/regions");

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Europe",
                mapper.readValue(response.getEntity().getContent(), Location.class).getLocalizedName());

        logger.info("Test test2RegionsContainEurope passed");
    }

    @Test
        void test3ContriesContainLatvia() throws URISyntaxException, IOException {
        logger.info("Test test3ContriesContainLatvia starts");
        ObjectMapper mapper = new ObjectMapper();
        Country country = new Country();
        country.setLocalizedName("Latvia");

        logger.debug("Create mock for /locations/v1/countries/EUR");
        stubFor(get(urlPathEqualTo("/locations/v1/countries/EUR"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(country))));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithOnePathParam("regionCode", regionCode,
                locationsURL + "/countries/" + regionCode);

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Latvia",
                mapper.readValue(response.getEntity().getContent(), Country.class).getLocalizedName());

        logger.info("Test test3ContriesContainLatvia passed");
    }

    @Test
    void test4TopCitiesContainDhaka() throws IOException, URISyntaxException {
        logger.info("Test test4TopCitiesContainDhaka starts");
        ObjectMapper mapper = new ObjectMapper();
        City city = new City();
        city.setLocalizedName("Dhaka");

        logger.debug("Create mock for /locations/v1/topcities/50");
        stubFor(get(urlPathEqualTo("/locations/v1/topcities/50"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(city))));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithOnePathParam( "group", String.valueOf(group),
                locationsURL + "/topcities/"+group);

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Dhaka",
                mapper.readValue(response.getEntity().getContent(), City.class).getLocalizedName());

        logger.info("Test test4TopCitiesContainDhaka passed");
    }

    @Test
    void test5AutocompleteSearch() throws IOException, URISyntaxException {
        logger.info("Test test5AutocompleteSearch starts");
        ObjectMapper mapper = new ObjectMapper();
        City testCity = new City();
        testCity.setLocalizedName(city);

        logger.debug("Create mock for /locations/v1/cities/autocomplete?q=Riga");
        stubFor(get(urlPathEqualTo("/locations/v1/cities/autocomplete?q=Riga"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(testCity))));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithOnePathParam( "q", city,
                locationsURL + "/cities/autocomplete?q="+city);

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Riga",
                mapper.readValue(response.getEntity().getContent(), City.class).getLocalizedName());

        logger.info("Test test5AutocompleteSearch passed");
    }

    @Test
    void test6RigaNeighbor() throws URISyntaxException, IOException {
        logger.info("Test test6RigaNeighbor starts");
        ObjectMapper mapper = new ObjectMapper();
        City testCity = new City();
        testCity.setLocalizedName("Bolderaja");

        logger.debug("Create mock for /locations/v1/cities/neighbors/225780");
        stubFor(get(urlPathEqualTo("/locations/v1/cities/neighbors/225780"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(testCity))));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithOnePathParam("locationKey", String.valueOf(locationKey),
                locationsURL + "/cities/neighbors/"+locationKey);

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Bolderaja",
                mapper.readValue(response.getEntity().getContent(), City.class).getLocalizedName());

        logger.info("Test test6RigaNeighbor passed");
    }

    @Test
    void test7SearchByLocationKey() throws URISyntaxException, IOException {
        logger.info("Test test7SearchByLocationKey starts");
        ObjectMapper mapper = new ObjectMapper();
        City testCity = new City();
        testCity.setLocalizedName(city);

        logger.debug("Create mock for /locations/v1/225780");
        stubFor(get(urlPathEqualTo("/locations/v1/225780"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(testCity))));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithOnePathParam("locationKey", String.valueOf(locationKey),
                locationsURL + "/" +locationKey);

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(city,
                mapper.readValue(response.getEntity().getContent(), City.class).getLocalizedName());

        logger.info("Test test7SearchByLocationKey passed");

    }

    @Test
    void test8DailyForecasts() throws URISyntaxException, IOException {
        logger.info("Test test8DailyForecasts starts");

        logger.debug("Create mock for /forecasts/v1/daily/1day/225780");
        stubFor(get(urlPathEqualTo("/forecasts/v1/daily/1day/225780"))
                .willReturn(aResponse().withStatus(200)));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithOnePathParam("locationKey", String.valueOf(locationKey),
                forecastsURL + "/daily/1day/"+locationKey);

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());

        logger.info("Test test8DailyForecasts passed");

    }

    @Test
    void test9CurrentConditions() throws URISyntaxException, IOException {
        logger.info("Test test9CurrentConditions starts");
        ObjectMapper mapper = new ObjectMapper();
        City testCity = new City();
        testCity.setLocalizedName(city);

        logger.debug("Create mock for /currentconditions/v1/225780");
        stubFor(get(urlPathEqualTo("/currentconditions/v1/225780"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(testCity))));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithOnePathParam("locationKey", String.valueOf(locationKey),
                currentconditionsURL + "/" +locationKey);

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(city,
                mapper.readValue(response.getEntity().getContent(), City.class).getLocalizedName());

        logger.info("Test test9CurrentConditions passed");

    }

    @Test
    void test10CurrentConditionsForTopCities() throws URISyntaxException, IOException {
        logger.info("Test test10CurrentConditionsForTopCities starts");
        ObjectMapper mapper = new ObjectMapper();
        City testCity = new City();
        testCity.setLocalizedName("Dhaka");

        logger.debug("Create mock for /currentconditions/v1/topcities/50");
        stubFor(get(urlPathEqualTo("/currentconditions/v1/topcities/50"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(testCity))));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithOnePathParam("group", String.valueOf(group),
                currentconditionsURL + "/topcities/"+group);

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Dhaka",
                mapper.readValue(response.getEntity().getContent(), City.class).getLocalizedName());

        logger.info("Test test10CurrentConditionsForTopCities passed");

    }

    @Test
    void test11ListOfDailyIndices() throws IOException {
        logger.info("Test test11ListOfDailyIndices starts");
        ObjectMapper mapper = new ObjectMapper();
        Indices testIndices = new Indices();
        testIndices.setTitle("Air Conditioning Index");

        logger.debug("Create mock for /indices/v1/daily");
        stubFor(get(urlPathEqualTo("/indices/v1/daily"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(testIndices))));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithoutPathParam(indicesURL);

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Air Conditioning Index",
                mapper.readValue(response.getEntity().getContent(), Indices.class).getTitle());

        logger.info("Test test11ListOfDailyIndices passed");

    }

    @Test
    void test12CitySearchCountryCode() throws IOException, URISyntaxException {
        logger.info("Test test12CitySearchCountryCode starts");
        ObjectMapper mapper = new ObjectMapper();
        City testCity = new City();
        testCity.setLocalizedName(city);

        logger.debug("Create mock for /locations/v1/cities/LV/search?q=Riga");
        stubFor(get(urlPathEqualTo("/locations/v1/cities/LV/search?q=Riga"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(testCity))));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithTwoPathParam(
                "countryCode", countryCode,
                "q", city,
                locationsURL + "/cities/"+countryCode+"/search?q="+city );

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(city,
                mapper.readValue(response.getEntity().getContent(), City.class).getLocalizedName());

        logger.info("Test test12CitySearchCountryCode passed");
    }

    @Test
    void test13FiveDaysForecasts() throws URISyntaxException, IOException {
        logger.info("Test test13FiveDaysForecasts starts");

        logger.debug("Create mock for /forecasts/v1/daily/5day/225780");
        stubFor(get(urlPathEqualTo("/forecasts/v1/daily/5day/225780"))
                .willReturn(aResponse().withStatus(200)));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithOnePathParam("locationKey", String.valueOf(locationKey),
                forecastsURL + "/daily/5day/" + locationKey);

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());

        logger.info("Test test13FiveDaysForecasts passed");
    }


    @Test
    void test14ListIndexGroups() throws IOException {
        logger.info("Test test14ListIndexGroups starts");
        ObjectMapper mapper = new ObjectMapper();
        Indices testIndices = new Indices();
        testIndices.setTitle("Health API");

        logger.debug("Create mock for /indices/v1/daily/groups");
        stubFor(get(urlPathEqualTo("/indices/v1/daily/groups"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(testIndices))));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithoutPathParam(indicesURL + "/groups");

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Health API",
                mapper.readValue(response.getEntity().getContent(), Indices.class).getTitle());

        logger.info("Test test14ListIndexGroups passed");

    }

    @Test
    void test15ValuesGroupIndices() throws IOException, URISyntaxException {
        logger.info("Test test15ValuesGroupIndices starts");
        ObjectMapper mapper = new ObjectMapper();
        Indices testIndices = new Indices();
        testIndices.setTitle("Healthy Heart Fitness Forecast");

        logger.debug("Create mock for /indices/v1/daily/1day/225780/groups/10");
        stubFor(get(urlPathEqualTo("/indices/v1/daily/1day/225780/groups/10"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(testIndices))));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithTwoPathParam(
                "locationKey", String.valueOf(locationKey),
                "ID", String.valueOf(ID),
                indicesURL + "/1day/"+locationKey+"/groups/"+ID);

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Healthy Heart Fitness Forecast",
                mapper.readValue(response.getEntity().getContent(), Indices.class).getTitle());

        logger.info("Test test15ValuesGroupIndices passed");

    }

    @Test
    void test16ValuesSpecificIndices() throws URISyntaxException, IOException {
        logger.info("Test test16ValuesSpecificIndices starts");
        ObjectMapper mapper = new ObjectMapper();
        Indices testIndices = new Indices();
        testIndices.setTitle("Dust & Dander Forecast");

        logger.debug("Create mock for /indices/v1/daily/groups/10");
        stubFor(get(urlPathEqualTo("/indices/v1/daily/groups/10"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(testIndices))));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithOnePathParam(
                "ID", String.valueOf(ID), indicesURL + "/groups/"+ID);

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Dust & Dander Forecast",
                mapper.readValue(response.getEntity().getContent(), Indices.class).getTitle());

        logger.info("Test test16ValuesSpecificIndices passed");
    }

    @Test
    void test17OneDayValuesSpecificIndices() throws IOException, URISyntaxException {
        logger.info("Test test17OneDayValuesSpecificIndices starts");
        ObjectMapper mapper = new ObjectMapper();
        Indices testIndices = new Indices();
        testIndices.setTitle("Beach & Pool Forecast");

        logger.debug("Create mock for /indices/v1/daily/1day/225780/10");
        stubFor(get(urlPathEqualTo("/indices/v1/daily/1day/225780/10"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(testIndices))));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithTwoPathParam(
                "locationKey", String.valueOf(locationKey),
                "ID", String.valueOf(ID),
                indicesURL + "/1day/"+locationKey+"/"+ID);

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Beach & Pool Forecast",
                mapper.readValue(response.getEntity().getContent(), Indices.class).getTitle());

        logger.info("Test test17OneDayValuesSpecificIndices passed");

    }

    @Test
    void test18MetadataSpecificIndices() throws IOException {
        logger.info("Test test18MetadataSpecificIndices starts");
        String desc = "The AccuWeather.com Beach and Pool Forecast considers numerous weather and water factors " +
                "to help predict the conditions you will encounter at the beach or pool.";
        ObjectMapper mapper = new ObjectMapper();
        Indices testIndices = new Indices();
        testIndices.setDescription(desc);

        logger.debug("Create mock for /indices/v1/daily");
        stubFor(get(urlPathEqualTo("/indices/v1/daily"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(testIndices))));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithoutPathParam(indicesURL);

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(desc,
                mapper.readValue(response.getEntity().getContent(), Indices.class).getDescription());

        logger.info("Test test18MetadataSpecificIndices passed");

    }

    @Test
    void test19OneDayForAllIndices() throws URISyntaxException, IOException {
        logger.info("Test test19OneDayForAllIndices starts");
        ObjectMapper mapper = new ObjectMapper();
        Indices testIndices = new Indices();
        testIndices.setTitle("Indoor Activity Forecast");

        logger.debug("Create mock for /indices/v1/daily/1day/225780");
        stubFor(get(urlPathEqualTo("/indices/v1/daily/1day/225780"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(testIndices))));

        logger.debug("Create http client");
        HttpResponse response = getResponseWithOnePathParam("locationKey", String.valueOf(locationKey),
                indicesURL + "/1day/"+locationKey);

        logger.debug("Start assertions");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Indoor Activity Forecast",
                mapper.readValue(response.getEntity().getContent(), Indices.class).getTitle());

        logger.info("Test test19OneDayForAllIndices passed");

    }

    @Test
    void test20AlertsLocationKey() throws URISyntaxException, IOException {
        logger.info("Test test20AlertsLocationKey starts");

        logger.debug("Create mock for /alerts/v1/225780");
        stubFor(get(urlPathEqualTo("/alerts/v1/225780"))
                .willReturn(aResponse().withStatus(401)));

        logger.debug("Create http client");

        HttpResponse response = getResponseWithOnePathParam("locationKey", String.valueOf(locationKey),
                getBaseUrl()+"/alerts/v1/"+locationKey);

        logger.debug("Start assertions");
        assertEquals(401, response.getStatusLine().getStatusCode());

        logger.info("Test test20AlertsLocationKey passed");

    }

//******************************************************************

    public HttpResponse getResponseWithOnePathParam(
            String paramTitle, String pathParam, String URL) throws URISyntaxException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(URL);
        URI uri = new URIBuilder(request.getURI())
                .addParameter(paramTitle, pathParam)
                .build();
        request.setURI(uri);
        return httpClient.execute(request);
    }
    public HttpResponse getResponseWithoutPathParam(String URL) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(URL);
        return httpClient.execute(request);
    }

    public HttpResponse getResponseWithTwoPathParam(
           String paramTitle1, String pathParam1, String paramTitle2, String pathParam2,String URL) throws IOException, URISyntaxException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(URL);
        URI uri = new URIBuilder(request.getURI())
                .addParameter(paramTitle1, pathParam1)
                .addParameter(paramTitle2, pathParam2)
                .build();
        request.setURI(uri);
        return httpClient.execute(request);
    }
}
