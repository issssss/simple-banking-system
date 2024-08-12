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
public class CustomerFileGenerator extends FileGenerator {

    @Autowired
    public CustomerFileGenerator(StringHelper stringHelper) {
        super(stringHelper);
    }
    public void generateFile() throws IOException {
        File file = new File("customers.csv");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write("name,surname,email,address,phone\n".getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < 10000; i++) {
            int nameLength = new Random().nextInt(3,15);
            int surnameLength = new Random().nextInt(5,15);
            String name = stringHelper.randomStringGenerator(nameLength, StringContentEnum.FIRST_LETTER_CAPITALIZED);
            String surname = stringHelper.randomStringGenerator(surnameLength, StringContentEnum.FIRST_LETTER_CAPITALIZED);
            String email = String.format("%s.%s@gmail.com", name, surname);
            String address = stringHelper.randomStringGenerator(nameLength, StringContentEnum.FIRST_LETTER_CAPITALIZED);
            String phone = stringHelper.randomStringGenerator(10, StringContentEnum.NO_LETTERS_ALL_NUMBERS);
            fos.write((name + separator + surname + separator + email + separator + address + separator + phone + "\n")
                    .getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
    }

}
