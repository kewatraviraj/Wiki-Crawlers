/**
 * 
 */
package org.inexture.wikicrawler.service;

/**
 * @author Dell
 *
 */
public interface WikiCrawlerService {
	void saveWikiContent(String articleTitle, String articleUrl, String wikiUrl, String downloadedImageUrl);
}
