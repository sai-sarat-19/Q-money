
package com.crio.warmup.stock;

import java.util.*;
import java.util.ArrayList;
import com.crio.warmup.stock.dto.*;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;


public class PortfolioManagerApplication {










  // TODO: CRIO_TASK_MODULE_REST_API
  //  Find out the closing price of each stock on the end_date and return the list
  //  of all symbols in ascending order by its close value on end date.

  // Note:
  // 1. You may have to register on Tiingo to get the api_token.
  // 2. Look at args parameter and the module instructions carefully.
  // 2. You can copy relevant code from #mainReadFile to parse the Json.
  // 3. Use RestTemplate#getForObject in order to call the API,
  //    and deserialize the results in List<Candle>


  

  private static void printJsonObject(Object object) throws IOException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  private static File resolveFileFromResources(String filename) throws URISyntaxException {
    return Paths.get(
        Thread.currentThread().getContextClassLoader().getResource(filename).toURI()).toFile();
  }

  

  public static List<String> mainReadFile(String[] args) throws IOException, URISyntaxException {

    File file = resolveFileFromResources(args[0]);
    ObjectMapper objectMapper= getObjectMapper();
    PortfolioTrade[] trades= objectMapper.readValue(file,PortfolioTrade[].class);
    List<String> symbols=new ArrayList<String>();
    for(PortfolioTrade t:trades){
      symbols.add(t.getSymbol());
    }
     return symbols;
  }



  public static List<TotalReturnsDto> mainReadQuotesHelper(String[] args, 
    List<PortfolioTrade> trades) throws IOException, URISyntaxException {

  RestTemplate restTemplate = new RestTemplate();
  List<TotalReturnsDto> tests = new ArrayList<TotalReturnsDto>();
   for (PortfolioTrade t : trades) {
    String uri = "https://api.tiingo.com/tiingo/daily/" + t.getSymbol() + "/prices?startDate=" 
        + t.getPurchaseDate().toString() + "&endDate=" + args[1]
        + "&token=9643ca045000d02c609e3be2180e06f6e5c46c9e";

    TiingoCandle[] results = restTemplate.getForObject(uri, TiingoCandle[].class);
     if (results != null) {

    tests.add(new TotalReturnsDto (t.getSymbol(), results [results.length-1].getClose()));

}

}

return tests;
}



  public static List<String> mainReadQuotes(String[] args) throws IOException, URISyntaxException {
    ObjectMapper objectMapper = getObjectMapper();
    List<PortfolioTrade> trades = Arrays.asList(objectMapper.readValue(resolveFileFromResources(args[0]), PortfolioTrade[].class));
    List<TotalReturnsDto> sortedByValue = mainReadQuotesHelper(args, trades);
    Collections.sort(sortedByValue, TotalReturnsDto.closingComparator);
    List<String> stocks = new ArrayList<String>();
    for (TotalReturnsDto trd: sortedByValue) {
       stocks.add(trd.getSymbol());
}
    return stocks;

  }

  public static List<String> debugOutputs() {

    String valueOfArgument0 = "trades.json";
    String resultOfResolveFilePathArgs0 = "/home/crio-user/workspace/m-saisarat17-ME_QMONEY_V2/qmoney/src/test/resources/assessments/trades.json";
    String toStringOfObjectMapper = "com.fasterxml.jackson.databind.ObjectMapper@5a9d6f02";
    String functionNameFromTestFileInStackTrace = "PortfolioManagerApplicationTest.mainReadFile()";
    String lineNumberFromTestFileInStackTrace = "24:1";


   return Arrays.asList(new String[]{valueOfArgument0, resultOfResolveFilePathArgs0,
       toStringOfObjectMapper, functionNameFromTestFileInStackTrace,
       lineNumberFromTestFileInStackTrace});
 }


  // TODO:
  //  After refactor, make sure that the tests pass by using these two commands
  //  ./gradlew test --tests PortfolioManagerApplicationTest.readTradesFromJson
  //  ./gradlew test --tests PortfolioManagerApplicationTest.mainReadFile
   public static List<PortfolioTrade> readTradesFromJson(String filename) throws IOException, URISyntaxException {
    ObjectMapper objectMapper = getObjectMapper();
    return  Arrays.asList(objectMapper.readValue(resolveFileFromResources(filename), PortfolioTrade[].class));
  }


  // TODO:
  //  Build the Url using given parameters and use this function in your code to cann the API.
   public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
     //return Collections.emptyList();
     String url = "https://api.tiingo.com/tiingo/daily/" + trade.getSymbol() + "/prices?startDate=" 
     + trade.getPurchaseDate().toString() + "&endDate=" + endDate.toString()
     + "&token=" +token;
    return url;
  }

  
  








  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());


    printJsonObject(mainReadQuotes(args));


  }
}

