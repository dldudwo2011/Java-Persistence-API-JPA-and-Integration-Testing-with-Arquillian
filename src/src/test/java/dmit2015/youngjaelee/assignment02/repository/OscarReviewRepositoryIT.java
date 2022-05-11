/**
 * @author Youngjae Lee
 * @version 2022-02-03
 *
 * description: Testing class
 */

package dmit2015.youngjaelee.assignment02.repository;

import common.config.ApplicationConfig;

import dmit2015.youngjaelee.assignment02.entity.OscarReview;
import jakarta.validation.ConstraintViolationException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import jakarta.inject.Inject;


import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(ArquillianExtension.class)                  // Run with JUnit 5 instead of JUnit 4
public class OscarReviewRepositoryIT {

    @Inject
    private OscarReviewRepostiory _reviewRepository;

    static OscarReview currentReview;  // the Movie that is currently being added, find, update, or delete

    @Deployment
    public static WebArchive createDeployment() {
        PomEquippedResolveStage pomFile = Maven.resolver().loadPomFromFile("pom.xml");

        return ShrinkWrap.create(WebArchive.class,"test.war")
//                .addAsLibraries(pomFile.resolve("groupId:artifactId:version").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("com.h2database:h2:2.1.210").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("org.hsqldb:hsqldb:2.6.1").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("com.microsoft.sqlserver:mssql-jdbc:8.4.1.jre11").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("com.oracle.database.jdbc:ojdbc11:21.1.0.0").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.hamcrest:hamcrest:2.2").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("org.hibernate:hibernate-core-jakarta:5.6.5.Final").withTransitivity().asFile())
                .addClass(ApplicationConfig.class)
                .addClasses(OscarReview.class, OscarReviewRepostiory.class)
                .addAsResource("META-INF/persistence.xml")
//                .addAsResource("META-INF/sql/import-data.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE,"beans.xml");
    }

    @Order(2)
    @Test
    void shouldCreate() {
        currentReview = new OscarReview();
        currentReview.setCategory("actor");
        currentReview.setNominee("O Yeong-su");
        currentReview.setReview("Squid game!!");
        currentReview.setUsername("Youngjae Lee");
        _reviewRepository.add(currentReview);

        Optional<OscarReview> optionalOscarReview = _reviewRepository.findById(currentReview.getId());
        assertTrue(optionalOscarReview.isPresent());
        OscarReview existingReview = optionalOscarReview.get();
        assertNotNull(existingReview);
        assertEquals(currentReview.getCategory(), existingReview.getCategory());
        assertEquals(currentReview.getNominee(), existingReview.getNominee());
        assertEquals(currentReview.getReview(), existingReview.getReview());
        assertEquals(currentReview.getUsername(), existingReview.getUsername());

        // Testing JBean Validation
        OscarReview invalidReview = new OscarReview();
        invalidReview.setCategory("");
        invalidReview.setNominee("Youngjae Leeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        invalidReview.setReview("");
        invalidReview.setUsername("Youngjae Leeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");

        Exception exception = assertThrows(Exception.class, () -> {_reviewRepository.add(invalidReview);});

        assertTrue(exception.getMessage().contains("Category is required"));
        assertTrue(exception.getMessage().contains("size must be between 1 and 25"));
        assertTrue(exception.getMessage().contains("You must type review"));
        assertTrue(exception.getMessage().contains("size must be between 1 and 30"));
    }

    @Order(3)
    @Test
    void shouldFindOne() {
        final UUID reviewId = currentReview.getId();
        Optional<OscarReview> optionalOscarReview = _reviewRepository.findById(reviewId);
        assertTrue(optionalOscarReview.isPresent());
        OscarReview existingReview = optionalOscarReview.get();
        assertNotNull(existingReview);
        assertEquals(currentReview.getCategory(), existingReview.getCategory());
        assertEquals(currentReview.getNominee(), existingReview.getNominee());
        assertEquals(currentReview.getReview(), existingReview.getReview());
        assertEquals(currentReview.getUsername(), existingReview.getUsername());
        long createdDateTimeDifference = currentReview.getCreatedDateTime().until(existingReview.getCreatedDateTime(), ChronoUnit.MINUTES);
        assertEquals(0, createdDateTimeDifference);
        long lastModifedDateTimeDifferce = currentReview.getLastModifiedDateTime().until(existingReview.getLastModifiedDateTime(), ChronoUnit.MINUTES);
        assertEquals(0, lastModifedDateTimeDifferce);
    }

    @Order(1)
    @Transactional(TransactionMode.ROLLBACK)
    @Test
    void shouldFindAll() {

        var review1 = new OscarReview();
        review1.setCategory("film");
        review1.setNominee("Bong Joon-ho");
        review1.setReview("Parasite!!");
        review1.setUsername("Robert Downey Jr");
        _reviewRepository.add(review1);

        var review2 = new OscarReview();
        review2.setCategory("actor");
        review2.setNominee("Lee Jung-jae");
        review2.setReview("Squid game!!");
        review2.setUsername("Youngjae Lee");
        _reviewRepository.add(review2);

        List<OscarReview> queryResultList = _reviewRepository.findAll();
        assertEquals(2, queryResultList.size());

        OscarReview firstReview = queryResultList.get(0);
        assertEquals("film", firstReview.getCategory());
        assertEquals("Bong Joon-ho", firstReview.getNominee());
        assertEquals("Parasite!!", firstReview.getReview());
        assertEquals("Robert Downey Jr", firstReview.getUsername());


        OscarReview lastReview = queryResultList.get(queryResultList.size() - 1);
        assertEquals("actor", lastReview.getCategory());
        assertEquals("Lee Jung-jae", lastReview.getNominee());
        assertEquals("Squid game!!", lastReview.getReview());
        assertEquals("Youngjae Lee", lastReview.getUsername());
    }

    @Order(4)
    @Test
    void shouldUpdate() {
        currentReview.setCategory("actress");
        currentReview.setNominee("Kim Joo-ryeong");
        currentReview.setReview("Squid game!!!");
        currentReview.setUsername("Johnny Depp");

        _reviewRepository.update(currentReview);

        Optional<OscarReview> optionalUpdatedReview = _reviewRepository.findById(currentReview.getId());

        assertTrue(optionalUpdatedReview.isPresent());
        OscarReview updatedReview = optionalUpdatedReview.get();

        assertNotNull(updatedReview);
        assertEquals(currentReview.getCategory(), updatedReview.getCategory());
        assertEquals(currentReview.getNominee(), updatedReview.getNominee());
        assertEquals(currentReview.getReview(), updatedReview.getReview());
        assertEquals(currentReview.getUsername(), updatedReview.getUsername());

        long createdDateTimeDifference = currentReview.getCreatedDateTime().until(updatedReview.getCreatedDateTime(), ChronoUnit.MINUTES);
        assertEquals(0, createdDateTimeDifference);
        long lastModifedDateTimeDifferce = currentReview.getLastModifiedDateTime().until(updatedReview.getLastModifiedDateTime(), ChronoUnit.MINUTES);
        assertEquals(0, lastModifedDateTimeDifferce);
    }

    @Order(5)
    @Test
    void shouldDelete() {
        final UUID reviewId = currentReview.getId();
        Optional<OscarReview> optionalOscarReview = _reviewRepository.findById(reviewId);
        assertTrue(optionalOscarReview.isPresent());
        OscarReview existingReview = optionalOscarReview.get();
        assertNotNull(existingReview);
        _reviewRepository.remove(existingReview.getId());
        optionalOscarReview = _reviewRepository.findById(reviewId);
        assertTrue(optionalOscarReview.isEmpty());
    }
}