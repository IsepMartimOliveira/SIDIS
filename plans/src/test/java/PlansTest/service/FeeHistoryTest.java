package PlansTest.service;

import com.example.psoft_22_23_project.plansmanagement.model.*;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepositoryDB;
import com.example.psoft_22_23_project.plansmanagement.services.CreatePlansMapper;
import com.example.psoft_22_23_project.plansmanagement.services.PlansServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class FeeHistoryTest {

    @Mock
    private PlansRepositoryDB plansRepository;

    @Mock
    private CreatePlansMapper createPlansMapper;

    @InjectMocks
    private PlansServiceImpl plansService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void feeHistoryTest() {
        Name name = new Name();
        name.setName("Plan Name");

        Description description = new Description();
        description.setDescription("Plan Description");

        NumberOfMinutes numberOfMinutes = new NumberOfMinutes();
        numberOfMinutes.setNumberOfMinutes("100");

        MaximumNumberOfUsers maximumNumberOfUsers = new MaximumNumberOfUsers();
        maximumNumberOfUsers.setMaximumNumberOfUsers(5);

        MusicCollection musicCollection = new MusicCollection();
        musicCollection.setMusicCollection(10);

        MusicSuggestion musicSuggestion = new MusicSuggestion();
        musicSuggestion.setMusicSuggestion("personalized");

        AnnualFee annualFee = new AnnualFee();
        annualFee.setAnnualFee(150.00);

        MonthlyFee monthlyFee = new MonthlyFee();
        monthlyFee.setMonthlyFee(15.00);

        Active active = new Active();
        active.setActive(true);

        Promoted promoted = new Promoted();
        promoted.setPromoted(false);

        Plans plan = new Plans(name, description, numberOfMinutes, maximumNumberOfUsers,
                musicCollection, musicSuggestion, annualFee, monthlyFee, active, promoted);

        FeeRevision one = new FeeRevision(100.00, 10.00,"chico");

        plan.getFeeRevisions().add(one);

        when(plansRepository.findByName_Name(plan.getName().getName())).thenReturn(Optional.of(plan));

        plansRepository.save(plan);
/*
        List<FeeRevision> result = plansService.history(plan.getName().getName());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(150.00, result.get(0).getAnnualFee());
        assertEquals(15.00, result.get(0).getMonthlyFee());
        assertEquals(100.00, result.get(1).getAnnualFee());
        assertEquals(10.00, result.get(1).getMonthlyFee());

 */
    }

    @Test
    public void Test_PlanDoesNotExist() {
        String planName = "Test_PlanDoesNotExist";
        assertThrows(IllegalArgumentException.class, () -> plansService.history(planName));
    }


}
