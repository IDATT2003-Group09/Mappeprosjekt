package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import edu.ntnu.iir.bidata.idatt2003.group09.base.news.NewsPaper;
import edu.ntnu.iir.bidata.idatt2003.group09.base.news.StockSpecificEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
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

		setTop(buildHeader(week));
		setCenter(buildBody(newsPaper));
		setBottom(buildFooter());
	}

	private VBox buildHeader(int week) {
		Label title = new Label("THE MARKET GAZETTE");
		title.getStyleClass().add("newspaper-title");

		LocalDate issueDate = LocalDate.of(2026, 4, 9).plusWeeks(Math.max(week - 1, 0));
		Label meta = new Label("WEEK " + week + "   |   " + issueDate.format(DATE_FORMATTER).toUpperCase());
		meta.getStyleClass().add("newspaper-meta");

		VBox header = new VBox(5, title, createRuleLine(false), meta, createRuleLine(false));
		header.getStyleClass().add("newspaper-header");
		return header;
	}

	private VBox buildBody(NewsPaper newsPaper) {
		if (newsPaper == null) {
			Label noNews = new Label("No market news available yet. Advance one week to generate headlines.");
			noNews.getStyleClass().add("article-description");
			VBox emptyBody = new VBox(noNews);
			emptyBody.getStyleClass().add("newspaper-body");
			return emptyBody;
		}

		Label globalHeadline = new Label(newsPaper.getGlobalEvent().getHeadline().toUpperCase());
		globalHeadline.getStyleClass().add("global-headline");

		Label globalDescription = new Label(newsPaper.getGlobalEvent().getDescription());
		globalDescription.getStyleClass().add("global-description");
		globalDescription.setWrapText(true);

		Label sectionTitle = new Label("BUSINESS");
		sectionTitle.getStyleClass().add("section-title");

		FlowPane articles = new FlowPane();
		articles.setHgap(12);
		articles.setVgap(12);
		articles.getStyleClass().add("articles-grid");

		List<StockSpecificEvent> specificEvents = newsPaper.getStockSpecificEvents();
		for (StockSpecificEvent event : specificEvents) {
			articles.getChildren().add(buildArticleCard(event));
		}

		VBox body = new VBox(
				10,
				globalHeadline,
				globalDescription,
				createRuleLine(true),
				sectionTitle,
				createRuleLine(false),
				articles,
				createRuleLine(false)
		);
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
		card.setPrefWidth(260);
		card.setMinHeight(170);
		VBox.setVgrow(description, Priority.ALWAYS);
		return card;
	}

	private HBox buildFooter() {
		Label footerText = new Label("THE MARKET GAZETTE • ALL THE NEWS WORTH TRADING • 2026");
		footerText.getStyleClass().add("newspaper-footer-text");
		HBox footer = new HBox(footerText);
		footer.getStyleClass().add("newspaper-footer");
		return footer;
	}

	private Region createRuleLine(boolean thick) {
		Region line = new Region();
		line.getStyleClass().add(thick ? "rule-line-thick" : "rule-line");
		line.setMaxWidth(Double.MAX_VALUE);
		return line;
	}
}
