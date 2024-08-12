package org.simple.bankingsystem.fileimport.generators;

import org.simple.bankingsystem.helpers.StringHelper;

import java.io.IOException;

public abstract class FileGenerator {
    final String separator = ",";
    final StringHelper stringHelper;

    protected FileGenerator(StringHelper stringHelper) {
        this.stringHelper = stringHelper;
    }

    public abstract void generateFile() throws IOException;
}
