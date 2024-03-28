package com.cos.bank.currency.service.impl;

import com.cos.bank.currency.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Value("${openapi.key}")
    private String apiKey;

    @Override
    public void getAllExchangeRates() {

        System.out.println(getLatestRateString());

    }

    // get string from open api
    private String getLatestRateString(){

        // make a request to the Free Currency API to get all the latest exchange rates
        String apiUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=" + apiKey;

        try{
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            BufferedReader bufferedReader;

            if(responseCode==200){
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();

            while((inputLine = bufferedReader.readLine())!=null){
                response.append(inputLine);
            }

            bufferedReader.close();
            return response.toString();

        }catch (Exception e) {
            return "failed to get response";
        }
    }
}
