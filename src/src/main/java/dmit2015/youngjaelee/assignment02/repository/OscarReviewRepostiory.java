/**
 * @author Youngjae Lee
 * @version 2022-02-03
 *
 * description: Repository class
 */

package dmit2015.youngjaelee.assignment02.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import dmit2015.youngjaelee.assignment02.entity.OscarReview;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
@Transactional
public class OscarReviewRepostiory {

    @PersistenceContext(unitName = "oracle-jpa-pu")
    private EntityManager em;

    public void add(OscarReview newReview) {
        em.persist(newReview);
        em.flush();
    }

    public void update(OscarReview updatedReview){
        Optional<OscarReview> optionalOscarReview = findById(updatedReview.getId());
        if (optionalOscarReview.isPresent()) {
            OscarReview existingReview = optionalOscarReview.get();
            existingReview.setCategory(updatedReview.getCategory());
            existingReview.setNominee(updatedReview.getNominee());
            existingReview.setReview(updatedReview.getReview());
            existingReview.setUsername(updatedReview.getUsername());
            em.merge(existingReview);
            em.flush();
        }
    }

    public void remove(UUID reviewID) {
        Optional<OscarReview> optionalOscarReview = findById(reviewID);
        if (optionalOscarReview.isPresent()) {
            OscarReview existingReview = optionalOscarReview.get();
            em.remove(existingReview);
            em.flush();
        }
    }

    public Optional<OscarReview> findById(UUID reviewID) {
        Optional<OscarReview> optionalOscarReview = Optional.empty();
        try {
            OscarReview querySingleResult = em.find(OscarReview.class, reviewID);
            if (querySingleResult != null) {
                optionalOscarReview = Optional.of(querySingleResult);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return optionalOscarReview;
    }

    public List<OscarReview> findAll() {
        return em.createQuery(
                "SELECT m FROM OscarReview m "
                , OscarReview.class)
                .getResultList();
    }

}