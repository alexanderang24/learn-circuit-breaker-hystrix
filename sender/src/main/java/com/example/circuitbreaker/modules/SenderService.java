package com.example.circuitbreaker.modules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Future;

@Service
@Slf4j
public class SenderService {

    @Async
    public Future<String> getResult() {
        log.info("Get Result");
        String hasil = "empty";
        String line;
        try {
            URL url = new URL("http://localhost:8080/receiver");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((line = rd.readLine()) != null) {
                if (hasil.equals("empty")) {
                    hasil = line;
                } else {
                    hasil = hasil + line;
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        log.info(hasil);

        if("Circuit broken".equals(hasil)) {
            throw new RuntimeException("Failed!");
        }

        return new AsyncResult<>(hasil);
    }

}
