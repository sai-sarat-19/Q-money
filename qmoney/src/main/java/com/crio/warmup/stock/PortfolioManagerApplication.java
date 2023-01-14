
package com.crio.warmup.stock;

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

  // TODO:
  //  After refactor, make sure that the tests pass by using these two commands
  //  ./gradlew test --tests PortfolioManagerApplicationTest.readTradesFromJson
  //  ./gradlew test --tests PortfolioManagerApplicationTest.mainReadFile
  public static List<PortfolioTrade> readTradesFromJson(String filename) throws IOException, URISyntaxException {
     return Collections.emptyList();
  }


  // TODO:
  //  Build the Url using given parameters and use this function in your code to cann the API.
  public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
     return Collections.emptyList();
  }

  public static final Comparator<TotalReturnsDto> closingComparator = new
  Comparator<TotalReturnsDto>() {
public int compare(TotalReturnsDto t1 , TotalReturnsDto t2){
    return (int) (t1.getClosingPrice().compareTo(t2.getClosingPrice()));
    }
  };








  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());


    printJsonObject(mainReadQuotes(args));


  }
}

