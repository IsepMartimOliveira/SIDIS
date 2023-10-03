package PlansTest.service;

import com.example.psoft_22_23_project.plansmanagement.model.*;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepository;
import com.example.psoft_22_23_project.plansmanagement.services.PlansServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class FindActiveTest {

    @Mock
    private PlansRepository plansRepository;

    @InjectMocks
    private PlansServiceImpl plansService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findActive() {
        // Create test data
        Name name = new Name();
        name.setName("Plan Name");

        Description description = new Description();
        description.setDescription("Plan Description");

        NumberOfMinutes numberOfMinutes = new NumberOfMinutes();
        numberOfMinutes.setNumberOfMinutes("100");

        MaximumNumberOfUsers maximumNumberOfUsers = new MaximumNumberOfUsers();
        maximumNumberOfUsers.setMaximumNumberOfUsers(6);

        MusicCollection musicCollection = new MusicCollection();
        musicCollection.setMusicCollection(10);

        MusicSuggestion musicSuggestion = new MusicSuggestion();
        musicSuggestion.setMusicSuggestion("personalized");

        AnnualFee annualFee = new AnnualFee();
        annualFee.setAnnualFee(100.00);

        MonthlyFee monthlyFee = new MonthlyFee();
        monthlyFee.setMonthlyFee(10.00);

        Promoted promoted = new Promoted();
        promoted.setPromoted(false);

        Active active = new Active();
        active.setActive(true);

        Active inactive = new Active();
        inactive.setActive(false);

        Plans activePlan = new Plans(name, description, numberOfMinutes, maximumNumberOfUsers,
                musicCollection, musicSuggestion, annualFee, monthlyFee, active, promoted);

        Plans inactivePlan = new Plans(name, description, numberOfMinutes, maximumNumberOfUsers,
                musicCollection, musicSuggestion, annualFee, monthlyFee, inactive, promoted);

        List<Plans> plansList = new ArrayList<>();
        plansList.add(activePlan);

        when(plansRepository.findByActive_Active(true)).thenReturn(Collections.singleton(activePlan));

        plansList.add(inactivePlan);

        Iterable<Plans> result = plansService.findAtive();
        assertNotNull(result);

        List<Plans> resultList = new ArrayList<>();
        result.forEach(resultList::add);

        assertEquals(1, resultList.size());
        assertTrue(resultList.get(0).getActive().getActive());
    }


}
