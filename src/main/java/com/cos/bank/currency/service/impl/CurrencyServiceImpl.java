package com.cos.bank.currency.service.impl;

import com.cos.bank.currency.domain.Currency;
import com.cos.bank.currency.dto.ExchangeRateDto;
import com.cos.bank.currency.dto.ExchangeRateListDto;
import com.cos.bank.currency.repository.CurrencyRepository;
import com.cos.bank.currency.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Value("${openapi.key}")
    private String apiKey;

    @Override
    public ExchangeRateListDto getAllExchangeRates() {
        // get data from db
        List<Currency> currencyList = currencyRepository.findAll();
        // if there is no data, get all latest exchange rates
        if (currencyList.isEmpty()) {
            getLatestExchangeRates(); // api request, parse, save process
            currencyList = currencyRepository.findAll(); // get data from db again
        }
        return ExchangeRateListDto.of(currencyList);
    }

    @Override
    public ExchangeRateDto.Response getExchangeRate(String searchCurrencyCode) {
        Currency currency = currencyRepository.findByCurrencyCode(searchCurrencyCode);
        if (currency == null) {
            getLatestExchangeRates(); // api request, parse, save process
            currency = currencyRepository.findByCurrencyCode(searchCurrencyCode); // get data from db again
        }
        return ExchangeRateDto.Response.of(currency);
    }

    // get all exchange rates for the system
    private void getLatestExchangeRates() {
        // get data from the api as String
        String latestRateString = getLatestRateString();
        // parse String data
        Map<String, BigDecimal> latestExchangeRates = parseLatestRateString(latestRateString);
        // save parsed data
        saveLatestExchangeRates(latestExchangeRates);
    }

    // save parsed data as an entity
    private void saveLatestExchangeRates(Map<String, BigDecimal> latestExchangeRates) {
        // get values to save
        for (Map.Entry<String, BigDecimal> entry : latestExchangeRates.entrySet()) {
            String currencyCode = entry.getKey();
            BigDecimal exchangeRate = (BigDecimal) entry.getValue();
            // make currency
            Currency currency = Currency.builder()
                    .currencyCode(currencyCode)
                    .exchangeRate(exchangeRate)
                    .build();
            // save it to db
            currencyRepository.save(currency);
        }
    }

    // - make a request to the Free Currency API to get all the latest exchange rates (get jsonString data)
    private String getLatestRateString() {
        // url to get the latest exchange rates
        String baseUrl = "https://api.freecurrencyapi.com/v1/latest?";
        String baseCurrency = "CAD";

        String apiKeyQueryParam = "apikey=" + apiKey;
        String baseCurrencyQueryParam = "&base_currency=" + baseCurrency;

        String apiUrl = baseUrl + apiKeyQueryParam + baseCurrencyQueryParam;

        try {
            // make the URL for the http request
            URL url = new URL(apiUrl);
            // http request
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // get response of the http request
            int responseCode = connection.getResponseCode();

            // needs BufferedReader to read response
            BufferedReader bufferedReader;
            if (responseCode == 200) {
                // success - BufferedReader reads from the connection's input stream
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                // fail - BufferedReader reads from the connection's error stream
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            // to read each line and make it as string
            String inputLine;
            StringBuilder response = new StringBuilder();

            // reads each line of the response and appends it to the StringBuilder until there are no more lines
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            // close bufferedReader
            bufferedReader.close();
            // return data as String
            System.out.println(response.toString());
            return response.toString();
        } catch (Exception e) {
            return "failed to get response";
        }
    }

    private Map<String, BigDecimal> parseLatestRateString(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            // parse JSON strings into JSON objects
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // get the data object from the JSON response
        JSONObject dataObject = (JSONObject) jsonObject.get("data");

        // dataObject holds currency code and exchange rates (key: currency code)
        Map<String, BigDecimal> resultMap = new HashMap<>();

        // iterate over each key-value pair in the dataObject
        for (Object key : dataObject.keySet()) {
            String currencyCode = (String) key;
            // parse exchange rate as BigDecimal
            BigDecimal exchangeRate = new BigDecimal(dataObject.get(currencyCode).toString());
            resultMap.put(currencyCode, exchangeRate);
        }
        return resultMap;
    }
}
