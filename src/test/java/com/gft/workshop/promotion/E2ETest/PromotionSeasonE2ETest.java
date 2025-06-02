package com.gft.workshop.promotion.E2ETest;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.promotion.business.model.PromotionSeason;
import com.gft.workshop.promotion.business.model.PromotionType;
import com.gft.workshop.promotion.integration.model.PromotionSeasonPL;
import com.gft.workshop.promotion.integration.repositories.PromotionSeasonPLRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class PromotionSeasonE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PromotionSeasonPLRepository promotionSeasonPLRepository;

    @LocalServerPort
    private int port;

    private final String uri = "/api/v1/promotionsSeason";

    private PromotionSeason promotionSeason1;
    private PromotionSeason newPromotionSeason;
    private PromotionSeasonPL savePromotionSeasonPL;
    private PromotionSeasonPL newPromotionSeasonPL;

    private PromotionSeasonPL promotionSeasonPL;

    private HttpHeaders headers;

    @BeforeEach
    void init(){
        initObjects();
    }

    @Test
    @DisplayName(("should create a promotion season and return 201"))
    void createPromotionSeasonOkTest(){

        promotionSeason1.setId(null);

        String url = "http://localhost:" + port + uri;

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PromotionSeason> request = new HttpEntity<>(promotionSeason1, headers);

        ResponseEntity<Long> postResponse = restTemplate.postForEntity(url, request, Long.class);

        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResponse.getBody()).isNotNull();

        Optional<PromotionSeasonPL> optional = promotionSeasonPLRepository.findById(postResponse.getBody());
        assertThat(optional).isPresent();
    }


    @Test
    @DisplayName("Should return existing PromotionSeason by ID and 200 OK")
    void getPromotionSeasonByIdTest(){

        promotionSeason1.setId(null);

        String postUrl = "http://localhost:" + port + uri;

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PromotionSeason> request = new HttpEntity<>(promotionSeason1, headers);

        ResponseEntity<Long> postResponse = restTemplate.postForEntity(postUrl, request, Long.class);

        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long id = postResponse.getBody();

        assertThat(id).isNotNull();

        String getUrl = "http://localhost:" + port + uri + "/" + id;
        ResponseEntity<PromotionSeason> getResponse = restTemplate.getForEntity(getUrl, PromotionSeason.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getId()).isEqualTo(id);
    }

    // *******************************************************
    //
    // Private Methods
    //
    // *******************************************************

    private void initObjects() {

        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.MONTH, 4);
        Date endDate = cal.getTime();

        promotionSeason1 = new PromotionSeason();
        promotionSeason1.setId(1L);
        promotionSeason1.setStartDate(startDate);
        promotionSeason1.setEndDate(endDate);
        promotionSeason1.setDiscount(15.0);
        promotionSeason1.setPromotionType(PromotionType.SEASON);
        promotionSeason1.setName("Spring Toys Promo");
        promotionSeason1.setAffectedCategories(List.of(Category.TOYS));

        newPromotionSeason = new PromotionSeason();
        newPromotionSeason.setId(2L);
        newPromotionSeason.setStartDate(startDate);
        newPromotionSeason.setEndDate(endDate);
        newPromotionSeason.setDiscount(20.0);
        newPromotionSeason.setPromotionType(PromotionType.SEASON);
        newPromotionSeason.setName("Book Festival");
        newPromotionSeason.setAffectedCategories(List.of(Category.BOOKS));

        promotionSeasonPL = new PromotionSeasonPL();
        promotionSeasonPL.setId(1L);
        promotionSeasonPL.setStartDate(startDate);
        promotionSeasonPL.setEndDate(endDate);
        promotionSeasonPL.setDiscount(15.0);
        promotionSeasonPL.setPromotionType(PromotionType.SEASON);
        promotionSeasonPL.setName("Toys Season PL");
        promotionSeasonPL.setAffectedCategories(List.of(Category.TOYS));

        newPromotionSeasonPL = new PromotionSeasonPL();
        newPromotionSeasonPL.setId(2L);
        newPromotionSeasonPL.setStartDate(startDate);
        newPromotionSeasonPL.setEndDate(endDate);
        newPromotionSeasonPL.setDiscount(20.0);
        newPromotionSeasonPL.setPromotionType(PromotionType.SEASON);
        newPromotionSeasonPL.setName("Books Season PL");
        newPromotionSeasonPL.setAffectedCategories(List.of(Category.BOOKS));

        headers = new HttpHeaders();
    }
}
