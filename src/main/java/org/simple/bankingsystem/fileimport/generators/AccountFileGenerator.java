package org.simple.bankingsystem.fileimport.generators;

import org.simple.bankingsystem.helpers.StringContentEnum;
import org.simple.bankingsystem.helpers.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Component
public class AccountFileGenerator extends FileGenerator {

    @Autowired
    public AccountFileGenerator(StringHelper stringHelper) {
        super(stringHelper);
    }
    @Override
    public void generateFile() throws IOException {
        File file = new File("accounts.csv");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write("customerId,accountNumber,accountType,balance,pastMonthTurnover\n".getBytes(StandardCharsets.UTF_8));
        int customerId = 1;
        for (int i = 0; i < 20000; i++) {
            if (i > 0 && i % 2 == 0) {
                customerId   += 1;
            }
            int accountNumberLength = 20;
            int accountTypeLength = 3;
            String accountNumber = stringHelper.randomStringGenerator(accountNumberLength, StringContentEnum.FIRST_LETTER_CAPITALIZED);
            String accountType = stringHelper.randomStringGenerator(accountTypeLength, StringContentEnum.ALL_LETTERS_CAPITALIZED);
            double balance = new Random().nextDouble(0, 2000000);
            double pastMonthTurnover = new Random().nextDouble(0, 500000);
            fos.write((customerId + separator + accountNumber + separator + accountType + separator + balance
                    + separator + pastMonthTurnover +"\n")
                    .getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
    }
}
