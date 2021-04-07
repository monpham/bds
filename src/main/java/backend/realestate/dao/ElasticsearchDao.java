package backend.realestate.dao;

import backend.realestate.configuration.Config;
import backend.realestate.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pilato.elasticsearch.tools.ElasticsearchBeyonder;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.Suggest.Suggestion;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.completion.context.CategoryQueryContext;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Component
public class ElasticsearchDao {
    private final Client esClient;
    private final ObjectMapper mapper;
    private final BulkProcessor bulkProcessor;
    private final Logger logger = LoggerFactory.getLogger(ElasticsearchDao.class);

    public ElasticsearchDao(ObjectMapper mapper) throws IOException {
        String clusterUrl = "https://product-7114141628.ap-southeast-2.bonsaisearch.net:443";
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("n4omcse5ri", "xg0cbolzdp"));
        esClient = new PreBuiltTransportClient(Settings.builder()
                .put("cluster.name", "mycluster1").put("node.name", "My First Node").build()).addTransportAddress(
                new TransportAddress(
                        new InetSocketAddress("68.183.178.106", 9300)));

        this.mapper = mapper;
        this.bulkProcessor = BulkProcessor.builder(esClient, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long l, BulkRequest bulkRequest) {

            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {

            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {

            }
        }).setBulkActions(1000).setFlushInterval(TimeValue.timeValueSeconds(5)).build();
        try {
            ElasticsearchBeyonder.start(esClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(Product product) {
        byte[] bytes = new byte[0];
        String string = "";
        try {
            string = mapper.writeValueAsString(product);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        bulkProcessor.add(new IndexRequest("product", "doc").id(product.idAsString()).source(string, XContentType.JSON));
    }

    public SearchResponse search(QueryBuilder query, Integer from, Integer size) {
        SearchResponse response = null;
        try {
            response = esClient.search(new SearchRequest("product").source(
                    new SearchSourceBuilder().query(query).from(from).size(size))).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//        SearchResponse response2 = esClient.search(new SearchRequest("product").source(
//                new SearchSourceBuilder().query(query).from(from).size(size).suggest(skillNameSuggest)));
//        Suggest suggestions = response2.getSuggest();
        return response;
    }

    public List<TermSuggestion.Entry> getHints(Integer from, Integer size, String term) throws IOException {
        SearchRequest searchRequest = new SearchRequest("product");
        searchRequest.types("text");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SuggestionBuilder termSuggestionBuilder = SuggestBuilders.termSuggestion("tenSanPham").text(term);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("my-suggestion", termSuggestionBuilder);
        searchSourceBuilder.suggest(suggestBuilder);
        SearchResponse response = null;
        try {
            response = esClient.search(new SearchRequest("product").source(
                    new SearchSourceBuilder().from(from).size(size).suggest(suggestBuilder))).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Suggest suggestions = response.getSuggest();
        List entryList = suggestions.getSuggestion("my-suggestion").getEntries();
        return entryList;
    }

    public SearchResponse search1(QueryBuilder query, Integer from, Integer size) throws ExecutionException, InterruptedException, IOException {
        SearchResponse response = esClient.search(new SearchRequest("project").source(
                new SearchSourceBuilder().query(query).from(from).size(size))).get();
        return response;
    }

    public SearchResponse search2(QueryBuilder query, Integer from, Integer size) throws ExecutionException, InterruptedException, IOException {
        SearchResponse response = esClient.search(new SearchRequest("news").source(
                new SearchSourceBuilder().query(query).from(from).size(size))).get();
        return response;
    }
//    public SearchResponse search(QueryBuilder query, Integer from, Integer size) throws ExecutionException, InterruptedException, IOException {
//        logger.debug("elasticsearch query: {}", query.toString());
//        SearchResponse response = esClient.search(new SearchRequest("product")
//                .source(new SearchSourceBuilder()
//                        .query(query)
//                        .from(from)
//                        .size(size)
//                        .trackTotalHits(true)
//                        .sort(SortBuilders.scoreSort())
//                ), RequestOptions.DEFAULT);
//
//        logger.debug("elasticsearch response: {} hits", response.getHits().getTotalHits());
//        logger.trace("elasticsearch response: {} hits", response.toString());
//        return response;
//    }
//    public ElasticsearchDao(ObjectMapper mapper) {
//        this.mapper = mapper;
//        this.esClient = new RestHighLevelClient(
//                RestClient.builder(new HttpHost("https://ajxaypur1l:9priwv4uq4@product-7114141628.ap-southeast-2.bonsaisearch.net", 443, "https")));
//    }

//    public void save(Product product) throws IOException, ExecutionException, InterruptedException, IOException {
//        byte[] bytes = mapper.writeValueAsBytes(product);
//        IndexRequest indexRequest = new IndexRequest("product-9315306240", "doc", product.idAsString());
//        System.out.println(mapper.writeValueAsString(product));
//        IndexRequest request = new IndexRequest("product");
//        request.source(mapper.writeValueAsString(product), XContentType.JSON);
////        highLevelClient.get(indexRequest)
//        highLevelClient.index(request);
//    }

    public void save(Agent agent) throws IOException, ExecutionException, InterruptedException, IOException {
        byte[] bytes = mapper.writeValueAsBytes(agent);
        esClient.index(new IndexRequest("agent", "doc", agent.idAsString()).source(bytes, XContentType.JSON));
    }

    public void save(Project project) throws IOException, ExecutionException, InterruptedException, IOException {
        byte[] bytes = mapper.writeValueAsBytes(project);
        bulkProcessor.add(new IndexRequest("project", "_doc").id(project.idAsString()).source(bytes, XContentType.JSON));
    }

    public void save(News news) throws IOException, ExecutionException, InterruptedException, IOException {
        byte[] bytes = mapper.writeValueAsBytes(news);
        bulkProcessor.add(new IndexRequest("news", "_doc").id(news.idAsString()).source(bytes, XContentType.JSON));
    }

    public void save(Partner partner) throws IOException, ExecutionException, InterruptedException, IOException {
        byte[] bytes = mapper.writeValueAsBytes(partner);
        esClient.index(new IndexRequest("partner", "doc", partner.idAsString()).source(bytes, XContentType.JSON));
    }

    public void save(Category category) throws IOException, ExecutionException, InterruptedException, IOException {
        byte[] bytes = mapper.writeValueAsBytes(category);
        esClient.index(new IndexRequest("category", "doc", category.idAsString()).source(bytes, XContentType.JSON));
    }

    public void save(Image image) throws IOException, ExecutionException, InterruptedException, IOException {
        byte[] bytes = mapper.writeValueAsBytes(image);
        esClient.index(new IndexRequest("image", "doc", image.idAsString()).source(bytes, XContentType.JSON));
    }

    public void delete(Long id) {
        DeleteRequest request = new DeleteRequest("product", "_doc", id.toString());
        try {
            esClient.delete(request).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void delete1(Long id) throws ExecutionException, InterruptedException, IOException {
        DeleteRequest request = new DeleteRequest("project", "_doc", id.toString());
        try {
            esClient.delete(request).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void delete3(Long id) throws ExecutionException, InterruptedException, IOException {
        esClient.delete(new DeleteRequest("agent", "doc", id.toString()));
    }

    public void delete2(Long id) {
        DeleteRequest request = new DeleteRequest("news", "_doc", id.toString());
        try {
            esClient.delete(request).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void delete4(Long id) throws ExecutionException, InterruptedException, IOException {
        esClient.delete(new DeleteRequest("partner", "doc", id.toString()));
    }

    public void delete5(Long id) throws ExecutionException, InterruptedException, IOException {
        esClient.delete(new DeleteRequest("category", "doc", id.toString()));
    }

    public void delete6(Long id) throws ExecutionException, InterruptedException, IOException {
        esClient.delete(new DeleteRequest("image", "doc", id.toString()));
    }


}
