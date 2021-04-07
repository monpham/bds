package backend.realestate;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.net.InetAddress;

@SpringBootApplication(exclude = {ElasticsearchDataAutoConfiguration.class, ElasticsearchRestClientAutoConfiguration.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    public Client client() throws Exception {
////        File tempFile = File.createTempFile("temp-elastic", Long.toString(System.nanoTime()));
////        Settings esSettings = Settings.builder()
////                .put("http.enabled", "true")
//////                .put("index.number_of_shards", "1")
////                .put("path.data", new File(tempFile, "data").getAbsolutePath())
////                .put("path.logs", new File(tempFile, "logs").getAbsolutePath())
////                .put("path.home", tempFile.getAbsolutePath())
////                .put("cluster.name", "elasticsearch")
////                .build();
////        TransportClient transportClient = new PreBuiltTransportClient(esSettings);
////        Client client = transportClient.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));//just masking ip with xxx for SO Question
////        System.out.println("tesing");
//        Settings settings = Settings.builder()
//                .put("cluster.name", "myapplication").build();
//        TransportClient client = new PreBuiltTransportClient(settings)
//                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
////        TransportClient client = new PreBuiltTransportClient(esSettings);
////        client.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
//        return client;
//    }
//
//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
//        return new ElasticsearchTemplate(client());
//    }

}
