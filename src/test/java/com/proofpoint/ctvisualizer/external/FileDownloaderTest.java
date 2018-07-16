package com.proofpoint.ctvisualizer.external;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDownloaderTest {

    @Test @Ignore
    public void testFileDownloader() throws MalformedURLException, IOException {
        String url = "http://archive.apache.org/dist/phoenix/apache-phoenix-4.13.2-cdh5.11.2/bin/apache-phoenix-4.13.2-cdh5.11.2-bin.tar.gz";
        Path target = Paths.get(".", "apache-phoenix-4.13.2-cdh5.11.2-bin.tar.gz");
        FileUtils.copyURLToFile(new URL(url), target.toFile(), 1000, 1000);
    }
}
