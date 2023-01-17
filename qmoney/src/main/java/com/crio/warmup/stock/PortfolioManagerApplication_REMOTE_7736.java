
package com.crio.warmup.stock;

import java.util.*;
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
import java.time.format.DateTimeParseException;
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


public class PortfolioManagerApplication_REMOTE_7736 {




  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Now that you have the list of PortfolioTrade and their data, calculate annualized returns
  //  for the stocks provided in the Json.
  //  Use the function you just wrote #calculateAnnualizedReturns.
  //  Return the list of AnnualizedReturns sorted by annualizedReturns in descending order.

  // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.




  // TODO
  //  Ensure all tests are passing using below command
  //  ./gradlew test --tests ModuleThreeRefactorTest
  private static void printJsonObject(Object object) throws IOException {
   Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
   ObjectMapper mapper = new ObjectMapper();
   logger.info(mapper.writeValueAsString(object));
 }

  private static File resolveFileFromResources(String filename) throws URISyntaxException {
   return Paths.get(
       Thread.currentThread().getContextClassLoader().getResource(filename).toURI()).toFile();
 }

  private static ObjectMapper getObjectMapper() {
   ObjectMapper objectMapper = new ObjectMapper();
   objectMapper.registerModule(new JavaTimeModule());
   return objectMapper;
 }
 public static final String TOKEN = "126e087ce5700d1db8c9a09e6d530a170cf43ee6";


static Double getOpeningPriceOnStartDate(List<Candle> candles) {
   //return 0.0;
     
       Candle first = candles.get(0);
       return first.getopen();
}

public static Double getClosingPriceOnEndDate(List<Candle> candles) {
   return 0.0;
}

public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) { 
   return Collections.emptyList();
}

public static AnnualizedReturn getAnnualizedReturn(PortfolioTrade trade, LocalDate endLocalDate) {
      String ticker = trade.getSymbol();
      LocalDate startLocalDate = trade.getPurchaseDate();
      if (startLocalDate.compareTo(endLocalDate) >= 0) { 
         throw new RuntimeException();} 
         // create a url object for the api call
         // TOKEN is a class variable 
         String url = String.format("https://api.tiingo.com/tiingo/daily/%s/prices?" + "startDate-Xs&endDate-Xs&token=%s", ticker, startLocalDate.toString(), endLocalDate.toString(), TOKEN);
         RestTemplate restTemplate= new RestTemplate();
         // api returns a list of results for each day's closing details
         TiingoCandle[] stocksStartToEndDate = restTemplate.getForObject(url, TiingoCandle[].class);
         // Extract stocks for startDate  endDate 
         if (stocksStartToEndDate != null) {
            TiingoCandle stockStartDate = stocksStartToEndDate[0];
            TiingoCandle stockLatest = stocksStartToEndDate[stocksStartToEndDate.length - 1];
            Double buyPrice= stockStartDate.getOpen();
            Double sellPrice = stockLatest.getClose();
            AnnualizedReturn annualizedReturn =calculateAnnualizedReturns (endLocalDate, trade, buyPrice, sellPrice);
            return annualizedReturn; }  
            else {
               return new AnnualizedReturn(ticker, Double.NaN, Double.NaN); 
            }
}



public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args) throws IOException, URISyntaxException { 
   //return Collections.emptyList();
   List<AnnualizedReturn> annualizedReturns = new ArrayList<>();
   LocalDate endLocalDate = LocalDate.parse(args[1]);
   File trades = resolveFileFromResources (args[0]); 
   ObjectMapper objectMapper = getObjectMapper();
   PortfolioTrade[] tradeJsons = objectMapper.readValue(trades, PortfolioTrade[].class); 
   for (int i = 0; i < tradeJsons.length; i++) { 
      annualizedReturns.add(getAnnualizedReturn(tradeJsons[i], endLocalDate));
 } 
 // Sort in Descending order
   Comparator<AnnualizedReturn> SortByAnnReturn =Comparator.comparing(AnnualizedReturn :: getAnnualizedReturn) .reversed();
   Collections.sort(annualizedReturns, SortByAnnReturn);
   return annualizedReturns;

}

// TODO: CRIO_TASK_MODULE_CALCULATIONS
 // Return the populated list of AnnualizedReturn for all stocks.
// Annualized returns should be calculated in two steps:
 // 1. Calculate totalReturn = (sell_value - buy_value) / buy_value.
// 1.1 Store the same as totalReturns
 // 2. Calculate extrapolated annualized returns by scaling the same in years span.
//  The formula is: 
//annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
//2.1 Store the same as annualized_returns 
 //Test the same using below specified command. The build should be successful.
// ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn
public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,PortfolioTrade trade, Double buyPrice, Double sellPrice) {
   //return new AnnualizedReturn("", 0.0, 0.0);
   // calculate total returns 
   Double absReturn = (sellPrice - buyPrice) / buyPrice;
   String symbol = trade.getSymbol();
   LocalDate purchaseDate = trade.getPurchaseDate();
// calculate years
    Double numYears = (double) ChronoUnit.DAYS.between(purchaseDate, endDate) / 365;
// calculate annualized returns using formula 
   Double annualizedReturns = Math.pow((1+absReturn),(1/numYears)) - 1;
 //return AnnualizedReturn object 
     return new AnnualizedReturn(symbol, annualizedReturns, absReturn);

}













  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());



    printJsonObject(mainCalculateSingleReturn(args));

  }
}

