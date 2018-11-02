/**
 * Program for web crawling on wikipedia 
 * 
 * @author Dell
 *	Main Class
 */

package org.inexture.wikicrawler.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.inexture.wikicrawler.service.WikiCrawlerService;
import org.inexture.wikicrawler.service.WikiCrawlerServiceImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author Inexture 
 * Controller Class
 */
public class WikiCrawlerController {
	private static final Logger _log = Logger.getLogger(WikiCrawlerController.class.getName());

	public static void main(String[] args) {

		WikiCrawlerService service = new WikiCrawlerServiceImpl();
		boolean programController = true;

		while (programController) {
			System.out.println("Enter your Wiki Url:");

			@SuppressWarnings("resource")
			Scanner userArticle = new Scanner(System.in, "UTF-8");

			String wikiUrl = userArticle.nextLine(); // get article url input from user

			try {
				Document doc = Jsoup.connect(wikiUrl).get();

				/* Getting image tag from WikiPage */
				Element image = doc.select("table tbody tr td a img[src]").first();

				String articleUrl = image.absUrl("src"); // get src attribute of img tag of WikiPage

				/* Getting image name from src atribute of image */
				int indexname = articleUrl.lastIndexOf("/");
				if (indexname == articleUrl.length()) {
					articleUrl = articleUrl.substring(1, indexname);
				}

				InputStream inputsrc = new URL(articleUrl).openStream(); // get InputStream of image

				/* call a method to Save Wiki Content to database */
				service.saveWikiContent(doc.getElementById("firstHeading").text(), image.absUrl("src"), wikiUrl,
						saveImageLocal(inputsrc,
								articleUrl.substring(articleUrl.lastIndexOf("/"), articleUrl.length())));

				programController = false;
			} catch (org.jsoup.HttpStatusException | IllegalArgumentException e) {
				_log.error("Entered Url is not Correct" + e);
			} catch (IOException e) {
				_log.error("Filepath is Not Correct" + e);
			} catch (NullPointerException e) {
				_log.error("WebUrl InCorrect" + e);
			}
		}
	}

	/**
	 * Save Image To Local File System
	 * 
	 * @param inputsrc
	 * @param imageName
	 * @return
	 * @throws IOException
	 */
	private static String saveImageLocal(InputStream inputsrc, String imageName) {
		/* save image to given location */
		OutputStream output = null;
		String imagePath = new File("").getAbsolutePath() + File.separator + "src" + File.separator + "main"
				+ File.separator + "resources" + File.separator + "images";

		File file = new File(imagePath + imageName);
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(file);

			output = new BufferedOutputStream(fileOutputStream);
			for (int length; (length = inputsrc.read()) != -1;) {
				output.write(length);
			}
			_log.info("Image Saved in LocalSystem");
		} catch (IOException e) {
			_log.error(e);
		} finally {
			try {
				if (output != null)
					output.close();
				inputsrc.close();
			} catch (IOException e) {
				_log.error(e);
			}
		}
		return file.getAbsolutePath();
	}
}
