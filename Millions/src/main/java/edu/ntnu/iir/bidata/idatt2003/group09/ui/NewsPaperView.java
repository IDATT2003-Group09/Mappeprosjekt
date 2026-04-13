package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import edu.ntnu.iir.bidata.idatt2003.group09.base.news.NewsPaper;
import edu.ntnu.iir.bidata.idatt2003.group09.base.news.StockSpecificEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class NewsPaperView extends BorderPane {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy");

	public NewsPaperView(int week, NewsPaper newsPaper) {
		getStyleClass().add("newspaper-root");
		getStylesheets().add(getClass().getResource("/styling/newspaperview.css").toExternalForm());
		setPadding(new Insets(14));

		setCenter(buildBody(week, newsPaper));
	}

	private VBox buildMasthead(int week) {
		Label title = new Label("THE MILLIONS JOURNAL");
		title.getStyleClass().add("newspaper-title");

		LocalDate issueDate = LocalDate.of(2026, 4, 9).plusWeeks(Math.max(week - 1, 0));
		Label meta = new Label("WEEK " + week + "   |   " + issueDate.format(DATE_FORMATTER).toUpperCase());
		meta.getStyleClass().add("newspaper-meta");

		VBox header = new VBox(5, title, createRuleLine(false), meta, createRuleLine(false));
		header.getStyleClass().add("newspaper-header");
		return header;
	}

	private VBox buildBody(int week, NewsPaper newsPaper) {
		if (newsPaper == null) {
			Label noNews = new Label("No market news available yet. Advance one week to generate headlines.");
			noNews.getStyleClass().add("article-description");
			VBox emptyBody = new VBox(10, buildMasthead(week), noNews);
			emptyBody.getStyleClass().add("newspaper-body");
			return emptyBody;
		}

		Label leftPageTitle = new Label("GLOBAL EVENT");
		leftPageTitle.getStyleClass().add("section-title");

		Label globalHeadline = new Label(newsPaper.getGlobalEvent().getHeadline().toUpperCase());
		globalHeadline.getStyleClass().add("global-headline");
		globalHeadline.setWrapText(true);

		Label globalDescription = new Label(newsPaper.getGlobalEvent().getDescription());
		globalDescription.getStyleClass().add("global-description");
		globalDescription.setWrapText(true);

		Region leftPageSpacer = new Region();
		VBox.setVgrow(leftPageSpacer, Priority.ALWAYS);

		VBox leftPage = new VBox(10,
				buildMasthead(week),
				leftPageTitle,
				createRuleLine(false),
				globalHeadline,
				globalDescription,
				leftPageSpacer,
				buildPageFooter()
		);
		leftPage.getStyleClass().addAll("newspaper-page", "left-page");
		leftPage.setMaxWidth(Double.MAX_VALUE);

		Label rightPageTitle = new Label("STOCK-SPECIFIC EVENTS");
		rightPageTitle.getStyleClass().add("section-title");

		VBox rightPageArticles = new VBox(12);
		rightPageArticles.getStyleClass().add("articles-grid");
		rightPageArticles.setFillWidth(true);

		List<StockSpecificEvent> specificEvents = newsPaper.getStockSpecificEvents();
		for (StockSpecificEvent event : specificEvents) {
			rightPageArticles.getChildren().add(buildArticleCard(event));
		}

		Region rightPageSpacer = new Region();
		VBox.setVgrow(rightPageSpacer, Priority.ALWAYS);

		VBox rightPage = new VBox(10,
				rightPageTitle,
				createRuleLine(false),
				rightPageArticles,
				rightPageSpacer,
				buildPageFooter()
		);
		rightPage.getStyleClass().addAll("newspaper-page", "right-page");
		rightPage.setMaxWidth(Double.MAX_VALUE);

		Region centerFoldLine = new Region();
		centerFoldLine.getStyleClass().add("center-fold-line");
		centerFoldLine.setMinWidth(2);
		centerFoldLine.setPrefWidth(2);
		centerFoldLine.setMaxWidth(2);

		GridPane spread = new GridPane();
		spread.add(leftPage, 0, 0);
		spread.add(centerFoldLine, 1, 0);
		spread.add(rightPage, 2, 0);

		ColumnConstraints leftCol = new ColumnConstraints();
		leftCol.setPercentWidth(50);

		ColumnConstraints centerCol = new ColumnConstraints();
		centerCol.setMinWidth(2);
		centerCol.setPrefWidth(2);
		centerCol.setMaxWidth(2);

		ColumnConstraints rightCol = new ColumnConstraints();
		rightCol.setPercentWidth(50);

		spread.getColumnConstraints().setAll(leftCol, centerCol, rightCol);
		spread.getStyleClass().add("newspaper-spread");
		GridPane.setHgrow(leftPage, Priority.ALWAYS);
		GridPane.setHgrow(rightPage, Priority.ALWAYS);
		centerFoldLine.prefHeightProperty().bind(spread.heightProperty());

		VBox body = new VBox(10, spread, createRuleLine(false));
		body.getStyleClass().add("newspaper-body");
		return body;
	}

	private VBox buildArticleCard(StockSpecificEvent event) {
		Label ticker = new Label(event.getTargetSymbol() != null ? event.getTargetSymbol().toUpperCase() : "MARKET");
		ticker.getStyleClass().add("article-ticker");

		Label headline = new Label(event.getGeneratedHeadline().toUpperCase());
		headline.getStyleClass().add("article-headline");
		headline.setWrapText(true);

		Label description = new Label(event.getGeneratedDescription());
		description.getStyleClass().add("article-description");
		description.setWrapText(true);

		VBox card = new VBox(4, ticker, headline, description);
		card.getStyleClass().add("article-card");
		card.setMaxWidth(Double.MAX_VALUE);
		VBox.setVgrow(description, Priority.ALWAYS);
		return card;
	}

	private HBox buildPageFooter() {
		Label footerText = new Label("THE MILLIONS JOURNAL • ALL THE NEWS WORTH TRADING • 2026");
		footerText.getStyleClass().add("newspaper-footer-text");
		HBox footer = new HBox(footerText);
		footer.getStyleClass().addAll("newspaper-footer", "page-footer");
		return footer;
	}

	private Region createRuleLine(boolean thick) {
		Region line = new Region();
		line.getStyleClass().add(thick ? "rule-line-thick" : "rule-line");
		line.setMaxWidth(Double.MAX_VALUE);
		return line;
	}
}
