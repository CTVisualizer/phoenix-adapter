package com.proofpoint.ctvisualizer.external;

import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JarExtractorTest  {

    @Test
    @Disabled
    public void testJarExtractor() throws FileNotFoundException, IOException {
        Path compressedFile = Paths.get(".", "apache-phoenix-4.13.2-cdh5.11.2-bin.tar.gz");
        Path targetFile = Paths.get(".","phoenix-client.jar");

        TarArchiveInputStream tar = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(compressedFile.toFile())));
        while(!tar.getNextTarEntry().getName().endsWith("client.jar")) {
            tar.getNextTarEntry();
        }
        FileUtils.copyInputStreamToFile(tar, targetFile.toFile());
    }


}
