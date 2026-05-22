package edu.ntnu.iir.bidata.idatt2003.group09.model.game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class GameProgressTest {

    private static final BigDecimal BASE_REQUIREMENT =
            new BigDecimal("0.04");

    private static final BigDecimal STARTING_MONEY =
            new BigDecimal("1000.00");

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        @Test
        @DisplayName("Two-arg constructor sets week to 0 and checkpoint to level 1")
        void twoArgConstructor_setsInitialState() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            assertEquals(0, gp.getCurrentWeek());
            assertEquals(1, gp.getCheckpointLevel());
            assertEquals(1, gp.getLastCalculatedLevel());

            assertEquals(
                    0,
                    BASE_REQUIREMENT.compareTo(gp.getBaseRequirement())
            );
        }

        @Test
        @DisplayName("Three-arg constructor with week 0 behaves like two-arg constructor")
        void threeArgConstructor_weekZero_matchesTwoArg() {

            GameProgress twoArg =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            GameProgress threeArg =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY, 0);

            assertEquals(
                    twoArg.getCurrentWeek(),
                    threeArg.getCurrentWeek()
            );

            assertEquals(
                    twoArg.getCheckpointLevel(),
                    threeArg.getCheckpointLevel()
            );

            assertEquals(
                    0,
                    twoArg.getCurrentTarget()
                            .compareTo(threeArg.getCurrentTarget())
            );
        }

        @Test
        @DisplayName("Three-arg constructor mid-quarter still lands on next checkpoint boundary")
        void threeArgConstructor_midQuarter_correctCheckpoint() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY, 7);

            assertEquals(7, gp.getCurrentWeek());
            assertEquals(1, gp.getCheckpointLevel());
        }

        @Test
        @DisplayName("Three-arg constructor at exact quarter boundary advances to next level")
        void threeArgConstructor_atQuarterBoundary_advancesLevel() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY, 13);

            assertEquals(2, gp.getCheckpointLevel());
        }

        @Test
        @DisplayName("Three-arg constructor clamps negative weeks to 0")
        void threeArgConstructor_negativeWeek_clampedToZero() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY, -5);

            assertEquals(0, gp.getCurrentWeek());
        }

        @Test
        @DisplayName("Initial target equals startingMoney * (1 + base)^level")
        void constructor_currentTarget_calculatedCorrectly() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            BigDecimal expected =
                    STARTING_MONEY.multiply(
                            BigDecimal.ONE.add(BASE_REQUIREMENT).pow(1)
                    );

            assertEquals(
                    0,
                    expected.compareTo(gp.getCurrentTarget())
            );
        }
    }

    @Nested
    @DisplayName("nextWeek and isQuarterComplete")
    class WeekProgressionTests {

        @Test
        @DisplayName("nextWeek increments the current week by 1")
        void nextWeek_incrementsCurrentWeek() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            gp.nextWeek();

            assertEquals(1, gp.getCurrentWeek());
        }

        @Test
        @DisplayName("isQuarterComplete is false before reaching checkpoint week")
        void isQuarterComplete_falseBeforeCheckpoint() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            for (int i = 0; i < 12; i++) {
                gp.nextWeek();
            }

            assertFalse(gp.isQuarterComplete());
        }

        @Test
        @DisplayName("isQuarterComplete is true at checkpoint week")
        void isQuarterComplete_trueAtCheckpoint() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            for (int i = 0; i < 13; i++) {
                gp.nextWeek();
            }

            assertTrue(gp.isQuarterComplete());
        }

        @Test
        @DisplayName("isQuarterComplete is true past checkpoint week")
        void isQuarterComplete_truePastCheckpoint() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            for (int i = 0; i < 20; i++) {
                gp.nextWeek();
            }

            assertTrue(gp.isQuarterComplete());
        }
    }

    @Nested
    @DisplayName("getWeeksUntilDeadline")
    class DeadlineTests {

        @Test
        @DisplayName("Returns 13 at start")
        void weeksUntilDeadline_atStart() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            assertEquals(13, gp.getWeeksUntilDeadline());
        }

        @Test
        @DisplayName("Decreases by 1 after each nextWeek call")
        void weeksUntilDeadline_decreasesWithEachWeek() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            gp.nextWeek();

            assertEquals(12, gp.getWeeksUntilDeadline());
        }

        @Test
        @DisplayName("Returns 0 at or past checkpoint")
        void weeksUntilDeadline_neverNegative() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            for (int i = 0; i < 20; i++) {
                gp.nextWeek();
            }

            assertEquals(0, gp.getWeeksUntilDeadline());
        }
    }

    @Nested
    @DisplayName("meetsRequirement")
    class RequirementTests {

        @Test
        @DisplayName("Returns false when net worth is below target")
        void meetsRequirement_belowTarget_returnsFalse() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            assertFalse(
                    gp.meetsRequirement(new BigDecimal("500.00"))
            );
        }

        @Test
        @DisplayName("Returns true when net worth equals target exactly")
        void meetsRequirement_exactTarget_returnsTrue() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            assertTrue(
                    gp.meetsRequirement(gp.getCurrentTarget())
            );
        }

        @Test
        @DisplayName("Returns true when net worth exceeds target")
        void meetsRequirement_aboveTarget_returnsTrue() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            assertTrue(
                    gp.meetsRequirement(
                            gp.getCurrentTarget().add(BigDecimal.ONE)
                    )
            );
        }
    }

    @Nested
    @DisplayName("advanceCheckpoint")
    class AdvanceCheckpointTests {

        @Test
        @DisplayName("Increments checkpointLevel by 1")
        void advanceCheckpoint_incrementsLevel() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            int before = gp.getCheckpointLevel();

            gp.advanceCheckpoint();

            assertEquals(before + 1, gp.getCheckpointLevel());
        }

        @Test
        @DisplayName("Advances checkpoint week by 13")
        void advanceCheckpoint_advancesCheckpointWeek() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            int weeksBefore = gp.getWeeksUntilDeadline();

            gp.advanceCheckpoint();

            assertEquals(
                    weeksBefore + 13,
                    gp.getWeeksUntilDeadline()
            );
        }

        @Test
        @DisplayName("Recalculates base requirement using power function")
        void advanceCheckpoint_recalculatesBaseRequirement() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            gp.advanceCheckpoint();

            BigDecimal expected =
                    BigDecimal.valueOf(
                            0.03 * Math.pow(2, 1.3)
                    ).setScale(4, RoundingMode.HALF_UP);

            assertEquals(
                    0,
                    expected.compareTo(gp.getBaseRequirement())
            );
        }

        @Test
        @DisplayName("Increases the current target")
        void advanceCheckpoint_increasesCurrentTarget() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            BigDecimal before =
                    gp.getCurrentTarget();

            gp.advanceCheckpoint();

            assertTrue(
                    gp.getCurrentTarget().compareTo(before) > 0
            );
        }

        @Test
        @DisplayName("Multiple advances keep recalculating the requirement")
        void advanceCheckpoint_multipleAdvances_keepRecalculating() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            gp.advanceCheckpoint();
            gp.advanceCheckpoint();

            BigDecimal expected =
                    BigDecimal.valueOf(
                            0.03 * Math.pow(3, 1.3)
                    ).setScale(4, RoundingMode.HALF_UP);

            assertEquals(
                    0,
                    expected.compareTo(gp.getBaseRequirement())
            );
        }
    }

    @Nested
    @DisplayName("lastCalculatedLevel")
    class LastCalculatedLevelTests {

        @Test
        @DisplayName("setLastCalculatedLevel stores the value getLastCalculatedLevel returns")
        void setAndGetLastCalculatedLevel() {

            GameProgress gp =
                    new GameProgress(BASE_REQUIREMENT, STARTING_MONEY);

            gp.setLastCalculatedLevel(42);

            assertEquals(
                    42,
                    gp.getLastCalculatedLevel()
            );
        }
    }
}