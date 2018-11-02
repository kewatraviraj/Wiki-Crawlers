package org.inexture.wikicrawler.service;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.inexture.wikicrawler.configuration.DatabaseConnection;
import org.inexture.wikicrawler.controller.WikiCrawlerController;

/**
 * @author Inexture
 * Persist Wiki Contents to database
 */
public class WikiCrawlerServiceImpl implements WikiCrawlerService {
	private static final Logger _log = Logger.getLogger(WikiCrawlerController.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.inexture.wikicrawlers.Service.Service#saveWikiContent(java.lang.String,
	 * java.io.InputStream, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void saveWikiContent(String articleTitle, String articleImageUrl, String wikiUrl,
			String downloadedImageUrl) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = DatabaseConnection.getConnection().prepareStatement(
					"insert into wiki_content(article_title,article_image_url,wiki_url,downloaded_image_url) values(?,?,?,?)");
			preparedStatement.setString(1, articleTitle);
			preparedStatement.setString(2, articleImageUrl);
			preparedStatement.setString(3, wikiUrl);
			preparedStatement.setString(4, downloadedImageUrl);
			preparedStatement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			_log.error(e);
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				DatabaseConnection.getConnection().close();
			} catch (ClassNotFoundException | SQLException e) {
				_log.error(e);
			}
		}
	}
}
