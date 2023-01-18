
package com.crio.warmup.stock.portfolio;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.PortfolioTrade;
import java.time.LocalDate;
import java.util.List;

public interface PortfolioManager {


  //CHECKSTYLE:OFF
  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Refactor the code to fit below signature.

  // Note:
  // We will not use file to transfer json data anymore, rather we will try to use java objects.
  // The reason is, this service is going to get exposed as a library in future.

List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades, LocalDate endDate){
      /*AnnualizedReturn annualizedReturn;
      List<AnnualizedReturn> annualizedReturns = new ArrayList<AnnualizedReturn>();
  
      for(int i=0;i<portfolioTrades.size();i++){
  
        annualizedReturn=getAnnualizedReturn(portfolioTrades.get(i),endDate);
  
        annualizedReturns.add(annualizedReturn);
      }
  
  
      Comparator<AnnualizedReturn> SortByAnnReturn =Comparator.comparing (AnnualizedReturn :: getAnnualizedReturn) .reversed();
      Collections.sort(annualizedReturns, SortByAnnReturn);
      return annualizedReturns;*/
  //CHECKSTYLE:ON
}
}

