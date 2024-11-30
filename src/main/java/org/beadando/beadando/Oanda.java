package org.beadando.beadando;

import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.account.AccountSummary;
import com.oanda.v20.instrument.Candlestick;
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

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Oanda {
    public static Context ctx = new Context("https://api-fxpractice.oanda.com", "e2cb5ade7b1d388aa7f72cdf8c74777e-da75f9dffbfe5b8b9b883149da2f030b");
    public static AccountID accountID = new AccountID("101-004-30473865-001");

    public static String getAccountSummary() {

        AccountSummary summary;

            try {
                summary = ctx.account.summary(accountID).getAccount();
                //System.out.println("Account Summary: " + summary);
                return summary.toString();
            } catch (Exception e) {
                return null;
            }
    }
    public static String getPricing(String instrument) {

        PricingGetRequest request = new PricingGetRequest(accountID, List.of(instrument));
        PricingGetResponse response;

        try {
            response = ctx.pricing.get(request);
            System.out.println("Pricing: " + response);
            return response.getPrices().toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static InstrumentCandlesResponse getHistoricalData(String instrument, DateTime from, DateTime to) {

        InstrumentCandlesRequest request = new InstrumentCandlesRequest(new InstrumentName(instrument));
        request.setGranularity(CandlestickGranularity.H1);
        request.setFrom(from);
        request.setTo(to);
        InstrumentCandlesResponse response;

        try {
            response = ctx.instrument.candles(request);
            for(Candlestick candle: response.getCandles())
                System.out.println(candle.getTime()+"\t"+candle.getMid().getC());
            System.out.println("Historical Data: " + response.getCandles().toString());
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    public static TransactionID openTrade(String instrument, int units) {

        InstrumentName instrumentName = new InstrumentName(instrument);
        try {
            OrderCreateRequest request = new OrderCreateRequest(accountID);
            MarketOrderRequest marketOrderRequest = new MarketOrderRequest();
            System.out.println("Instrument: " + instrumentName);
            System.out.println("Units: " + units);
            System.out.println("Request: " + request);
            marketOrderRequest.setInstrument(instrumentName);
            marketOrderRequest.setUnits(units);
            System.out.println("MarketOrderRequest: " + marketOrderRequest);
            request.setOrder(marketOrderRequest);
            OrderCreateResponse response = ctx.order.create(request);
            return response.getOrderFillTransaction().getId();
        } catch (Exception e) {
            System.out.println("Oanda API hiba - \nÜzenet: " + e.getMessage());
        }
        return null;

    }
    public static List<Trade> getTrades() {

        try {
            return ctx.trade.list(accountID).getTrades();
        } catch (Exception e) {
            System.out.println("Oanda API hiba - \nÜzenet: " + e.getMessage());
            return null;
        }
    }


    public static void closeTrade(String tradeID) {

        TradeSpecifier tradeSpecifier = new TradeSpecifier(tradeID);
        try {
            TradeCloseRequest request = new TradeCloseRequest(accountID, tradeSpecifier);
            ctx.trade.close(request);
            System.out.println("Trade closed");
        } catch (Exception e) {
            System.out.println("Oanda API hiba - \nÜzenet: " + e.getMessage());
        }
    }

}
