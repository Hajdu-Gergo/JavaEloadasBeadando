package org.beadando.beadando;

import com.oanda.v20.Context;
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.account.AccountSummary;
import com.oanda.v20.instrument.CandlestickGranularity;
import com.oanda.v20.instrument.InstrumentCandlesRequest;
import com.oanda.v20.instrument.InstrumentCandlesResponse;
import com.oanda.v20.order.MarketOrderRequest;
import com.oanda.v20.order.OrderCreateRequest;
import com.oanda.v20.order.OrderCreateResponse;
import com.oanda.v20.pricing.PricingGetRequest;
import com.oanda.v20.pricing.PricingGetResponse;
import com.oanda.v20.primitives.DateTime;
import com.oanda.v20.primitives.InstrumentName;
import com.oanda.v20.trade.Trade;
import com.oanda.v20.trade.TradeCloseRequest;
import com.oanda.v20.trade.TradeSpecifier;
import com.oanda.v20.transaction.Transaction;
import com.oanda.v20.transaction.TransactionID;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Oanda {
    public static String getAccountSummary() {
        Context ctx = new Context("https://api-fxpractice.oanda.com", "e2cb5ade7b1d388aa7f72cdf8c74777e-da75f9dffbfe5b8b9b883149da2f030b");
        AccountID accountID = new AccountID("101-004-30473865-001");
        AccountSummary summary;

            try {
                summary = ctx.account.summary(accountID).getAccount();
                //System.out.println("Account Summary: " + summary);
                return summary.toString();
            } catch (Exception e) {
                return null;
            }


    }

}
