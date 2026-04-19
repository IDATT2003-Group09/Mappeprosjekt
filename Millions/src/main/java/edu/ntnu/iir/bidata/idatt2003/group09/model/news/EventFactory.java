package edu.ntnu.iir.bidata.idatt2003.group09.model.news;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;

public class EventFactory implements Serializable {

  public EventFactory() {

  }

    public List<Event> generateGlobalEvents(){
        List<Event> events = new ArrayList<>();

        GlobalEvent event1 = new GlobalEvent(
                "Tech Boom",
                "The technology sector is experiencing unprecedented growth."
        );
        event1.addEventData("Technology", BigDecimal.valueOf(0.06));
        event1.addEventData("Energy", BigDecimal.valueOf(0.03));
        event1.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.02));
        event1.addEventData("Communication Services", BigDecimal.valueOf(0.02));
        event1.addEventData("Financials", BigDecimal.valueOf(0.015));
        event1.addEventData("Consumer Staples", BigDecimal.valueOf(0.01));
        event1.addEventData("Health Care", BigDecimal.valueOf(0.01));
        event1.addEventData("Industrials", BigDecimal.valueOf(0.02));
        event1.addEventData("Materials", BigDecimal.valueOf(0.015));
        event1.addEventData("Utilities", BigDecimal.valueOf(0.005));
        event1.addEventData("Real Estate", BigDecimal.valueOf(0.01));
        events.add(event1);

        GlobalEvent event2 = new GlobalEvent(
                "Financial Crisis",
                "A major financial institution has collapsed."
        );
        event2.addEventData("Financials", BigDecimal.valueOf(-0.05));
        event2.addEventData("Real Estate", BigDecimal.valueOf(-0.04));
        event2.addEventData("Technology", BigDecimal.valueOf(-0.03));
        event2.addEventData("Consumer Discretionary", BigDecimal.valueOf(-0.03));
        event2.addEventData("Communication Services", BigDecimal.valueOf(-0.025));
        event2.addEventData("Consumer Staples", BigDecimal.valueOf(-0.01));
        event2.addEventData("Health Care", BigDecimal.valueOf(-0.01));
        event2.addEventData("Energy", BigDecimal.valueOf(-0.02));
        event2.addEventData("Industrials", BigDecimal.valueOf(-0.03));
        event2.addEventData("Materials", BigDecimal.valueOf(-0.025));
        event2.addEventData("Utilities", BigDecimal.valueOf(-0.01));
        events.add(event2);

        GlobalEvent e3 = new GlobalEvent(
                "Interest Rate Cuts",
                "Central banks lower interest rates."
        );
        e3.addEventData("Financials", BigDecimal.valueOf(0.04));
        e3.addEventData("Real Estate", BigDecimal.valueOf(0.03));
        e3.addEventData("Technology", BigDecimal.valueOf(0.02));
        e3.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.03));
        e3.addEventData("Communication Services", BigDecimal.valueOf(0.02));
        e3.addEventData("Consumer Staples", BigDecimal.valueOf(0.01));
        e3.addEventData("Health Care", BigDecimal.valueOf(0.015));
        e3.addEventData("Energy", BigDecimal.valueOf(0.01));
        e3.addEventData("Industrials", BigDecimal.valueOf(0.02));
        e3.addEventData("Materials", BigDecimal.valueOf(0.015));
        e3.addEventData("Utilities", BigDecimal.valueOf(0.005));
        events.add(e3);

        GlobalEvent e4 = new GlobalEvent(
                "Interest Rate Hike",
                "Central banks increase interest rates."
        );
        e4.addEventData("Financials", BigDecimal.valueOf(-0.03));
        e4.addEventData("Real Estate", BigDecimal.valueOf(-0.04));
        e4.addEventData("Technology", BigDecimal.valueOf(-0.02));
        e4.addEventData("Consumer Discretionary", BigDecimal.valueOf(-0.03));
        e4.addEventData("Communication Services", BigDecimal.valueOf(-0.02));
        e4.addEventData("Consumer Staples", BigDecimal.valueOf(-0.01));
        e4.addEventData("Health Care", BigDecimal.valueOf(-0.01));
        e4.addEventData("Energy", BigDecimal.valueOf(-0.01));
        e4.addEventData("Industrials", BigDecimal.valueOf(-0.025));
        e4.addEventData("Materials", BigDecimal.valueOf(-0.02));
        e4.addEventData("Utilities", BigDecimal.valueOf(-0.005));
        events.add(e4);

        GlobalEvent e5 = new GlobalEvent(
                "Oil Price Surge",
                "Oil prices spike due to geopolitical tensions."
        );
        e5.addEventData("Energy", BigDecimal.valueOf(0.05));
        e5.addEventData("Consumer Discretionary", BigDecimal.valueOf(-0.02));
        e5.addEventData("Technology", BigDecimal.valueOf(-0.01));
        e5.addEventData("Communication Services", BigDecimal.valueOf(-0.01));
        e5.addEventData("Financials", BigDecimal.valueOf(-0.015));
        e5.addEventData("Consumer Staples", BigDecimal.valueOf(-0.01));
        e5.addEventData("Health Care", BigDecimal.valueOf(-0.005));
        e5.addEventData("Industrials", BigDecimal.valueOf(-0.02));
        e5.addEventData("Materials", BigDecimal.valueOf(0.01));
        e5.addEventData("Utilities", BigDecimal.valueOf(0.015));
        e5.addEventData("Real Estate", BigDecimal.valueOf(-0.01));
        events.add(e5);

        GlobalEvent e6 = new GlobalEvent(
                "Supply Chain Recovery",
                "Global logistics improve."
        );
        e6.addEventData("Industrials", BigDecimal.valueOf(0.04));
        e6.addEventData("Materials", BigDecimal.valueOf(0.03));
        e6.addEventData("Technology", BigDecimal.valueOf(0.015));
        e6.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.02));
        e6.addEventData("Communication Services", BigDecimal.valueOf(0.01));
        e6.addEventData("Financials", BigDecimal.valueOf(0.015));
        e6.addEventData("Consumer Staples", BigDecimal.valueOf(0.01));
        e6.addEventData("Health Care", BigDecimal.valueOf(0.01));
        e6.addEventData("Energy", BigDecimal.valueOf(0.01));
        e6.addEventData("Utilities", BigDecimal.valueOf(0.005));
        e6.addEventData("Real Estate", BigDecimal.valueOf(0.01));
        events.add(e6);

        GlobalEvent e7 = new GlobalEvent(
                "Recession Fears",
                "Economic slowdown concerns spread."
        );
        e7.addEventData("Financials", BigDecimal.valueOf(-0.03));
        e7.addEventData("Industrials", BigDecimal.valueOf(-0.02));
        e7.addEventData("Technology", BigDecimal.valueOf(-0.02));
        e7.addEventData("Consumer Discretionary", BigDecimal.valueOf(-0.03));
        e7.addEventData("Communication Services", BigDecimal.valueOf(-0.02));
        e7.addEventData("Consumer Staples", BigDecimal.valueOf(0.005));
        e7.addEventData("Health Care", BigDecimal.valueOf(0.005));
        e7.addEventData("Energy", BigDecimal.valueOf(-0.015));
        e7.addEventData("Materials", BigDecimal.valueOf(-0.02));
        e7.addEventData("Utilities", BigDecimal.valueOf(0.005));
        e7.addEventData("Real Estate", BigDecimal.valueOf(-0.02));
        events.add(e7);

        GlobalEvent e8 = new GlobalEvent(
                "AI Breakthrough",
                "Major AI advancements boost tech."
        );
        e8.addEventData("Technology", BigDecimal.valueOf(0.07));
        e8.addEventData("Communication Services", BigDecimal.valueOf(0.03));
        e8.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.02));
        e8.addEventData("Financials", BigDecimal.valueOf(0.015));
        e8.addEventData("Consumer Staples", BigDecimal.valueOf(0.005));
        e8.addEventData("Health Care", BigDecimal.valueOf(0.015));
        e8.addEventData("Energy", BigDecimal.valueOf(-0.005));
        e8.addEventData("Industrials", BigDecimal.valueOf(0.02));
        e8.addEventData("Materials", BigDecimal.valueOf(0.01));
        e8.addEventData("Utilities", BigDecimal.valueOf(0.005));
        e8.addEventData("Real Estate", BigDecimal.valueOf(0.01));
        events.add(e8);

        GlobalEvent e9 = new GlobalEvent(
                "Cybersecurity Threat",
                "Large-scale cyber attacks raise concerns."
        );
        e9.addEventData("Technology", BigDecimal.valueOf(-0.03));
        e9.addEventData("Communication Services", BigDecimal.valueOf(-0.02));
        e9.addEventData("Financials", BigDecimal.valueOf(-0.02));
        e9.addEventData("Consumer Discretionary", BigDecimal.valueOf(-0.015));
        e9.addEventData("Consumer Staples", BigDecimal.valueOf(-0.005));
        e9.addEventData("Health Care", BigDecimal.valueOf(-0.01));
        e9.addEventData("Energy", BigDecimal.valueOf(-0.005));
        e9.addEventData("Industrials", BigDecimal.valueOf(-0.01));
        e9.addEventData("Materials", BigDecimal.valueOf(-0.01));
        e9.addEventData("Utilities", BigDecimal.valueOf(-0.005));
        e9.addEventData("Real Estate", BigDecimal.valueOf(-0.01));
        events.add(e9);

        GlobalEvent e10 = new GlobalEvent(
                "Green Energy Push",
                "Governments invest in renewable energy."
        );
        e10.addEventData("Energy", BigDecimal.valueOf(0.06));
        e10.addEventData("Utilities", BigDecimal.valueOf(0.03));
        e10.addEventData("Technology", BigDecimal.valueOf(0.02));
        e10.addEventData("Industrials", BigDecimal.valueOf(0.02));
        e10.addEventData("Materials", BigDecimal.valueOf(0.02));
        e10.addEventData("Financials", BigDecimal.valueOf(0.01));
        e10.addEventData("Health Care", BigDecimal.valueOf(0.01));
        e10.addEventData("Communication Services", BigDecimal.valueOf(0.005));
        e10.addEventData("Consumer Staples", BigDecimal.valueOf(0.005));
        e10.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.005));
        e10.addEventData("Real Estate", BigDecimal.valueOf(0.01));
        events.add(e10);

        GlobalEvent e11 = new GlobalEvent(
                "Housing Slowdown",
                "Housing demand declines."
        );
        e11.addEventData("Real Estate", BigDecimal.valueOf(-0.03));
        e11.addEventData("Financials", BigDecimal.valueOf(-0.02));
        e11.addEventData("Consumer Discretionary", BigDecimal.valueOf(-0.02));
        e11.addEventData("Industrials", BigDecimal.valueOf(-0.015));
        e11.addEventData("Materials", BigDecimal.valueOf(-0.015));
        e11.addEventData("Technology", BigDecimal.valueOf(-0.01));
        e11.addEventData("Communication Services", BigDecimal.valueOf(-0.01));
        e11.addEventData("Health Care", BigDecimal.valueOf(-0.005));
        e11.addEventData("Energy", BigDecimal.valueOf(-0.01));
        e11.addEventData("Consumer Staples", BigDecimal.valueOf(0.005));
        e11.addEventData("Utilities", BigDecimal.valueOf(0.005));
        events.add(e11);

        GlobalEvent e12 = new GlobalEvent(
                "Consumer Boom",
                "Consumers increase spending."
        );
        e12.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.05));
        e12.addEventData("Consumer Staples", BigDecimal.valueOf(0.02));
        e12.addEventData("Communication Services", BigDecimal.valueOf(0.02));
        e12.addEventData("Technology", BigDecimal.valueOf(0.02));
        e12.addEventData("Financials", BigDecimal.valueOf(0.02));
        e12.addEventData("Health Care", BigDecimal.valueOf(0.01));
        e12.addEventData("Energy", BigDecimal.valueOf(0.015));
        e12.addEventData("Industrials", BigDecimal.valueOf(0.02));
        e12.addEventData("Materials", BigDecimal.valueOf(0.015));
        e12.addEventData("Utilities", BigDecimal.valueOf(0.005));
        e12.addEventData("Real Estate", BigDecimal.valueOf(0.02));
        events.add(e12);

        GlobalEvent e13 = new GlobalEvent(
                "Trade War",
                "New tariffs disrupt trade."
        );
        e13.addEventData("Industrials", BigDecimal.valueOf(-0.03));
        e13.addEventData("Materials", BigDecimal.valueOf(-0.02));
        e13.addEventData("Technology", BigDecimal.valueOf(-0.015));
        e13.addEventData("Consumer Discretionary", BigDecimal.valueOf(-0.02));
        e13.addEventData("Communication Services", BigDecimal.valueOf(-0.01));
        e13.addEventData("Financials", BigDecimal.valueOf(-0.015));
        e13.addEventData("Consumer Staples", BigDecimal.valueOf(-0.01));
        e13.addEventData("Health Care", BigDecimal.valueOf(-0.005));
        e13.addEventData("Energy", BigDecimal.valueOf(-0.01));
        e13.addEventData("Utilities", BigDecimal.valueOf(-0.005));
        e13.addEventData("Real Estate", BigDecimal.valueOf(-0.01));
        events.add(e13);

        GlobalEvent e14 = new GlobalEvent(
                "Healthcare Breakthrough",
                "Medical innovation improves outlook."
        );
        e14.addEventData("Health Care", BigDecimal.valueOf(0.05));
        e14.addEventData("Technology", BigDecimal.valueOf(0.015));
        e14.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.01));
        e14.addEventData("Communication Services", BigDecimal.valueOf(0.01));
        e14.addEventData("Financials", BigDecimal.valueOf(0.01));
        e14.addEventData("Consumer Staples", BigDecimal.valueOf(0.01));
        e14.addEventData("Energy", BigDecimal.valueOf(0.005));
        e14.addEventData("Industrials", BigDecimal.valueOf(0.01));
        e14.addEventData("Materials", BigDecimal.valueOf(0.005));
        e14.addEventData("Utilities", BigDecimal.valueOf(0.01));
        e14.addEventData("Real Estate", BigDecimal.valueOf(0.01));
        events.add(e14);

        GlobalEvent e15 = new GlobalEvent(
                "Banking Concerns",
                "Uncertainty in financial markets."
        );
        e15.addEventData("Financials", BigDecimal.valueOf(-0.03));
        e15.addEventData("Technology", BigDecimal.valueOf(-0.015));
        e15.addEventData("Consumer Discretionary", BigDecimal.valueOf(-0.02));
        e15.addEventData("Communication Services", BigDecimal.valueOf(-0.015));
        e15.addEventData("Consumer Staples", BigDecimal.valueOf(-0.005));
        e15.addEventData("Health Care", BigDecimal.valueOf(-0.005));
        e15.addEventData("Energy", BigDecimal.valueOf(-0.01));
        e15.addEventData("Industrials", BigDecimal.valueOf(-0.015));
        e15.addEventData("Materials", BigDecimal.valueOf(-0.015));
        e15.addEventData("Utilities", BigDecimal.valueOf(-0.005));
        e15.addEventData("Real Estate", BigDecimal.valueOf(-0.02));
        events.add(e15);

        GlobalEvent e16 = new GlobalEvent(
                "Strong Job Market",
                "Employment rates improve."
        );
        e16.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.04));
        e16.addEventData("Consumer Staples", BigDecimal.valueOf(0.02));
        e16.addEventData("Communication Services", BigDecimal.valueOf(0.02));
        e16.addEventData("Technology", BigDecimal.valueOf(0.02));
        e16.addEventData("Financials", BigDecimal.valueOf(0.02));
        e16.addEventData("Health Care", BigDecimal.valueOf(0.01));
        e16.addEventData("Energy", BigDecimal.valueOf(0.01));
        e16.addEventData("Industrials", BigDecimal.valueOf(0.02));
        e16.addEventData("Materials", BigDecimal.valueOf(0.01));
        e16.addEventData("Utilities", BigDecimal.valueOf(0.005));
        e16.addEventData("Real Estate", BigDecimal.valueOf(0.02));
        events.add(e16);

        GlobalEvent e17 = new GlobalEvent(
                "Inflation Spike",
                "Inflation rises unexpectedly."
        );
        e17.addEventData("Consumer Staples", BigDecimal.valueOf(-0.02));
        e17.addEventData("Financials", BigDecimal.valueOf(-0.02));
        e17.addEventData("Energy", BigDecimal.valueOf(0.015));
        e17.addEventData("Materials", BigDecimal.valueOf(0.015));
        e17.addEventData("Utilities", BigDecimal.valueOf(0.01));
        e17.addEventData("Technology", BigDecimal.valueOf(-0.015));
        e17.addEventData("Consumer Discretionary", BigDecimal.valueOf(-0.03));
        e17.addEventData("Communication Services", BigDecimal.valueOf(-0.015));
        e17.addEventData("Health Care", BigDecimal.valueOf(-0.01));
        e17.addEventData("Industrials", BigDecimal.valueOf(-0.02));
        e17.addEventData("Real Estate", BigDecimal.valueOf(-0.02));
        events.add(e17);

        GlobalEvent e18 = new GlobalEvent(
                "Utility Rate Increase",
                "Electricity prices rise, boosting utility revenues."
        );
        e18.addEventData("Utilities", BigDecimal.valueOf(0.03));
        e18.addEventData("Technology", BigDecimal.valueOf(-0.01));
        e18.addEventData("Consumer Discretionary", BigDecimal.valueOf(-0.015));
        e18.addEventData("Communication Services", BigDecimal.valueOf(-0.01));
        e18.addEventData("Financials", BigDecimal.valueOf(-0.01));
        e18.addEventData("Consumer Staples", BigDecimal.valueOf(-0.005));
        e18.addEventData("Health Care", BigDecimal.valueOf(-0.005));
        e18.addEventData("Energy", BigDecimal.valueOf(-0.01));
        e18.addEventData("Industrials", BigDecimal.valueOf(-0.015));
        e18.addEventData("Materials", BigDecimal.valueOf(-0.01));
        e18.addEventData("Real Estate", BigDecimal.valueOf(-0.01));
        events.add(e18);

        GlobalEvent e19 = new GlobalEvent(
                "Energy Price Drop",
                "Oil and gas prices fall due to oversupply."
        );
        e19.addEventData("Energy", BigDecimal.valueOf(-0.03));
        e19.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.02));
        e19.addEventData("Consumer Staples", BigDecimal.valueOf(0.01));
        e19.addEventData("Industrials", BigDecimal.valueOf(0.015));
        e19.addEventData("Materials", BigDecimal.valueOf(-0.01));
        e19.addEventData("Utilities", BigDecimal.valueOf(-0.015));
        e19.addEventData("Technology", BigDecimal.valueOf(0.01));
        e19.addEventData("Communication Services", BigDecimal.valueOf(0.005));
        e19.addEventData("Financials", BigDecimal.valueOf(0.005));
        e19.addEventData("Health Care", BigDecimal.valueOf(0.005));
        e19.addEventData("Real Estate", BigDecimal.valueOf(0.01));
        events.add(e19);

        GlobalEvent e20 = new GlobalEvent(
                "Streaming Boom",
                "Demand for streaming services surges."
        );
        e20.addEventData("Communication Services", BigDecimal.valueOf(0.04));
        e20.addEventData("Technology", BigDecimal.valueOf(0.02));
        e20.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.015));
        e20.addEventData("Financials", BigDecimal.valueOf(0.01));
        e20.addEventData("Consumer Staples", BigDecimal.valueOf(0.005));
        e20.addEventData("Health Care", BigDecimal.valueOf(0.005));
        e20.addEventData("Energy", BigDecimal.valueOf(0.005));
        e20.addEventData("Industrials", BigDecimal.valueOf(0.01));
        e20.addEventData("Materials", BigDecimal.valueOf(0.005));
        e20.addEventData("Utilities", BigDecimal.valueOf(0.005));
        e20.addEventData("Real Estate", BigDecimal.valueOf(0.01));
        events.add(e20);

        GlobalEvent e21 = new GlobalEvent(
                "Advertising Slump",
                "Companies cut marketing budgets."
        );
        e21.addEventData("Communication Services", BigDecimal.valueOf(-0.03));
        e21.addEventData("Technology", BigDecimal.valueOf(-0.015));
        e21.addEventData("Consumer Discretionary", BigDecimal.valueOf(-0.015));
        e21.addEventData("Financials", BigDecimal.valueOf(-0.01));
        e21.addEventData("Consumer Staples", BigDecimal.valueOf(-0.005));
        e21.addEventData("Health Care", BigDecimal.valueOf(-0.005));
        e21.addEventData("Energy", BigDecimal.valueOf(-0.005));
        e21.addEventData("Industrials", BigDecimal.valueOf(-0.01));
        e21.addEventData("Materials", BigDecimal.valueOf(-0.01));
        e21.addEventData("Utilities", BigDecimal.valueOf(-0.005));
        e21.addEventData("Real Estate", BigDecimal.valueOf(-0.01));
        events.add(e21);

        GlobalEvent e22 = new GlobalEvent(
                "Retail Expansion",
                "Retail chains expand aggressively."
        );
        e22.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.03));
        e22.addEventData("Consumer Staples", BigDecimal.valueOf(0.015));
        e22.addEventData("Communication Services", BigDecimal.valueOf(0.015));
        e22.addEventData("Technology", BigDecimal.valueOf(0.015));
        e22.addEventData("Financials", BigDecimal.valueOf(0.01));
        e22.addEventData("Health Care", BigDecimal.valueOf(0.005));
        e22.addEventData("Energy", BigDecimal.valueOf(0.01));
        e22.addEventData("Industrials", BigDecimal.valueOf(0.015));
        e22.addEventData("Materials", BigDecimal.valueOf(0.01));
        e22.addEventData("Utilities", BigDecimal.valueOf(0.005));
        e22.addEventData("Real Estate", BigDecimal.valueOf(0.01));
        events.add(e22);

        GlobalEvent e23 = new GlobalEvent(
                "Consumer Confidence Drops",
                "Consumers reduce spending amid uncertainty."
        );
        e23.addEventData("Consumer Discretionary", BigDecimal.valueOf(-0.03));
        e23.addEventData("Consumer Staples", BigDecimal.valueOf(-0.01));
        e23.addEventData("Communication Services", BigDecimal.valueOf(-0.015));
        e23.addEventData("Technology", BigDecimal.valueOf(-0.015));
        e23.addEventData("Financials", BigDecimal.valueOf(-0.015));
        e23.addEventData("Health Care", BigDecimal.valueOf(-0.005));
        e23.addEventData("Energy", BigDecimal.valueOf(-0.01));
        e23.addEventData("Industrials", BigDecimal.valueOf(-0.015));
        e23.addEventData("Materials", BigDecimal.valueOf(-0.01));
        e23.addEventData("Utilities", BigDecimal.valueOf(0.005));
        e23.addEventData("Real Estate", BigDecimal.valueOf(-0.01));
        events.add(e23);

        GlobalEvent e24 = new GlobalEvent(
                "Staples Demand Surge",
                "Demand for essential goods increases."
        );
        e24.addEventData("Consumer Staples", BigDecimal.valueOf(0.03));
        e24.addEventData("Consumer Discretionary", BigDecimal.valueOf(-0.01));
        e24.addEventData("Communication Services", BigDecimal.valueOf(-0.005));
        e24.addEventData("Technology", BigDecimal.valueOf(-0.005));
        e24.addEventData("Financials", BigDecimal.valueOf(0.005));
        e24.addEventData("Health Care", BigDecimal.valueOf(0.01));
        e24.addEventData("Energy", BigDecimal.valueOf(0.005));
        e24.addEventData("Industrials", BigDecimal.valueOf(-0.005));
        e24.addEventData("Materials", BigDecimal.valueOf(0.005));
        e24.addEventData("Utilities", BigDecimal.valueOf(0.01));
        e24.addEventData("Real Estate", BigDecimal.valueOf(0.005));
        events.add(e24);

        GlobalEvent e25 = new GlobalEvent(
                "Food Cost Pressure",
                "Rising input costs hurt consumer staples margins."
        );
        e25.addEventData("Consumer Staples", BigDecimal.valueOf(-0.02));
        e25.addEventData("Consumer Discretionary", BigDecimal.valueOf(-0.015));
        e25.addEventData("Communication Services", BigDecimal.valueOf(-0.01));
        e25.addEventData("Technology", BigDecimal.valueOf(-0.01));
        e25.addEventData("Financials", BigDecimal.valueOf(-0.01));
        e25.addEventData("Health Care", BigDecimal.valueOf(-0.005));
        e25.addEventData("Energy", BigDecimal.valueOf(-0.005));
        e25.addEventData("Industrials", BigDecimal.valueOf(-0.01));
        e25.addEventData("Materials", BigDecimal.valueOf(0.005));
        e25.addEventData("Utilities", BigDecimal.valueOf(-0.005));
        e25.addEventData("Real Estate", BigDecimal.valueOf(-0.01));
        events.add(e25);

        GlobalEvent e26 = new GlobalEvent(
                "Industrial Growth",
                "Manufacturing output increases globally."
        );
        e26.addEventData("Industrials", BigDecimal.valueOf(0.04));
        e26.addEventData("Materials", BigDecimal.valueOf(0.03));
        e26.addEventData("Energy", BigDecimal.valueOf(0.02));
        e26.addEventData("Technology", BigDecimal.valueOf(0.015));
        e26.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.015));
        e26.addEventData("Communication Services", BigDecimal.valueOf(0.01));
        e26.addEventData("Financials", BigDecimal.valueOf(0.015));
        e26.addEventData("Consumer Staples", BigDecimal.valueOf(0.01));
        e26.addEventData("Health Care", BigDecimal.valueOf(0.01));
        e26.addEventData("Utilities", BigDecimal.valueOf(0.005));
        e26.addEventData("Real Estate", BigDecimal.valueOf(0.01));
        events.add(e26);

        GlobalEvent e27 = new GlobalEvent(
                "Factory Slowdown",
                "Production slows due to weak demand."
        );
        e27.addEventData("Industrials", BigDecimal.valueOf(-0.03));
        e27.addEventData("Materials", BigDecimal.valueOf(-0.02));
        e27.addEventData("Energy", BigDecimal.valueOf(-0.015));
        e27.addEventData("Technology", BigDecimal.valueOf(-0.015));
        e27.addEventData("Consumer Discretionary", BigDecimal.valueOf(-0.02));
        e27.addEventData("Communication Services", BigDecimal.valueOf(-0.01));
        e27.addEventData("Financials", BigDecimal.valueOf(-0.015));
        e27.addEventData("Consumer Staples", BigDecimal.valueOf(-0.005));
        e27.addEventData("Health Care", BigDecimal.valueOf(-0.005));
        e27.addEventData("Utilities", BigDecimal.valueOf(-0.005));
        e27.addEventData("Real Estate", BigDecimal.valueOf(-0.01));
        events.add(e27);

        GlobalEvent e28 = new GlobalEvent(
                "Materials Shortage",
                "Shortage of raw materials drives prices higher."
        );
        e28.addEventData("Materials", BigDecimal.valueOf(0.03));
        e28.addEventData("Energy", BigDecimal.valueOf(0.02));
        e28.addEventData("Industrials", BigDecimal.valueOf(-0.01));
        e28.addEventData("Consumer Discretionary", BigDecimal.valueOf(-0.01));
        e28.addEventData("Consumer Staples", BigDecimal.valueOf(-0.005));
        e28.addEventData("Technology", BigDecimal.valueOf(-0.005));
        e28.addEventData("Communication Services", BigDecimal.valueOf(-0.005));
        e28.addEventData("Financials", BigDecimal.valueOf(-0.005));
        e28.addEventData("Health Care", BigDecimal.valueOf(-0.005));
        e28.addEventData("Utilities", BigDecimal.valueOf(-0.005));
        e28.addEventData("Real Estate", BigDecimal.valueOf(-0.005));
        events.add(e28);

        GlobalEvent e29 = new GlobalEvent(
                "Commodity Price Drop",
                "Raw material prices fall sharply."
        );
        e29.addEventData("Materials", BigDecimal.valueOf(-0.03));
        e29.addEventData("Industrials", BigDecimal.valueOf(0.015));
        e29.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.015));
        e29.addEventData("Consumer Staples", BigDecimal.valueOf(0.01));
        e29.addEventData("Technology", BigDecimal.valueOf(0.01));
        e29.addEventData("Communication Services", BigDecimal.valueOf(0.005));
        e29.addEventData("Financials", BigDecimal.valueOf(0.005));
        e29.addEventData("Health Care", BigDecimal.valueOf(0.005));
        e29.addEventData("Energy", BigDecimal.valueOf(-0.01));
        e29.addEventData("Utilities", BigDecimal.valueOf(0.005));
        e29.addEventData("Real Estate", BigDecimal.valueOf(0.005));
        events.add(e29);

        GlobalEvent e30 = new GlobalEvent(
                "Real Estate Investment Boom",
                "Investors flood into real estate markets."
        );
        e30.addEventData("Real Estate", BigDecimal.valueOf(0.04));
        e30.addEventData("Financials", BigDecimal.valueOf(0.03));
        e30.addEventData("Industrials", BigDecimal.valueOf(0.015));
        e30.addEventData("Materials", BigDecimal.valueOf(0.015));
        e30.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.015));
        e30.addEventData("Consumer Staples", BigDecimal.valueOf(0.005));
        e30.addEventData("Technology", BigDecimal.valueOf(0.01));
        e30.addEventData("Communication Services", BigDecimal.valueOf(0.01));
        e30.addEventData("Health Care", BigDecimal.valueOf(0.005));
        e30.addEventData("Energy", BigDecimal.valueOf(0.01));
        e30.addEventData("Utilities", BigDecimal.valueOf(0.01));
        events.add(e30);

        GlobalEvent e31 = new GlobalEvent(
                "Tech Momentum",
                "Tech stocks continue strong upward momentum."
        );
        e31.addEventData("Technology", BigDecimal.valueOf(0.04));
        e31.addEventData("Communication Services", BigDecimal.valueOf(0.02));
        e31.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.015));
        e31.addEventData("Financials", BigDecimal.valueOf(0.015));
        e31.addEventData("Consumer Staples", BigDecimal.valueOf(0.005));
        e31.addEventData("Health Care", BigDecimal.valueOf(0.01));
        e31.addEventData("Energy", BigDecimal.valueOf(0.005));
        e31.addEventData("Industrials", BigDecimal.valueOf(0.015));
        e31.addEventData("Materials", BigDecimal.valueOf(0.01));
        e31.addEventData("Utilities", BigDecimal.valueOf(0.005));
        e31.addEventData("Real Estate", BigDecimal.valueOf(0.01));
        events.add(e31);

        GlobalEvent e32 = new GlobalEvent(
                "Consumer Strength",
                "Strong consumer spending boosts markets."
        );
        e32.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.04));
        e32.addEventData("Consumer Staples", BigDecimal.valueOf(0.02));
        e32.addEventData("Communication Services", BigDecimal.valueOf(0.02));
        e32.addEventData("Technology", BigDecimal.valueOf(0.015));
        e32.addEventData("Financials", BigDecimal.valueOf(0.015));
        e32.addEventData("Health Care", BigDecimal.valueOf(0.01));
        e32.addEventData("Energy", BigDecimal.valueOf(0.01));
        e32.addEventData("Industrials", BigDecimal.valueOf(0.015));
        e32.addEventData("Materials", BigDecimal.valueOf(0.01));
        e32.addEventData("Utilities", BigDecimal.valueOf(0.005));
        e32.addEventData("Real Estate", BigDecimal.valueOf(0.01));
        events.add(e32);

        GlobalEvent e33 = new GlobalEvent(
                "Healthcare Stability",
                "Stable growth in healthcare sector."
        );
        e33.addEventData("Health Care", BigDecimal.valueOf(0.03));
        e33.addEventData("Technology", BigDecimal.valueOf(0.01));
        e33.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.01));
        e33.addEventData("Communication Services", BigDecimal.valueOf(0.01));
        e33.addEventData("Financials", BigDecimal.valueOf(0.01));
        e33.addEventData("Consumer Staples", BigDecimal.valueOf(0.01));
        e33.addEventData("Energy", BigDecimal.valueOf(0.005));
        e33.addEventData("Industrials", BigDecimal.valueOf(0.01));
        e33.addEventData("Materials", BigDecimal.valueOf(0.005));
        e33.addEventData("Utilities", BigDecimal.valueOf(0.01));
        e33.addEventData("Real Estate", BigDecimal.valueOf(0.01));
        events.add(e33);

        GlobalEvent e34 = new GlobalEvent(
                "Market Pullback",
                "Markets experience a short-term correction."
        );
        e34.addEventData("Technology", BigDecimal.valueOf(-0.03));
        e34.addEventData("Financials", BigDecimal.valueOf(-0.03));
        e34.addEventData("Consumer Discretionary", BigDecimal.valueOf(-0.02));
        e34.addEventData("Communication Services", BigDecimal.valueOf(-0.015));
        e34.addEventData("Consumer Staples", BigDecimal.valueOf(-0.005));
        e34.addEventData("Health Care", BigDecimal.valueOf(-0.005));
        e34.addEventData("Energy", BigDecimal.valueOf(-0.015));
        e34.addEventData("Industrials", BigDecimal.valueOf(-0.02));
        e34.addEventData("Materials", BigDecimal.valueOf(-0.02));
        e34.addEventData("Utilities", BigDecimal.valueOf(-0.005));
        e34.addEventData("Real Estate", BigDecimal.valueOf(-0.015));
        events.add(e34);

        GlobalEvent e35 = new GlobalEvent(
                "Market Rally",
                "Broad market rally driven by strong investor optimism."
        );
        e35.addEventData("Technology", BigDecimal.valueOf(0.06));
        e35.addEventData("Financials", BigDecimal.valueOf(0.05));
        e35.addEventData("Industrials", BigDecimal.valueOf(0.04));
        e35.addEventData("Consumer Discretionary", BigDecimal.valueOf(0.03));
        e35.addEventData("Communication Services", BigDecimal.valueOf(0.025));
        e35.addEventData("Consumer Staples", BigDecimal.valueOf(0.015));
        e35.addEventData("Health Care", BigDecimal.valueOf(0.02));
        e35.addEventData("Energy", BigDecimal.valueOf(0.02));
        e35.addEventData("Materials", BigDecimal.valueOf(0.025));
        e35.addEventData("Utilities", BigDecimal.valueOf(0.01));
        e35.addEventData("Real Estate", BigDecimal.valueOf(0.02));
        events.add(e35);

        return events;
    }

  public List<Event> generateStockSpecificEvents(){
    List<Event> events = new ArrayList<>();

    StockSpecificEvent event1 = new StockSpecificEvent(
            "{stock} to the moon",
            "{stock} has reported record-breaking earnings for the quarter, exceeding analyst expectations.",
            BigDecimal.valueOf(0.015)
    );
    events.add(event1);

    StockSpecificEvent event2 = new StockSpecificEvent(
            "{stock} faces regulatory investigation",
            "{stock} is under investigation by regulatory authorities, raising concerns about potential legal issues.",
            BigDecimal.valueOf(-0.025)
    );
    events.add(event2);

        StockSpecificEvent event3 = new StockSpecificEvent(
          "{stock} secures major long-term contract",
          "{stock} has secured a long-term contract that is expected to stabilize revenue for years.",
          BigDecimal.valueOf(0.01)
        );
        events.add(event3);

      events.add(new StockSpecificEvent(
              "{stock} beats earnings expectations",
              "{stock} reported earnings above expectations, boosting investor confidence.",
              BigDecimal.valueOf(0.02)
      ));

      events.add(new StockSpecificEvent(
              "{stock} misses earnings",
              "{stock} reported disappointing earnings results.",
              BigDecimal.valueOf(-0.02)
      ));

      events.add(new StockSpecificEvent(
              "{stock} announces layoffs",
              "{stock} announces workforce reductions to cut costs.",
              BigDecimal.valueOf(-0.015)
      ));

      events.add(new StockSpecificEvent(
              "{stock} launches new product",
              "{stock} unveils a new product expected to drive revenue growth.",
              BigDecimal.valueOf(0.015)
      ));

      events.add(new StockSpecificEvent(
              "{stock} CEO resigns",
              "{stock}'s CEO steps down unexpectedly.",
              BigDecimal.valueOf(-0.02)
      ));

      events.add(new StockSpecificEvent(
              "{stock} expands internationally",
              "{stock} expands into new international markets.",
              BigDecimal.valueOf(0.02)
      ));

      events.add(new StockSpecificEvent(
              "{stock} faces lawsuit",
              "{stock} is hit with a major lawsuit.",
              BigDecimal.valueOf(-0.025)
      ));

      events.add(new StockSpecificEvent(
              "{stock} upgrades outlook",
              "{stock} raises its future guidance.",
              BigDecimal.valueOf(0.02)
      ));

      events.add(new StockSpecificEvent(
              "{stock} downgraded by analysts",
              "{stock} receives a downgrade from major analysts.",
              BigDecimal.valueOf(-0.02)
      ));

      events.add(new StockSpecificEvent(
              "{stock} announces stock buyback",
              "{stock} announces a share buyback program.",
              BigDecimal.valueOf(0.015)
      ));

      events.add(new StockSpecificEvent(
              "{stock} supply issues",
              "{stock} faces supply chain disruptions.",
              BigDecimal.valueOf(-0.015)
      ));

      events.add(new StockSpecificEvent(
              "{stock} strategic partnership",
              "{stock} enters a strategic partnership.",
              BigDecimal.valueOf(0.015)
      ));

      events.add(new StockSpecificEvent(
              "{stock} data breach",
              "{stock} suffers a major data breach.",
              BigDecimal.valueOf(-0.03)
      ));

      events.add(new StockSpecificEvent(
              "{stock} strong quarterly growth",
              "{stock} reports strong quarterly growth.",
              BigDecimal.valueOf(0.02)
      ));

      events.add(new StockSpecificEvent(
              "{stock} regulatory approval",
              "{stock} receives regulatory approval for a key product.",
              BigDecimal.valueOf(0.02)
      ));

    return events;
  }

  public StockSpecificEvent applyRandomStockToEvent(StockSpecificEvent event, List<Stock> stocks){
    if (stocks.isEmpty()) {
      throw new IllegalArgumentException("Stock list cannot be empty");
    }
    int randomIndex = (int) (Math.random() * stocks.size());
    Stock randomStock = stocks.get(randomIndex);
    event.addStock(randomStock);
    return event;
  }
}
