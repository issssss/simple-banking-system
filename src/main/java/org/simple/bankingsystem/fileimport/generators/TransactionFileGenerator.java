package org.simple.bankingsystem.fileimport.generators;

import org.simple.bankingsystem.helpers.StringContentEnum;
import org.simple.bankingsystem.helpers.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Random;

@Component
public class TransactionFileGenerator extends FileGenerator{
    @Autowired
    public TransactionFileGenerator(StringHelper stringHelper) {
        super(stringHelper);
    }

    public void generateFile() throws IOException {
        File file = new File("transactions.csv");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write("senderAccountId,receiverAccountId,amount,currencyId,description,timestamp\n"
                .getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < 100000; i++) {
            int senderAccountId = new Random().nextInt(1,20000);
            int receiverAccountId = 0;
            while (receiverAccountId == 0 || receiverAccountId == senderAccountId) {
                receiverAccountId = new Random().nextInt(1,20000);
            }
            double amount = new Random().nextDouble(0, 200000);
            int currencyId = new Random().nextInt(100,500);
            int descriptionLength = new Random().nextInt(2,200);
            String description = stringHelper.randomStringGenerator(descriptionLength, StringContentEnum.FIRST_LETTER_CAPITALIZED);
            int randomMinusDays = new Random().nextInt(500);
            LocalDateTime timestamp = LocalDateTime.now().minusDays(randomMinusDays);
            fos.write((senderAccountId + separator + receiverAccountId + separator + amount + separator + currencyId
                    + separator + description + separator + timestamp + "\n")
                    .getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
    }
}
