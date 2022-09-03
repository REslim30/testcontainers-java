package org.testcontainers.junit;

import lombok.Cleanup;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.NginxContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.utility.DockerImageName;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.HttpClient;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpGet;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpResponse;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.net.Host;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author richardnorth
 */
public class SimpleNginxTest {

    private static final DockerImageName NGINX_IMAGE = DockerImageName.parse("nginx:1.9.4");

    private static String tmpDirectory = System.getProperty("user.home") + "/.tmp-test-container";

    // creatingContainer {
    public NginxContainer<?> nginx = new NginxContainer<>(NGINX_IMAGE)
        .withCustomContent(tmpDirectory)
        .waitingFor(new HttpWaitStrategy());

    // }

    @SuppressWarnings({ "Duplicates", "ResultOfMethodCallIgnored" })
    @BeforeClass
    public static void setupContent() throws Exception {
        // addCustomContent {
        // Create a temporary dir
        File contentFolder = new File(tmpDirectory);
        contentFolder.mkdir();
        contentFolder.deleteOnExit();

        // And "hello world" HTTP file
        File indexFile = new File(contentFolder, "index.html");
        indexFile.deleteOnExit();
        @Cleanup
        PrintStream printStream = new PrintStream(new FileOutputStream(indexFile));
        printStream.println("<html><body>Hello World!</body></html>");
        // }
    }

    @Test
    public void testSimple() throws Exception {
        // getFromNginxServer {
        URL baseUrl = nginx.getBaseUrl("http", 80);

        assertThat(responseFromNginx(baseUrl))
            .as("An HTTP GET from the Nginx server returns the index.html from the custom content directory")
            .contains("Hello World!");
        // }
    }

    // @Test
    // public void testAidan() {
    //     Network network = Network.newNetwork();
    //     try (
    //         NginxContainer<?> container = new NginxContainer<>(NGINX_IMAGE)
    //             .withCreateContainerCmdModifier(cmd -> {
    //                 HostConfig hostConfig = new HostConfig()
    //                     .withPortBindings(new PortBinding(Ports.Binding.bindPort(80), new ExposedPort(8081)));
    //                 cmd.withHostConfig(hostConfig);
    //             })
    //             .withNetwork(network);
    //     ) {
    //         container.start();

    //         HttpClient httpClient = HttpClientBuilder.create().build();
    //         HttpGet httpGet = new HttpGet("http://localhost:8081/");

    //         try {
    //             HttpResponse res = httpClient.execute(httpGet);
    //             assertThat(res.getCode())
    //                 .isEqualTo(200);
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }

    private static String responseFromNginx(URL baseUrl) throws IOException {
        URLConnection urlConnection = baseUrl.openConnection();
        @Cleanup
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        return reader.readLine();
    }
}
