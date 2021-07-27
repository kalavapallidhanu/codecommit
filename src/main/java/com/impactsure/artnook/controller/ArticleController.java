package com.impactsure.artnook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.impactsure.artnook.dto.ArticleDto;
import com.impactsure.artnook.entity.Announcements;
import com.impactsure.artnook.repository.AnnouncementsRepository;
import com.impactsure.artnook.repository.ArticleRepository;
import com.impactsure.artnook.service.AnnouncementsService;
import com.impactsure.artnook.utils.GlobalConstants;

@Controller
@RequestMapping(value = "/artnook")
public class ArticleController {

	@Autowired
	private AnnouncementsService service;
	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private AnnouncementsRepository announcementRepository;

	/*
	 * Added By Gufran Khan Open All_Article Screen on Click of Article Icon
	 */
	@RequestMapping(value = "/article")
	public ModelAndView getArticle() {
		ModelAndView mv = new ModelAndView();
		List<ArticleDto> art = articleRepository.getArticleId();
		mv.addObject("categories", art);
		mv.setViewName("All_article");
		return mv;
	}

	@RequestMapping(value = "/getData")
	@ResponseBody
	public Iterable<Announcements> getAnnouncment() {
		return announcementRepository.findAll();
	}

	@RequestMapping("/getArticleData/{id}")
	@ResponseBody
	public List<ArticleDto> getArticleData(@PathVariable(name = "id") Integer id) {
		return articleRepository.getArticleByCategoryId(id);
	}

	/*
	 * Added By Gufran Khan Open New Article Screen on Click of New Article Link
	 */
	@RequestMapping("/newArticle")
	public String showNewProductPage(Model model) {
		ArticleDto product = new ArticleDto();
		List<ArticleDto> art = articleRepository.getArticleId();
		model.addAttribute("categories", art);
		model.addAttribute("article", product);

		return "new_article";
	}

	@RequestMapping(value = "/saveArticle", method = RequestMethod.POST)
	public String saveArticle(@ModelAttribute("article") ArticleDto article) {
		Announcements articleDet = new Announcements();

		/*
		 * Added By Gufran Khan Save Banner Details Based On Category(i.e 6)
		 */
		if (article.getCategory().equals(GlobalConstants.BANNER)) {
			if (article.getId() != null) {
				Announcements announcement = service.findOne(article.getId());
				Integer oldDisplayOrder = announcement.getDisplayOrder();
				if (announcement.getDisplayOrder() != article.getDisplayOrder()) {
					if (announcement.getDisplayOrder() < article.getDisplayOrder()) {
						articleRepository.updateDecrement(article, oldDisplayOrder);
					} else {
						articleRepository.updateIncrement(article, oldDisplayOrder);
					}

				}
				articleDet.setId(article.getId());
			} else {
				articleRepository.update(article);
			}
			articleDet.setCategory(article.getCategory());
			articleDet.setTitle(article.getTitle());
			articleDet.setMobileImage(article.getMobileImage());
			articleDet.setWebImage(article.getWebImage());
			articleDet.setActive(article.getActive());
			articleDet.setDisplayOrder(article.getDisplayOrder());

		}
		/*
		 * Added By Gufran Khan Save OtherArticle Details Based On Category(i.e
		 * 1,2,3,4,5)
		 */

		else {
			if (article.getId() != null) {
				Announcements announcement = service.findOne(article.getId());
				Integer oldDisplayOrder = announcement.getDisplayOrder();
				if (announcement.getDisplayOrder() != article.getDisplayOrder()) {
					if (announcement.getDisplayOrder() < article.getDisplayOrder()) {
						articleRepository.updateDecrement(article, oldDisplayOrder);
					} else {
						articleRepository.updateIncrement(article, oldDisplayOrder);
					}

				}
				articleDet.setId(article.getId());
			}

			else {
				articleRepository.update(article);
			}
			articleDet.setCategory(article.getCategory());
			articleDet.setTitle(article.getTitle());
			articleDet.setSubTitle(article.getSubTitle());

			articleDet.setSlipContent(article.getSlipContent());

			articleDet.setReadMoreContent(article.getReadMoreContent());

			articleDet.setDisplayOrder(article.getDisplayOrder());
			articleDet.setActive(article.getActive());

		}
		service.save(articleDet);

		return "new_article";
	}

	@RequestMapping("/edit/{id}")
	public ModelAndView editAnnouncemet(@PathVariable(name = "id") Long id) {
		ModelAndView mav = new ModelAndView();
		List<ArticleDto> art = articleRepository.getArticleId();
		mav.addObject("categories", art);
		Announcements announcement = service.findOne(id);
		mav.addObject("article", announcement);
		mav.setViewName("new_article");
		return mav;
	}

	@RequestMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Long id) {
		articleRepository.delete(id);
		return "All_article";
	}

}
