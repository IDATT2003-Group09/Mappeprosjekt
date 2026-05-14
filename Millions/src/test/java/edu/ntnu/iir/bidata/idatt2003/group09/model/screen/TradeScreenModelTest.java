package edu.ntnu.iir.bidata.idatt2003.group09.model.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class TradeScreenModelTest {

    private Stock techStock;
    private Stock financeStock;
    private Stock healthStock;
    private TradeScreenModel model;

    @BeforeEach
    void setUp() {
        techStock    = new Stock("AAPL", "Apple Inc.",        new BigDecimal("150.00"), "Technology", 3);
        financeStock = new Stock("JPM",  "JPMorgan Chase",    new BigDecimal("200.00"), "Finance",    4);
        healthStock  = new Stock("JNJ",  "Johnson & Johnson", new BigDecimal("100.00"), "Health",     2);

        model = new TradeScreenModel(List.of(techStock, financeStock, healthStock));
    }

    @Nested
    @DisplayName("getAllSectors")
    class GetAllSectorsTests {

        @Test
        @DisplayName("Returns all unique sectors from stock list")
        void getAllSectors_returnsUniqueSectors() {
            assertEquals(Set.of("Technology", "Finance", "Health"), model.getAllSectors());
        }
    }

    @Nested
    @DisplayName("Sector selection")
    class SectorSelectionTests {

        @Test
        @DisplayName("selectSector adds sector to selected set")
        void selectSector_addsSector() {
            model.selectSector("Technology");
            assertTrue(model.getSelectedSectors().contains("Technology"));
        }

        @Test
        @DisplayName("deselectSector removes sector from selected set")
        void deselectSector_removesSector() {
            model.selectSector("Technology");
            model.deselectSector("Technology");
            assertFalse(model.getSelectedSectors().contains("Technology"));
        }

        @Test
        @DisplayName("clearSelectedSectors empties the selected set")
        void clearSelectedSectors_emptiesSet() {
            model.selectSector("Technology");
            model.selectSector("Finance");
            model.clearSelectedSectors();
            assertTrue(model.getSelectedSectors().isEmpty());
        }

        @Test
        @DisplayName("selectAllSectors adds every sector")
        void selectAllSectors_addsAllSectors() {
            model.selectAllSectors();
            assertEquals(model.getAllSectors(), model.getSelectedSectors());
        }

        @Test
        @DisplayName("selectAllSectors clears previous selection first")
        void selectAllSectors_clearsPreviousSelection() {
            model.selectSector("NonExistentSector");
            model.selectAllSectors();
            assertFalse(model.getSelectedSectors().contains("NonExistentSector"));
        }
    }

    @Nested
    @DisplayName("filterStocks")
    class FilterStocksTests {

        @Test
        @DisplayName("Returns all stocks when search is empty and no sectors selected")
        void filterStocks_noFilters_returnsAll() {
            assertEquals(3, model.filterStocks("").size());
        }

        @Test
        @DisplayName("Filters by symbol (case-insensitive)")
        void filterStocks_bySymbol_caseInsensitive() {
            List<Stock> result = model.filterStocks("aapl");
            assertEquals(1, result.size());
            assertEquals("AAPL", result.get(0).getSymbol());
        }

        @Test
        @DisplayName("Filters by company name (case-insensitive)")
        void filterStocks_byCompanyName_caseInsensitive() {
            List<Stock> result = model.filterStocks("apple");
            assertEquals(1, result.size());
            assertEquals("Apple Inc.", result.get(0).getCompany());
        }

        @Test
        @DisplayName("Returns empty list when no stocks match search text")
        void filterStocks_noMatch_returnsEmpty() {
            assertTrue(model.filterStocks("ZZZZ").isEmpty());
        }

        @Test
        @DisplayName("Filters by selected sector only")
        void filterStocks_bySector_onlyReturnsSectorStocks() {
            model.selectSector("Finance");
            List<Stock> result = model.filterStocks("");
            assertEquals(1, result.size());
            assertEquals("JPM", result.get(0).getSymbol());
        }

        @Test
        @DisplayName("Combines sector and search text filters")
        void filterStocks_sectorAndSearch_combined() {
            model.selectSector("Technology");
            model.selectSector("Finance");
            List<Stock> result = model.filterStocks("JPM");
            assertEquals(1, result.size());
            assertEquals("JPM", result.get(0).getSymbol());
        }

        @Test
        @DisplayName("Search text does not match stock in unselected sector")
        void filterStocks_searchInUnselectedSector_returnsEmpty() {
            model.selectSector("Technology");
            assertTrue(model.filterStocks("JPM").isEmpty());
        }

        @Test
        @DisplayName("Null search text returns all stocks")
        void filterStocks_nullSearch_treatedAsEmpty() {
            assertEquals(3, model.filterStocks(null).size());
        }
    }

    @Nested
    @DisplayName("calculateMaxBuyQuantity")
    class CalculateMaxBuyQuantityTests {

        @Test
        @DisplayName("Returns 0 when stock is null")
        void calculateMaxBuyQuantity_nullStock_returnsZero() {
            assertEquals("0", model.calculateMaxBuyQuantity(null, new BigDecimal("1000"), new BigDecimal("0.01")));
        }

        @Test
        @DisplayName("Returns 0 when cash is null")
        void calculateMaxBuyQuantity_nullCash_returnsZero() {
            assertEquals("0", model.calculateMaxBuyQuantity(techStock, null, new BigDecimal("0.01")));
        }

        @Test
        @DisplayName("Returns 0 when commission rate is null")
        void calculateMaxBuyQuantity_nullCommission_returnsZero() {
            assertEquals("0", model.calculateMaxBuyQuantity(techStock, new BigDecimal("1000"), null));
        }

        @Test
        @DisplayName("Returns 0 when cash is zero")
        void calculateMaxBuyQuantity_zeroCash_returnsZero() {
            assertEquals("0", model.calculateMaxBuyQuantity(techStock, BigDecimal.ZERO, new BigDecimal("0.01")));
        }

        @Test
        @DisplayName("Returns a positive quantity when cash covers at least one share")
        void calculateMaxBuyQuantity_sufficientCash_returnsPositive() {
            String result = model.calculateMaxBuyQuantity(techStock, new BigDecimal("10000"), new BigDecimal("0.01"));
            assertTrue(new BigDecimal(result).compareTo(BigDecimal.ZERO) > 0);
        }

        @Test
        @DisplayName("Returns 0 when cash is less than price of one share plus commission")
        void calculateMaxBuyQuantity_insufficientCash_returnsZero() {
            assertEquals("0", model.calculateMaxBuyQuantity(techStock, new BigDecimal("1.00"), new BigDecimal("0.01")));
        }
    }

    @Nested
    @DisplayName("calculateMaxSellQuantity")
    class CalculateMaxSellQuantityTests {

        @Test
        @DisplayName("Returns 0 for null share list")
        void calculateMaxSellQuantity_nullList_returnsZero() {
            assertEquals("0", model.calculateMaxSellQuantity(null));
        }

        @Test
        @DisplayName("Returns 0 for empty share list")
        void calculateMaxSellQuantity_emptyList_returnsZero() {
            assertEquals("0", model.calculateMaxSellQuantity(Collections.emptyList()));
        }

        @Test
        @DisplayName("Returns summed quantity for owned shares")
        void calculateMaxSellQuantity_sumsQuantities() {
            Share first = new Share(techStock, new BigDecimal("1.5"), techStock.getSalesPrice());
            Share second = new Share(techStock, new BigDecimal("2.0"), techStock.getSalesPrice());

            assertEquals("3.5", model.calculateMaxSellQuantity(List.of(first, second)));
        }
    }

    @Nested
    @DisplayName("filterStocksAdvanced")
    class FilterStocksAdvancedTests {
        @Test
        @DisplayName("Owned filter keeps only owned symbols")
        void filterStocksAdvanced_ownedOnly_filtersByOwnership() {
            List<Stock> result = model.filterStocksAdvanced("", false, true, Set.of("AAPL"), false, false);

            assertEquals(1, result.size());
            assertEquals("AAPL", result.get(0).getSymbol());
        }

        @Test
        @DisplayName("Winners filter keeps and sorts positive movers descending")
        void filterStocksAdvanced_winners_filtersAndSortsDescending() {
            techStock.addNewSalesPrice(new BigDecimal("165.00"));
            financeStock.addNewSalesPrice(new BigDecimal("210.00"));
            healthStock.addNewSalesPrice(new BigDecimal("90.00"));

            List<Stock> result = model.filterStocksAdvanced("", false, false, Set.of(), true, false);

            assertEquals(2, result.size());
            assertEquals("AAPL", result.get(0).getSymbol());
            assertEquals("JPM", result.get(1).getSymbol());
        }

        @Test
        @DisplayName("Losers filter keeps and sorts negative movers ascending")
        void filterStocksAdvanced_losers_filtersAndSortsAscending() {
            techStock.addNewSalesPrice(new BigDecimal("165.00"));
            financeStock.addNewSalesPrice(new BigDecimal("210.00"));
            healthStock.addNewSalesPrice(new BigDecimal("90.00"));

            List<Stock> result = model.filterStocksAdvanced("", false, false, Set.of(), false, true);

            assertEquals(1, result.size());
            assertEquals("JNJ", result.get(0).getSymbol());
        }


        @Test
        @DisplayName("allSectorsToggle false with no sectors selected still returns all stocks")
        void filterStocksAdvanced_allSectorsToggleFalse_noSectors_returnsAll() {
            List<Stock> result = model.filterStocksAdvanced("", false, false, Set.of(), false, false);

            assertEquals(3, result.size());
        }

    }
}