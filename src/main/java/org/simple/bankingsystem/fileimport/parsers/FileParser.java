package org.simple.bankingsystem.fileimport.parsers;

import org.simple.bankingsystem.SimpleBankingSystemApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class FileParser {

    final int chunk_size = 500;
    final String separator = ",";
    final Logger log = LoggerFactory.getLogger(SimpleBankingSystemApplication.class);

    public void parseFile(String fileName) throws IOException
    {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            List<String> lines = new ArrayList<>();
            String line = br.readLine(); // skip first line which is the header

            while ((line = br.readLine()) != null) {
                lines.add(line);
                if (lines.size() >= chunk_size){
                    mapAndSave(new ArrayList<>(lines));
                    lines.clear();
                }
            }

            if (!lines.isEmpty()) {
                mapAndSave(new ArrayList<>(lines));
            }
        }
    }

    @Async
    abstract CompletableFuture<Void> mapAndSave(List<String> lines);
}
